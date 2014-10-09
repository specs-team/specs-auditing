package org.specs.auditing.queryapi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LuceneIndexSearcher {
    private static Logger log = LogManager.getLogger(LuceneIndexSearcher.class);
    private Directory directory;
    private IndexSearcher indexSearcher;
    private QueryParser parser;

    public LuceneIndexSearcher() throws IOException {
        directory = FSDirectory.open(new File(Conf.getLuceneIndexDir()));
        DirectoryReader dirReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(dirReader);
        Analyzer analyzer = new StandardAnalyzer();
        parser = new QueryParser("attachments", analyzer);

        log.debug("LuceneIndexSearcher has been initialized successfully.");
    }

   public List<Integer> search(String searchQuery) throws ParseException, IOException {
       Query query = parser.parse(searchQuery);
       ScoreDoc[] hits = indexSearcher.search(query, Conf.getLuceneMaxHits()).scoreDocs;

       List<Integer> idList = new ArrayList<Integer>();
       for (int i = 0; i < hits.length; i++) {
           Document hitDoc = indexSearcher.doc(hits[i].doc);
           idList.add(Integer.valueOf(hitDoc.get("id")));
       }

       return idList;
   }

    public void close() {
        try {
            directory.close();
        }
        catch (IOException e) {
            log.error("Failed to close the Lucene Directory: " + e.getMessage(), e);
        }
    }
}
