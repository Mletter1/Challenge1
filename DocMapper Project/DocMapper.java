package challenge1;

//package org.myorg;
//Source: http://www.cloudera.com/content/cloudera/en/documentation/HadoopTutorial/CDH4/Hadoop-Tutorial/ht_wordcount1_source.html?scroll=topic_5_1

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.metrics.file.FileContext;
import org.apache.hadoop.util.*;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.servlet.Context;

import challenge1.PorterStemmer;

public class DocMapper {
	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> 
	{
		//private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
		private String stemTerm( String term ) {
			PorterStemmer stemmer = new PorterStemmer();
			return stemmer.stem(term);
		}

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			// Method 2 (from Hint in assignment) - Collect Document Name  
			Text doc = new Text();
			FileSplit filesplit = (FileSplit) reporter.getInputSplit();
			String fileName = filesplit.getPath().getName();
			doc.set(fileName.toString());
			
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line);
			
			while (tokenizer.hasMoreTokens()) {
				String w = stemTerm( tokenizer.nextToken().toLowerCase() );
				
				if( Stopwords.isStopWord(w) ) {
					word.set( w );
					output.collect(word, doc);
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> 
	{	
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			int sum = 0;
			
			while (values.hasNext()) {
				sum += 1;
			}
			
			output.collect(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("docmapper");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
