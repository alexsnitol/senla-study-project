package autoservice.service;

import autoservice.repository.IGarageRepository;
import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends IAbstractService<Order> {

    void setOrderRepository(IOrderRepository orderRepository);
    void setMasterRepository(IMasterRepository masterRepository);
    void setGarageRepository(IGarageRepository garageRepository);
    void setGarageService(IGarageService garageService);
    void setMasterService(IMasterService masterService);

    void setTimeOfCompletion(Long orderId, int minutes);
    void setStatus(Long idOrder, OrderStatusEnum newStatus);
    void setPrice(Long orderId, float price);

    void assignMasterById(Long orderId, Long masterId);
    void removeMasterById(Long orderId, Long masterId);

    void shiftTimeOfCompletion(Long orderId, int shiftMinutes);

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
