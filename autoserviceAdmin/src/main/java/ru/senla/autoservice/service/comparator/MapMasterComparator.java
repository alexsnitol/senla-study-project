package ru.senla.autoservice.service.comparator;

import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.service.comparator.mastercomparator.MasterAlphabeticallyComparator;
import ru.senla.autoservice.service.comparator.mastercomparator.MasterNumberOfActiveOrdersComparator;

public class MapMasterComparator extends AbstractMapComparator<Master> {

    public MapMasterComparator() {
        this.comparatorMap.put("Alphabetically", new MasterAlphabeticallyComparator());
        this.comparatorMap.put("NumberOfActiveOrders", new MasterNumberOfActiveOrdersComparator());
    }

}
