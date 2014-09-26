package org.specs.auditing.dal;

import org.specs.auditing.common.auditevent.AuditEvent;

public interface AuditEventDAO {

    public void save(AuditEvent auditEvent);

    public void retrieve(int auditEventId);
}
