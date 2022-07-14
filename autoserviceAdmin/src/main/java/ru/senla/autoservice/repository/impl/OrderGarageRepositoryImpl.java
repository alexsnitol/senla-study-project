package ru.senla.autoservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.senla.autoservice.repository.IOrderGarageRepository;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderGarage;
import ru.senla.autoservice.util.EntityManagerUtil;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class OrderGarageRepositoryImpl extends AbstractRepositoryImpl<OrderGarage> implements IOrderGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(OrderGarage.class);
    }

    @Override
    public OrderGarage findByOrderId(Long orderId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderGarage> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<OrderGarage> orderGarage = criteriaQuery.from(OrderGarage.class);
        Join<OrderGarage, Order> order = orderGarage.join("order");

        criteriaQuery
                .select(orderGarage)
                .where(criteriaBuilder.gt(order.get("id"), orderId));

        return entityManager.createQuery(criteriaQuery).unwrap(clazz);
    }

    @Override
    public List<OrderGarage> findByGarageId(Long garageId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderGarage> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<OrderGarage> root = criteriaQuery.from(OrderGarage.class);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.gt(root.get("garage").get("id"), garageId));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public OrderGarage findByOrderIdAndByGarageId(Long orderId, Long garageId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderGarage> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<OrderGarage> orderGarage = criteriaQuery.from(OrderGarage.class);
        Join<OrderGarage, Order> order = orderGarage.join("order");
        Join<OrderGarage, Garage> garage = orderGarage.join("garage");

        criteriaQuery
                .select(orderGarage)
                .where(criteriaBuilder.and(
                        criteriaBuilder.gt(order.get("id"), orderId),
                        criteriaBuilder.gt(garage.get("id"), garageId)
                ));

        return entityManager.createQuery(criteriaQuery).unwrap(clazz);
    }

    @Override
    public List<OrderGarage> findAllSorted(String sortType) {
        return findAll();
    }

    @Override
    public void sortCriteriaQuery(CriteriaQuery<OrderGarage> cr, Root<OrderGarage> root, String sortType) {
        return;
    }
}
