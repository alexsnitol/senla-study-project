package ru.senla.autoservice.util;

import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.controller.MasterController;
import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AutoserviceUtil {

    private AutoserviceUtil() {

    }

    public static int getNumberOfFreePlacesByDate(LocalDate date) {
        GarageController garageController = GarageController.getInstance();
        MasterController masterController = MasterController.getInstance();
        OrderController orderController = OrderController.getInstance();

        List<Garage> garages = garageController.getAll();
        List<Master> masters = masterController.getAll();
        List<Order> orders = orderController.getAll();

        LocalDateTime from = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
        LocalDateTime to = from.plusDays(1);

        List<Order> ordersOnDate = orderController.getOrdersFilteredByDateTime(orders, from, to);
        List<Order> ordersInProcessOnDate = orderController.getOrdersFilteredByStatus(
                ordersOnDate,
                OrderStatusEnum.IN_PROCESS);
        List<Order> ordersPostponedOnDate = orderController.getOrdersFilteredByStatus(
                ordersOnDate,
                OrderStatusEnum.POSTPONED);

        List<Master> listOfMastersIdOnAllOrders = new ArrayList<>();
        for (Order order : ordersOnDate) {
            if (order.getStatus() == OrderStatusEnum.IN_PROCESS || order.getStatus() == OrderStatusEnum.POSTPONED) {
                for (Master master : order.getMasters()) {
                    if (!listOfMastersIdOnAllOrders.contains(master)) {
                        listOfMastersIdOnAllOrders.add(master);
                    }
                }
            }
        }

        int numberOfPlaces = 0;
        for (Garage garage : garages) {
            numberOfPlaces += garage.getSize();
        }

        int numberOfFreePlaces = numberOfPlaces - ordersInProcessOnDate.size() - ordersPostponedOnDate.size();
        int numberOfFreeMasters = masters.size() - listOfMastersIdOnAllOrders.size();

        return Math.min(numberOfFreePlaces, numberOfFreeMasters);
    }

    public static LocalDate getNearestDate() {
        LocalDate tmpDate = LocalDate.now();
        while (AutoserviceUtil.getNumberOfFreePlacesByDate(tmpDate) == 0) {
            tmpDate.plusDays(1);
        }

        return tmpDate;
    }

}
