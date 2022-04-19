package autoservice.service.comparator.ordercomparator;

import autoservice.repository.model.Order;

import java.util.Calendar;
import java.util.Comparator;

public class OrderTimeOfCreatedComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        Calendar timeOfCreatedOrder1 = o1.getTimeOfCreated();
        Calendar timeOfCreatedOrder2 = o2.getTimeOfCreated();

        return timeOfCreatedOrder1.compareTo(timeOfCreatedOrder2);
    }

}
