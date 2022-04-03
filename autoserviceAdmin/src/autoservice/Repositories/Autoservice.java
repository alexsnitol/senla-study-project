package autoservice.Repositories;

import autoservice.Controllers.OrderContoller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Autoservice {

    private String name;
    private MasterRepository masters = new MasterRepository();
    private GarageRepository garage = new GarageRepository();
    private OrderRepository orders = new OrderRepository();

    public Autoservice() {
    }

    public Autoservice(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MasterRepository getMasters() {
        return this.masters;
    }

    public GarageRepository getGarage() {
        return this.garage;
    }

    public OrderRepository getOrders() {
        return this.orders;
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

        OrderRepository ordersOnDate = OrderContoller.getOrdersFiltered(this.orders, 0, from, to);
        OrderRepository ordersInProcessOnDate = OrderContoller.getOrdersFiltered(ordersOnDate, 1, OrderStatus.IN_PROCESS);
        OrderRepository ordersPostponedOnDate = OrderContoller.getOrdersFiltered(ordersOnDate, 1, OrderStatus.POSTPONED);

        List<Master> mastersOfOrders = new ArrayList<>();
        for (Order order : ordersOnDate.getOrders()) {
            if (order.getStatus() == OrderStatus.IN_PROCESS || order.getStatus() == OrderStatus.POSTPONED) {
                for (Master master : order.getMasters().getMasters()) {
                    if (mastersOfOrders.indexOf(master) == -1) {
                        mastersOfOrders.add(master);
                    }
                }
            }
        }

        int numberOfFreePlaces = this.garage.sizePlaces() - ordersInProcessOnDate.sizeOrders() - ordersPostponedOnDate.sizeOrders();
        int numberOfFreeMasters = this.masters.sizeMasters() - mastersOfOrders.size();

        if (numberOfFreePlaces < numberOfFreeMasters)
            return numberOfFreePlaces;
        else
            return numberOfFreeMasters;
    }

    public Calendar getNearestDate() {
        Calendar tmpDate = new GregorianCalendar();
        while (getNumberOfFreePlacesByDate(tmpDate) == 0)
            tmpDate.add(Calendar.DAY_OF_MONTH, 1);

        return tmpDate;
    }
}