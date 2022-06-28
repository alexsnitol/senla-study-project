package ru.senla.autoservice;

import configuremodule.ApplicationContext;
import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.controller.MasterController;
import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.util.IdDistributorUtil;
import ru.senla.autoservice.view.cli.MenuController;

import java.util.HashMap;

public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = configuremodule.Application.run("ru.senla.autoservice", new HashMap<>());

        IdDistributorUtil.updateId();

        MenuController menuController = context.getObject(MenuController.class);
        menuController.run();

        GarageController.getInstance().exportAllGaragesToJsonFile();
        MasterController.getInstance().exportAllMastersToJsonFile();
        OrderController.getInstance().exportAllOrdersToJsonFile();
    }
}
