package org.specs.auditing.auditserver.http.utils;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfTest {

    @Before
    public void setUp() throws Exception {
        Conf.load("src/test/resources/test-config.xml");
    }

    @Test
    public void testGetIndexableAttachmentContentTypes() throws Exception {
        Set<String> contentTypes = Conf.getIndexableAttContentTypes();
        assertEquals(contentTypes.size(), 2);
        assertTrue(contentTypes.contains(MediaType.TEXT_PLAIN));
    }
}