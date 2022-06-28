package ru.senla.autoservice.service.comparator.ordercomparator;

import ru.senla.autoservice.repository.model.Order;

import java.time.LocalDateTime;
import java.util.Comparator;

public class OrderTimeOfCreatedComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        LocalDateTime timeOfCreatedOrder1 = o1.getTimeOfCreated();
        LocalDateTime timeOfCreatedOrder2 = o2.getTimeOfCreated();

        return timeOfCreatedOrder1.compareTo(timeOfCreatedOrder2);
    }

}
