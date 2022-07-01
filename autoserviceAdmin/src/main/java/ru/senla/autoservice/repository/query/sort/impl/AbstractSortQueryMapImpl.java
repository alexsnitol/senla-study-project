package ru.senla.autoservice.repository.query.sort.impl;

import ru.senla.autoservice.repository.query.sort.ISortQueryMap;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSortQueryMapImpl implements ISortQueryMap {

    protected Map<String, String> sortQueryMap = new HashMap<>();

    public String getQuery(String sortType) {
        return sortQueryMap.get(sortType);
    }

}
