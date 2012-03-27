package di.sample.cdi.dsl.test.palindrome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import di.sample.cdi.dsl.test.palindrome.bindings.PalindromeServiceBinding;

@Named
@ApplicationScoped
public class PalindromeApplication implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Inject @PalindromeServiceBinding 
	private Palindrome palindromeBean; 

	
	private String word;
    private Boolean palindrome;
    // Keep track of the previous attempts 
    private List<String> previousWords;

    public void checkPalindrome() {
    	this.palindrome = palindromeBean.isPalindrome(word);
        if ( this.previousWords == null )
        	this.previousWords = new ArrayList<String>();
        this.previousWords.add(word);
    }

    public Boolean getPalindrome() {
        return palindrome;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }

	public String getPrevious() {
		StringBuilder previous = new StringBuilder();
		int i = 0;
		if ( previousWords != null ) {
			for ( String s : previousWords ) {
				if ( i > 0 )
					previous.append(", ");
				previous.append("[").append(s).append("]");
				i++;
			}
		}
		return previous.toString();
	}

	public DataStore getDataStore() {
		return this.palindromeBean.getDataStore();
	}

}
