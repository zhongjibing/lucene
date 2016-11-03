package icezhg.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhongjibing on 2016/11/2.
 */
public class QueryIndex {

    private static final Logger LOG = LoggerFactory.getLogger(QueryIndex.class);

    public static List<Document> searchIndex(String text) {
        List<Document> retList = new ArrayList<>();
        try {
            Directory directory = FSDirectory.open(new File(WriteIndex.INDEX_DIR));
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(text);
            BooleanQuery boolQuery = new BooleanQuery();

            boolQuery.add(query, BooleanClause.Occur.SHOULD);
            boolQuery.add(new TermQuery(new Term("filename", "txt")), BooleanClause.Occur.SHOULD);

            ScoreDoc[] hits = isearcher.search(boolQuery, null, 1000).scoreDocs;

            for (ScoreDoc hit : hits) {
                retList.add(isearcher.doc(hit.doc));
            }
        } catch (IOException | ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return retList;
    }
}
