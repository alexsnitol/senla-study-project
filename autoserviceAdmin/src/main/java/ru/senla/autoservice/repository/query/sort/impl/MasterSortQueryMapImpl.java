package ru.senla.autoservice.repository.query.sort.impl;

import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.repository.query.sort.ISortQueryMap;

public class MasterSortQueryMapImpl extends AbstractSortQueryMapImpl implements ISortQueryMap {

    public MasterSortQueryMapImpl() {
        sortQueryMap.put("Alphabetically",
                "from Master order by lastName, firstName, patronymic"
        );
        sortQueryMap.put("AlphabeticallyDesc",
                "from Master order by lastName, firstName, patronymic desc"
        );
        sortQueryMap.put("NumberOfActiveOrders",
                " select master"
                + " from OrderMaster"
                + " where order.status = '" + OrderStatusEnum.IN_PROCESS + "'"
                + " or order.status = '" + OrderStatusEnum.POSTPONED + "'"
                + " group by master"
                + " order by count(order) desc, master.lastName, master.firstName, master.patronymic");
    }

}
