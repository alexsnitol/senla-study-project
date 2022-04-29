package autoservice.controller;

import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.service.IGarageService;
import autoservice.service.IMasterService;
import autoservice.service.IOrderService;
import autoservice.service.impl.OrderServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderController extends AbstractController<Order, IOrderService> {

    private static OrderController instance;
    private IOrderService orderService;
    private IGarageService garageService;
    private IMasterService masterService;


    private OrderController() {
        super(new OrderServiceImpl());
    }

    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }

    public void setOrderService(IOrderService orderService) {
        this.defaultService = orderService;
        this.orderService = orderService;
    }

    public void setGarageService(IGarageService garageService) {
        this.garageService = garageService;
    }

    public void setMasterService(IMasterService masterService) {
        this.masterService = masterService;
    }

    public List<Long> add(Order order) {
        orderService.add(order);
        return garageService.takePlace(order.getId());
    }

    public void deleteById(Long orderId) {
        orderService.deleteById(orderId);
        garageService.freePlaceByOrderId(orderId);
    }

    public void setTimeOfCompletion(Long orderId, int minutes) {
        orderService.setTimeOfCompletion(orderId, minutes);
    }

    public void setStatus(Long orderId, OrderStatusEnum newStatus) {
        orderService.setStatus(orderId, newStatus);
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

    public List<Order> getOrdersFilteredByDateTime(LocalDateTime from, LocalDateTime to) {
        return orderService.getOrdersFilteredByDateTime(orderService.getAll(), from, to);
    }

    public List<Order> getOrdersFilteredByDateTime(List<Order> orders, LocalDateTime from, LocalDateTime to) {
        return orderService.getOrdersFilteredByDateTime(orders, from, to);
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

    public List<Order> getSorted(String sortType) {
        return orderService.getSorted(sortType);
    }

    public List<Order> getSorted(List<Order> listOfOrder, String sortType) {
        return orderService.getSorted(listOfOrder, sortType);
    }

    public void exportOrderToJsonFile(Long orderId, String fileName) throws IOException {
        orderService.exportOrderToJsonFile(orderId, fileName);
    }

    public void importOrderFromJsonFile(String path) throws IOException {
        orderService.importOrderFromJsonFile(path);
    }

    public void exportAllOrdersToJsonFile() throws IOException {
        orderService.exportAllOrdersToJsonFile();
    }

    public void importAllOrdersFromJsonFile() throws IOException {
        orderService.importAllOrdersFromJsonFile();
    }

}
