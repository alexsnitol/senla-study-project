package autoservice.service;

import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;

import java.util.Calendar;
import java.util.List;

public interface IOrderService extends IAbstractService<Order> {
    void setTimeOfCompletion(Long orderId, int minutes);
    void setStatus(Long idOrder, OrderStatusEnum status);
    void assignMasterById(Long orderId, Long masterId);
    void removeMasterById(Long orderId, Long masterId);
    void shiftTimeOfCompletion(Long orderId, int shiftMinutes);
    void setPrice(Long orderId, float price);
    String getInfoOfOrder(Order order);
    List<Order> getOrdersFilteredByDate(Calendar from, Calendar to);
    List<Order> getOrdersFilteredByDate(List<Order> orders, Calendar from, Calendar to);
    List<Order> getOrdersFilteredByStatus(OrderStatusEnum status);
    List<Order> getOrdersFilteredByStatus(List<Order> orders, OrderStatusEnum status);
    List<Order> getOrdersFilteredByMaster(Long masterId);
    List<Order> getOrdersFilteredByMaster(List<Order> orders, Long masterId);
}
