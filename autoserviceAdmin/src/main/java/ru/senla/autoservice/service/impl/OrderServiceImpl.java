package ru.senla.autoservice.service.impl;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NonNull;
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
import ru.senla.autoservice.util.EntityManagerUtil;
import ru.senla.autoservice.util.JsonUtil;
import ru.senla.autoservice.util.PropertyUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public List<Long> addOrderAndTakePlace(Order order) {
        List<Long> place = Collections.emptyList();

        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            orderRepository.create(order);
            place = garageService.takePlace(order.getId());

            transaction.commit();
        } catch (Exception e) {
            log.error(e.toString());
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        return place;
    }

    @Override
    public void deleteByIdAndFreePlace(Long orderId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Order order = getById(orderId);
            order = reducedNumberOfActiveOrdersOfMastersByOrder(order);
            order.setStatus(OrderStatusEnum.DELETED);

            orderRepository.update(order);

            garageService.freePlaceByOrderId(orderId);

            transaction.commit();
        } catch (Exception e) {
            log.error(e.toString());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    public Order setTimeOfCompletion(Order order, int minutes) {
        if (order.getTimeOfBegin() == null) {
            log.error("Time of completion of order with id {} not set", order.getId());
            return order;
        }

        order.setTimeOfCompletion(order.getTimeOfBegin().plusMinutes(minutes));

        return order;
    }

    @Override
    public Order setTimeOfCompletionInOrderByIdAndUpdate(Long orderId, int minutes) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = setTimeOfCompletion(order, minutes);
        update(order);

        return order;
    }

    public Order setStatus(Order order, OrderStatusEnum newStatus) {
        OrderStatusEnum currentStatus = order.getStatus();
        List<Master> mastersByOrder = order.getMasters();
        Long orderId = order.getId();

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

        return order;
    }

    @Override
    public Order setStatusInOrderByIdAndUpdate(Long orderId, OrderStatusEnum newStatus) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = setStatus(order, newStatus);
        update(order);

        return order;
    }

    public Order assignMasterById(Order order, Long masterId) {
        Master masterById = masterRepository.findById(masterId);
        Long orderId = order.getId();

        if (masterById != null) {
            if (order.getStatus() == OrderStatusEnum.IN_PROCESS
                    || order.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() + 1);
            }

            List<Master> masters = order.getMasters();
            masters.add(masterById);
            order.setMasters(masters);

            log.info("Assigning master with id {} for order with id {} successful completed",
                    masterId, orderId);
        } else {
            log.error("Assigning master for order with id {} cancelled, because master with id {} not exist",
                    orderId, masterId);
        }

        return order;
    }

    @Override
    public Order assignMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = assignMasterById(order, masterId);
        update(order);

        return order;
    }

    public Order removeMasterById(Order order, Long masterId) {
        Master masterById = null;
        Long orderId = order.getId();

        for (Master tmpMaster : order.getMasters()) {
            if (tmpMaster.getId().equals(masterId)) {
                masterById = tmpMaster;
                break;
            }
        }

        if (masterById != null) {
            if (order.getStatus() == OrderStatusEnum.IN_PROCESS
                    || order.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() - 1);
            }

            List<Master> masters = order.getMasters();
            masters.remove(masterById);
            order.setMasters(masters);

            log.info("Removing master with id {} for order with id {} successful completed",
                    masterId, orderId);
        } else {
            log.error("Removing master for order with id {} cancelled, because master with id {} not exist",
                    orderId, masterId);
        }

        return order;
    }

    @Override
    public Order removeMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = removeMasterById(order, masterId);
        update(order);

        return order;
    }

    private Order shiftTimeOfCompletionOneOrder(Order order, int shiftMinutes) {
        OrderStatusEnum orderStatus = order.getStatus();
        LocalDateTime timeOfBegin = order.getTimeOfBegin();
        LocalDateTime timeOfCompletion = order.getTimeOfCompletion();

        if (orderStatus == OrderStatusEnum.IN_PROCESS) {
            order.setTimeOfCompletion(timeOfCompletion.plusMinutes(shiftMinutes));
        } else if (orderStatus == OrderStatusEnum.POSTPONED) {
            order.setTimeOfBegin(timeOfBegin.plusMinutes(shiftMinutes));
            order.setTimeOfCompletion(timeOfCompletion.plusMinutes(shiftMinutes));
        }

        return order;
    }

    public Order shiftTimeOfCompletion(@NonNull Order order, int shiftMinutes) {
        try {
            PropertyUtil.getPropertyShiftTimeOfCompletion();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

        Long orderId = order.getId();

        if (order.getStatus() != OrderStatusEnum.IN_PROCESS) {
            log.error("Shifting time of completion for order with id {} cancelled," +
                            "because order has not status IN_PROCESS",
                    orderId);
            return order;
        }

        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            order = shiftTimeOfCompletionOneOrder(order, shiftMinutes);
            orderRepository.update(order);

            List<Master> listOfMastersByOrder = order.getMasters();

            for (Order tmpOrder : orderRepository.findAllByStatus(OrderStatusEnum.POSTPONED)) {
                for (Master master : listOfMastersByOrder) {
                    if (tmpOrder.getMasters().stream()
                            .filter(m -> m.equals(master))
                            .findFirst().orElse(null) != null) {
                        tmpOrder = shiftTimeOfCompletionOneOrder(tmpOrder, shiftMinutes);
                        orderRepository.update(tmpOrder);
                        break;
                    }
                }
            }
            log.info("Shifting time of completion for order with id {} on {} minutes successful completed",
                    orderId, shiftMinutes);
        } catch (Exception e) {
            log.error(e.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        return order;
    }

    public Order setPrice(Order order, float price) {
        order.setPrice(price);
        return order;
    }

    @Override
    public Order setPriceInOrderByIdAndUpdate(Long orderId, float price) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = setPrice(order, price);
        update(order);

        return order;
    }

    @Override
    public void deleteById(Long id) {
        try {
            PropertyUtil.getPropertyDeleteOrder();
        } catch (Exception e) {
            err.println(e.getMessage());
            return;
        }

        Order order = getById(id);

        order = reducedNumberOfActiveOrdersOfMastersByOrder(order);
        order.setStatus(OrderStatusEnum.DELETED);

        update(order);
    }

    public Order reducedNumberOfActiveOrdersOfMastersByOrder(Order order) {
        List<Master> masters = order.getMasters();
        for (Master master : masters) {
            master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);
        }
        order.setMasters(masters);

        return order;
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
    public List<Order> getSorted(List<Order> orders, String sortType) {
        List<Order> sortedOrders = new ArrayList<>(orders);
        MapOrderComparator mapOrderComparator = new MapOrderComparator();

        sortedOrders.sort(mapOrderComparator.exetuce(sortType));

        return sortedOrders;
    }

    @Override
    public List<Order> getOrdersByTimeOfCompletion(LocalDateTime from, LocalDateTime to) {
        return getOrdersByTimeOfCompletion(this.orderRepository.findAll(), from, to);
    }

    @Override
    public List<Order> getOrdersByTimeOfCompletion(List<Order> orders, LocalDateTime from, LocalDateTime to) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getTimeOfCompletion().isAfter(from) && o.getTimeOfCompletion().isBefore(to))
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatusEnum status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> getOrdersByStatus(List<Order> orders, OrderStatusEnum status) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId) {
        return orderRepository.findAllByStatusAndMasterId(orderStatus, masterId);
    }

    @Override
    public List<Order> getOrdersByMasterId(Long masterId) {
        return orderRepository.findAllByMasterId(masterId);
    }

    @Override
    public List<Order> getOrdersByMasterId(List<Order> orders, Long masterId) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getMasters().stream()
                        .filter(m -> m.equals(masterId))
                        .findFirst().orElse(null) != null)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getAllByStatusSorted(OrderStatusEnum orderStatus, String sortType) {
        return orderRepository.findAllByStatusSorted(orderStatus, sortType);
    }

    @Override
    public List<Order> getAllByStatusesSorted(List<OrderStatusEnum> orderStatuses, String sortType) {
        return orderRepository.findAllByStatusesSorted(orderStatuses, sortType);
    }

    @Override
    public List<Order> getAllByTimeOfCompletionSorted(LocalDateTime from, LocalDateTime to, String sortType) {
        return orderRepository.findAllByTimeOfCompletionSorted(from, to, sortType);
    }

    @Override
    public List<Order> getAllByMasterIdSorted(Long masterId, String sortType) {
        return orderRepository.findAllByMasterIdSorted(masterId, sortType);
    }

    public List<Order> getOrdersByStatusSorted(OrderStatusEnum status, String sortType) {
        return orderRepository.findAllByStatusSorted(status, sortType);
    }

    public void exportOrderToJsonFile(Long orderId, String fileName) throws IOException {
        Order orderById = getById(orderId);
        JsonUtil.exportModelToJsonFile(orderById, fileName);
        log.info("Order with id {} successful exported", orderId);
    }

    public void importOrderFromJsonFile(String path) throws IOException {
        Order orderJson = JsonUtil.importModelFromJsonFile(new Order(), path);

        if (orderRepository.isExist(orderJson)) {
            update(orderJson);
        } else {
            add(orderJson);
        }
        log.info("Order successful imported");
    }

    public void exportAllOrdersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(orderRepository.findAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "orderList");
        log.info("All orders successful exported");
    }

    public void importAllOrdersFromJsonFile() throws IOException {
        List<Order> orderList = JsonUtil.importModelListFromJsonFile(new Order(),
                JsonUtil.JSON_CONFIGURATION_PATH + "orderList.json");
        orderRepository.setRepository(orderList);
        log.info("All orders successful imported");
    }

    @Override
    public void shiftTimeOfCompletionInOrderById(Long orderId, int shiftMinutes) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        shiftTimeOfCompletion(order, shiftMinutes);
    }

}
