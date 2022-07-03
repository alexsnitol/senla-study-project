package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.repository.query.sort.ISortQueryMap;
import ru.senla.autoservice.repository.query.sort.impl.OrderSortFullQueryMapImpl;
import ru.senla.autoservice.repository.query.sort.impl.OrderSortQueryMapImpl;
import ru.senla.autoservice.util.EntityManagerUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements IOrderRepository {

    @PostConstruct
    public void init() {
        setClazz(Order.class);
    }

    @Override
    public List<Order> findAllSorted(String sortType) {
        ISortQueryMap sortQueryMap = new OrderSortFullQueryMapImpl();
        Query query = EntityManagerUtil.getEntityManager()
                .createQuery(sortQueryMap.getQuery(sortType));

        return query.getResultList();
    }

    @Override
    public List<Order> findAllByStatus(OrderStatusEnum orderStatus) {
        return EntityManagerUtil.getEntityManager().createQuery(
                "from Order where status = " + orderStatus.toString()
        ).getResultList();
    }

    @Override
    public List<Order> findAllByStatuses(List<OrderStatusEnum> orderStatuses) {
        String query = "from Order where ";

        for (int i = 0; i < orderStatuses.size(); i++) {
            query += "status = " + orderStatuses.get(i).toString();

            if (i != orderStatuses.size() - 1) {
                query += " or ";
            }
        }

        return EntityManagerUtil.getEntityManager().createQuery(query).getResultList();
    }

    @Override
    public List<Order> findAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to) {
        return EntityManagerUtil.getEntityManager().createQuery(
                "from Order where timeOfCompletion between :from and :to"
        )
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Order> findAllByMasterId(Long masterId) {
        List<Order> orders = findAll();

        orders = orders.stream()
                .filter(o -> o.getMasters()
                        .stream()
                        .filter(m -> m.getId().equals(masterId))
                        .findFirst()
                        .orElse(null) != null)
                .collect(Collectors.toList());

        return orders;
    }

    @Override
    public List<Order> findAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId) {
        List<Order> orders = EntityManagerUtil.getEntityManager().createQuery(
                "from Order where status = " + orderStatus.toString()
        ).getResultList();

        orders = orders.stream()
                .filter(o -> o.getMasters()
                        .stream()
                        .filter(m -> m.getId().equals(masterId))
                        .findFirst()
                        .orElse(null) != null)
                .collect(Collectors.toList());

        return orders;
    }

    @Override
    public List<Order> findAllByStatusSorted(OrderStatusEnum orderStatus, String sortType) {
        ISortQueryMap sortQueryMap = new OrderSortQueryMapImpl();
        Query query = EntityManagerUtil.getEntityManager()
                .createQuery(
                        "from Order"
                                + " where status = " + orderStatus.toString()
                                + sortQueryMap.getQuery(sortType)
                );

        return query.getResultList();
    }

    @Override
    public List<Order> findAllByStatusesSorted(List<OrderStatusEnum> orderStatuses, String sortType) {
        ISortQueryMap sortQueryMap = new OrderSortQueryMapImpl();
        String query = "from Order where ";

        for (int i = 0; i < orderStatuses.size(); i++) {
            query += "status = " + orderStatuses.get(i).toString();

            if (i != orderStatuses.size() - 1) {
                query += " or ";
            }
        }

        query += sortQueryMap.getQuery(sortType);

        return EntityManagerUtil.getEntityManager().createQuery(query).getResultList();
    }

    @Override
    public List<Order> findAllByTimeOfCompletionSorted(LocalDateTime from, LocalDateTime to, String sortType) {
        ISortQueryMap sortQueryMap = new OrderSortQueryMapImpl();
        String query = "from Order where ";

        query += "timeOfCompletion between :from and :to";
        query += sortQueryMap.getQuery(sortType);

        return EntityManagerUtil.getEntityManager().createQuery(query)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Order> findAllByMasterIdSorted(Long masterId, String sortType) {
        ISortQueryMap sortQueryMap = new OrderSortQueryMapImpl();
        String query = sortQueryMap.getQuery(sortType);

        List<Order> orders = EntityManagerUtil.getEntityManager().createQuery(query).getResultList();
        orders = orders.stream()
                .filter(o -> o.getMasters()
                        .stream()
                        .filter(m -> m.getId().equals(masterId))
                        .findFirst()
                        .orElse(null) != null)
                .collect(Collectors.toList());

        return orders;
    }

}