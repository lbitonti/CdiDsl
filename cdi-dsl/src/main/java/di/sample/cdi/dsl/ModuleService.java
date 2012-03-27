package di.sample.cdi.dsl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;


public class ModuleService {

	private static ModuleService service;
	private ServiceLoader<Module> loader;

	private ModuleService() {
		loader = ServiceLoader.load(Module.class);
	}

	public synchronized static ModuleService getInstance() {
		if (service == null) {
			service = new ModuleService();
		}
		return service;
	}

	public Set<Module> findModules() {
		Set<Module> modules = new HashSet<Module>();
		if ( loader != null ) {
			Iterator<Module> mIter = loader.iterator();
			while (mIter.hasNext()) {
				modules.add(mIter.next());
			}
		}
		return modules;
	}

}
