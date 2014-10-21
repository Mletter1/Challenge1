import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LuceneDemo {
    public static final String FILE_NAME = "/Users/matthewletter/Documents/Challenge1/Hadoop2.5.1/input/abstract_step5";

    public static void main(String[] args) throws IOException, ParseException {
        String currentLine = "(aa,24915.txt)\t(2,22,2,0.090909,9.931005,0.902819)";
        String[] currentLineArray;

        String word = "";
        String documentId = "";
        String A = "";
        String B = "";
        String C = "";
        String D = "";
        String E = "";
        String F = "";
        Scanner myScanner = new Scanner(new File(FILE_NAME));

        Directory directory = new RAMDirectory();
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_40);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);


        double time = System.currentTimeMillis();
        while(myScanner.hasNext()){
            currentLine = myScanner.nextLine();
            currentLine = currentLine.trim();
            currentLine = currentLine.replaceAll("\\).+\\(",",");
            //System.out.println(currentLine);
            currentLine = currentLine.replaceAll("\\(","");
            currentLine = currentLine.replaceAll("\\)","");
            currentLine = currentLine.replaceAll("\\s+","");
            //System.out.println(currentLine);

            currentLineArray = currentLine.split(",");
            //System.out.println(currentLineArray.length);

            if(currentLineArray.length > 7) {
                word = currentLineArray[0];
                documentId = currentLineArray[1];
                A = currentLineArray[2];
                B = currentLineArray[3];
                C = currentLineArray[4];
                D = currentLineArray[5];
                E = currentLineArray[6];
                F = currentLineArray[7];
                //System.out.println(word + " " + F);
            }
            addDoc(writer, word, documentId, A, B, C, D, E, F);
        }
        System.out.print((System.currentTimeMillis() - time) /1000);
        //addDoc(writer, "aa", "24915.txt", "2", "22", "2", "0.090909", "9.931005", "0.902819");
        writer.close();
        DirectoryReader reader = DirectoryReader.open(directory);
        DocsEnum de = MultiFields.getTermDocsEnum(reader, MultiFields.getLiveDocs(reader), "word", new BytesRef("aa"));

        if(de != null) {
            int doc;
            while ((doc = de.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
                System.out.println(de.freq());
            }
        }
        else{
            System.out.println("null");
        }

        reader.close();

        IndexReader reader1 = DirectoryReader.open(directory);
        System.out.println(reader1.docFreq(new Term("documentId", "2")));
        reader.close();
    }

    private static void addDoc(IndexWriter w, String word, String documentId, String A,String B,
                               String C, String D, String E, String F) throws IOException {
        FieldType fieldType = new FieldType();
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setIndexed(true);
        fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(true);

        Document doc = new Document();
        doc.add(new Field("word", word, fieldType));
        doc.add(new Field("documentId", documentId, fieldType));
        doc.add(new Field("A", documentId, fieldType));
        doc.add(new Field("B", documentId, fieldType));
        doc.add(new Field("C", documentId, fieldType));
        doc.add(new Field("D", documentId, fieldType));
        doc.add(new Field("E", documentId, fieldType));
        doc.add(new Field("F", documentId, fieldType));
        w.addDocument(doc);
    }
}