package di.sample.cdi.dsl;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import di.sample.cdi.dsl.test.palindrome.DataStore;
import di.sample.cdi.dsl.test.palindrome.PalindromeApplication;


public class OWBDslTest {

    protected ContainerLifecycle lifecycle = null;
	protected PalindromeApplication pa;


	protected void boot(Object startupObject)  {
		lifecycle = WebBeansContext.getInstance().getService(ContainerLifecycle.class);
		lifecycle.startApplication(startupObject);
	}

	protected void shutdown(Object endObject) {
		lifecycle.stopApplication(endObject);
	}

	@BeforeClass
	public void init() {
        boot(null);        
        BeanManager beanManager = lifecycle.getBeanManager();
        Bean<?> bean = beanManager.getBeans("palindromeApplication").iterator().next();
        pa = (PalindromeApplication)lifecycle.getBeanManager().getReference(
        		bean, PalindromeApplication.class, beanManager.createCreationalContext(bean));
	}

	@AfterClass
	public void shutdown() {
        shutdown(pa);
	}
	
	@Test
	public void testBadPalindrome() {
		pa.setWord("pa");
		pa.checkPalindrome();
		Assert.assertFalse(pa.getPalindrome());
	}
	
	@Test(dependsOnMethods = "testBadPalindrome")
	public void testGoodPalindrome() {
		pa.setWord("pap");
		pa.checkPalindrome();
		Assert.assertTrue(pa.getPalindrome());
	}

	@Test(dependsOnMethods = "testGoodPalindrome")
	public void testDataStoreContent() {
		DataStore ds = pa.getDataStore();
		Assert.assertEquals(ds.dump().size(), 2);
		for ( String s : ds.dump() ) {
			if ( !"pa".equals(s) && !"pap".equals(s) )
				Assert.fail("Bad DataStore content: " + ds.dump() );
		}
	}

	@Test(dependsOnMethods = "testDataStoreContent")
	public void testDataStoreOWBContainer() {
        BeanManager beanManager = lifecycle.getBeanManager();
        Bean<?> bean = beanManager.getBeans("inMemoryDataStore").iterator().next();
        DataStore ds = (DataStore)lifecycle.getBeanManager().getReference(
        		bean, DataStore.class, beanManager.createCreationalContext(bean));
		Assert.assertNotNull(ds);
		Assert.assertEquals(ds.dump().size(), 2);
	}
	
}
