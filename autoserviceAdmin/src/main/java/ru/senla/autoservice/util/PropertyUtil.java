package ru.senla.autoservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;
import ru.senla.autoservice.exception.PropertyAccessDeniedException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static java.lang.System.err;

@UtilityClass
public class PropertyUtil {

    private static final String APP_PROPS_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "rules\\app.xml";
    private static final String DEFAULT_PROPS_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "rules\\default.xml";

    
    private Properties getPropertiesXML() {
        Properties properties = new Properties();

        try (FileInputStream file = new FileInputStream(ResourceUtils.getFile(APP_PROPS_PATH))) {
            properties.loadFromXML(file);
        } catch (IOException e1) {
            err.println(e1);

            try (FileInputStream file = new FileInputStream(DEFAULT_PROPS_PATH)) {
                properties.loadFromXML(file);
            } catch (IOException e2) {
                err.println(e2);

                return (Properties) Collections.emptyMap();
            }
        }

        return properties;
    }

    public void getAccessProperty(String property) throws PropertyAccessDeniedException {
        Properties properties = getPropertiesXML();

        if (properties.equals(Collections.emptyMap())) {
            throw new PropertyAccessDeniedException("");
        }

        boolean value = Boolean.parseBoolean(properties.getProperty(property));

        if (!value) {
            throw new PropertyAccessDeniedException("");
        }
    }

    public void getPropertyAddAndDeleteFreePlaces() throws PropertyAccessDeniedException {
        getAccessProperty("AddAndDeleteFreePlaces");
    }

    public void getPropertyShiftTimeOfCompletion() throws PropertyAccessDeniedException {
        getAccessProperty("ShiftTimeOfCompletion");
    }

    public void getPropertyDeleteOrder() throws PropertyAccessDeniedException {
        getAccessProperty("DeleteOrder");
    }

}
