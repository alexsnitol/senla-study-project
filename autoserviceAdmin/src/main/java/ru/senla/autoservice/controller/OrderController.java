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

    public List<Long> add(Order order) {
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
        Order order;
        try {
            order = orderService.getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        order = orderService.setTimeOfCompletion(order, minutes);
        orderService.update(order);
    }

    public void setStatus(Long orderId, OrderStatusEnum newStatus) {
        log.info("Setting new status for order with id {}", orderId);
        Order order;
        try {
            order = orderService.getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        order = orderService.setStatus(order, newStatus);
        orderService.update(order);
    }

    public void assignMasterById(Long orderId, Long masterId) {
        log.info("Assign master with id {} on order with id {}", masterId, orderId);
        Order order;
        try {
            order = orderService.getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        order = orderService.assignMasterById(order, masterId);
        orderService.update(order);
    }

    public void removeMasterById(Long orderId, Long masterId) {
        log.info("Remove master with id {} from order with id {}", masterId, orderId);
        Order order;
        try {
            order = orderService.getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        order = orderService.removeMasterById(order, masterId);
        orderService.update(order);
    }

    public void shiftTimeOfCompletion(Long orderId, int shiftMinutes) {
        log.info("Shifting time of completion of order with id {} on {} minutes", orderId, shiftMinutes);
        Order order;
        try {
            order = orderService.getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        orderService.shiftTimeOfCompletion(order, shiftMinutes);
    }

    public void setPrice(Long orderId, float price) {
        Order order;
        try {
            order = orderService.getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        order = orderService.setPrice(order, price);
        orderService.update(order);
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
