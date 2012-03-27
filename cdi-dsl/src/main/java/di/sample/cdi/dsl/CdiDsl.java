package di.sample.cdi.dsl;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Extension;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.ProcessAnnotatedType;


/**
 * CDI Dsl Extension
 *
 */
public class CdiDsl implements Extension {

	//private static final Logger logger = LoggerFactory.getLogger(CdiDsl.class);
	
	private Configuration config;
	
	
	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager beanManager) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Before Bean Discovery (start)");
		Set<Module> modules = ModuleService.getInstance().findModules();
		if ( modules != null ) {
			for ( Module m : modules ) {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("BBD Found module [" + m.getClass().getName() + "]");
				config = m.configure(beanManager);
			}
		}
		if ( config != null ) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("BBD Beans size [" + config.getBuilders().size() + "]");
		}
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Before Bean Discovery (end)");
	}

	
	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat, BeanManager beanManager) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());
//		Set<Annotation> ans = pat.getAnnotatedType().getAnnotations();
//		if ( ans != null ) {
//			for ( Annotation a : ans ) {
//				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("   Annotation [" + a.annotationType() + "]");			
//			}
//		}
		AnnotatedType at = pat.getAnnotatedType();
		if ( config != null && config.hasVetoers() ) {
			if ( config.hasVeto(at.getJavaClass(), at.getAnnotations().toArray(new Annotation[at.getAnnotations().size()])) ) {
				pat.veto();
			}
		}
	}

	
	@SuppressWarnings("rawtypes")
	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("After Bean Discovery (start)");			
		if ( config != null ) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ABD Beans size [" + config.getBuilders().size() + "]");
			for ( BeanBuilder builder : config.getBuilders() ) {
				abd.addBean(builder.build(beanManager));
			}
		}
		else {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("No Dsl config found.");			
		}
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("After Bean Discovery (end)");	
	}

}
