package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderGarage;
import ru.senla.autoservice.util.EntityManagerUtil;

import java.util.List;

@Singleton
public class GarageRepositoryImpl extends AbstractRepositoryImpl<Garage> implements IGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(Garage.class);
    }

    public Garage findByOrderId(Long orderId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Garage> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<OrderGarage> root = criteriaQuery.from(OrderGarage.class);
        Join<OrderGarage, Order> order = root.join("order");

        criteriaQuery
                .select(root.get("garage"))
                .where(criteriaBuilder.gt(order.get("id"), orderId));

        return entityManager.createQuery(criteriaQuery).unwrap(clazz);
    }

    @Override
    public List<Garage> findAllSorted(String sortType) {
        return findAll();
    }

    @Override
    public void sortCriteriaQuery(CriteriaQuery<Garage> cr, Root<Garage> root, String sortType) {
        return;
    }
}