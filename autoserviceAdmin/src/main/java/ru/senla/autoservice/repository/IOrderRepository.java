package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;

import java.util.List;

public interface IOrderRepository extends IAbstractRepository<Order> {
    List<Order> findOrdersByStatus(OrderStatusEnum orderStatus);
    List<Order> findOrdersByStatuses(List<OrderStatusEnum> orderStatuses);
}
