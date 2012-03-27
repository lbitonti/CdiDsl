package di.sample.cdi.dsl.test.palindrome.beans;

import java.util.regex.Pattern;

import di.sample.cdi.dsl.test.palindrome.InputSanitizer;


public class RegexInputSanitizer implements InputSanitizer {

	private Pattern rep = Pattern.compile("\\W");

	public String sanitize(String arg) {
		if ( arg == null )
			return null;
		return rep.matcher(arg).replaceAll("");
	}

}
