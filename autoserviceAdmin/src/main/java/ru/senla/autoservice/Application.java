package ru.senla.autoservice;

import configuremodule.ApplicationContext;
import ru.senla.autoservice.view.cli.MenuController;

import java.util.HashMap;

public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = configuremodule.Application.run("ru.senla.autoservice", new HashMap<>());

        MenuController menuController = context.getObject(MenuController.class);
        menuController.run();
    }
}
