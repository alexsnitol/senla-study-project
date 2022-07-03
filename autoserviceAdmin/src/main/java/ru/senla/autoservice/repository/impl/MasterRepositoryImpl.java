package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.Query;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.query.sort.ISortQueryMap;
import ru.senla.autoservice.repository.query.sort.impl.MasterSortFullQueryMapImpl;
import ru.senla.autoservice.util.EntityManagerUtil;

import java.util.List;

@Singleton
public class MasterRepositoryImpl extends AbstractRepositoryImpl<Master> implements IMasterRepository {

    @PostConstruct
    public void init() {
        setClazz(Master.class);
    }

    @Override
    public List<Master> findAllSorted(String sortType) {
        ISortQueryMap sortQueryMap = new MasterSortFullQueryMapImpl();
        Query query = EntityManagerUtil.getEntityManager()
                .createQuery(sortQueryMap.getQuery(sortType));

        return query.getResultList();
    }

    @Override
    public List<Master> findMastersByOrderId(Long orderId) {
        Order order = EntityManagerUtil.getEntityManager().createQuery(
                "from Order where id = " + orderId
        ).unwrap(Order.class);

        return order.getMasters();
    }

}