package org.specs.auditing.client.http;

import org.specs.auditing.client.http.utils.Conf;
import org.specs.auditing.common.auditevent.AuditEvent;
import org.specs.auditing.common.auditevent.Severity;
import org.specs.auditing.common.auditevent.Target;

import java.util.Date;

public class HttpAuditorTest {
    private static final String CONF_FILE = "src/test/resources/test.properties";

    private boolean listenerReady = false;
    private final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Conf.load(CONF_FILE);
        new HttpAuditorTest().testAuditor();
    }

    private void testAuditor() throws Exception {
        HttpAuditor httpAuditor = new HttpAuditor(Conf.getProps());
        AuditEvent auditEvent = createAuditEvent();
        httpAuditor.audit(auditEvent);
        httpAuditor.close();
    }

    private AuditEvent createAuditEvent() {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setAction("READ");
        auditEvent.setEventTime(new Date());
        auditEvent.setEventType("REST_API_CALL");
        org.specs.auditing.common.auditevent.Initiator initiator = new org.specs.auditing.common.auditevent.Initiator("test_user");
        initiator.setType("USER");
        auditEvent.setInitiator(initiator);
        Target target = new Target("specs-server1/federation-api");
        target.setType("WEB_SERVICE");
        auditEvent.setTarget(target);
        auditEvent.setSeverity(Severity.INFO);
        auditEvent.setOutcome(org.specs.auditing.common.auditevent.Outcome.SUCCESS);

        return auditEvent;
    }
}
