package autoservice.service.comparator;

import autoservice.repository.model.Master;
import autoservice.service.comparator.mastercomparator.MasterAlphabeticallyComparator;
import autoservice.service.comparator.mastercomparator.MasterNumberOfActiveOrdersComparator;

public class MapMasterComparator extends AbstractMapComparator<Master> {

    public MapMasterComparator() {
        this.comparatorMap.put("Alphabetically", new MasterAlphabeticallyComparator());
        this.comparatorMap.put("NumberOfActiveOrders", new MasterNumberOfActiveOrdersComparator());
    }

}
