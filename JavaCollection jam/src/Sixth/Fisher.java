package Sixth;

import java.util.HashMap;
import java.util.Set;

public class Fisher extends Classifier{
	private HashMap<String, Double> minimums = new HashMap<String, Double>();

	public Fisher(Feature getfeatures) {
		super(getfeatures);
	}
	
	public double fisherprob(Object item, String cat) {
		double p = 1.0;
		Set<String> features = feature.getfeature(item);
		for (String f : features) {
			p = p * this.weightedprob(f, cat, new Cprob());
		}
		double fscore = -2 * Math.log(p);
		return invchi2(fscore, features.size() * 2);

	}

	private double invchi2(double chi, int df) {
		double m = chi / 2.0;
		double sum = Math.exp(-m);
		double term = Math.exp(-m);
		for (int i = 1; i < df / 2; i++) {
			term = term * m / i;
			sum = sum + term;
		}
		return Math.min(sum, 1.0);
	}
	
	public void setminimum(String cat, double min) {
		this.minimums.put(cat, min);
	}

	public double getminimum(String cat) {
		if (!this.minimums.containsKey(cat)) {
			return 0.0;
		}
		return this.minimums.get(cat);
	}
	
	public String classify(Object item, String _default) {
		String best = _default;
		double max = 0.0;

		for (String c : categories()) {
			double p = fisherprob(item, c);
			if (p > this.getminimum(c) && p > max) {
				best = c;
				max = p;
			}
		}

		return best;
	}
}
