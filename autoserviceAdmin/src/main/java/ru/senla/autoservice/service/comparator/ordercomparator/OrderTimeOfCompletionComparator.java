package ru.senla.autoservice.service.comparator.ordercomparator;

import ru.senla.autoservice.repository.model.Order;

import java.time.LocalDateTime;
import java.util.Comparator;

public class OrderTimeOfCompletionComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        LocalDateTime timeOfCompletionOrder1 = o1.getTimeOfBegin();
        LocalDateTime timeOfCompletionOrder2 = o2.getTimeOfBegin();

        return timeOfCompletionOrder1.compareTo(timeOfCompletionOrder2);
    }

}
