package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.model.Garage;

import java.util.List;

@Singleton
public class GarageRepositoryImpl extends AbstractRepositoryImpl<Garage> implements IGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(Garage.class);
    }

    public Garage findByOrderId(Long orderId) {
        return entityManager.createQuery(
                "select garage from OrderGarage where order.id = " + orderId
        ).unwrap(Garage.class);
    }

    @Override
    public List<Garage> findAllSorted(String sortType) {
        return findAll();
    }
}