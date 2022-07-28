package ru.senla.autoservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AutoserviceUtil {

    private static IGarageService garageService;
    private static IMasterService masterService;
    private static IOrderService orderService;

    @Autowired
    public AutoserviceUtil(IGarageService garageService, IMasterService masterService, IOrderService orderService) {
        this.garageService = garageService;
        this.masterService = masterService;
        this.orderService = orderService;
    }

    public static Integer getNumberOfFreePlacesByDate(LocalDate date) {
        List<Garage> garages = garageService.getAll();
        List<Master> masters = masterService.getAll();
        List<Order> orders = orderService.getAll();

        LocalDateTime from = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
        LocalDateTime to = from.plusDays(1);

        List<Order> ordersOnDate = orderService.getAllByTimeOfCompletion(orders, from, to);
        List<Order> ordersInProcessOnDate = orderService.getAllByStatus(
                ordersOnDate,
                OrderStatusEnum.IN_PROCESS);
        List<Order> ordersPostponedOnDate = orderService.getAllByStatus(
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
