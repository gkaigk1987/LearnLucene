package com.gk.lucene.demo1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	/**
	 * 获取IndexWriter
	 * @param indexStorePath
	 * @return
	 * @throws IOException
	 */
	private IndexWriter getWriter(String indexStorePath) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexStorePath));
		Analyzer analyzer = new StandardAnalyzer();//标准分词器
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE); //生成index的模式(创建模式)
		IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);
		return indexWriter;
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
		IndexWriter indexWriter = getWriter(indexStorePath);
		if(folder.isDirectory()) {
			File[] files = folder.listFiles();
			for(File file : files) {
				Document doc = getDocument(file);
				System.out.println("正在创建索引:"+file.getName());
				indexWriter.addDocument(doc);
			}
		}
		indexWriter.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		String indexFilePath = "D:/Lucene/data";
		String indexStorePath = "D:/Lucene/index";
		Indexer indexer = new Indexer();
		indexer.writeToIndex(indexFilePath,indexStorePath);
	}
}
