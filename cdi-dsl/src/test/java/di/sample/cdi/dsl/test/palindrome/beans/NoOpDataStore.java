package di.sample.cdi.dsl.test.palindrome.beans;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import di.sample.cdi.dsl.test.palindrome.DataStore;
import di.sample.cdi.dsl.test.palindrome.bindings.DataStoreBinding;


@DataStoreBinding
@ApplicationScoped
//@Alternative 
public class NoOpDataStore implements DataStore {


	public Boolean getValue(String word) {
		return null;
	}

	public void putValue(String word, boolean palindrome) {
	}

	public Set<String> dump() {
		return null;
	}

} 
