package org.specs.auditing.model.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.specs.auditing.model.entities.Attachment;
import org.specs.auditing.model.entities.AuditEvent;

import java.io.IOException;

public class JsonUtils {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String toJson(AuditEvent auditEvent) throws JsonProcessingException {
        return objectMapper.writeValueAsString(auditEvent);
    }

    public static AuditEvent fromJson(String json) throws IOException {
        AuditEvent auditEvent = objectMapper.readValue(json, AuditEvent.class);
        for (Attachment attachment : auditEvent.getAttachmentList()) {
            attachment.setAuditEvent(auditEvent);
        }
        return auditEvent;
    }
}
