import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by matthewletter on 10/16/14.
 */
public class vectorBuilder {
    private static String dictPath  = "/Users/matthewletter/Documents/Challenge1/matLab/dictionary.csv";				// Local filesystem
    private static String tfidfPath = "/Users/matthewletter/Documents/Challenge1/matLab/doc_term_normTFIDF.csv";		// Local filesystem
    private static Vector<String> dict;
    public static void main(String [] args)
    {
        loadDictionary();
        generateCsvFile("/Users/matthewletter/Documents/Challenge1/matLab/vectorMatrix.csv");
    }

    private static void generateCsvFile(String sFileName)
    {
        //id,word,tfidf
        try
        {
            FileWriter writer = new FileWriter(sFileName);

//            writer.append("DisplayName");
//            writer.append(',');
//            writer.append("Age");
//            writer.append('\n');
//
//            writer.append("MKYONG");
//            writer.append(',');
//            writer.append("26");
//            writer.append('\n');
//
//            writer.append("YOUR NAME");
//            writer.append(',');
//            writer.append("29");
//            writer.append('\n');
            Scanner sc = new Scanner( new File(tfidfPath) );
            System.out.println(dict.size());
            String docID = "1";
            String oldID = "1";
            double doneNess = 0;
            int startIndex = 0;
            Vector v = new Vector(dict.size(), 1);
            for(int i = 0; i<dict.size();i++){
                v.add("0");
            }
            //generate whatever data you want
            double time = System.currentTimeMillis();
            while(sc.hasNext()) {
                String[] line = sc.nextLine().split(",");
                if(Integer.parseInt(docID) % 2000 ==0){
                    doneNess += 0.5;
                    System.out.println(doneNess + "% done");
                }

                if( line.length > 2 ) {
                    docID    = line[0].trim();
                    String word  = line[1].trim();
                    String tfidf = line[2].trim();

                    if( !docID.equals(oldID) ) {
                        writer.append(v.toString().replaceAll("\\[", "").replaceAll("]", ""));
                        writer.append('\n');
                        v = new Vector(dict.size(), 1);
                        for(int i = 0; i<dict.size();i++){
                            v.add("0");
                        }
                        startIndex = 0;
                        oldID = docID;
                    }

                    // Find index for word and set in vector
                    int index = dict.indexOf(word, startIndex);
                    v.set(index, tfidf);
                    startIndex = index;
                }
            }
            System.out.println("built vectors in: "+((System.currentTimeMillis()-time)/1000) +"seconds");
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Loads the dictionary file into an normal Java Vector because it supports
     * getting an objects index.
     */
    private static void loadDictionary() {
        dict = new java.util.Vector<String>();

        try {
            Scanner sc = new Scanner( new File(dictPath) );

            while(sc.hasNextLine()) {
                String word = sc.next().trim();

                if( !word.equals("") )
                    dict.add( word );
            }

            sc.close();
        }
        catch(IOException e) {
            System.out.println(e.toString());
        }
    }

}
