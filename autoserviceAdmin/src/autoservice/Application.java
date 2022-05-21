package autoservice;

import autoservice.controller.GarageController;
import autoservice.controller.MasterController;
import autoservice.controller.OrderController;
import autoservice.util.IdDistributorUtil;
import autoservice.view.cli.MenuController;
import configuremodule.*;

import java.util.HashMap;

public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = configuremodule.Application.run("autoservice", new HashMap<>());

        IdDistributorUtil.updateId();

        MenuController menuController = context.getObject(MenuController.class);
        menuController.run();

        GarageController.getInstance().exportAllGaragesToJsonFile();
        MasterController.getInstance().exportAllMastersToJsonFile();
        OrderController.getInstance().exportAllOrdersToJsonFile();
    }
}
