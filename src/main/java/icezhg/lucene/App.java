package icezhg.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        writeIndex();
        queryIndex();
    }

    private static void writeIndex() throws IOException {
        WriteIndex.writeIndex(new File("test.txt"));
    }

    private static void queryIndex() {
        List<Document> docs = QueryIndex.searchIndex("String+class");
        for (Document doc : docs) {
            System.out.println(doc.get("filename"));
            System.out.println(doc.get("content"));
            System.out.println(doc.get("path"));
        }
    }
}
