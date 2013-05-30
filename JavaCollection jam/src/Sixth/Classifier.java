package Sixth;

import java.util.Set;

//分類器のクラス
public class Classifier {
	//抽象クラスFeature
	protected Feature feature;
	//インターフェイスのCategory
	private Category category;

	//このクラスの主実行部分
	public Classifier(Feature getfeatures) {
		//インターフェイスに動作部分を定義したクラスを当てはめる
		category = new CategoryPlan();

		//抽象クラスの動作が実行される
		this.feature = getfeatures;
	}
	
	//特徴・カテゴリのカウントを増やす。
	public void incf(String f, String cat) {
		category.incf(f, cat);
	}

	public void incc(String cat) {
		category.incc(cat);
	}

	public double fcount(String f, String cat) {
		return category.fcount(f, cat);
	}

	public int catcount(String cat) {
		return category.catcount(cat);
	}

	public int totalcount() {
		return category.totalcount();
	}

	public Set<String> categories() {
		return category.categories();
	}

	//アイテムを特徴に分類し、カテゴリのカウントを増加させる。
	public void train(Object item, String cat) {
		Set<String> features = this.feature.getfeature(item);
		//特徴がある分だけ、カウントを増やす
		for (String f : features) {
			this.incf(f, cat);
		}

		//カテゴリのカウントを増やす。
		this.incc(cat);
		this.category.finishtrain();
	}
	
	//推測をする関数
	//不適切な単語を一概に不適切と決めつけないための、仮の確率を設定する
	public double weightedprob(String f, String cat, Prf prf, double weight, double ap) {
		//現在の確率を計算する
		double basicprob = prf.execute(f, cat);

		int totals = 0;
		//この特徴がすべてのカテゴリ中に出現する数を数える。
		for (String c : categories()) {
			totals += this.fcount(f, c);
		}

		//重み付けした平均を計算
		double bp = ((weight * ap) + (totals * basicprob)) / (weight + totals);

		return bp;
	}

	public double weightedprob(String f, String cat, Prf prf) {
		return weightedprob(f, cat, prf, 1.0, 0.5);
	}
	
	//サンプルとして走らせるための
	public void sampletrain(Classifier cl) {
		cl.train("Nobody owns the water.", "good");
		cl.train("the quick rabbit jums fences", "good");
		cl.train("buy pharmaceuticals now", "bad");
		cl.train("make quick money at the online casino", "bad");
		cl.train("the quick brown fox jumps", "good");
	}
}