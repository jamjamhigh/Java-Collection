package Sixth;


//確率を計算するクラス（正確な確率）
public class Fprob extends Prf{
	Classifier fier;
	
	public double execute(String f, String cat) {
		if (fier.catcount(cat) == 0) {
			return 0.0;
		}
		return  fier.fcount(f,cat) / fier.catcount(cat);
	}
}
