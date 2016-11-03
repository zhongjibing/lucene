package icezhg.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Created by zhongjibing on 2016/11/2.
 */
public class WriteIndex {

    public static final String INDEX_DIR = "index";

    public static void writeIndex(File file) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(new File(INDEX_DIR));
        File indexFile = new File(INDEX_DIR);
        if (!indexFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            indexFile.mkdirs();
        }
        System.out.println(indexFile.getAbsolutePath());

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document doc = new Document();
        doc.add(new TextField("filename", file.getName(), Field.Store.YES));
        doc.add(new TextField("content", txt2String(file), Field.Store.YES));
        doc.add(new TextField("path", file.getPath(), Field.Store.YES));

        indexWriter.addDocument(doc);
        indexWriter.commit();
        indexWriter.close();
    }

    private static String txt2String(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
