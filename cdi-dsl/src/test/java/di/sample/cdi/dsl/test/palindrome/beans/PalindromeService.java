package di.sample.cdi.dsl.test.palindrome.beans;

import javax.inject.Inject;

import di.sample.cdi.dsl.test.palindrome.DataStore;
import di.sample.cdi.dsl.test.palindrome.InputSanitizer;
import di.sample.cdi.dsl.test.palindrome.Palindrome;
import di.sample.cdi.dsl.test.palindrome.bindings.DataStoreBinding;


//@PalindromeServiceBinding
//@ApplicationScoped
public class PalindromeService implements Palindrome {

	@Inject @DataStoreBinding
	private DataStore dataStoreBean;

//	private Pattern pattern; 
	private InputSanitizer inputSanitizer;
	
	
	public PalindromeService() {
	}
//	@Inject
//	public PalindromeService(@InputSanitizerBinding Pattern pattern) {
//		this.pattern = pattern;
//	}
	@Inject
	public PalindromeService(InputSanitizer inputSanitizer) {
		this.inputSanitizer = inputSanitizer;
	}
	
	public boolean isPalindrome(String arg) {
		if (arg == null)
			return false;
//		String word = pattern.matcher(arg).replaceAll("");
		String word = inputSanitizer.sanitize(arg);
		Boolean palindrome = dataStoreBean.getValue(word);
		if ( palindrome == null ) {
			StringBuilder rev = new StringBuilder(word);
			rev.reverse();
			if (word.equalsIgnoreCase(rev.toString()))
				palindrome = true;
			else
				palindrome = false;
			dataStoreBean.putValue(word, palindrome);
		}
		return palindrome;
	}

	public DataStore getDataStore() {
		return this.dataStoreBean;
	}

} 
