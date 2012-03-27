package di.sample.cdi.dsl.test.palindrome.beans;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import di.sample.cdi.dsl.test.palindrome.DataStore;


//@DataStoreBinding
//@ApplicationScoped
//@Alternative 
public class InMemoryDataStore implements DataStore {

	private ConcurrentMap<String, Boolean> values = new ConcurrentHashMap<String, Boolean>();
	
	public Boolean getValue(String word) {
		if ( word == null ) 
			return null;
		Boolean isPalindrome = values.get(word);
		return isPalindrome;
	}

	public void putValue(String word, boolean palindrome) {
		if ( word != null ) {
			values.put(word, palindrome);
		}
	}

	public Set<String> dump() {
		return Collections.unmodifiableSet(values.keySet());
	}


} 
