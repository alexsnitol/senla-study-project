package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderRepository extends IAbstractRepository<Order> {
    List<Order> findAllByStatus(OrderStatusEnum orderStatus);
    List<Order> findAllByStatuses(List<OrderStatusEnum> orderStatuses);
    List<Order> findAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to);
    List<Order> findAllByMasterId(Long masterId);
    List<Order> findAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId);

    List<Order> findAllByStatusSorted(OrderStatusEnum orderStatus, String sortType);
    List<Order> findAllByStatusesSorted(List<OrderStatusEnum> orderStatus, String sortType);
    List<Order> findAllByTimeOfCompletionSorted(LocalDateTime from, LocalDateTime to, String sortType);

    List<Order> findAllByMasterIdSorted(Long masterId, String sortType);
}
