package di.sample.cdi.dsl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Configuration {

	private List<BeanBuilder> builders = new ArrayList<BeanBuilder>();
	private List<BeanMatcher> vetoers = new ArrayList<BeanMatcher>();

	
	public void addBuilder(BeanBuilder builder) {
		if ( builder != null ) {
			this.builders.add(builder);
		}
	}
	
	public void addVetoer(BeanMatcher vetoer) {
		if ( vetoer != null ) {
			this.vetoers.add(vetoer);
		}
	}

	public boolean hasVeto(Type type, Annotation... qualifiers) {
		if ( type != null ) {
			for ( BeanMatcher bm : vetoers ) {
				if ( bm != null && bm.matches(type, qualifiers) )
					return true;
			}
		}
		return false;
	}

	public List<BeanBuilder> getBuilders() {
		return builders;
	}

	public boolean hasVetoers() {
		return vetoers != null && !vetoers.isEmpty();
	}
	
//	public List<BeanMatcher> getVetoers() {
//		return vetoers;
//	}

}
