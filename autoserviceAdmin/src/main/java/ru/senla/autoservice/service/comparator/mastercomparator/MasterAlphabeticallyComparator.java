package ru.senla.autoservice.service.comparator.mastercomparator;

import ru.senla.autoservice.repository.model.Master;

import java.util.Comparator;

public class MasterAlphabeticallyComparator implements Comparator<Master> {

    @Override
    public int compare(Master m1, Master m2) {
        String fullNameMaster1 = m1.getLastName() + m1.getFirstName() + m1.getPatronymic();
        String fullNameMaster2 = m2.getLastName() + m2.getFirstName() + m2.getPatronymic();

        return fullNameMaster1.compareTo(fullNameMaster2);
    }

}
