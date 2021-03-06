package org.specs.auditing.auditserver.http;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;

public class Utils {
    private static Logger log = Logger.getLogger(Utils.class);

    public static void dropTestDatabase() throws Exception {
        log.debug("Dropping Derby test database...");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:stsDB;drop=true");
            conn.close();
        }
        catch (SQLNonTransientConnectionException e) {
            // nothing wrong
            log.info("Test database has been dropped successfully.");
        }
    }
}
