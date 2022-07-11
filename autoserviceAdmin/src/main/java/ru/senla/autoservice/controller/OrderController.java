package ru.senla.autoservice.controller;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;
import ru.senla.autoservice.util.JsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
@Slf4j
public class OrderController extends AbstractController<Order, IOrderService> {

    private static OrderController instance;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IGarageService garageService;
    @Autowired
    private IMasterService masterService;

    @PostConstruct
    public void setInstance() {
        instance = this;
    }

    public static OrderController getInstance() {
        return instance;
    }

    @PostConstruct
    public void init() {
        this.defaultService = orderService;
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

    public List<Long> addOrderAndTakePlace(Order order) {
        log.info("Adding new order with id {}", order.getId());
        return orderService.addOrderAndTakePlace(order);
    }

    public void deleteByIdAndFreePlace(Long orderId) {
        log.info("Deleting order with id {}", orderId);
        try {
            orderService.deleteByIdAndFreePlace(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
    }

    public void deleteById(Long orderId) {
        log.info("Deleting order with id {}", orderId);
        try {
            orderService.deleteById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
    }

    public void setTimeOfCompletion(Long orderId, int minutes) {
        orderService.setTimeOfCompletionInOrderByIdAndUpdate(orderId, minutes);
    }

    public void setStatus(Long orderId, OrderStatusEnum newStatus) {
        log.info("Setting new status for order with id {}", orderId);
        orderService.setStatusInOrderByIdAndUpdate(orderId, newStatus);
    }

    public void assignMasterById(Long orderId, Long masterId) {
        log.info("Assign master with id {} on order with id {}", masterId, orderId);
        orderService.assignMasterByIdInOrderByIdAndUpdate(orderId, masterId);
    }

    public void removeMasterById(Long orderId, Long masterId) {
        log.info("Remove master with id {} from order with id {}", masterId, orderId);
        orderService.removeMasterByIdInOrderByIdAndUpdate(orderId, masterId);
    }

    public void shiftTimeOfCompletion(Long orderId, int shiftMinutes) {
        log.info("Shifting time of completion of order with id {} on {} minutes", orderId, shiftMinutes);
        orderService.shiftTimeOfCompletionInOrderById(orderId, shiftMinutes);
    }

    public void setPrice(Long orderId, float price) {
        orderService.setPriceInOrderByIdAndUpdate(orderId, price);
    }

    public String getInfoOfOrder(Order order) {
        return orderService.getInfoOfOrder(order);
    }

    public List<Order> getAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to) {
        return orderService.getOrdersByTimeOfCompletion(orderService.getAll(), from, to);
    }

    public List<Order> getAllByTimeOfCompletion(List<Order> orders, LocalDateTime from, LocalDateTime to) {
        return orderService.getOrdersByTimeOfCompletion(orders, from, to);
    }

    public List<Order> getAllByStatus(OrderStatusEnum status) {
        return orderService.getOrdersByStatus(orderService.getAll(), status);
    }

    public List<Order> getAllByStatus(List<Order> orders, OrderStatusEnum status) {
        return orderService.getOrdersByStatus(orders, status);
    }

    public List<Order> getAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId) {
        return orderService.getAllByStatusAndMasterId(orderStatus, masterId);
    }

    public List<Order> getAllByMasterId(Long masterId) {
        return orderService.getOrdersByMasterId(orderService.getAll(), masterId);
    }

    public List<Order> getAllByMasterId(List<Order> orders, Long masterId) {
        return orderService.getOrdersByMasterId(orders, masterId);
    }
    public List<Order> getAllByStatusSorted(OrderStatusEnum orderStatus, String sortType) {
        return orderService.getAllByStatusSorted(orderStatus, sortType);
    }
    public List<Order> getAllByStatusesSorted(List<OrderStatusEnum> orderStatuses, String sortType) {
        return orderService.getAllByStatusesSorted(orderStatuses, sortType);
    }
    public List<Order> getAllByTimeOfCompletionSorted(LocalDateTime from, LocalDateTime to, String sortType) {
        return orderService.getAllByTimeOfCompletionSorted(from, to, sortType);
    }

    public List<Order> getAllByMasterIdSorted(Long masterId, String sortType) {
        return orderService.getAllByMasterIdSorted(masterId, sortType);
    }

    public List<Order> getSorted(String sortType) {
        return orderService.getSorted(sortType);
    }

    public List<Order> getSorted(List<Order> listOfOrder, String sortType) {
        return orderService.getSorted(listOfOrder, sortType);
    }

    public void exportOrderToJsonFile(Long orderId, String fileName) throws IOException {
        log.info("Export order with id {} to json file: {}", orderId, fileName);
        orderService.exportOrderToJsonFile(orderId, fileName);
    }

    public void importOrderFromJsonFile(String path) throws IOException {
        log.info("Import order from json file: {}", path);
        orderService.importOrderFromJsonFile(path);
    }

    public void exportAllOrdersToJsonFile() throws IOException {
        log.info("Export all orders to json file: {}", JsonUtil.JSON_CONFIGURATION_PATH + "orderList.json");
        orderService.exportAllOrdersToJsonFile();
    }
//    @PostConstruct

    public void importAllOrdersFromJsonFile() throws IOException {
        log.info("Import all orders from json file: {}", JsonUtil.JSON_CONFIGURATION_PATH + "orderList.json");
        orderService.importAllOrdersFromJsonFile();
    }
}
