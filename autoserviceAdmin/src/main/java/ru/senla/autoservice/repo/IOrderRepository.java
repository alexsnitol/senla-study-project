package ru.senla.autoservice.repo;

import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderRepository extends IAbstractRepository<Order> {

    List<Order> findAllByStatus(OrderStatusEnum orderStatus);
    List<Order> findAllByStatuses(List<OrderStatusEnum> orderStatuses);
    List<Order> findAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to);
    List<Order> findAllByMasterId(Long masterId);
    List<Order> findAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId);

}
