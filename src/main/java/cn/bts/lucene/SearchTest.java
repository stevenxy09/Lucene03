package cn.bts.lucene;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
* @author stevenxy E-mail:random_xy@163.com
* @Date 2018年6月13日
* @Description 
*/
public class SearchTest {
	
	private Directory dir;
	private IndexReader reader;
	private IndexSearcher is;	
	
	@Before
	public void setUp() throws Exception {
		dir=FSDirectory.open(Paths.get("D:\\lucene4"));
		reader=DirectoryReader.open(dir);
		is=new IndexSearcher(reader);
	}

	@After
	public void tearDown() throws Exception {
		reader.close();
	}
	
	/**
	 * 对特定项搜索
	 */
	@Test
	public void testTermQuery()throws Exception {
		String searchField="contents";
		String qString="particular";
		Term term=new Term(searchField,qString);
		Query query=new TermQuery(term);
		TopDocs hits=is.search(query, 10);
		System.out.println("匹配'"+qString+"',总共查询到"+hits.totalHits+"个文档");
		for(ScoreDoc scoreDocs:hits.scoreDocs) { 
			Document document=is.doc(scoreDocs.doc);
			System.out.println(document.get("fullPath"));
		}
	}
	
	/**
	 * 解析查询表达式
	 * @throws Exception
	 */
	@Test
	public void testQueryParsers()throws Exception {
		String searchField="contents";
		String q="abc~";
		Analyzer analyzer=new StandardAnalyzer();
		QueryParser parser=new QueryParser(searchField, analyzer);
		Query query=parser.parse(q);
		TopDocs hits=is.search(query, 10);
		System.out.println("匹配'"+q+"',总共查询到"+hits.totalHits+"个文档");
		for(ScoreDoc scoreDoc:hits.scoreDocs) {
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullPath"));
		}
	}
}
