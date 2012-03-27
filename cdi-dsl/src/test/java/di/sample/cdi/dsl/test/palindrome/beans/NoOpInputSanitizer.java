package di.sample.cdi.dsl.test.palindrome.beans;

import di.sample.cdi.dsl.test.palindrome.InputSanitizer;


public class NoOpInputSanitizer implements InputSanitizer {

	public String sanitize(String arg) {
		return arg;
	}

}
