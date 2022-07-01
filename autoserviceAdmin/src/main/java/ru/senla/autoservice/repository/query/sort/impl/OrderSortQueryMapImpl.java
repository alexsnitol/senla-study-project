package ru.senla.autoservice.repository.query.sort.impl;

import ru.senla.autoservice.repository.query.sort.ISortQueryMap;

public class OrderSortQueryMapImpl extends AbstractSortQueryMapImpl implements ISortQueryMap {

    public OrderSortQueryMapImpl() {
        sortQueryMap.put("TimeOfCreated",
                "from Order order by timeOfCreated"
        );
        sortQueryMap.put("TimeOfCreatedDesc",
                "from Order order by timeOfCreated desc"
        );
        sortQueryMap.put("TimeOfBegin",
                "from Order order by timeOfBegin"
        );
        sortQueryMap.put("TimeOfBeginDesc",
                "from Order order by timeOfBegin desc"
        );
        sortQueryMap.put("TimeOfCompletion",
                "from Order order by timeOfCompletion"
        );
        sortQueryMap.put("TimeOfCompletionDesc",
                "from Order order by timeOfCompletion desc"
        );
        sortQueryMap.put("Price",
                "from Order order by price"
        );
        sortQueryMap.put("PriceDesc",
                "from Order order by price desc"
        );
    }

}
