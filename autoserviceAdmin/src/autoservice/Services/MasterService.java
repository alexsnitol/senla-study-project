package autoservice.Services;

import autoservice.Repositories.Master;
import autoservice.Repositories.MasterRepository;
import autoservice.Repositories.OrderMasterRepository;
import autoservice.Repositories.OrderRepository;

import java.util.List;

public class MasterService {
    public static MasterRepository sortMasters(MasterRepository masters, int sortType) {
        switch (sortType) {
            case 0: // ByAlphabetically
                return masters.getMastersSortedByAlphabetically();
            case 1: // ByNumberOfActiveOrders
                return masters.getMastersSortedByNumberOfActiveOrders();
        }

        return null;
    }

    public static <T> MasterRepository filterMasters(MasterRepository masters, int filterType, T param) {
        return null;
    }

    public static OrderMasterRepository getMastersByOrder(OrderRepository orders, int idOrder) {
        return orders.getOrderById(idOrder).getMasters();
    }
}
