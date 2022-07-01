package configuremodule.configurator;

import configuremodule.ApplicationContext;
import configuremodule.annotation.ConfigProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class ConfigPropertyObjectConfiguratorImpl implements ObjectConfigurator {

    private Map<String, String> propertiesMap;

    public ConfigPropertyObjectConfiguratorImpl() throws Exception {
        URL url = ClassLoader.getSystemClassLoader().getResource("application.properties");

        if (url == null) {
            propertiesMap = Collections.emptyMap();
            return;
        }

        String path = url.getPath();

        try {
            Stream<String> lines = new BufferedReader(new FileReader(path)).lines();
            propertiesMap = lines.map(line -> line.split("=")).collect(toMap(arr -> arr[0], arr -> arr[1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(Object obj, ApplicationContext context) throws Exception {
        Class<?> implClass = obj.getClass();
        for (Field field : implClass.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation != null) {
                String value = annotation.value()
                        .isEmpty() ? propertiesMap.get(field.getName()) : propertiesMap.get(annotation.value());
                field.setAccessible(true);
                field.set(obj, value);
            }
        }
    }

}
