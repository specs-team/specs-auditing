package org.specs.auditing.queryapi.utils;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Conf {
    private static Logger log = LogManager.getLogger(Conf.class);
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

    public static boolean isLuceneIndexSearchEnabled() {
        return config.getBoolean("luceneIndexSearch.enabled");
    }

    public static String getLuceneIndexDir() {
        return config.getString("luceneIndexSearch.indexDirectory");
    }

    public static int getLuceneMaxHits() {
        return config.getInt("luceneIndexSearch.maxHits");
    }
}
