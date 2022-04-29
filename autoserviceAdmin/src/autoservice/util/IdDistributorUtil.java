package autoservice.util;

import autoservice.controller.GarageController;
import autoservice.controller.MasterController;
import autoservice.controller.OrderController;

import java.util.ArrayList;
import java.util.List;

public class IdDistributorUtil {
    private static final IdDistributorUtil instance = new IdDistributorUtil();
    private static Long lastId = 0L;

    private IdDistributorUtil() {
    }

    public static IdDistributorUtil getInstance() {
        return instance;
    }

    public static Long getNewId() {
        lastId++;
        return lastId;
    }

    public static void updateId() {
        GarageController garageController = GarageController.getInstance();
        MasterController masterController = MasterController.getInstance();
        OrderController orderController = OrderController.getInstance();

        List<Long> idList = new ArrayList<>();
        idList.add(garageController.getAll().get(garageController.getAll().size()-1).getId());
        idList.add(masterController.getAll().get(masterController.getAll().size()-1).getId());
        idList.add(orderController.getAll().get(orderController.getAll().size()-1).getId());
        Long max = idList.get(0);
        for (Long id : idList) {
            if (max < id) {
                max = id;
            }
        }
        IdDistributorUtil.setLastId(max);
    }

    public static void setLastId(Long id) {
        lastId = id;
    }
}
