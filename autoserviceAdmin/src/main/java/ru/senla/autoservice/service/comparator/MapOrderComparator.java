package ru.senla.autoservice.service.comparator;

import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.service.comparator.ordercomparator.OrderPriceComparator;
import ru.senla.autoservice.service.comparator.ordercomparator.OrderTimeOfBeginComparator;
import ru.senla.autoservice.service.comparator.ordercomparator.OrderTimeOfCompletionComparator;
import ru.senla.autoservice.service.comparator.ordercomparator.OrderTimeOfCreatedComparator;

public class MapOrderComparator extends AbstractMapComparator<Order> {

    public MapOrderComparator() {
        this.comparatorMap.put("TimeOfCreated", new OrderTimeOfCreatedComparator());
        this.comparatorMap.put("TimeOfBegin", new OrderTimeOfBeginComparator());
        this.comparatorMap.put("TimeOfCompletion", new OrderTimeOfCompletionComparator());
        this.comparatorMap.put("Price", new OrderPriceComparator());
    }

}
