package ru.senla.autoservice.service.impl;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;
import ru.senla.autoservice.service.comparator.MapOrderComparator;
import ru.senla.autoservice.util.JsonUtil;
import ru.senla.autoservice.util.PropertyUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.err;

@Singleton
@Setter
@Slf4j
public class OrderServiceImpl extends AbstractServiceImpl<Order, IOrderRepository> implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IMasterRepository masterRepository;
    @Autowired
    private IGarageRepository garageRepository;

    @Autowired
    private IGarageService garageService;
    @Autowired
    private IMasterService masterService;


    @PostConstruct
    public void init() {
        this.defaultRepository = orderRepository;
    }

    public void setOrderRepository(IOrderRepository orderRepository) {
        this.defaultRepository = orderRepository;
        this.orderRepository = orderRepository;
    }

    public void setTimeOfCompletion(Long orderId, int minutes) {
        Order orderById = orderRepository.getById(orderId);

        if (orderById.getTimeOfBegin() == null) {
            return;
        }

        orderById.setTimeOfCompletion(orderById.getTimeOfBegin().plusMinutes(minutes));
    }

    public void setStatus(Long orderId, OrderStatusEnum newStatus) {
        Order order = orderRepository.getById(orderId);
        OrderStatusEnum currentStatus = order.getStatus();
        List<Master> mastersByOrder = masterService.getMastersByOrder(orderId);

        if (currentStatus != OrderStatusEnum.IN_PROCESS && currentStatus != OrderStatusEnum.POSTPONED) {
            if (newStatus == OrderStatusEnum.IN_PROCESS || newStatus == OrderStatusEnum.POSTPONED) {
                garageService.takePlace(orderId);
                for (Master master : mastersByOrder) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() + 1);
                }
            }
        } else {
            if (newStatus != OrderStatusEnum.IN_PROCESS && newStatus != OrderStatusEnum.POSTPONED) {
                garageService.freePlaceByOrderId(orderId);
                for (Master master : mastersByOrder) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);
                }
            }
        }

        order.setStatus(newStatus);
        log.info("Order with id {} changed status from {} on {} successful", orderId, currentStatus, newStatus);
    }

    public void assignMasterById(Long orderId, Long masterId) {
        Order orderById = orderRepository.getById(orderId);
        Master masterById = masterRepository.getById(masterId);

        if (masterById != null) {
            if (orderById.getStatus() == OrderStatusEnum.IN_PROCESS
                    || orderById.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() + 1);
            }

            orderById.getListOfMastersId().add(masterId);
            log.info("Assigning master with id {} for order with id {} successful completed",
                    masterId, orderId);
        } else {
            log.error("Assigning master for order with id {} cancelled, because master with id {} not exist",
                    orderId, masterId);
        }
    }

    public void removeMasterById(Long orderId, Long masterId) {
        Order orderById = orderRepository.getById(orderId);
        Master masterById = null;

        for (Long tmpMasterId : orderById.getListOfMastersId()) {
            if (tmpMasterId.equals(masterId)) {
                masterById = masterRepository.getById(tmpMasterId);
                break;
            }
        }

        if (masterById != null) {
            if (orderById.getStatus() == OrderStatusEnum.IN_PROCESS
                    || orderById.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() - 1);
            }

            orderById.getListOfMastersId().remove(masterId);
            log.info("Removing master with id {} for order with id {} successful completed",
                    masterId, orderId);
        } else {
            log.error("Removing master for order with id {} cancelled, because master with id {} not exist",
                    orderId, masterId);
        }
    }

    private void shiftTimeOfCompletionOneOrder(Order order, int shiftMinutes) {
        if (order.getStatus() == OrderStatusEnum.IN_PROCESS) {
            order.setTimeOfCompletion(order.getTimeOfCompletion().plusMinutes(shiftMinutes));
        } else if (order.getStatus() == OrderStatusEnum.POSTPONED) {
            order.setTimeOfBegin(order.getTimeOfBegin().plusMinutes(shiftMinutes));
            order.setTimeOfCompletion(order.getTimeOfCompletion().plusMinutes(shiftMinutes));
        }
    }

    public void shiftTimeOfCompletion(Long orderId, int shiftMinutes) {
        try {
            PropertyUtil.getPropertyShiftTimeOfCompletion();
        } catch (Exception e) {
            err.println(e.getMessage());
            return;
        }

        Order orderById = orderRepository.getById(orderId);

        if (orderById == null) {
            log.error("Shifting time of completion for order with id {} cancelled," +
                            "because order not exist",
                    orderId);
            return;
        }

        if (orderById.getStatus() != OrderStatusEnum.IN_PROCESS) {
            log.error("Shifting time of completion for order with id {} cancelled," +
                            "because order has not status IN_PROCESS",
                    orderId);
            return;
        }

        shiftTimeOfCompletionOneOrder(orderById, shiftMinutes);

        List<Long> listOfMastersIdByOrder = orderById.getListOfMastersId();

        for (Order order : orderRepository.getAll()) {
            if (order.getStatus() == OrderStatusEnum.POSTPONED) {
                for (Long masterId : listOfMastersIdByOrder) {
                    if (order.getListOfMastersId().stream()
                            .filter(m -> m.equals(masterId))
                            .findFirst().orElse(null) != null) {
                        shiftTimeOfCompletionOneOrder(order, shiftMinutes);
                        break;
                    }
                }
            }
        }
        log.info("Shifting time of completion for order with id {} on {} minutes successful completed",
                orderId, shiftMinutes);
    }

    public void setPrice(Long orderId, float price) {
        orderRepository.getById(orderId).setPrice(price);
    }

    @Override
    public void deleteById(Long id) {
        try {
            PropertyUtil.getPropertyDeleteOrder();
        } catch (Exception e) {
            err.println(e.getMessage());
            return;
        }

        this.orderRepository.deleteById(id);
    }

    public String getInfoOfOrder(Order order) {
        return "id: " + order.getId()
                + "\nprice: " + order.getPrice()
                + "\nstatus: " + order.getStatus()
                + "\ntime of created: " + order.getTimeOfCreated().toString()
                + "\ntime of begin: " + order.getTimeOfBegin().toString()
                + "\ntime of completion: " + order.getTimeOfCompletion().toString();
    }

    @Override
    public List<Order> getSorted(String sortType) {
        return getSorted(orderRepository.getAll(), sortType);
    }

    @Override
    public List<Order> getSorted(List<Order> orders, String sortType) {
        List<Order> sortedOrders = new ArrayList<>(orders);
        MapOrderComparator mapOrderComparator = new MapOrderComparator();

        sortedOrders.sort(mapOrderComparator.exetuce(sortType));

        return sortedOrders;
    }

    @Override
    public List<Order> getOrdersFilteredByDateTime(LocalDateTime from, LocalDateTime to) {
        return getOrdersFilteredByDateTime(this.orderRepository.getAll(), from, to);
    }

    @Override
    public List<Order> getOrdersFilteredByDateTime(List<Order> orders, LocalDateTime from, LocalDateTime to) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getTimeOfCompletion().isAfter(from) && o.getTimeOfCompletion().isBefore(to))
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getOrdersFilteredByStatus(OrderStatusEnum status) {
        return getOrdersFilteredByStatus(this.orderRepository.getAll(), status);
    }

    @Override
    public List<Order> getOrdersFilteredByStatus(List<Order> orders, OrderStatusEnum status) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getOrdersFilteredByMaster(Long masterId) {
        return getOrdersFilteredByMaster(this.orderRepository.getAll(), masterId);
    }

    @Override
    public List<Order> getOrdersFilteredByMaster(List<Order> orders, Long masterId) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getListOfMastersId().stream()
                        .filter(m -> m.equals(masterId))
                        .findFirst().orElse(null) != null)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    public void exportOrderToJsonFile(Long orderId, String fileName) throws IOException {
        Order orderById = getById(orderId);
        JsonUtil.exportModelToJsonFile(orderById, fileName);
        log.info("Order with id {} successful exported", orderId);
    }

    public void importOrderFromJsonFile(String path) throws IOException {
        Order orderJson = JsonUtil.importModelFromJsonFile(new Order(), path);
        Order orderByJsonId = getById(orderJson.getId());

        if (orderByJsonId != null) {
            update(orderByJsonId, orderJson);
        } else {
            add(orderJson);
        }
        log.info("Order successful imported");
    }

    public void exportAllOrdersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(orderRepository.getAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "orderList");
        log.info("All orders successful exported");
    }

    public void importAllOrdersFromJsonFile() throws IOException {
        List<Order> orderList = JsonUtil.importModelListFromJsonFile(new Order(),
                JsonUtil.JSON_CONFIGURATION_PATH + "orderList.json");
        orderRepository.setRepository(orderList);
        log.info("All orders successful imported");
    }

}
