package com.gk.lucene.demo2;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Searcher {

	private IndexSearcher indexSearcher;
	
	private IndexReader indexReader;
	
	public Searcher() {
		
	}
	
	public Searcher(String indexStorePath) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexStorePath));
		indexReader = DirectoryReader.open(dir);
		indexSearcher = new IndexSearcher(indexReader);
	}
	
	public void search(String keyword) throws Exception {
//		Analyzer analyzer = new IKAnalyzer();//new StandardAnalyzer();
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser("content", analyzer);
		Query query = parser.parse(keyword);
		TopDocs topDocs = indexSearcher.search(query, 10);
		for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			System.out.println("检索到内容的文件："+doc.get("path"));
		}
	}
	
	public void close() throws IOException {
		indexReader.close();
	}
	
	public static void main(String[] args) throws Exception {
		Searcher searcher = new Searcher("D:/Lucene/index");
		searcher.search("东南第一学");
		searcher.close();
	}
}
