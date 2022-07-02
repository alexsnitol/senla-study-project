package ru.senla.autoservice.repository.query.sort.impl;

import ru.senla.autoservice.repository.query.sort.ISortQueryMap;

public class OrderSortQueryMapImpl extends AbstractSortQueryMapImpl implements ISortQueryMap {

    public OrderSortQueryMapImpl() {
        sortQueryMap.put("TimeOfCreated",
                " order by timeOfCreated"
        );
        sortQueryMap.put("TimeOfCreatedDesc",
                " order by timeOfCreated desc"
        );
        sortQueryMap.put("TimeOfBegin",
                " order by timeOfBegin"
        );
        sortQueryMap.put("TimeOfBeginDesc",
                " order by timeOfBegin desc"
        );
        sortQueryMap.put("TimeOfCompletion",
                " order by timeOfCompletion"
        );
        sortQueryMap.put("TimeOfCompletionDesc",
                " order by timeOfCompletion desc"
        );
        sortQueryMap.put("Price",
                " order by price"
        );
        sortQueryMap.put("PriceDesc",
                " order by price desc"
        );
    }

}
