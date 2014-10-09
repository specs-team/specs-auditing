package org.specs.auditing.auditserver.http.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.specs.auditing.auditserver.http.utils.Conf;
import org.specs.auditing.model.entities.Attachment;
import org.specs.auditing.model.entities.AuditEvent;
import org.specs.auditing.model.utils.EMF;
import org.specs.auditing.model.utils.JsonUtils;

import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Ignore
public class AuditEventsResourceTest extends JerseyTest {
    private static final String TEST_PU_NAME = "specs-auditing-test-pu";

    public AuditEventsResourceTest() throws Exception {
        super(new WebAppDescriptor.Builder("org.specs.auditing.auditserver.http.rest").build());
    }

    @Before
    public void setUp() throws Exception {
        Conf.load("src/test/resources/test-config.xml");
        EMF.init(TEST_PU_NAME);
    }

    @After
    public void tearDown() throws Exception {
        EMF.close();
    }

    @Test
    public void testPostAuditEvent() throws Exception {
        WebResource webResource = resource();

        AuditEvent auditEvent = createAuditEvent();
        String json = JsonUtils.toJson(auditEvent);
        ClientResponse response = webResource.path("/audit-events")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, json);
        assertEquals(response.getStatus(), 204);
    }

    private AuditEvent createAuditEvent() throws JsonProcessingException {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setAction("READ");
        auditEvent.setEventTime(new Date());
        auditEvent.setEventType("REST_API_CALL");
        auditEvent.setInitiatorId("test_user");
        auditEvent.setInitiatorType("USER");
        auditEvent.setTargetId("specs-server1/federation-api");
        auditEvent.setTargetType("WEB_SERVICE");
        auditEvent.setSeverity("INFO");
        auditEvent.setOutcome("SUCCESS");

        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("method", "GET");
        requestData.put("uri", "https://specs-server1/federation-api/users/523aebaa-cf04-4bd4-b067-dcf17e74ff50");
        Attachment attachment1 = new Attachment("httpRequestData", "application/json", requestData);
        auditEvent.addAttachment(attachment1);

        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("statusCode", 200);
        responseData.put("contentType", "application/json");
        responseData.put("content", "{'userId':'523aebaa-cf04-4bd4-b067-dcf17e74ff50', 'username':'test_user'}");
        Attachment attachment2 = new Attachment("httpResponseData", "application/json", responseData);
        auditEvent.addAttachment(attachment2);

        return auditEvent;
    }
}
