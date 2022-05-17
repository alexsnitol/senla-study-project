package autoservice;

import autoservice.controller.GarageController;
import autoservice.controller.MasterController;
import autoservice.controller.OrderController;
import autoservice.repository.IGarageRepository;
import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.impl.GarageRepositoryImpl;
import autoservice.repository.impl.MasterRepositoryImpl;
import autoservice.repository.impl.OrderRepositoryImpl;
import autoservice.repository.model.Garage;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.service.IGarageService;
import autoservice.service.IMasterService;
import autoservice.service.IOrderService;
import autoservice.service.impl.GarageServiceImpl;
import autoservice.service.impl.MasterServiceImpl;
import autoservice.service.impl.OrderServiceImpl;
import autoservice.util.IdDistributorUtil;
import autoservice.util.JsonUtil;
import autoservice.view.cli.MenuController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class Application {
    public static void main(String[] args) throws Exception {

        IGarageRepository garageRepository = new GarageRepositoryImpl();
        IMasterRepository masterRepository = new MasterRepositoryImpl();
        IOrderRepository  orderRepository = new OrderRepositoryImpl();

        IGarageService garageService = new GarageServiceImpl(garageRepository);
        IMasterService masterService = new MasterServiceImpl(masterRepository, orderRepository);
        IOrderService  orderService = new OrderServiceImpl(orderRepository, masterRepository, garageRepository, garageService, masterService);

        GarageController garageController = GarageController.getInstance();
        MasterController masterController = MasterController.getInstance();
        OrderController  orderController  = OrderController.getInstance();

        garageController.setGarageService(garageService);
        masterController.setMasterService(masterService);
        orderController.setOrderService(orderService);
        orderController.setGarageService(garageService);
        orderController.setMasterService(masterService);

        garageController.importAllGaragesFromJsonFile();
        masterController.importAllMastersFromJsonFile();
        orderController.importAllOrdersFromJsonFile();

        IdDistributorUtil.updateId();

        /*
        Master tmpMaster;
        tmpMaster = new Master("Slotin", "Alexander", "Sergeevich");
        tmpMaster.setId(IdDistributorUtil.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Novikov", "Alexey", "Pavlovich");
        tmpMaster.setId(IdDistributorUtil.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Smirnov", "Ilya", "Vassilyevich");
        tmpMaster.setId(IdDistributorUtil.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Abalakov", "Anatolii", "Vasilyevich2");
        tmpMaster.setId(IdDistributorUtil.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Abalakov", "Anatolii", "Vasilyevich1");
        tmpMaster.setId(IdDistributorUtil.getId());
        masterController.add(tmpMaster);

        Garage tmpGarage;
        tmpGarage = new Garage(10);
        tmpGarage.setId(IdDistributorUtil.getId());
        garageController.add(tmpGarage);

        tmpGarage = new Garage(5);
        tmpGarage.setId(IdDistributorUtil.getId());
        garageController.add(tmpGarage);

        tmpGarage = new Garage(2);
        tmpGarage.setId(IdDistributorUtil.getId());
        garageController.add(tmpGarage);

        Order tmpOrder = new Order(60);
        Long tmpId = IdDistributorUtil.getId();
        tmpOrder.setId(tmpId);
        tmpOrder.setPrice(5000);
        orderController.add(tmpOrder);
        orderController.assignMasterById(tmpId, tmpMaster.getId());
        */

        MenuController menuController = new MenuController();

        menuController.run();

        garageController.exportAllGaragesToJsonFile();
        masterController.exportAllMastersToJsonFile();
        orderController.exportAllOrdersToJsonFile();
    }
}
