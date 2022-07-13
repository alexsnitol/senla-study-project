package ru.senla.autoservice.util;

import ru.senla.autoservice.exception.PropertyAccessDeniedException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import static java.lang.System.err;

public class PropertyUtil {

    private static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() +
            "\\autoserviceAdmin\\src\\main\\java\\ru\\senla\\autoservice";
    private static final String APP_PROPS_PATH = ROOT_PATH + "\\configuration\\property\\app.xml";
    private static final String DEFAULT_PROPS_PATH = ROOT_PATH + "\\configuration\\property\\default.xml";


    private PropertyUtil() {
    }

    private static Properties getPropertiesXML() {
        Properties properties = new Properties();

        try (FileInputStream file = new FileInputStream(APP_PROPS_PATH)) {
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

    public static void getAccessProperty(String property) throws PropertyAccessDeniedException {
        Properties properties = getPropertiesXML();

        if (properties.equals(Collections.emptyMap())) {
            throw new PropertyAccessDeniedException("");
        }

        boolean value = Boolean.parseBoolean(properties.getProperty(property));

        if (!value) {
            throw new PropertyAccessDeniedException("");
        }
    }

    public static void getPropertyAddAndDeleteFreePlaces() throws PropertyAccessDeniedException {
        getAccessProperty("AddAndDeleteFreePlaces");
    }

    public static void getPropertyShiftTimeOfCompletion() throws PropertyAccessDeniedException {
        getAccessProperty("ShiftTimeOfCompletion");
    }

    public static void getPropertyDeleteOrder() throws PropertyAccessDeniedException {
        getAccessProperty("DeleteOrder");
    }

}
