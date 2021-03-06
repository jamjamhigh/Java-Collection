
package Fourth;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;



/*
 * Crawlerクラス。
 * ページからデータを撮ってきてDB化して出力する。
 */
public class Crawler
{
	//無視する単語を設定する
	static Set<String> ignorewords = new HashSet<String>();
	static {
		ignorewords.add("the");
		ignorewords.add("of");
		ignorewords.add("to");
		ignorewords.add("and");
		ignorewords.add("a");
		ignorewords.add("in");
		ignorewords.add("is");
		ignorewords.add("it");
	}
	
	private String dbname = null; //データベースの名前
	private Connection con = null; //特定のデータベースと接続するための

	//メイン関数
	/*
	public static void main(String[] args) throws ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		Set<String> pagelist=new HashSet<String>();
		pagelist.add("http://kiwitobes.com/wiki/Perl.html");
		Crawler crawler = new Crawler("test.db");
		crawler.createindextables();
		crawler.crawl(pagelist, 2);
	}
	*/
	
	//データベースへの埋め込みを始める関数。　dbnameに接続文字列を入れる。
	public Crawler(String dbname) throws ClassNotFoundException {
		this.dbname = dbname;
		init();
	}

	//Derbyを起動させる関数
	private void init() throws ClassNotFoundException {
		if (this.con == null) {
			try {
				//SQLiteのデータベースに接続
				Class.forName("org.sqlite.JDBC");
				this.con = DriverManager.getConnection("jdbc:sqlite:" + this.dbname);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//データベースと接続する関数
	public void dbcommit() {
		try {
			this.con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	//そのサイトのページ数（深さ）だけ繰り返し解析する関数
	public void crawl(Set<String> pages, int depth) throws ClassNotFoundException {
		for (int i = 0; i < depth; i++) {
			Set<String> newpages = new HashSet<String>();
			for (String page : pages) { //そのページの数だけ繰り返す
				NodeList soup;
				try { //Parserクラスを使って解析する一連の流れ
					Parser parser = new Parser();
					parser.setResource(page);
					soup = parser.parse(null);
					addtoindex(page, soup); //目録にそのページと、解析した内容を追加
				} catch (ParserException e) { //エラーに成ったときは
					System.out.println(page + "は開けません");
					e.printStackTrace();
					continue;
				}
				// soupをフィルタリングして、それをlinksとする。
				NodeList links = soup.extractAllNodesThatMatch(new TagNameFilter("a"), true);
				for (int j = 0; j < links.size(); j++) { //linksのサイズ分だけ繰り返す
					TagNode link = (TagNode) links.elementAt(j); //HTMLとしてのノードとして整理するための。
					
					//もし、ページ内にhrefのリンクタグがあった場合
					if (link.getAttribute("href") != null) {
						String url;
						try {
							//リンクのurlを抽出する
							url = new URL(new URL(page), link.getAttribute("href")).toString();
						} catch (MalformedURLException e) {
							continue;
						}
						if (url.indexOf("'") != -1) {
							continue;
						}
						//URLに#の文字があった場合
						url = url.split("#")[0];
						if (url.startsWith("http") && !isindexed(url)) {
							newpages.add(url);
						}

						String linktext = gettextonly(link); //ゲットテキスト関数を使う
						addlinkref(page, url, linktext); //リンク同士をつなげる関数を使用
					}
				}
				dbcommit(); //データベースと接続
			}
			pages = newpages; //作られたpageにnewpagesを埋め込む。　そしてまた繰り返し。
		}
	}

	//インデックスに項目を追加する
	public void addtoindex(String url, NodeList soup) throws ClassNotFoundException {
		if (isindexed(url)) { //もしもう既にされていたのならそのまま還す。
			return;
		}
		System.out.println("Indexing " + url);
		String text = gettextonly(soup); //soupからテキストを取ってきて
		String[] words = separatewords(text); //構文解析、単語分割する。

		Integer urlid = getenetryid("urllist", "url", url, true); //idを取得

		for (int i = 0; i < words.length; i++) { //その言葉の数だけ繰り返す
			String word = words[i];
			if (ignorewords.contains(word)) { //無視するべき単語は除く
				continue;
			}
			Integer wordid = getenetryid("wordlist", "word", word, true);
			execute("insert into wordlocation(urlid,wordid,location)"
					+ " values (" + urlid + "," + wordid + "," + i + ")");
		}
		return;
	}
	
	private int execute(String sql) throws ClassNotFoundException {
		init();
		Statement stat;
		try {
			stat = this.con.createStatement();
			stat.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id =0;
			ResultSet rs = stat.getGeneratedKeys();
			if(rs != null && rs.next()){
			   id = rs.getInt(1);
			}

			stat.close();
			dbcommit();
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String gettextonly(Node soup) {
		NodeList children = soup.getChildren();
		if (children != null) {
			return gettextonly(children);
		} else {
			return "";
		}

	}

	//タグを消してテキストだけを抽出する関数
	public String gettextonly(NodeList v) {
		NodeList soup = v.extractAllNodesThatMatch(new NodeClassFilter(TextNode.class), true);

		String resulttext = "";
		for (int i = 0; i < soup.size(); i++) {
			TextNode text = (TextNode) soup.elementAt(i);
			resulttext += text.getText() + "\n";
		}
		return resulttext;
	}
	
	//単語を分割する関数
	public String[] separatewords(String text) {
		String[] tokens = text.split("\\W");
		List<String> results = new ArrayList<String>();
		for (String token : tokens) {
			if (!token.equals("")) {
				results.add(token.toLowerCase());
			}
		}
		return results.toArray(new String[0]);
	}
	
	public Integer getenetryid(String table, String field, String value,
			boolean createNew) throws ClassNotFoundException {
		init();
		Statement stat = null;
		try {
			stat = this.con.createStatement();
			ResultSet result = stat.executeQuery
					("select rowid from " + table + " where " + field + " = '" + value + "'");
			if (result.next()) {
				return result.getInt(1);
			} else if (createNew) {
				return execute("insert into " + table + " (" + field
						+ ") values( '" + value + "')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.close(stat);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public Boolean isindexed(String url) throws ClassNotFoundException {
		init();
		Statement stat = null;
		try {
			stat = this.con.createStatement();
			ResultSet result = stat
					.executeQuery("select rowid from urllist where url = '"
							+ url + "'");
			if (result.next()) {
				int urlid = result.getInt(1);
				return stat.executeQuery(
						"select rowid from wordlocation where urlid = " + urlid
								+ "").next();
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.close(stat);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void addlinkref(String urlFrom, String urlTo, String linkText) throws ClassNotFoundException {
		String[] words = separatewords(linkText);

		Integer fromid = getenetryid("urllist", "url", urlFrom, true);
		Integer toid = getenetryid("urllist", "url", urlTo, true);
		if (fromid.equals(toid)) {
			return;
		}

		int linkid = execute("insert into link(fromid,toid) values (" + fromid
				+ "," + toid + ")");

		for (String word : words) {
			if (ignorewords.contains(word)) {
				continue;
			}
			int wordid = getenetryid("wordlist", "word", word, true);
			execute("insert into linkwords(linkid,wordid) values (" + linkid
					+ "," + wordid + ")");
		}

	}

	//SQLiteのDBテーブルを作るための関数
	public void createindextables() {
		Statement statement;
		try {
			statement = con.createStatement();
			statement.execute("create table urllist(url)");
			statement.execute("create table wordlist(word)");
			statement.execute("create table wordlocation(urlid,wordid,location)");
			statement.execute("create table link(fromid integer,toid integer)");
			statement.execute("create table linkwords(wordid,linkid)");
			statement.execute("create index wordidx on urllist(url)");
			statement.execute("create index wordurlidx on wordlocation(wordid)");
			statement.execute("create index urltoidx on link(toid)");
			statement.execute("create index urlfromidx on link(fromid)");
			statement.close();
			dbcommit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

