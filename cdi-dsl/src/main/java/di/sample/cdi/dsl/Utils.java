package di.sample.cdi.dsl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.jboss.solder.reflection.AnnotationInstanceProvider;

public class Utils {

	public static Annotation annotationInstance(Class<? extends Annotation> clazz) {
		if ( clazz == null )
			throw new IllegalArgumentException("Class == null");
		AnnotationInstanceProvider provider = new AnnotationInstanceProvider();
		Map<String, Object> values = new HashMap<String, Object>();
		return provider.get(clazz, values);
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable
	 * type.
	 * See: http://www.artima.com/weblogs/viewpost.jsp?thread=208860
	 * 
	 * @param type the type
	 * @return the underlying class
	 */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;
		} 
		else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType)type).getRawType());
		} 
		else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType)type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} 
			else {
				return null;
			}
		} 
		else {
			return null;
		}
	}

}
