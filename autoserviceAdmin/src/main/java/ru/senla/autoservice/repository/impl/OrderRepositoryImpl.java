package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.repository.query.sort.ISortQueryMap;
import ru.senla.autoservice.repository.query.sort.impl.OrderSortQueryMapImpl;

import java.util.List;

@Singleton
@Slf4j
public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements IOrderRepository {

    @PostConstruct
    public void init() {
        setClazz(Order.class);
    }

    @Override
    public List<Order> findAllSorted(String sortType) {
        ISortQueryMap sortQueryMap = new OrderSortQueryMapImpl();
        Query query = entityManager
                .createQuery(sortQueryMap.getQuery(sortType));

        return query.getResultList();
    }

    @Override
    public List<Order> findOrdersByStatus(OrderStatusEnum orderStatus) {
        return entityManager.createQuery(
                "from Order where status = " + orderStatus.toString()
        ).getResultList();
    }

    @Override
    public List<Order> findOrdersByStatuses(List<OrderStatusEnum> orderStatuses) {
        String query = "from Order where ";

        for (int i = 0; i < orderStatuses.size(); i++) {
            OrderStatusEnum status = orderStatuses.get(i);
            query += "status = " + status.toString();

            if (i != orderStatuses.size() - 1) {
                query += " or ";
            }
        }

        return entityManager.createQuery(query).getResultList();
    }
}