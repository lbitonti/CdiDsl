package di.sample.cdi.dsl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;


public class BeanBuilder<T> {
	
	private Class<T> beanClass;
	private Set<Class<? extends Annotation>> beanQualifiers;
	private Set<Class<? extends Annotation>> beanStereotypes;
	private Set<Type> beanTypes;
	private Class<? extends Annotation> scope;
	private String name;
	private boolean alternative = false;
	
	
	public BeanBuilder(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	    
	
	public Bean<T> build(BeanManager beanManager) {
		AnnotatedType<T> at = beanManager.createAnnotatedType(beanClass); 

        final InjectionTarget<T> it = beanManager.createInjectionTarget(at); 

		return new Bean<T>() {
			@Override
			public Class<?> getBeanClass() {
				return beanClass;
			}

			@Override
			public Set<InjectionPoint> getInjectionPoints() {
				return it.getInjectionPoints();
			}

            @Override
            public String getName() {
            	if ( name != null && !"".equals(name) )
            		return name;
                return "Bean<" + beanClass + ">";
            }

            @Override
            public Set<Annotation> getQualifiers() {
                Set<Annotation> qualifiers = new HashSet<Annotation>();
                qualifiers.add( new AnnotationLiteral<Any>() {} );
                if ( beanQualifiers == null || beanQualifiers.isEmpty() ) {
                    qualifiers.add( new AnnotationLiteral<Default>() {} );
                }
                if ( beanQualifiers != null ) {
                	for ( final Class<? extends Annotation> clazz : beanQualifiers ) {
            			if ( clazz != null ) {
            				Annotation annotation = Utils.annotationInstance(clazz);
            				qualifiers.add(annotation);
            			}
                	}
                }
                return qualifiers;
            }

            @Override
            public Class<? extends Annotation> getScope() {
            	if ( scope != null )
            		return scope;
            	return Dependent.class;
            }

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                if ( beanStereotypes != null ) {
                    return beanStereotypes;
                }
                else {
                	return Collections.emptySet();                	
                }
            }

            @Override
            public Set<Type> getTypes() {
                Set<Type> types = new HashSet<Type>();
                types.add(beanClass);
                types.add(Object.class);
                if ( beanTypes != null ) {
                	for ( Type c : beanTypes ) {
                		if ( c != null )
                			types.add(c);
                	}
                }
                return types;
            }

            @Override
            public boolean isAlternative() {
                return alternative;
            }

            @Override
            public boolean isNullable() {
                return false;
            }

            @Override
            public T create(CreationalContext<T> ctx) {
                T instance = it.produce(ctx);
                it.inject(instance, ctx);
                it.postConstruct(instance);
                return instance;
            }

            @Override
            public void destroy(T instance, CreationalContext<T> ctx) {
                it.preDestroy(instance);
                it.dispose(instance);
                ctx.release();
            }        

        };

	}


	public BeanBuilder<T> withQualifiers(Class<? extends Annotation>... qualifiers) {
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

	public BeanBuilder<T> withStereotypes(Class<? extends Annotation>... stereotypes) {
		if ( stereotypes != null ) {
			if ( beanStereotypes == null )
				beanStereotypes = new HashSet<Class<? extends Annotation>>();
			for ( Class<? extends Annotation> c : stereotypes ) {
				if ( c != null && !beanStereotypes.contains(c) ) {
					beanStereotypes.add(c);
				}
			}
		}
		return this;
	}

	public BeanBuilder<T> withBeanTypes(Type... types) {
		if ( types != null ) {
			if ( beanTypes == null )
				beanTypes = new HashSet<Type>();
			for ( Type c : types ) {
				if ( c != null && !beanTypes.contains(c) ) {
					beanTypes.add(c);
				}
			}
		}
		return this;
	}

	public BeanBuilder<T> InScope(Class<? extends Annotation> scope) {
		if ( scope != null ) {
			this.scope = scope;
		}
		return this;
	}

	public BeanBuilder<T> withName(String name) {
		this.name = name;
		return this;
	}

	public BeanBuilder<T> asAlternative() {
		this.alternative = true;
		return this;
	}
	
}
