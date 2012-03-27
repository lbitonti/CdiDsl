package di.sample.cdi.dsl.test.palindrome;

import javax.enterprise.context.ApplicationScoped;

import di.sample.cdi.dsl.Module;
import di.sample.cdi.dsl.test.palindrome.beans.InMemoryDataStore;
import di.sample.cdi.dsl.test.palindrome.beans.PalindromeService;
import di.sample.cdi.dsl.test.palindrome.beans.RegexInputSanitizer;
import di.sample.cdi.dsl.test.palindrome.bindings.DataStoreBinding;
import di.sample.cdi.dsl.test.palindrome.bindings.PalindromeServiceBinding;

public class AppModule extends Module {

	@Override
	public void configure() {
		removeManagedBean(DataStore.class)
				.withQualifiers(DataStoreBinding.class);			
		addManagedBean(InMemoryDataStore.class)
				.withQualifiers(DataStoreBinding.class)
				.withBeanTypes(DataStore.class)
				.InScope(ApplicationScoped.class)
				.withName("inMemoryDataStore");

		addManagedBean(PalindromeService.class)
				.withQualifiers(PalindromeServiceBinding.class)
				.withBeanTypes(Palindrome.class)
				.InScope(ApplicationScoped.class)
				.withName("palindromeService");

		removeManagedBean(InputSanitizer.class);			
		addManagedBean(RegexInputSanitizer.class)
				.withBeanTypes(InputSanitizer.class)
				.InScope(ApplicationScoped.class)
				.withName("regexInputSanitizer");
	}

}
