package ru.senla.autoservice.service.comparator.ordercomparator;

import ru.senla.autoservice.model.Order;

import java.time.LocalDateTime;
import java.util.Comparator;

public class OrderTimeOfBeginComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        LocalDateTime timeOfBeginOrder1 = o1.getTimeOfBegin();
        LocalDateTime timeOfBeginOrder2 = o2.getTimeOfBegin();

        return timeOfBeginOrder1.compareTo(timeOfBeginOrder2);
    }

}
