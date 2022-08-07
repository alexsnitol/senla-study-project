package ru.senla.autoservice.service.comparator.mastercomparator;

import ru.senla.autoservice.model.Master;

import java.util.Comparator;

public class MasterNumberOfActiveOrdersComparator implements Comparator<Master> {

    @Override
    public int compare(Master m1, Master m2) {
        Integer numberOfActiveOrdersMaster1 = m1.getNumberOfActiveOrders();
        Integer numberOfActiveOrdersMaster2 = m2.getNumberOfActiveOrders();

        return numberOfActiveOrdersMaster1.compareTo(numberOfActiveOrdersMaster2);
    }

}
