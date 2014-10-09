package org.specs.auditing.auditserver.http.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.specs.auditing.model.utils.EMF;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebAppInitializer implements ServletContextListener {
    private static Logger log = LogManager.getLogger(WebAppInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ServletContext context = servletContextEvent.getServletContext();
            String configFilePath = context.getInitParameter("conf-file");
            if (configFilePath == null) {
                throw new RuntimeException("Missing parameter 'conf-file' in web.xml file.");
            }

            // load configuration file
            Conf.load(configFilePath);

            // initialize JPA context
            EMF.init();

            log.info("Audit-server-http was initialized successfully.");
        }
        catch (Exception e) {
            log.error("Failed to initialize audit-server-http: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize audit-server-http.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
