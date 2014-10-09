package org.specs.auditing.auditserver.http.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.specs.auditing.model.entities.Attachment;
import org.specs.auditing.model.entities.AuditEvent;

import java.io.File;
import java.io.IOException;

public class AuditEventsIndexer {
    private static Logger log = LogManager.getLogger(AuditEventsIndexer.class);
    private IndexWriter writer;

    public AuditEventsIndexer() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        Directory directory = FSDirectory.open(new File(Conf.getLuceneIndexDir()));
        writer = new IndexWriter(directory, config);
        log.debug("AuditEventsIndexer has been initialized successfully.");
    }

    public void add(AuditEvent auditEvent) throws IOException {
        Document document = new Document();
        document.add(new StoredField("id", auditEvent.getAuditEventId()));
        document.add(new StringField("eventType", auditEvent.getEventType(), Field.Store.NO));
        document.add(new TextField("initiatorId", auditEvent.getInitiatorId(), Field.Store.NO));
        document.add(new StringField("initiatorType", auditEvent.getInitiatorType(), Field.Store.NO));
        document.add(new StringField("action", auditEvent.getAction(), Field.Store.NO));
        document.add(new TextField("targetId", auditEvent.getTargetId(), Field.Store.NO));
        document.add(new StringField("targetType", auditEvent.getTargetType(), Field.Store.NO));
        document.add(new StringField("outcome", auditEvent.getOutcome(), Field.Store.NO));
        if (auditEvent.getSeverity() != null) {
            document.add(new StringField("severity", auditEvent.getSeverity(), Field.Store.NO));
        }
        if (auditEvent.getAttachmentList() != null && auditEvent.getAttachmentList().size() > 0) {
            for (Attachment attachment : auditEvent.getAttachmentList()) {
                if (Conf.getIndexableAttContentTypes().contains(attachment.getContentType())) {
                    document.add(new TextField("attachments", attachment.getContent(), Field.Store.NO));
                }
            }
        }

        writer.addDocument(document);
        writer.commit();
        log.trace("Audit event {} has been indexed successfully.", auditEvent.getAuditEventId());
    }

    public void close() {
        try {
            writer.close();
        }
        catch (IOException e) {
            log.error("Failed to close Lucene IndexWriter: " + e.getMessage(), e);
        }
    }
}
