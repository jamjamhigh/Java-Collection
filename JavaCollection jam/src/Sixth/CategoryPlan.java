package Sixth;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Classifierで使われるCategoryクラスの内部処理を記述したクラス
public class CategoryPlan implements Category{
	//インスタンス変数
	private Map<String,Map<String,Integer>> fc;
	private Map<String , Integer> cc;
	
	public CategoryPlan(){
		//特徴・カテゴリのカウント
		this.fc = new HashMap<String, Map<String, Integer>>();
		//それぞれのカテゴリの中のドキュメント数
		this.cc = new HashMap<String, Integer>();
	}
	
	//incf関数の内部処理（特徴・カテゴリのカウント増加）
	public void incf(String f, String cat) {
		Map<String, Integer> map = this.fc.get(f);
		if (map == null) {
			map = new HashMap<String, Integer>();
			this.fc.put(f, map);
		}
		Integer count = map.get(cat);
		if (count == null) {
			count = 0;
		}
		map.put(cat, count + 1);

	}

	//incc関数の内部処理（カテゴリのカウントのみ増加）
	public void incc(String cat) {
		Integer count = this.cc.get(cat);
		if (count == null) {
			count = 0;
		}

		this.cc.put(cat, count + 1);
	}

	//あるカテゴリの中に特徴が現れた数
	public double fcount(String f, String cat) {
		if (this.fc.containsKey(f) && this.fc.get(f).containsKey(cat)) {
			return this.fc.get(f).get(cat);
		}
		return 0;
	}

	//あるカテゴリの中のアイテムたちの数
	public int catcount(String cat) {
		if (this.cc.containsKey(cat)) {
			return this.cc.get(cat);
		}
		return 0;
	}

	//アイテムたちの総数
	public int totalcount() {
		int sum = 0;
		for (Integer val : this.cc.values()) {
			sum += val;
		}
		return sum;
	}

	//すべてのカテゴリたちのリスト
	public Set<String> categories() {
		return this.cc.keySet();
	}

	@Override
	public void finishtrain() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	
}
