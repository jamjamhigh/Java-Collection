/*
 * 実行環境：
 * JREはSE-1.7
 */

package Fourth;

import java.util.Set;

import javax.xml.soap.Node;

import org.w3c.dom.NodeList;

/*
 * Crawlerクラス。
 * ページから文字列を引っ張りだしてデータベースの型に当てはめるクラス。
 */
public class Crawler{
	public Crawler(String dbname)
	{
		init();
	}

	private void init()
	{
	}

	public void addtoindex(String url, NodeList soup)
	{
		System.out.println("Indexing " + url);
	}
	
	public String gettextonly(Node soup)
	{
		return null;
	}
	
	public String gettextonly(NodeList children)
	{
		return null;
	}
	
	public Boolean isindexed(String url)
	{
		return null;
	}
	
	public void addlinkref(String urlFrom,String urlTo, String linkText)
	{
	}
	
	public void crawl(Set<String> pages, int depth)
	{
	}
	
	public void createindextables()
	{
	}
}

