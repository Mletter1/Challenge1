package com.ml.clusterer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

//import org.apache.mahout.clustering.display.DisplayKMeans;


public class DocumentClusterer {
	private static String dictPath  = "/users/waybrd/cluster/input/dictionary.csv";				// Local filesystem
	private static String tfidfPath = "/users/waybrd/cluster/input/doc_term_normTFIDF.csv";		// Local filesystem
	private static String clusterVectors = "/users/waybrd/cluster/input/clusterVectors";		// HDFS filesystem
	private static String clusterInit    = "/users/waybrd/cluster/input/randomStart";			// HDFS filesystem
	
	private static java.util.Vector<String> dict;
	private static List<Vector> data;
	
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
	
	
	private static void loadTFIDF() {
		loadDictionary();
		
		data = new ArrayList<Vector>();
		
		try {
			Scanner sc = new Scanner( new File(tfidfPath) );
			String docID = "1";
			String oldID = "1";
			Vector v = new NamedVector( new RandomAccessSparseVector(dict.size()), docID);
			
			int startIndex = 0;
			
			
			while(sc.hasNextLine()) {
				String[] line = sc.nextLine().split(",");
				
				if( line.length > 2 ) {
					docID    = line[0].trim();
					String word  = line[1].trim();
					double tfidf = Double.parseDouble(line[2].trim());
					
					if( !docID.equals(oldID) ) {
						data.add(v);		
						v = new NamedVector( new RandomAccessSparseVector(dict.size()), docID);
						startIndex = 0;
						oldID = docID;
					}
					
					// Find index for word and set in vector
					int index = dict.indexOf(word, startIndex);
					v.set(index, tfidf);
					startIndex = index;
				}
			}
			
			data.add(v);	
			
			sc.close();
		} 
		catch(IOException e) {
			System.out.println(e.toString());
		}
		
		// Release dictionary
		dict = null;
		System.gc();
	}
	
	
	public static void main(String[] args) throws IOException 
	{	
		// Create the TF-IDF Vectors from dictionary and map-reduce output file
		loadTFIDF();

		// Now that we have vectors, open sequenceWriter and output them
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		SequenceFile.Writer writer;
		
		writer = new SequenceFile.Writer(fs, conf, new Path(clusterVectors), LongWritable.class, VectorWritable.class);
		long recNum = 0;
		VectorWritable vec = new VectorWritable();
		for ( Vector point : data ) {
		    vec.set(point);
		    writer.append(new LongWritable(recNum++), vec);
		}
		writer.close();
		
		System.out.println("Finished writing clusterVectors");
		
		/**
		// Need to seed with random clusters to begin computation from, and add a vector to it
		writer = new SequenceFile.Writer(fs, conf, new Path(clusterInit), Text.class, Kluster.class);
		
		int k = 10;
		for (int i = 0; i < k; i++) {
		    Vector v = data.get(50 - i);
		    Kluster cluster = new Kluster(v, i, new CosineDistanceMeasure());
		    writer.append(new Text(cluster.getIdentifier()), cluster);
		}
		writer.close();
		
		System.out.println("Finished writing randomStart");
		**/
		
		// Canopy Generates an initial cluster configuration
		try {
			CanopyDriver.run(conf, new Path(clusterVectors), new Path(clusterInit), new CosineDistanceMeasure(), 0, 1, true, 0, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Attempt to run KMeans
		try {
			
			KMeansDriver.run(conf,										// Hadoop Configuration
							 new Path(clusterVectors),					// Path to vectors
							 new Path(clusterInit + "/clusters-0-final/part-r-00000"),		// Path to initial clusters
							 new Path("/users/waybrd/cluster/output/"),	// where to dump clustering output?
							 0.001,										// Convergence Delta
							 10,										// Max Iterations
							 true,										// runClustering
							 0,											// clusterClassificationThreshold
							 true);										// runSequential
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}										
		

		/**
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path("/users/waybrd/cluster/output/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-0"), conf);
	
		IntWritable key = new IntWritable();
		WeightedPropertyVectorWritable value = new WeightedPropertyVectorWritable();
	 
		while (reader.next(key, value)) {
			System.out.println(value.toString() + " belongs to cluster " + key.toString());
		}
		reader.close();
		**/
	}
}
