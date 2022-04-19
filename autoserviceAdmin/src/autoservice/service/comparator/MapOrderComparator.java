package autoservice.service.comparator;

import autoservice.repository.model.Order;
import autoservice.service.comparator.ordercomparator.OrderPriceComparator;
import autoservice.service.comparator.ordercomparator.OrderTimeOfBeginComparator;
import autoservice.service.comparator.ordercomparator.OrderTimeOfCompletionComparator;
import autoservice.service.comparator.ordercomparator.OrderTimeOfCreatedComparator;

public class MapOrderComparator extends AbstractMapComparator<Order> {

    public MapOrderComparator() {
        this.comparatorMap.put("TimeOfCreated", new OrderTimeOfCreatedComparator());
        this.comparatorMap.put("TimeOfBegin", new OrderTimeOfBeginComparator());
        this.comparatorMap.put("TimeOfCompletion", new OrderTimeOfCompletionComparator());
        this.comparatorMap.put("Price", new OrderPriceComparator());
    }

}
