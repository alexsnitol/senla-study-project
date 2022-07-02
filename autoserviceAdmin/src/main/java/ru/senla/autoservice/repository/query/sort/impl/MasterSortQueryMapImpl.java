package ru.senla.autoservice.repository.query.sort.impl;

import ru.senla.autoservice.repository.query.sort.ISortQueryMap;

public class MasterSortQueryMapImpl extends AbstractSortQueryMapImpl implements ISortQueryMap {

    public MasterSortQueryMapImpl() {
        sortQueryMap.put("Alphabetically",
                " order by lastName, firstName, patronymic"
        );
        sortQueryMap.put("AlphabeticallyDesc",
                " order by lastName, firstName, patronymic desc"
        );
    }

}
