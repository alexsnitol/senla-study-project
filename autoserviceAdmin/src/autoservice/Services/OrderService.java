package autoservice.Services;

import autoservice.Repositories.Master;
import autoservice.Repositories.Order;
import autoservice.Repositories.OrderRepository;
import autoservice.Repositories.OrderStatus;

import java.util.Calendar;
import java.util.List;

public class OrderService {
    public static OrderRepository sortOrders(OrderRepository orders, int sortType) {
        switch (sortType) {
            case 0: // ByTimeOfCreated
                return orders.getOrdersSortedByTimeOfCreated();
            case 1: // ByTimeOfBegin
                return orders.getOrdersSortedByTimeOfBegin();
            case 2: // ByTimeOfCompletion
                return orders.getOrdersSortedByTimeOfCompletion();
            case 3: // ByPrice
                return orders.getOrdersSortedByPrice();
        }

        return null;
    }

    public static <T> OrderRepository filterOrders(OrderRepository orders, int filterType, T param) {
        switch (filterType) {
            case 1: // ByStatus
                return orders.getOrdersFilteredByStatus((OrderStatus) param);
            case 2: // ByMaster
                return orders.getOrdersFilteredByMaster((int) param);
        }

        return null;
    }

    public static <T, TT> OrderRepository filterOrders(OrderRepository orders, int filterType, T param1, TT param2) {
        switch (filterType) {
            case 0: // ByDate
                return orders.getOrdersFilteredByDate((Calendar) param1, (Calendar) param2);
        }

        return null;
    }

    public static void shiftOrderTimeOfCompletion(OrderRepository orders, int orderId, int shiftMinutes) {
        Order orderOfId = orders.getOrderById(orderId);

        if (orderOfId == null)
            return;

        if (orderOfId.getStatus() != OrderStatus.IN_PROCESS)
            return;

        orderOfId.shiftTimeOfOrder(shiftMinutes);

        List<Master> masters = orderOfId.getMasters().getMasters();

        for (Order order : orders.getOrders()) {
            if (order.getStatus() == OrderStatus.POSTPONED) {
                for (Master master : masters) {
                    if (order.getMasters().getMasterById(master.getId()) != null) {
                        order.shiftTimeOfOrder(shiftMinutes);
                        break;
                    }
                }
            }
        }
    }
}
