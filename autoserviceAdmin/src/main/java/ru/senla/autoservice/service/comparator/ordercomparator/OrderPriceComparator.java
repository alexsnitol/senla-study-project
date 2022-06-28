package ru.senla.autoservice.service.comparator.ordercomparator;

import ru.senla.autoservice.repository.model.Order;

import java.util.Comparator;

public class OrderPriceComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        Float priceOrder1 = o1.getPrice();
        Float priceOrder2 = o2.getPrice();

        return priceOrder1.compareTo(priceOrder2);
    }
}
