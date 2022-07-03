package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import ru.senla.autoservice.repository.IOrderGarageRepository;
import ru.senla.autoservice.repository.model.OrderGarage;
import ru.senla.autoservice.util.EntityManagerUtil;

import java.util.List;

@Singleton
public class OrderGarageRepositoryImpl extends AbstractRepositoryImpl<OrderGarage> implements IOrderGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(OrderGarage.class);
    }

    @Override
    public OrderGarage findByOrderId(Long orderId) {
        return EntityManagerUtil.getEntityManager()
                .createQuery("from OrderGarage where order.id = " + orderId)
                .unwrap(clazz);
    }

    @Override
    public List<OrderGarage> findByGarageId(Long garageId) {
        return EntityManagerUtil.getEntityManager()
                .createQuery("from OrderGarage where garage.id = " + garageId)
                .getResultList();
    }

    @Override
    public OrderGarage findByOrderIdAndByGarageId(Long orderId, Long garageId) {
        return EntityManagerUtil.getEntityManager()
                .createQuery("from OrderGarage"
                + " where order.id = " + orderId + " and garage.id = " + garageId)
                .unwrap(clazz);
    }

    @Override
    public List<OrderGarage> findAllSorted(String sortType) {
        return findAll();
    }
}
