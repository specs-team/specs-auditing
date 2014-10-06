package org.specs.auditing.queryapi.utils;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class Conf {
    private static Conf instance = new Conf();
    private static Logger log = Logger.getLogger(Conf.class);
    private static XMLConfiguration config;

    private Conf() {
    }

    public static void load(String configFile) throws Exception {
        try {
            config = new XMLConfiguration(configFile);
            log.info(String.format("Configuration was loaded successfully from file '%s'.", configFile));
        }
        catch (Exception e) {
            throw new Exception(String.format("Failed to read configuration file '%s': %s", configFile,
                    e.getMessage()));
        }
    }
}
