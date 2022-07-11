package ru.senla.autoservice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.senla.autoservice.view.cli.MenuController;

public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("ru.senla.autoservice");

        MenuController menuController = context.getBean(MenuController.class);
        menuController.run();
    }
}
