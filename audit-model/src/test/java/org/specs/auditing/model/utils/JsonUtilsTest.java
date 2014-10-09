package org.specs.auditing.model.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.specs.auditing.model.entities.Attachment;
import org.specs.auditing.model.entities.AuditEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

    @Test
    public void testToJson() throws Exception {
        AuditEvent auditEvent = createAuditEvent();

        String json = JsonUtils.toJson(auditEvent);
        AuditEvent auditEvent1 = JsonUtils.fromJson(json);
        String json1 = JsonUtils.toJson(auditEvent1);
        assertEquals(json, json1);
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