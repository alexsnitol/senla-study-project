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
    Order setStatus(Order order, OrderStatusEnum newStatus);
    Order setPrice(Order order, float price);
    Order assignMasterById(Order order, Long masterId);
    Order removeMasterById(Order order, Long masterId);
    Order reducedNumberOfActiveOrdersOfMastersByOrder(Order order);
    Order shiftTimeOfCompletion(Order order, int shiftMinutes);
    String getInfoOfOrder(Order order);

    List<Order> getOrdersFilteredByDateTime(LocalDateTime from, LocalDateTime to);
    List<Order> getOrdersFilteredByDateTime(List<Order> orders, LocalDateTime from, LocalDateTime to);
    List<Order> getOrdersFilteredByStatus(OrderStatusEnum status);
    List<Order> getOrdersFilteredByStatus(List<Order> orders, OrderStatusEnum status);
    List<Order> getOrdersFilteredByMaster(Long masterId);
    List<Order> getOrdersFilteredByMaster(List<Order> orders, Long masterId);

    void exportOrderToJsonFile(Long orderId, String fileName) throws IOException;
    void importOrderFromJsonFile(String path) throws IOException;
    void exportAllOrdersToJsonFile() throws IOException;
    void importAllOrdersFromJsonFile() throws IOException;

}
