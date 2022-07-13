package configuremodule.configurator;

import configuremodule.ApplicationContext;
import configuremodule.annotation.Autowired;

import java.lang.reflect.Field;

public class AutowiredObjectConfiguratorIml implements ObjectConfigurator {

    @Override
    public void configure(Object obj, ApplicationContext context) throws Exception {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object objFromContext = context.getObject(field.getType());
                field.set(obj, objFromContext);
            }
        }
    }

}
