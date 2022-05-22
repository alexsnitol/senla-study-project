package configuremodule.configurator;

import configuremodule.ApplicationContext;

public interface ObjectConfigurator {
    void configure(Object obj, ApplicationContext context) throws Exception;
}