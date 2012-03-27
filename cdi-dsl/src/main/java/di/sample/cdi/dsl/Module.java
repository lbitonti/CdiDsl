package di.sample.cdi.dsl;

import javax.enterprise.inject.spi.BeanManager;


public abstract class Module {

	private Configuration config = new Configuration();
	
	protected abstract void configure();


	Configuration configure(BeanManager beanManager) {
		configure();
		return config;
	}

	protected <T> BeanBuilder<T> addManagedBean(Class<T> clazz) {
		if ( clazz == null )
			throw new IllegalArgumentException("Class == null");
		BeanBuilder<T> builder = new BeanBuilder<T>(clazz);		
		config.addBuilder(builder);
		return builder;
	}

	protected <T> BeanMatcher<T> removeManagedBean(Class<T> clazz) {
		if ( clazz == null )
			throw new IllegalArgumentException("Bean Class == null");
		BeanMatcher<T> vetoer = new BeanMatcher<T>(clazz);
		config.addVetoer(vetoer);
		return vetoer;
	}

}
