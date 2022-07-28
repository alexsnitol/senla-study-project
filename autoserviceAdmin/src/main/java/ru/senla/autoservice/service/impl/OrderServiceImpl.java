package ru.senla.autoservice.service.impl;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IOrderService;
import ru.senla.autoservice.service.comparator.MapOrderComparator;
import ru.senla.autoservice.service.helper.EntityHelper;
import ru.senla.autoservice.util.JsonUtil;
import ru.senla.autoservice.util.PropertyUtil;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.err;

@Setter
@Slf4j
@Service
public class OrderServiceImpl extends AbstractServiceImpl<Order, IOrderRepository> implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IMasterRepository masterRepository;

    private final IGarageService garageService;


    @Autowired
    public OrderServiceImpl(IOrderRepository orderRepository,
                            IMasterRepository masterRepository,
                            IGarageService garageService) {
        this.orderRepository = orderRepository;
        this.masterRepository = masterRepository;
        this.garageService = garageService;
    }

    @PostConstruct
    public void init() {
        this.clazz = Order.class;
        this.defaultRepository = orderRepository;
    }


    @Override
    @Transactional
    public TakenPlaceDto addAndTakePlace(Order order) {
        TakenPlaceDto place = null;

        orderRepository.create(order);
        place = garageService.takePlace(order.getId());

        return place;
    }

    @Override
    @Transactional
    public void deleteByIdAndFreePlace(Long orderId) {
        garageService.freePlaceByOrderId(orderId);

        Order order = getById(orderId);
        order = reducedNumberOfActiveOrdersOfMastersByOrder(order);
        order.setStatus(OrderStatusEnum.DELETED);

        orderRepository.update(order);
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
        orderRepository.update(order);

        return order;
    }

    /**
     * Setting status to order
     * and taking place if order status was change to "IN_PROCESS" or "POSTPONED"
     * or freeing place if order status was change to other status
     */
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

        return order;
    }

    @Override
    @Transactional
    public Order setStatusInOrderByIdAndUpdate(Long orderId, OrderStatusEnum newStatus) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }

        OrderStatusEnum currentStatus = order.getStatus();
        order = setStatus(order, newStatus);
        orderRepository.update(order);

        log.info("Order with id {} changed status from {} on {} successful", orderId, currentStatus, newStatus);

        return order;
    }

    public Order assignMasterById(Order order, Long masterId) {
        Master masterById = masterRepository.findById(masterId);
        EntityHelper.checkEntity(masterById, Master.class, masterId);

        Long orderId = order.getId();

        if (order.getStatus() == OrderStatusEnum.IN_PROCESS
                || order.getStatus() == OrderStatusEnum.POSTPONED) {
            masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() + 1);
        }

        List<Master> masters = order.getMasters();
        masters.add(masterById);
        order.setMasters(masters);

        log.info("Assigning master with id {} for order with id {} successful completed",
                masterId, orderId);

        return order;
    }

    @Override
    @Transactional
    public Order assignMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = assignMasterById(order, masterId);
        orderRepository.update(order);

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
    @Transactional
    public Order removeMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId) {
        Order order;
        try {
            order = getById(orderId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        order = removeMasterById(order, masterId);
        orderRepository.update(order);

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

    @Transactional
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

        return order;
    }

    @Override
    @Transactional
    public Order shiftTimeOfCompletionInOrderById(Long orderId, int shiftMinutes) {
        Order order;
        order = getById(orderId);
        return shiftTimeOfCompletion(order, shiftMinutes);
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
        orderRepository.update(order);

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

        orderRepository.update(order);
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
    public List<Order> getAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to) {
        return getAllByTimeOfCompletion(this.orderRepository.findAll(), from, to);
    }

    @Override
    public List<Order> getAllByTimeOfCompletion(List<Order> orders, LocalDateTime from, LocalDateTime to) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getTimeOfCompletion().isAfter(from) && o.getTimeOfCompletion().isBefore(to))
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getAllByStatus(OrderStatusEnum status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> getAllByStatus(List<Order> orders, OrderStatusEnum status) {
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
    public List<Order> getAllByMasterId(Long masterId) {
        return orderRepository.findAllByMasterId(masterId);
    }

    @Override
    public List<Order> getAllByMasterId(List<Order> orders, Long masterId) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getMasters().stream()
                        .filter(m -> m.equals(masterId))
                        .findFirst().orElse(null) != null)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getAllByMasterId(String masterIdStr, MultiValueMap<String, String> requestParams) {
        if (requestParams.containsKey("masterId")) {
            requestParams.set("masterId", masterIdStr);
        } else {
            requestParams.add("masterId", masterIdStr);
        }
        return getAll(requestParams);
    }

    @Override
    public List<Master> getMastersByOrderId(Long id) {
        Order order = getById(id);
        return order.getMasters();
    }

    @Override
    public List<Order> checkRequestParamsAndGetAll(MultiValueMap<String, String> requestParams) {
        requestParams.remove("masterId");
        return getAll(requestParams);
    }

    public void exportOrderToJsonFile(Long orderId, String fileName) throws IOException {
        Order orderById = getById(orderId);
        JsonUtil.exportModelToJsonFile(orderById, fileName);
        log.info("Order with id {} successful exported", orderId);
    }

    public void exportAllOrdersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(orderRepository.findAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "orderList");
        log.info("All orders successful exported");
    }

}
