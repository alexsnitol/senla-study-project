package ru.senla.autoservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.util.EntityManagerUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements IOrderRepository {

    @PostConstruct
    public void init() {
        setClazz(Order.class);
    }

    @Override
    public void sortCriteriaQuery(CriteriaQuery<Order> cr, Root<Order> root, String sortType) {
        CriteriaBuilder criteriaBuilder = EntityManagerUtil.getEntityManager().getCriteriaBuilder();

        switch (sortType) {
            case "TimeOfCreated":
            case "TimeOfCreatedAsc":
                cr.orderBy(criteriaBuilder.asc(root.get("timeOfCreated")));
                break;
            case "TimeOfCreatedDesc":
                cr.orderBy(criteriaBuilder.desc(root.get("timeOfCreated")));
                break;
            case "TimeOfBegin":
            case "TimeOfBeginAsc":
                cr.orderBy(criteriaBuilder.asc(root.get("timeOfBegin")));
                break;
            case "TimeOfBeginDesc":
                cr.orderBy(criteriaBuilder.desc(root.get("timeOfBegin")));
                break;
            case "TimeOfCompletion":
            case "TimeOfCompletionAsc":
                cr.orderBy(criteriaBuilder.asc(root.get("timeOfCompletion")));
                break;
            case "TimeOfCompletionDesc":
                cr.orderBy(criteriaBuilder.desc(root.get("timeOfCompletion")));
                break;
            case "Price":
            case "PriceAsc":
                cr.orderBy(criteriaBuilder.asc(root.get("price")));
                break;
            case "PriceDesc":
                cr.orderBy(criteriaBuilder.desc(root.get("price")));
                break;
        }
    }

    @Override
    public List<Order> findAllByStatus(OrderStatusEnum orderStatus) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("status"), orderStatus));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByStatuses(List<OrderStatusEnum> orderStatuses) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root)
                .where(root.get("status").in(orderStatuses));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.between(root.get("timeOfCompletion"), from, to));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByMasterId(Long masterId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> order = criteriaQuery.from(clazz);
        Join<Order, Master> master = order.join("masters");

        criteriaQuery
                .select(order)
                .where(criteriaBuilder.gt(master.get("id"), masterId));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> order = criteriaQuery.from(clazz);
        Join<Order, Master> master = order.join("masters");

        criteriaQuery
                .select(order)
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(master.get("id"), masterId),
                        criteriaBuilder.equal(order.get("status"), orderStatus)
                ));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByStatusSorted(OrderStatusEnum orderStatus, String sortType) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("status"), orderStatus));

        sortCriteriaQuery(criteriaQuery, root, sortType);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByStatusesSorted(List<OrderStatusEnum> orderStatuses, String sortType) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root)
                .where(root.get("status").in(orderStatuses));

        sortCriteriaQuery(criteriaQuery, root, sortType);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByTimeOfCompletionSorted(LocalDateTime from, LocalDateTime to, String sortType) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(clazz);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.between(root.get("timeOfCompletion"), from, to));

        sortCriteriaQuery(criteriaQuery, root, sortType);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Order> findAllByMasterIdSorted(Long masterId, String sortType) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> order = criteriaQuery.from(clazz);
        Join<Order, Master> master = order.join("masters");

        criteriaQuery
                .select(order)
                .where(criteriaBuilder.gt(master.get("id"), masterId));

        sortCriteriaQuery(criteriaQuery, criteriaQuery.from(clazz), sortType);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}