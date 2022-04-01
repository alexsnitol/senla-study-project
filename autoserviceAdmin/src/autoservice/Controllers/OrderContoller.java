package autoservice.Controllers;

import autoservice.Repositories.OrderRepository;
import autoservice.Services.OrderService;

public class OrderContoller {
    public static OrderRepository getOrdersSorted(OrderRepository orders, int sortType) {
        return OrderService.sortOrders(orders, sortType);
    }

    public static <T> OrderRepository getOrdersFiltered(OrderRepository orders, int filterType, T param) {
        return OrderService.filterOrders(orders, filterType, param);
    }

    public static <T, TT> OrderRepository getOrdersFiltered(OrderRepository orders, int filterType, T param1, TT param2) {
        return OrderService.filterOrders(orders, filterType, param1, param2);
    }

    public static void shiftOrderTimeOfCompletion(OrderRepository orders, int orderId, int shiftMinutes) {
        OrderService.shiftOrderTimeOfCompletion(orders, orderId, shiftMinutes);
    }
}
