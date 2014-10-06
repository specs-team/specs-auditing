package org.specs.auditing.queryapi.rest;

import org.apache.log4j.Logger;
import org.specs.auditing.dal.jpa.model.AuditEvent;
import org.specs.auditing.dal.jpa.utils.EMF;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/audit-events")
public class AuditEventsResource {
    protected static Logger log = Logger.getLogger(AuditEventsResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public AuditEvent getAuditEvent(@PathParam("id") int auditEventId) {
        EntityManager em = EMF.createEntityManager();
        try {
            AuditEvent auditEvent = em.find(AuditEvent.class, auditEventId);
            return auditEvent;
        }
        finally {
            EMF.closeEntityManager(em);
        }
    }
}
