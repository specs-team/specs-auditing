package org.specs.auditing.client;

import org.specs.auditing.common.auditevent.AuditEvent;

public interface Auditor {

    public void audit(AuditEvent auditEvent);

    public void close();

}
