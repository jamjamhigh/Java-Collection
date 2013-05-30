package Sixth;

import java.util.Set;

public class GetWordsFeature extends Feature{
	@Override
	public Set<String> getfeature(Object doc) {
		return DocClass.getwords((String)doc);
	}

}
