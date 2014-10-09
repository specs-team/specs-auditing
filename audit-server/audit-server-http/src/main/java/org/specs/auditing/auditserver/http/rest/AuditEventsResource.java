package org.specs.auditing.auditserver.http.rest;

import com.sun.jersey.spi.resource.Singleton;
import org.apache.log4j.Logger;
import org.specs.auditing.auditserver.http.utils.AuditEventsIndexer;
import org.specs.auditing.auditserver.http.utils.Conf;
import org.specs.auditing.model.entities.AuditEvent;
import org.specs.auditing.model.utils.EMF;
import org.specs.auditing.model.utils.JsonUtils;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/audit-events")
@Singleton
public class AuditEventsResource {
    protected static Logger log = Logger.getLogger(AuditEventsResource.class);
    private AuditEventsIndexer auditEventsIndexer;

    public AuditEventsResource() throws Exception {
        log.debug("AuditEventsResource constructor called.");
        if (Conf.isIndexerEnabled()) {
            auditEventsIndexer = new AuditEventsIndexer();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response storeAuditEvent(String data) throws Exception {
        AuditEvent auditEvent;
        try {
            auditEvent = JsonUtils.fromJson(data);
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid audit event: " + e.getMessage())
                    .build();
        }

        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(auditEvent);
            em.getTransaction().commit();

            if (log.isTraceEnabled()) {
                log.trace("Audit event has been stored successfully: " + data);
            }
        }
        catch (Exception e) {
            log.error("Failed to store audit event: " + e.getMessage(), e);
            throw new Exception("Failed to store audit event: " + e.getMessage(), e);
        }

        if (auditEventsIndexer != null) {
            try {
                auditEventsIndexer.add(auditEvent);
            }
            catch (IOException e) {
                log.error(String.format("Failed to index audit event %d: %s",
                        auditEvent.getAuditEventId(), e.getMessage()), e);
            }
        }

        return Response.noContent().build();
    }
}
