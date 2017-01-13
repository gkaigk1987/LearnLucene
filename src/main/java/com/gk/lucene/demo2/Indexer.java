package com.gk.lucene.demo2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.sample.IKAnalzyerDemo;

public class Indexer {
	
	private IndexWriter indexWriter;
	
	public Indexer() {
		
	}
	
	public Indexer(String indexStorePath) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexStorePath));
//		Analyzer analyzer = new IKAnalyzer();//new StandardAnalyzer();//标准分词器
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();//中文分词器
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE); //生成index的模式(创建模式)
		indexWriter = new IndexWriter(dir, indexWriterConfig);
	}
	
	/**
	 * 建立文档
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private Document getDocument(File file) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("content", new FileReader(file)));
		doc.add(new TextField("name", file.getName(), Field.Store.YES));
		doc.add(new TextField("path", file.getCanonicalPath(), Field.Store.YES));
		return doc;
	}
	
	/**
	 * 创建索引
	 * @param indexFilePath
	 * @param indexStorePath
	 * @throws Exception
	 */
	public void writeToIndex(String indexFilePath,String indexStorePath) throws Exception {
		File folder = new File(indexFilePath);
		if(folder.isDirectory()) {
			File[] files = folder.listFiles();
			for(File file : files) {
				Document doc = getDocument(file);
				System.out.println("正在创建索引:"+file.getName());
				indexWriter.addDocument(doc);
			}
		}
		
	}
	
	public void close() throws IOException {
		if(indexWriter != null) {
			indexWriter.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String indexFilePath = "D:/Lucene/data";
		String indexStorePath = "D:/Lucene/index";
		Indexer indexer = new Indexer(indexStorePath);
		indexer.writeToIndex(indexFilePath,indexStorePath);
		indexer.close();
	}
}
