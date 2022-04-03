package autoservice.Controllers;

import autoservice.Repositories.Master;
import autoservice.Repositories.MasterRepository;
import autoservice.Repositories.OrderMasterRepository;
import autoservice.Repositories.OrderRepository;
import autoservice.Services.MasterService;

import java.util.List;

public class MasterController {
    public static MasterRepository getMastersSorted(MasterRepository masters, int sortType) {
        return MasterService.sortMasters(masters, sortType);
    }

    public static <T> MasterRepository getMastersFiltered(MasterRepository masters, int filterType, T param) {
        return MasterService.filterMasters(masters, filterType, param);
    }

    public static OrderMasterRepository getMastersByOrder(OrderRepository orders, int idOrder) {
        return MasterService.getMastersByOrder(orders, idOrder);
    }
}
