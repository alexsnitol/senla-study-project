package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.Query;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.query.sort.ISortQueryMap;
import ru.senla.autoservice.repository.query.sort.impl.MasterSortQueryMapImpl;

import java.util.List;

@Singleton
public class MasterRepositoryImpl extends AbstractRepositoryImpl<Master> implements IMasterRepository {

    @PostConstruct
    public void init() {
        setClazz(Master.class);
    }

    @Override
    public List<Master> findAllSorted(String sortType) {
        ISortQueryMap sortQueryMap = new MasterSortQueryMapImpl();
        Query query = entityManager
                .createQuery(sortQueryMap.getQuery(sortType));

        return query.getResultList();
    }

}