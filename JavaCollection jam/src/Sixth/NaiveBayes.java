package Sixth;


/*
//ベイズ分類器
public class NaiveBayes extends Classifier{

	private Map<String, Double> thresholds = new HashMap<String, Double>();
	
	public NaiveBayes(Feature feature) {
		super(feature);
	}

	public double docprob(Object item, String cat) {
		Set<String> features = this.feature.getfeature(item);
		double p = 1;
		for (String f : features) {
			p = p * weightedprob(f, cat, new Fprob());
		}
		return p;
	}
	
	//確率計算メソッド
	public double prob(Object item, String cat) {
		double catprob = ((double) catcount(cat)) / totalcount();
		double docprob = docprob(item, cat);
		return docprob * catprob;
	}
	
	public void setthreshold(String cat, double t) {
		this.thresholds.put(cat, t);
	}

	public double getthreshold(String cat) {
		Double t = this.thresholds.get(cat);
		if (t == null) {
			return 1.0;
		}
		return t;
	}
	
	public String classify(Object item, String _default) {
		Map<String, Double> probs = new HashMap<String, Double>();

		double max = 0.0;
		String best = null;

		for (String cat : this.categories()) {
			probs.put(cat, prob(item, cat));
			if (probs.get(cat) > max) {
				max = probs.get(cat);
				best = cat;
			}
		}

		for (String cat : probs.keySet()) {
			if (cat.equals(best)) {
				continue;
			}
			if (probs.get(cat) * this.getthreshold(best) > probs.get(best)) {
				return _default;
			}
		}
		return best;

	}
}
*/