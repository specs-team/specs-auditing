package org.specs.auditing.auditserver.http.utils;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static boolean isIndexerEnabled() {
        return config.getBoolean("indexing.enabled");
    }

    public static Set<String> getIndexableAttContentTypes() {
        List<Object> contentTypeList = config.getList("indexing.indexableAttachments.contentType");

        Set<String> contentTypeSet = new HashSet<String>();
        for (Object contentType : contentTypeList) {
            contentTypeSet.add(contentType.toString());
        }
        return contentTypeSet;
    }

    public static String getLuceneIndexDir() {
        return config.getString("indexing.indexDirectory");
    }
}
