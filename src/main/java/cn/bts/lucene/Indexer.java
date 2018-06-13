package cn.bts.lucene;
/**
* @author stevenxy E-mail:random_xy@163.com
* @Date 2018年6月13日
* @Description 创建索引
*/

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	private IndexWriter indexWriter;
	
	/**
	 * 构造方法 实例化IndexWriter
	 * @param indexDir
	 * @throws Exception
	 */
	public Indexer(String indexDir)throws Exception {
		Directory directory=FSDirectory.open(Paths.get(indexDir));
		Analyzer analyzer=new StandardAnalyzer();
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
		indexWriter=new IndexWriter(directory, iwc);
		
	}
	
	/**
	 * 关闭写索引
	 * @throws Exception
	 */
	public void close()throws Exception{
		indexWriter.close();
	}
	
	/**
	 * 索引指定的所有文件
	 * @param dataDir
	 * @throws Exception
	 */
	public int index(String dataDir)throws Exception{
		File[] files=new File(dataDir).listFiles();
		for(File file:files) {
			indexFile(file);
		}
		return indexWriter.numDocs();
	}
	
	/**
	 * 索引指定文件
	 * @param file
	 */
	private void indexFile(File file)throws Exception {
		System.out.println("索引文件:"+file.getCanonicalPath());
		Document document=getDocument(file);
		indexWriter.addDocument(document);
	}
	
	/**
	 * 获取文档,文档里在设置每个字段
	 * @param file
	 */
	private Document getDocument(File file)throws Exception {
		Document doc=new Document();
		doc.add(new TextField("contents",new FileReader(file)));
		doc.add(new TextField("fileName", file.getName(),Field.Store.YES));
		doc.add(new TextField("fullPath", file.getCanonicalPath(),Field.Store.YES));
		return doc;
	}
	
	public static void main(String[] args) {
		String indexDir="D:\\lucene4";
		String dataDir="D:\\lucene4\\data";
		Indexer indexer = null;
		int numberIndex=0;
		long start=System.currentTimeMillis();
		try {
			indexer=new Indexer(indexDir);
			numberIndex=indexer.index(dataDir);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				indexer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long end=System.currentTimeMillis();
		System.out.println("索引:"+numberIndex+"个文件 花费了"+(end-start)+"毫秒");
	}
}
