package autoservice.service.comparator.ordercomparator;

import autoservice.repository.model.Order;

import java.util.Calendar;
import java.util.Comparator;

public class OrderTimeOfBeginComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        Calendar timeOfBeginOrder1 = o1.getTimeOfBegin();
        Calendar timeOfBeginOrder2 = o2.getTimeOfBegin();

        return timeOfBeginOrder1.compareTo(timeOfBeginOrder2);
    }

}
