package Sixth;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class DocClass {
	//テキストから特徴を抽出する関数
	public static Set<String> getwords(String doc) {
		//単語を非アルファベットの文字で分割する
		//Arrays.asListで配列をリストに変換する
		List<String> words = Arrays.asList(doc.split("\\W"));

		@SuppressWarnings("unchecked")
		Collection<String> secwords = CollectionUtils.collect(words, new Transformer(){
			@Override
			public Object transform(Object arg0) {
				// TODO 自動生成されたメソッド・スタブ
				//文字列を全部小文字にして返す。
				return ((String)arg0).toLowerCase();
			}
		});
		
		//最後は結合させて返す
		return new HashSet<String>(secwords);
	}
}