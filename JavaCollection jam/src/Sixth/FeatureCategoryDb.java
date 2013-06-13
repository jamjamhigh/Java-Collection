package Sixth;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;

public class FeatureCategoryDb implements Category{
	
	private Connection con = null;
	
	//とりあえずデータベースファイルに接続する
	public FeatureCategoryDb(String dbname){
		try {
			Class.forName("org.sqlite.JDBC");
			this.con = DriverManager.getConnection("jdbc:sqlite:C:/SQLiteDB/" + dbname
					+ ".sqlite");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public FeatureCategoryDb(String dbname,boolean setDb){
		this(dbname);
		if(setDb){
			setdb();
		}
	}
	
	//特徴・カテゴリのカウント増加
	public void incf(String f, String cat) {
		double count = fcount(f, cat);
		if (count == 0) {
			execute("insert into fc values ('" + f + "', '" + cat + "', 1)");
		} else {
			execute("update fc set count=" + (count + 1)
					+ " where category = '" + cat + "'");
		}

	}

	//カテゴリのカウントのみ増加
	public void incc(String cat) {
		int count = this.catcount(cat);
		if(count == 0){
			execute("insert into cc values ('"+cat+"', 1)");
		}else{
			execute("update cc set count="+(count+1)+" where category = '"+cat+"'");
		}
		
	}

	//あるカテゴリの中に特徴が現れた数
	public double fcount(String f, String cat) {
		Integer res = (Integer) fecthone("select count from fc where feature = '"+f+"' and category = '"+cat+"' ");
		if(res == null){
			return 0;
		}
		return res;
	}

	//あるカテゴリの中のアイテムたちの数
	public int catcount(String cat) {
		Integer res = (Integer) fecthone("select count from cc where category='"+cat+"'");
		if(res == null){
			return 0;
		}
		return res;
	}

	//アイテムたちの総数
	public int totalcount() {
		Integer res = (Integer) fecthone("select sum(count) from cc");
		if(res == null){
			return 0;
		}
		return res;
	}

	//すべてのカテゴリたちのリスト
	public Set<String> categories() {
		Set<String> categories = new HashSet<String>();
		Statement stat = null;
		try {
			stat = this.con.createStatement();
			ResultSet result = stat.executeQuery("select category from cc");
			while (result.next()) {
				categories.add(result.getString(1));
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
		return categories;
	}
	
	@Override
	public void finishtrain() {
		try {
			this.con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//必要なテーブルを作る
	public void setdb() {
		Statement statement = null;
		try {
			statement = this.con.createStatement();
			statement.execute("CREATE TABLE if not exists fc (feature VARCHAR(512),category VARCHAR(128), count int)");
			statement.execute("CREATE TABLE if not exists cc (category VARCHAR(128),count int)");
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.close(statement);
			} catch (SQLException e) {
			}
		}
	}

	private void execute(String sql) {
		Statement stat;
		try {
			stat = this.con.createStatement();
			stat.executeUpdate(sql);
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Object fecthone(String sql) {
		Statement stat = null;
		try {
			stat = this.con.createStatement();
			ResultSet result = stat.executeQuery(sql);
			if (result.next()) {
				return result.getObject(1);
			}
			return null;
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

}