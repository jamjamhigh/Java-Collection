package Sixth;


public class Cprob extends Prf{

	@Override
	public double execute(String f, String cat) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
	/*
	public double execute(String f, String cat) {
		Fprob fprob = new Fprob();
		double clf = fprob.execute(f, cat);
		if (clf == 0.0) {
			return 0;
		}

		double freqsum = 0.0;
		for (String c : categories()) {
			freqsum += fprob.execute(f, c);
		}

		double p = clf / freqsum;

		return p;
	}
	*/
}
