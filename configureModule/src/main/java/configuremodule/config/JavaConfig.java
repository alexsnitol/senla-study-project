package configuremodule.config;

import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class JavaConfig implements Config {

    private Reflections scanner;
    private Map<Class, Class> ifc2Impl;

    public JavaConfig(String packageToScan, Map<Class, Class> ifc2Impl) {
        this.ifc2Impl = ifc2Impl;
        this.scanner = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        return ifc2Impl.computeIfAbsent(ifc, aClass -> {
           Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
           if (classes.size() != 1) {
               throw new RuntimeException(ifc + " has 0 or more than one implementations." +
                       "Please update your configuration.");
           }
           return classes.iterator().next();
        });
    }

    @Override
    public Reflections getScanner() {
        return scanner;
    }
}
