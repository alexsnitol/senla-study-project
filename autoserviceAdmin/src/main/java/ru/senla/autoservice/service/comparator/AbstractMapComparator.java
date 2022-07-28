package ru.senla.autoservice.service.comparator;

import ru.senla.autoservice.model.AbstractModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMapComparator<M extends AbstractModel> {

    protected Map<String, Comparator<M>> comparatorMap = new HashMap<>();

    public Comparator<M> exetuce(String sortType) {
        return comparatorMap.get(sortType);
    }

}
