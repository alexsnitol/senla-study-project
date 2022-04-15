package autoservice.controller;

import autoservice.repository.IGarageRepository;
import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.impl.GarageRepositoryImpl;
import autoservice.repository.impl.MasterRepositoryImpl;
import autoservice.repository.impl.OrderRepositoryImpl;
import autoservice.repository.model.Garage;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.service.IGarageService;
import autoservice.service.IMasterService;
import autoservice.service.IOrderService;
import autoservice.service.impl.GarageServiceImpl;
import autoservice.service.impl.MasterServiceImpl;
import autoservice.service.impl.OrderServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AutoserviceController {

    private static AutoserviceController instance;
    private String name;

    private static IGarageRepository    garageRepository = new GarageRepositoryImpl();
    private static IMasterRepository    masterRepository = new MasterRepositoryImpl();
    private static IOrderRepository     orderRepository = new OrderRepositoryImpl();

    private static IGarageService       garageService = new GarageServiceImpl(garageRepository);
    private static IMasterService       masterService = new MasterServiceImpl(masterRepository, orderRepository);
    private static IOrderService        orderService = new OrderServiceImpl(orderRepository, masterRepository, garageRepository);

    private static GarageController     garageController = new GarageController(garageService);
    private static MasterController     masterController = new MasterController(masterService);
    private static OrderController      orderController = new OrderController(orderService);

    private AutoserviceController() {}

    public static AutoserviceController getInstance() {
        if (instance == null) {
            instance = new AutoserviceController();
        }
        return instance;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GarageController getGarageController() {
        return garageController;
    }

    public static MasterController getMasterController() {
        return masterController;
    }

    public static OrderController getOrderController() {
        return orderController;
    }

    public int getNumberOfFreePlacesByDate(Calendar date) {
        Calendar from = (Calendar) date.clone();
        from.set(Calendar.HOUR, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);

        Calendar to = (Calendar) date.clone();
        to.set(Calendar.HOUR, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        to.set(Calendar.MILLISECOND, 59);

        List<Garage> garages = garageController.getAll();
        List<Master> masters = masterController.getAll();
        List<Order> orders = orderController.getAll();

        List<Order> ordersOnDate = orderController.getOrdersFilteredByDate(orders, from, to);
        List<Order> ordersInProcessOnDate = orderController.getOrdersFilteredByStatus(ordersOnDate, OrderStatusEnum.IN_PROCESS);
        List<Order> ordersPostponedOnDate = orderController.getOrdersFilteredByStatus(ordersOnDate, OrderStatusEnum.POSTPONED);

        List<Master> mastersOfOrders = new ArrayList<>();
        for (Order order : ordersOnDate) {
            if (order.getStatus() == OrderStatusEnum.IN_PROCESS || order.getStatus() == OrderStatusEnum.POSTPONED) {
                for (Master master : order.getMasters()) {
                    if (mastersOfOrders.indexOf(master) == -1) {
                        mastersOfOrders.add(master);
                    }
                }
            }
        }

        int numberOfPlaces = 0;

        for (Garage garage : garages) {
            numberOfPlaces += garage.getSize();
        }


        int numberOfFreePlaces = numberOfPlaces - ordersInProcessOnDate.size() - ordersPostponedOnDate.size();
        int numberOfFreeMasters = masters.size() - mastersOfOrders.size();

        if (numberOfFreePlaces < numberOfFreeMasters) {
            return numberOfFreePlaces;
        } else {
            return numberOfFreeMasters;
        }
    }

    public Calendar getNearestDate() {
        Calendar tmpDate = new GregorianCalendar();
        while (getNumberOfFreePlacesByDate(tmpDate) == 0) {
            tmpDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return tmpDate;
    }
}