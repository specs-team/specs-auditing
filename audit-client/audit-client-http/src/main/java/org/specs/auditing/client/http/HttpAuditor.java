package org.specs.auditing.client.http;

import org.apache.log4j.Logger;
import org.specs.auditing.client.Auditor;
import org.specs.auditing.client.http.utils.Conf;
import org.specs.auditing.common.auditevent.AuditEvent;
import org.specs.auditing.common.utils.AuditEventSerializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class HttpAuditor implements Auditor {
    private static Logger log = Logger.getLogger(HttpAuditor.class);
    private static final int QUEUE_CAPACITY = 30;

    private AuditorEngine auditorEngine;
    private Thread auditorEngineThread;
    private HttpURLConnection connection;

    public HttpAuditor() {
        log.debug("HttpAuditor created.");
    }

    public HttpAuditor(Properties props) throws IOException {
        init(props);
    }

    public void init(Properties props) throws IOException {
        Conf.load(props);

        auditorEngine = new AuditorEngine();
        auditorEngineThread = new Thread(auditorEngine);
        auditorEngineThread.start();
        log.debug("HttpAuditor initialized successfully.");
    }

    public void close() {
        // TODO: check if audit events queue is empty. Wait some time if it is not.
        auditorEngineThread.interrupt();
        log.debug("HttpAuditor was closed.");
    }

    public void audit(AuditEvent auditEvent) {
        auditorEngine.audit(auditEvent);
    }

    private class AuditorEngine implements Runnable {
        private Logger log = Logger.getLogger(AuditorEngine.class);

        private ArrayBlockingQueue<AuditEvent> workQueue;
        private AuditEventSerializer auditEventSerializer;
        private URL auditServerEndpoint;

        private AuditorEngine() throws MalformedURLException {
            workQueue = new ArrayBlockingQueue<AuditEvent>(QUEUE_CAPACITY);
            auditEventSerializer = new AuditEventSerializer();
            auditServerEndpoint = new URL(Conf.getAuditServerAddress() + "/audit-events");
            log.debug("AuditorEngine initialized successfully.");
        }

        public void audit(AuditEvent auditEvent) {
            try {
                workQueue.add(auditEvent);
                log.debug("Work queue size: " + workQueue.size());
            }
            catch (IllegalStateException e) {
                log.error("Auditor queue is full. Audit event was discarded.");
            }
        }

        @Override
        public void run() {
            log.debug("AuditorEngine started.");
            try {

                while (!Thread.currentThread().isInterrupted()) {
                    log.debug("Waiting for audit events...");
                    AuditEvent auditEvent = workQueue.take();
                    log.trace("Received audit event.");

                    HttpURLConnection conn = null;
                    try {
                        String content = auditEventSerializer.serialize(auditEvent);

                        // TODO: if the audit event is not published successfully it should be returned to the queue
                        conn = (HttpURLConnection) auditServerEndpoint.openConnection();

                        //add request headers
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("User-Agent", "audit-client-http");
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        conn.setDoOutput(true);

                        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                        wr.writeBytes(content);
                        wr.flush();
                        wr.close();

                        int responseCode = conn.getResponseCode();
                        if (responseCode != 204) {
                            throw new Exception("Received an invalid HTTP response code from the audit-server:" +
                                    responseCode);
                        }

                        if (log.isTraceEnabled()) {
                            log.debug("Audit event has been published: " + content);
                        }
                    }
                    catch (Exception e) {
                        log.error("Failed to publish audit event: " + e.getMessage(), e);
                    }
                    finally {
                        if (conn != null) {
                            try {
                                // TODO: keep-alive connection?
                                conn.disconnect();
                            }
                            catch (Exception e) {
                                log.error("Failed to close HttpURLConnection: " + e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            catch (InterruptedException e) {
                log.debug("AuditorEngine was interrupted.");
            }
        }
    }
}

