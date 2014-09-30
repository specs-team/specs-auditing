package org.specs.auditing.client;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ServiceLoader;

public class AuditorFactory {
    private static Logger log = Logger.getLogger(AuditorFactory.class);
    private static Auditor auditor;

    private AuditorFactory() {
    }

    public static void init(String confFilePath) throws Exception {
        if (auditor != null) {
            throw new Exception("AuditorFactory already initialized.");
        }

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(confFilePath));
            log.info(String.format("Configuration loaded successfully from file '%s'.", confFilePath));
        }
        catch (IOException e) {
            throw new Exception(String.format("Failed to read configuration file '%s': %s", confFilePath, e.getMessage()));
        }

        init(props);
    }

    public static void init(Properties props) throws Exception {
        if (auditor != null) {
            throw new Exception("AuditorFactory already initialized.");
        }

        // discover the Auditor implementation class using the ServiceLoader
        ServiceLoader<Auditor> serviceLoader = ServiceLoader.load(Auditor.class);
        if (serviceLoader.iterator().hasNext()) {
            auditor = serviceLoader.iterator().next();
            log.debug("Using Auditor " + auditor.getClass().getName());
            try {
                auditor.init(props);
            }
            catch (Exception e) {
                throw new Exception(String.format(
                        "Failed to initialize the Auditor %s: %s", auditor.getClass().getName(), e.getMessage()));
            }
            log.info(String.format("Auditor %s has been initialized successfully.", auditor.getClass().getName()));
        }
        else {
            throw new Exception("No Auditor found on the classpath.");
        }
    }

    public static Auditor getAuditor() {
        if (auditor == null) {
            throw new RuntimeException("AuditorFactory is not initialized.");
        }

        return auditor;
    }
}
