package di.sample.cdi.dsl;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import di.sample.cdi.dsl.Utils;
import di.sample.cdi.dsl.test.palindrome.DataStore;
import di.sample.cdi.dsl.test.palindrome.PalindromeApplication;
import di.sample.cdi.dsl.test.palindrome.bindings.DataStoreBinding;

public class WeldDslTest {

	protected Weld weld;
	protected WeldContainer weldContainer;
	protected PalindromeApplication pa;
	
	@BeforeClass
	public void init() {
		weld = new Weld();
		weldContainer = weld.initialize();
		pa = weldContainer.instance().select(PalindromeApplication.class).get();
	}

	@AfterClass
	public void shutdown() {
		weld.shutdown();
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
	public void testDataStoreWeldSEContainer() {
		// This fails as beans introduced by the DSL module are not registered directly with Weld SE
//		DataStore ds = weldContainer.instance().select(DataStore.class, Utils.annotationInstance(DataStoreBinding.class)).get();
//		Assert.assertNotNull(ds);
		
		BeanManager beanManager = weldContainer.getBeanManager();
		Bean<?> bean = beanManager.getBeans(DataStore.class, Utils.annotationInstance(DataStoreBinding.class)).iterator().next();
		DataStore ds = (DataStore)beanManager.getReference(bean, DataStore.class, beanManager.createCreationalContext(bean));
		Assert.assertNotNull(ds);
		Assert.assertEquals(ds.dump().size(), 2);
	}

	
}
