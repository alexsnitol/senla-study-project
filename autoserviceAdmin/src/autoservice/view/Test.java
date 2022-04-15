package autoservice.view;

import autoservice.controller.GarageController;
import autoservice.controller.MasterController;
import autoservice.controller.OrderController;
import autoservice.controller.AutoserviceController;
import autoservice.repository.model.Master;
import autoservice.utility.IdDistributor;
import autoservice.view.cli.MenuController;

public class Test {
    public static void main(String[] args) {
        AutoserviceController autoserviceController = AutoserviceController.getInstance();
        IdDistributor idDistributor = IdDistributor.getInstance();

        GarageController garageController    = AutoserviceController.getGarageController();
        MasterController masterController    = AutoserviceController.getMasterController();
        OrderController  orderController     = AutoserviceController.getOrderController();

        Master tmpMaster;
        tmpMaster = new Master("Slotin", "Alexander", "Sergeevich");
        tmpMaster.setId(IdDistributor.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Novikov", "Alexey", "Pavlovich");
        tmpMaster.setId(IdDistributor.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Smirnov", "Ilya", "Vassilyevich");
        tmpMaster.setId(IdDistributor.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Abalakov", "Anatolii", "Vasilyevich2");
        tmpMaster.setId(IdDistributor.getId());
        masterController.add(tmpMaster);

        tmpMaster = new Master("Abalakov", "Anatolii", "Vasilyevich1");
        tmpMaster.setId(IdDistributor.getId());
        masterController.add(tmpMaster);


        MenuController menuController = new MenuController();

        menuController.run();

    }
}
