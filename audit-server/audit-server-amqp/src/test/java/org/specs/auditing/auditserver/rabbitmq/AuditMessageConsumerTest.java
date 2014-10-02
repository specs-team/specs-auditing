package org.specs.auditing.auditserver.rabbitmq;

import org.apache.log4j.Logger;
import org.specs.auditing.auditserver.rabbitmq.utils.Conf;
import org.specs.auditing.client.Auditor;
import org.specs.auditing.client.AuditorFactory;
import org.specs.auditing.common.auditevent.AuditEvent;
import org.specs.auditing.common.auditevent.Severity;
import org.specs.auditing.common.auditevent.Target;
import org.specs.auditing.dal.AuditEventDAO;
import org.specs.auditing.dal.jpa.JpaAuditEventDAO;

import java.util.Date;

public class AuditMessageConsumerTest {
    private static final String CONF_FILE = "src/test/resources/test.properties";
    private static final String TEST_PU_NAME = "specs-auditing-test-pu";
    private static final int NUMBER_OF_AUDIT_RECORDS = 3;

    private static Logger log = Logger.getLogger(AuditMessageConsumerTest.class);

    public static void main(String[] args) throws Exception {
        Conf.load(CONF_FILE);
        AuditorFactory.init(Conf.getProps());

        new AuditMessageConsumerTest().test();
    }

    public void test() throws Exception {
        AuditMessageConsumer auditMessageConsumer = new AuditMessageConsumer();
        AuditEventDAO auditEventDAO = new JpaAuditEventDAO(TEST_PU_NAME);
        auditMessageConsumer.setAuditEventDAO(auditEventDAO);
        auditMessageConsumer.start();
        Thread.sleep(500);

        Auditor auditor = AuditorFactory.getAuditor();

        for (int i = 0; i < NUMBER_OF_AUDIT_RECORDS; i++) {
            log.info("Auditing event " + i);
            AuditEvent auditEvent = createAuditEvent();
            auditor.audit(auditEvent);
            Thread.sleep(1000);
        }

        auditor.close();
        auditMessageConsumer.close();
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
