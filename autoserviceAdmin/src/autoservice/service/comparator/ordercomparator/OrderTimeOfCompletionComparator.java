package autoservice.service.comparator.ordercomparator;

import autoservice.repository.model.Order;

import java.util.Calendar;
import java.util.Comparator;

public class OrderTimeOfCompletionComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        Calendar timeOfCompletionOrder1 = o1.getTimeOfBegin();
        Calendar timeOfCompletionOrder2 = o2.getTimeOfBegin();

        return timeOfCompletionOrder1.compareTo(timeOfCompletionOrder2);
    }

}
