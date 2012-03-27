package di.sample.cdi.dsl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BeanMatcher<T> {
	
	private Class<T> beanClass;
	private Set<Class<? extends Annotation>> beanQualifiers;
	
	
	public BeanMatcher(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	    
	public boolean matches(Type type, Annotation... qualifiers) {
		boolean match = false;
		if ( type != null ) {
			Class clazz = Utils.getClass(type);
			if ( clazz != null ) {
				if ( beanClass.isAssignableFrom(clazz) ) {
					if ( qualifiers != null && qualifiers.length > 0 ) {
						List<Annotation> quals = Arrays.asList(qualifiers);
						for ( Class<? extends Annotation> bq : beanQualifiers ) {
	        				Annotation annotation = Utils.annotationInstance(bq);
							if ( !quals.contains(annotation) )
								return false;
						}
						match = true;
					}
					else {
						match = true;
					}
				}
			}
		}
		return match;
	}
	
	public BeanMatcher<T> withQualifiers(Class<? extends Annotation>... qualifiers) {
		if ( qualifiers != null ) {
			if ( beanQualifiers == null )
				beanQualifiers = new HashSet<Class<? extends Annotation>>();
			for ( Class<? extends Annotation> c : qualifiers ) {
				if ( c != null && !beanQualifiers.contains(c) ) {
					beanQualifiers.add(c);
				}
			}
		}
		return this;
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}
	
}
