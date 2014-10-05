package org.specs.auditing.auditserver.http.rest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.specs.auditing.common.auditevent.AuditEvent;
import org.specs.auditing.common.auditevent.Severity;
import org.specs.auditing.common.auditevent.Target;
import org.specs.auditing.common.utils.AuditEventSerializer;
import org.specs.auditing.dal.jpa.utils.EMF;

import javax.ws.rs.core.MediaType;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@Ignore
public class AuditEventsResourceTest extends JerseyTest {
    private static final String TEST_PU_NAME = "specs-auditing-test-pu";

    public AuditEventsResourceTest() throws Exception {
        super(new WebAppDescriptor.Builder("org.specs.auditing.auditserver.http.rest").build());
    }

    @Before
    public void setUp() throws Exception {
        EMF.init(TEST_PU_NAME);
    }

    @After
    public void tearDown() throws Exception {
        EMF.close();
    }

    @Test
    public void testPostAuditEvent() throws Exception {
        WebResource webResource = resource();
        AuditEventSerializer auditEventSerializer = new AuditEventSerializer();

        AuditEvent auditEvent = createAuditEvent();
        String auditEventJson = auditEventSerializer.serialize(auditEvent);
        ClientResponse response = webResource.path("/audit-events")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, auditEventJson);
        assertEquals(response.getStatus(), 204);
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
