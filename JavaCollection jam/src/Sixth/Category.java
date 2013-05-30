package Sixth;

import java.util.Set;

//Classifierで使われるインターフェイス。
public interface Category {
	public void incf(String f, String cat);

	public void incc(String cat);
	
	public double fcount(String f, String cat);
	
	public int catcount(String cat);
	
	public Set<String> categories();
	
	public int totalcount();
	
	public void finishtrain();
}
