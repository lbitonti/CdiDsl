package di.sample.cdi.dsl;

public class BeanVetoer<T> extends BeanMatcher<T> {

	public BeanVetoer(Class<T> beanClass) {
		super(beanClass);
	}

}
