package autoservice.controller;

import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.service.IOrderService;

import java.util.Calendar;
import java.util.List;

public class OrderController extends AbstractController<Order, IOrderService> {

    private IOrderService orderService;


    public OrderController(IOrderService defaultService) {
        super(defaultService);
        this.orderService = defaultService;
    }

    public void shiftOrderTimeOfCompletion(Long orderId, int shiftMinutes) {
        orderService.shiftTimeOfCompletion(orderId, shiftMinutes);
    }

    public void setTimeOfCompletion(Long orderId, int minutes) {
        orderService.setTimeOfCompletion(orderId, minutes);
    }

    public void setStatus(Long orderId, OrderStatusEnum status) {
        orderService.setStatus(orderId, status);
    }

    public void assignMasterById(Long orderId, Long masterId) {
        orderService.assignMasterById(orderId, masterId);
    }

    public void removeMasterById(Long orderId, Long masterId) {
        orderService.removeMasterById(orderId, masterId);
    }

    public void shiftTimeOfCompletion(Long orderId, int shiftMinutes) {
        orderService.shiftTimeOfCompletion(orderId, shiftMinutes);
    }

    public void setPrice(Long orderId, float price) {
        orderService.setPrice(orderId, price);
    }

    public String getInfoOfOrder(Order order) {
        return orderService.getInfoOfOrder(order);
    }

    public List<Order> getOrdersFilteredByDate(Calendar from, Calendar to) {
        return orderService.getOrdersFilteredByDate(orderService.getAll(), from, to);
    }

    public List<Order> getOrdersFilteredByDate(List<Order> orders, Calendar from, Calendar to) {
        return orderService.getOrdersFilteredByDate(orders, from, to);
    }

    public List<Order> getOrdersFilteredByStatus(OrderStatusEnum status) {
        return orderService.getOrdersFilteredByStatus(orderService.getAll(), status);
    }

    public List<Order> getOrdersFilteredByStatus(List<Order> orders, OrderStatusEnum status) {
        return orderService.getOrdersFilteredByStatus(orders, status);
    }

    public List<Order> getOrdersFilteredByMaster(Long masterId) {
        return orderService.getOrdersFilteredByMaster(orderService.getAll(), masterId);
    }

    public List<Order> getOrdersFilteredByMaster(List<Order> orders, Long masterId) {
        return orderService.getOrdersFilteredByMaster(orders, masterId);
    }

    public List<Order> getSorted(List<Order> orders, String sortType) {
        return orderService.getSorted(orders, sortType);
    }
}
