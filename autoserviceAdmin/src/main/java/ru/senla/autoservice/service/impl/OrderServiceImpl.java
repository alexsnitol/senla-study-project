package ru.senla.autoservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.exception.BusinessRuntimeException;
import ru.senla.autoservice.exception.order.MasterInOrderNotFoundException;
import ru.senla.autoservice.exception.order.TimeOfBeginInOrderNotSetException;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IOrderService;
import ru.senla.autoservice.service.helper.EntityHelper;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl extends AbstractServiceImpl<Order, IOrderRepository> implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IMasterRepository masterRepository;

    private final IGarageService garageService;


    @PostConstruct
    public void init() {
        this.clazz = Order.class;
        this.defaultRepository = orderRepository;
    }


    @Override
    @Transactional
    public TakenPlaceDto addAndTakePlace(@NonNull Order order) {
        TakenPlaceDto place = null;

        orderRepository.create(order);
        place = garageService.takePlace(order.getId());

        return place;
    }

    @Override
    @Transactional
    public void deleteByIdAndFreePlace(@NonNull Long orderId) {
        Order order = getById(orderId);

        OrderStatusEnum currentStatus = order.getStatus();

        if (currentStatus == OrderStatusEnum.DELETED) {
            log.warn("Order with id {} already deleted", orderId);
            return;
        } else if (currentStatus == OrderStatusEnum.IN_PROCESS || currentStatus == OrderStatusEnum.POSTPONED) {
            garageService.freePlaceByOrderId(orderId);
            reducedNumberOfActiveOrdersOfMastersByOrder(order);
        }

        order.setStatus(OrderStatusEnum.DELETED);

        orderRepository.update(order);
    }

    public void setTimeOfCompletion(@NonNull Order order, int minutes) throws TimeOfBeginInOrderNotSetException {
        if (minutes < 0) {
            String message = "Minutes don't be negative";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        if (order.getTimeOfBegin() == null) {
            String message = String.format("Time of begin of order with id %s not set", order.getId());
            log.error(message);
            throw new TimeOfBeginInOrderNotSetException(message);
        }

        order.setTimeOfCompletion(order.getTimeOfBegin().plusMinutes(minutes));
    }

    @Override
    public void setTimeOfCompletionInOrderByIdAndUpdate(@NonNull Long orderId, int minutes) {
        Order order = getById(orderId);
        try {
            setTimeOfCompletion(order, minutes);
        } catch (TimeOfBeginInOrderNotSetException e) {
            throw new BusinessRuntimeException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
        orderRepository.update(order);
    }

    /**
     * Setting status to order
     * and taking place if order status was change to "IN_PROCESS" or "POSTPONED"
     * or freeing place if order status was change to other status
     */
    public void setStatus(@NonNull Order order, @NonNull OrderStatusEnum newStatus) {
        if (newStatus.equals(OrderStatusEnum.DELETED)) {
            throw new BusinessRuntimeException("Can't set status to deleted");
        }

        OrderStatusEnum currentStatus = order.getStatus();
        List<Master> mastersByOrder = order.getMasters();
        Long orderId = order.getId();

        List<OrderStatusEnum> activeStatuses = List.of(
                OrderStatusEnum.IN_PROCESS,
                OrderStatusEnum.POSTPONED
        );

        if (!activeStatuses.contains(currentStatus)) {
            if (activeStatuses.contains(newStatus)) {
                garageService.takePlace(orderId);
                for (Master master : mastersByOrder) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() + 1);
                }
            }
        } else {
            if (!activeStatuses.contains(newStatus)) {
                garageService.freePlaceByOrderId(orderId);
                for (Master master : mastersByOrder) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);
                }
            }
        }

        order.setStatus(newStatus);
    }

    @Override
    @Transactional
    public void setStatusInOrderByIdAndUpdate(@NonNull Long orderId, @NonNull OrderStatusEnum newStatus) {
        Order order = getById(orderId);

        OrderStatusEnum currentStatus = order.getStatus();
        setStatus(order, newStatus);

        orderRepository.update(order);

        log.info("Order with id {} changed status from {} on {} successful", orderId, currentStatus, newStatus);
    }

    public void assignMasterById(@NonNull Order order, @NonNull Long masterId) {
        Master masterById = masterRepository.findById(masterId);
        EntityHelper.checkEntityOnNullAfterFindedById(masterById, Master.class, masterId);

        if (order.getStatus() == OrderStatusEnum.IN_PROCESS
                || order.getStatus() == OrderStatusEnum.POSTPONED) {
            masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() + 1);
        }

        List<Master> masters = order.getMasters();
        masters.add(masterById);
        order.setMasters(masters);

        log.info("Assigning master with id {} for order with id {} successful completed",
                masterId, order.getId());
    }

    @Override
    @Transactional
    public void assignMasterByIdInOrderByIdAndUpdate(@NonNull Long orderId, @NonNull Long masterId) {
        Order order = getById(orderId);
        assignMasterById(order, masterId);
        orderRepository.update(order);
    }

    public void removeMasterById(@NonNull Order order, @NonNull Long masterId) throws MasterInOrderNotFoundException {
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
            String message = String.format(
                    "Removing master for order with id %s cancelled, because master with id %s in this order not exist",
                    orderId, masterId
            );
            log.error(message);
            throw new MasterInOrderNotFoundException(message);
        }
    }

    @Override
    @Transactional
    public void removeMasterByIdInOrderByIdAndUpdate(@NonNull Long orderId, @NonNull Long masterId) {
        Order order = getById(orderId);

        try {
            removeMasterById(order, masterId);
        } catch (MasterInOrderNotFoundException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }

        orderRepository.update(order);
    }

    private void shiftTimeOfCompletionOneOrder(@NonNull Order order, int shiftMinutes) {
        OrderStatusEnum orderStatus = order.getStatus();
        LocalDateTime timeOfBegin = order.getTimeOfBegin();
        LocalDateTime timeOfCompletion = order.getTimeOfCompletion();

        if (orderStatus == OrderStatusEnum.IN_PROCESS) {
            order.setTimeOfCompletion(timeOfCompletion.plusMinutes(shiftMinutes));
        } else if (orderStatus == OrderStatusEnum.POSTPONED) {
            order.setTimeOfBegin(timeOfBegin.plusMinutes(shiftMinutes));
            order.setTimeOfCompletion(timeOfCompletion.plusMinutes(shiftMinutes));
        }
    }

    @Transactional
    public void shiftTimeOfCompletionWithUpdate(@NonNull Order order, int shiftMinutes) {
        if (shiftMinutes < 0) {
            String message = "Shift minutes can't be negative";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        Long orderId = order.getId();

        if (order.getStatus() != OrderStatusEnum.IN_PROCESS) {
            log.error("Shifting time of completion for order with id {} cancelled," +
                            "because order has not status IN_PROCESS",
                    orderId);
        }

        shiftTimeOfCompletionOneOrder(order, shiftMinutes);
        orderRepository.update(order);

        List<Master> listOfMastersByOrder = order.getMasters();

        for (Order tmpOrder : orderRepository.findAllByStatus(OrderStatusEnum.POSTPONED)) {
            for (Master master : listOfMastersByOrder) {
                if (tmpOrder.getMasters().stream()
                        .filter(m -> m.equals(master))
                        .findFirst().orElse(null) != null) {
                    shiftTimeOfCompletionOneOrder(tmpOrder, shiftMinutes);
                    orderRepository.update(tmpOrder);
                    break;
                }
            }
        }
        log.info("Shifting time of completion for order with id {} on {} minutes successful completed",
                orderId, shiftMinutes);
    }

    @Transactional
    public void shiftTimeOfCompletionInOrderByIdWithUpdate(@NonNull Long orderId, int shiftMinutes) {
        Order order;
        order = getById(orderId);
        shiftTimeOfCompletionWithUpdate(order, shiftMinutes);
    }

    public void setPrice(@NonNull Order order, float price) {
        if (price < 0) {
            String message = "Price can't be negative";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        order.setPrice(price);
    }

    @Override
    public void setPriceInOrderByIdAndUpdate(@NonNull Long orderId, float price) {
        Order order = getById(orderId);
        setPrice(order, price);
        orderRepository.update(order);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        Order order = getById(id);

        if (order.getStatus() == OrderStatusEnum.DELETED) {
            log.warn("Order with id {} already deleted", id);
            return;
        }

        if (order.getStatus() == OrderStatusEnum.IN_PROCESS
                || order.getStatus() == OrderStatusEnum.POSTPONED) {
            reducedNumberOfActiveOrdersOfMastersByOrder(order);
        }

        order.setStatus(OrderStatusEnum.DELETED);

        orderRepository.update(order);
    }

    public void reducedNumberOfActiveOrdersOfMastersByOrder(@NonNull Order order) {
        List<Master> masters = order.getMasters();
        for (Master master : masters) {
            master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);
        }
    }

    @Override
    public List<Order> getAllByTimeOfCompletion(@NonNull LocalDateTime from, @NonNull LocalDateTime to) {
        if (from.isAfter(to)) {
            String message = "Time of completion from can't be after to";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return orderRepository.findAllByTimeOfCompletion(from, to);
    }

    @Override
    public List<Order> getAllByStatus(@NonNull OrderStatusEnum status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> getAllByStatusAndMasterId(@NonNull OrderStatusEnum orderStatus, @NonNull Long masterId) {
        Master master = masterRepository.findById(masterId);
        EntityHelper.checkEntityOnNullAfterFindedById(master, Master.class, masterId);
        return orderRepository.findAllByStatusAndMasterId(orderStatus, masterId);
    }

    @Override
    public List<Order> getAllByMasterId(Long masterId) {
        Master master = masterRepository.findById(masterId);
        EntityHelper.checkEntityOnNullAfterFindedById(master, Master.class, masterId);
        return orderRepository.findAllByMasterId(masterId);
    }

    @Override
    public List<Order> getAllByMasterId(@NonNull String masterIdStr,
                                        @NonNull MultiValueMap<String, String> requestParams) {
        Long masterId = Long.valueOf(masterIdStr);

        Master master = masterRepository.findById(masterId);
        EntityHelper.checkEntityOnNullAfterFindedById(master, Master.class, masterId);

        if (requestParams.containsKey("masterId")) {
            requestParams.set("masterId", masterIdStr);
        } else {
            requestParams.add("masterId", masterIdStr);
        }
        return getAll(requestParams);
    }

    @Override
    public List<Master> getMastersByOrderId(@NonNull Long id) {
        Order order = getById(id);
        return order.getMasters();
    }

    @Override
    public List<Order> checkRequestParamsAndGetAll(@NonNull MultiValueMap<String, String> requestParams) {
        requestParams.remove("masterId");
        return getAll(requestParams);
    }

}
