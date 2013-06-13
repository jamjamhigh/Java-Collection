package Sixth;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


//ベイズ分類器
public class NaiveBayes extends Classifier{

	//しきい値設定のためのインスタンス変数（単語と値を入れる）
	private Map<String, Double> thresholds = new HashMap<String, Double>();
	
	public NaiveBayes(Feature feature) {
		super(feature);
	}
	
	public NaiveBayes(Feature feature, String dbname) {
		super(feature, dbname);
	}

	//特徴のある単語を抽出し、ドキュメント全体の確率を計算する
	public double docprob(Object item, String cat, Prf prf) {
		Set<String> features = this.feature.getfeature(item);
		double p = 1;
		for (String f : features) {
			p = p * weightedprob(f, cat, prf);
		}
		return p;
	}
	
	//ベイズ定理の右側を計算するメソッド
	public double prob(Object item, String cat, Prf prf) {
		double catprob = ((double) catcount(cat)) / totalcount();
		double docprob = docprob(item, cat, prf);
		return docprob * catprob;
	}
	
	//スパムフィルタリングの最低限のしきい値をセットする
	public void setthreshold(String cat, double t) {
		this.thresholds.put(cat, t);
	}

	//しきい値を取得する
	public double getthreshold(String cat) {
		Double t = this.thresholds.get(cat);
		if (t == null) {
			return 1.0;
		}
		return t;
	}
	
	//カテゴリの確率を計算する
	public String classify(Object item, String _default, Prf prf) {
		Map<String, Double> probs = new HashMap<String, Double>();

		double max = 0.0;
		String best = null;
		//最も確率の高いカテゴリを探す
		for (String cat : this.categories()) {
			probs.put(cat, prob(item, cat, prf));
			if (probs.get(cat) > max) {
				max = probs.get(cat);
				best = cat;
			}
		}

		//確率がしきい値×2番目にベストなものを超えているか確認する
		for (String cat : probs.keySet()) {
			//同値だったら続ける
			if (cat.equals(best)) {
				continue;
			}
			//超えていたらデフォルトの値を返す
			if (probs.get(cat) * this.getthreshold(best) > probs.get(best)) {
				return _default;
			}
		}
		return best;
	}
}
