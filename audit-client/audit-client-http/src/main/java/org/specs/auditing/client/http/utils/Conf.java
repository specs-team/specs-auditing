package org.specs.auditing.client.http.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Conf {

    private static Logger log = Logger.getLogger(Conf.class);
    private static Properties props;

    private Conf() {
    }

    public static void load(String confFilePath) throws Exception {
        load(new File(confFilePath));
    }

    public static void load(File confFile) throws Exception {
        props = new Properties();
        try {
            props.load(new FileInputStream(confFile));
            log.info(String.format("Configuration loaded successfully from file '%s'.", confFile));
        }
        catch (IOException e) {
            throw new Exception(String.format("Failed to read configuration file '%s': %s", confFile, e.getMessage()));
        }
    }

    public static void load(Properties props1) {
        props = props1;
    }

    public static Properties getProps() {
        return props;
    }

    public static String getAuditServerAddress() {
        return props.getProperty("audit-client-http.auditServerAddress");
    }

    public static int getConnRetryTime() {
        return Integer.parseInt(props.getProperty("audit-client-http.retryTime"));
    }
}
