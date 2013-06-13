package Sixth;

public class Main {
	public static void main(String[] args) {
		Classifier cl = new Classifier(new GetWordsFeature());
		cl.sampletrain(cl);
		
		/*
		//分類器のトレーニング
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
		
		/*
		//確率計算
		System.out.println(cl.execute("quick", "good"));
		*/
		
		/*
		//推測をしながらに確率計算
		System.out.println(cl.weightedprob("money", "good", cl));
		cl.sampletrain(cl);
		System.out.println(cl.weightedprob("money", "good", cl));
		*/
		
		//こっからベイズ定理
		NaiveBayes cl2 = new NaiveBayes(new GetWordsFeature());
		cl2.sampletrain(cl2);
		
		/*
		//ベイズ定理の実験その１
		System.out.println(cl2.prob("quick rabbit", "good", cl2));
		System.out.println(cl2.prob("quick rabbit", "bad", cl2));
		*/
		
		/*
		//ベイズ定理ドキュメント分類
		System.out.println(cl2.classify("quick rabbit", "unknown", cl2));
		System.out.println(cl2.classify("quick money", "unknown", cl2));
		cl2.setthreshold("bad", 3.0);
		System.out.println(cl2.classify("quick money", "unknown", cl2));
		for(int i = 0; i < 10; i++){
			cl2.sampletrain(cl2);
		}
		System.out.println(cl2.classify("quick money", "unknown", cl2));
		*/
		
		//データベース付き
		NaiveBayes nb = new NaiveBayes(new GetWordsFeature(), "collect_sixth");
		nb.sampletrain(nb);
		System.out.println(nb.classify("quick money", null, nb));
	}
}
