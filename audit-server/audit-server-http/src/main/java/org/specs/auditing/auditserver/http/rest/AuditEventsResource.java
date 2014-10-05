package org.specs.auditing.auditserver.http.rest;

import org.apache.log4j.Logger;
import org.specs.auditing.common.auditevent.AuditEvent;
import org.specs.auditing.common.utils.AuditEventDeserializer;
import org.specs.auditing.dal.AuditEventDAO;
import org.specs.auditing.dal.jpa.JpaAuditEventDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/audit-events")
public class AuditEventsResource {
    protected static Logger log = Logger.getLogger(AuditEventsResource.class);
    private AuditEventDeserializer auditEventDeserializer;
    private AuditEventDAO auditEventDAO;

    public AuditEventsResource() throws Exception {
        log.debug("AuditEventsResource constructor called.");
        auditEventDeserializer = new AuditEventDeserializer();
        auditEventDAO = new JpaAuditEventDAO();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createToken(String data) throws Exception {
        AuditEvent auditEvent;
        try {
            auditEvent = auditEventDeserializer.deserialize(data);
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid audit event: " + e.getMessage())
                    .build();
        }

        try {
            auditEventDAO.save(auditEvent);
            log.trace("Audit event has been stored successfully.");
        }
        catch (Exception e) {
            log.error("Failed to store audit event: " + e.getMessage(), e);
            throw new Exception("Failed to store audit event: " + e.getMessage(), e);
        }

        return Response.noContent().build();
    }
}
