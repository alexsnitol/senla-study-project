package ru.senla.autoservice.service;

import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends IAbstractService<Order> {

    void setOrderRepository(IOrderRepository orderRepository);
    void setMasterRepository(IMasterRepository masterRepository);
    void setGarageRepository(IGarageRepository garageRepository);
    void setGarageService(IGarageService garageService);
    void setMasterService(IMasterService masterService);

    List<Long> addOrderAndTakePlace(Order order);
    void deleteByIdAndFreePlace(Long orderId);
    Order setTimeOfCompletion(Order order, int minutes);
    Order setTimeOfCompletionInOrderByIdAndUpdate(Long orderId, int minutes);
    Order setStatus(Order order, OrderStatusEnum newStatus);
    Order setStatusInOrderByIdAndUpdate(Long orderId, OrderStatusEnum newStatus);
    Order setPrice(Order order, float price);
    Order setPriceInOrderByIdAndUpdate(Long orderId, float price);

    Order assignMasterById(Order order, Long masterId);
    Order assignMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId);
    Order removeMasterById(Order order, Long masterId);
    Order removeMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId);
    Order reducedNumberOfActiveOrdersOfMastersByOrder(Order order);
    Order shiftTimeOfCompletion(Order order, int shiftMinutes);
    void shiftTimeOfCompletionInOrderById(Long orderId, int shiftMinutes);
    String getInfoOfOrder(Order order);

    List<Order> getOrdersByTimeOfCompletion(LocalDateTime from, LocalDateTime to);
    List<Order> getOrdersByTimeOfCompletion(List<Order> orders, LocalDateTime from, LocalDateTime to);
    List<Order> getOrdersByStatus(OrderStatusEnum status);
    List<Order> getOrdersByStatus(List<Order> orders, OrderStatusEnum status);
    List<Order> getAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId);
    List<Order> getOrdersByMasterId(Long masterId);

    List<Order> getOrdersByMasterId(List<Order> orders, Long masterId);
    List<Order> getAllByStatusSorted(OrderStatusEnum orderStatus, String sortType);
    List<Order> getAllByStatusesSorted(List<OrderStatusEnum> orderStatuses, String sortType);
    List<Order> getAllByTimeOfCompletionSorted(LocalDateTime from, LocalDateTime to, String sortType);

    List<Order> getAllByMasterIdSorted(Long masterId, String sortType);
    void exportOrderToJsonFile(Long orderId, String fileName) throws IOException;
    void importOrderFromJsonFile(String path) throws IOException;
    void exportAllOrdersToJsonFile() throws IOException;

    void importAllOrdersFromJsonFile() throws IOException;
}
