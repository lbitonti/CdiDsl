package di.sample.cdi.dsl.test.palindrome;

import java.util.Set;

public interface DataStore {
	
	public Boolean getValue(String word);
	
	public void putValue(String word, boolean palindrome);

	public Set<String> dump();
}
