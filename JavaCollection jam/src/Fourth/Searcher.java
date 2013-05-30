package Fourth;


/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class Searcher {
	private String db = null;
	private Connection con;

	public Searcher(String db) {
		this.db = db;
		init();
	}
	
	//Derbyを起動させる関数
	private void init() {
		if (this.con == null) {
			try {
				//SQLiteのデータベースに接続
				this.con = DriverManager.getConnection("jdbc:sqlite:" + this.db);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MatchRows getmatchrows(String q) {
		String fieldlist = "w0.urlid";
		String tablelist = "";
		String clauselist = "";

		String[] words = q.split(" ");
		List<Integer> wordids = new ArrayList<Integer>();
		List<Object[]> resultList = null;

		int tablenumber = 0;

		Statement stat = null;
		try {
			stat = this.con.createStatement();
			for (String word : words) {
				ResultSet result = stat
						.executeQuery("select rowid from wordlist where word = '"
								+ word + "'");
				if (result.next()) {
					Integer wordid = result.getInt(1);
					wordids.add(wordid);
					if (tablenumber > 0) {
						tablelist += ",";
						clauselist += " and ";
						clauselist += "w" + (tablenumber - 1) + ".urlid = w"
								+ tablenumber + ".urlid and ";
					}

					fieldlist += " ,w" + tablenumber + ".location";
					tablelist += " wordlocation w" + tablenumber;
					clauselist += "w" + tablenumber + ".wordid=" + wordid;
					tablenumber += 1;
				}

			}

			String fullquery = "select " + fieldlist + " from " + tablelist
					+ " where " + clauselist;
			ResultSetHandler h = new ArrayListHandler();

			// No DataSource so we must handle Connections manually
			QueryRunner run = new QueryRunner();

			resultList = (List<Object[]>) run.query(this.con, fullquery, h);

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
		return new MatchRows(resultList, wordids);
	}
	
	public Map<Integer, Double> getscoredlist(MatchRows matchrows) {
		List<Object[]> rows = matchrows.rows;
		Map<Integer, Double> totalscores = new HashMap<Integer, Double>();
		for (Object[] objs : rows) {
			totalscores.put((Integer) objs[0], 0.0);
		}

		List<Pair<Double, Map<Integer, Double>>> weights = new ArrayList<Pair<Double, Map<Integer, Double>>>();
		weights.add(new Pair<Double, Map<Integer, Double>>(1.0,
				frequencyscore(matchrows)));

		for (Pair<Double, Map<Integer, Double>> weightPair : weights) {
			Double weight = weightPair.i1;
			Map<Integer, Double> scores = weightPair.i2;
			for (Integer url : totalscores.keySet()) {
				double score = totalscores.get(url);
				totalscores.put(url, score + weight * scores.get(url));
			}

		}
		return totalscores;
	}
	
	public WordAndUrls query(String q) {
		MatchRows matchrows = getmatchrows(q);
		Map<Integer, Double> totalscoreMap = getscoredlist(matchrows);
		List<Score> totalscores = new ArrayList<Score>();
		for (Integer url : totalscoreMap.keySet()) {
			totalscores.add(new Score(url, totalscoreMap.get(url)));
		}
		// sort して、トップ10だけ表示
		Collections.sort(totalscores);
		Collections.reverse(totalscores);

		List<Integer> urls = new ArrayList<Integer>();
		for (int i = 0; i < Math.min(10, totalscores.size()); i++) {
			Score totalscore = totalscores.get(i);
			System.out.println(totalscore.score + " "
					+ geturlname(totalscore.urlid));
			urls.add(totalscore.urlid);
		}
		return new WordAndUrls(matchrows.wordids,urls);
	}
	
	private Map<Integer, Double> normalizescores(Map<Integer, Double> scores,
			boolean smallIsBetter) {
		double vsmall = 0.00001;
		if (smallIsBetter) {
			double minscore = getMin(scores.values());
			for (Integer url : scores.keySet()) {
				double score = scores.get(url);
				scores.put(url, minscore / Math.max(score, vsmall));
			}

		} else {
			double maxscore = getMax(scores.values());
			for (Integer url : scores.keySet()) {
				double score = scores.get(url);
				scores.put(url, score / maxscore);
			}
		}

		return scores;

	}

	private double getMax(Collection<Double> scores) {

		double max = 0.0;
		for (Double score : scores) {
			if (max < score) {
				max = score;
			}
		}

		return max;
	}

	private double getMin(Collection<Double> scores) {
		double min = Double.MAX_VALUE;
		for (Double score : scores) {
			if (min > score) {
				min = score;
			}
		}

		return min;
	}
	
	public Map<Integer, Double> frequencyscore(MatchRows matchrows) {
		Map<Integer, Double> counts = new HashMap<Integer, Double>();
		for (Object[] objs : matchrows.rows) {
			counts.put((Integer) objs[0], 0.0);
		}
		for (Object[] row : matchrows.rows) {
			double count = counts.get(row[0]);
			counts.put((Integer) row[0], count + 1);
		}
		return normalizescores(counts, false);
	}
}

class MatchRows {

	List<Object[]> rows;
	List<Integer> wordids;

	public MatchRows(List<Object[]> rows, List<Integer> wordids) {
		this.rows = rows;
		this.wordids = wordids;
	}
}

class Score implements Comparable<Score> {

	Integer urlid;
	Double score;

	Score(Integer urlid, Double score) {
		this.urlid = urlid;
		this.score = score;
	}

	public int compareTo(Score obj) {
		return this.score.compareTo(obj.score);
	}

	@Override
	public boolean equals(Object obj) {
		return this.urlid.equals(((Score) obj).urlid);
	}

	@Override
	public int hashCode() {
		return this.urlid.hashCode();
	}
}

class WordAndUrls{
	
	List<Integer> wordids;
	List<Integer> urls;
	
	public WordAndUrls(List<Integer> wordids,List<Integer> urls){
		this.wordids = wordids;
		this.urls = urls;
	}
}

*/