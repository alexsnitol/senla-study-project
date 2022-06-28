package configuremodule;

import configuremodule.annotation.Singleton;
import configuremodule.config.JavaConfig;

import java.util.Map;
import java.util.Set;

public class Application {
    public static ApplicationContext run(String packageToScan, Map<Class, Class> ifc2Impl) throws Exception {
        JavaConfig config = new JavaConfig(packageToScan, ifc2Impl);

        ApplicationContext context = new ApplicationContext(config);

        ObjectFactory objectFactory = new ObjectFactory(context);
        context.setObjectFactory(objectFactory);

        Set<Class<?>> singletonClasses = config.getScanner().getTypesAnnotatedWith(Singleton.class);
        singletonClasses.forEach(sClass -> {
            try {
                context.getObject(sClass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return context;
    }
}
