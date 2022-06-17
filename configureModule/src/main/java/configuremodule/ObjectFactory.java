package configuremodule;

import configuremodule.annotation.PostConstruct;
import configuremodule.configurator.ObjectConfigurator;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {
    private final ApplicationContext context;
    private List<ObjectConfigurator> configurators = new ArrayList<>();

    public ObjectFactory(ApplicationContext context) throws Exception {
        this.context = context;
        Reflections scannerConfigure = new Reflections("configuremodule");
        for (Class<? extends ObjectConfigurator> aClass : scannerConfigure.getSubTypesOf(ObjectConfigurator.class)) {
            configurators.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    public <T> T createObject(Class<T> impl) throws Exception {
        T obj = create(impl);
        configure(obj);
        invokeInit(impl, obj);
        return obj;
    }

    private <T> void invokeInit(Class<T> impl, T obj) throws Exception {
        for (Method method : impl.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(obj);
            }
        }
    }

    private <T> void configure(T obj) {
        configurators.forEach(objectConfigurator -> {
            try {
                objectConfigurator.configure(obj, this.context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private <T> T create(Class<T> impl) throws Exception {
        return impl.getDeclaredConstructor().newInstance();
    }


}
