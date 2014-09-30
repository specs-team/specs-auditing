package org.specs.auditing.client;

import org.specs.auditing.common.auditevent.AuditEvent;

import java.io.IOException;
import java.util.Properties;

public interface Auditor {

    public void init(Properties props) throws IOException;

    public void audit(AuditEvent auditEvent);

    public void close();

}
