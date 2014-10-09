package org.specs.auditing.queryapi.rest;

import com.sun.jersey.spi.resource.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.specs.auditing.model.utils.JsonUtils;
import org.specs.auditing.queryapi.utils.Conf;
import org.specs.auditing.queryapi.utils.LuceneIndexSearcher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/lucene-search")
@Singleton
public class LuceneSearchResource {
    private static Logger log = LogManager.getLogger(LuceneSearchResource.class);
    private LuceneIndexSearcher luceneIndexSearcher;

    public LuceneSearchResource() throws IOException {
        log.debug("LuceneSearchResource constructor called.");
        if (Conf.isLuceneIndexSearchEnabled()) {
            luceneIndexSearcher = new LuceneIndexSearcher();
        }
    }

    @GET
    public Response search(@QueryParam("query") String query) throws IOException, ParseException {
        List<Integer> idList = luceneIndexSearcher.search(query);
        String json = JsonUtils.getObjectMapper().writeValueAsString(idList);
        return Response.ok(json).build();
    }
}
