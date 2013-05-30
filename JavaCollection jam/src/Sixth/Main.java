package Sixth;

public class Main {
	public static void main(String[] args) {
		/*
		cl.train("the quick brown fox jumps over the lazy dog", "good");
		cl.train("make quick money in the online casino", "bad");
		cl.train("Nobody owns the water.", "good");
		cl.train("the quick rabbit jums fences", "good");
		cl.train("buy pharmaceuticals now", "bad");
		cl.train("make quick money at the online casino", "bad");
		cl.train("the quick brown fox jumps", "good");
		System.out.println(cl.fcount("quick", "good"));
		System.out.println(cl.fcount("quick", "bad"));
		*/
		Classifier cl = new Classifier(new GetWordsFeature());
		cl.sampletrain(cl);
		System.out.println(new Fprob().execute("quick", "good"));
	}
}
