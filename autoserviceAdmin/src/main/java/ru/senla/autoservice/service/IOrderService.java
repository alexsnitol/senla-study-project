package ru.senla.autoservice.service;

import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.exception.order.MasterInOrderNotFoundException;
import ru.senla.autoservice.exception.order.TimeOfBeginInOrderNotSetException;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends IAbstractService<Order> {

    TakenPlaceDto addAndTakePlace(Order order);
    void deleteByIdAndFreePlace(Long orderId);

    void setTimeOfCompletion(Order order, int minutes) throws TimeOfBeginInOrderNotSetException;
    void setTimeOfCompletionInOrderByIdAndUpdate(Long orderId, int minutes);
    void setStatus(Order order, OrderStatusEnum newStatus);
    void setStatusInOrderByIdAndUpdate(Long orderId, OrderStatusEnum newStatus);
    void setPrice(Order order, float price);
    void setPriceInOrderByIdAndUpdate(Long orderId, float price);

    void assignMasterById(Order order, Long masterId);
    void assignMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId);
    void removeMasterById(Order order, Long masterId) throws MasterInOrderNotFoundException;
    void removeMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId);
    void reducedNumberOfActiveOrdersOfMastersByOrder(Order order);
    void shiftTimeOfCompletionWithUpdate(Order order, int shiftMinutes);
    void shiftTimeOfCompletionInOrderByIdWithUpdate(Long orderId, int shiftMinutes);

    List<Order> getAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to);
    List<Order> getAllByStatus(OrderStatusEnum status);
    List<Order> getAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId);
    List<Order> getAllByMasterId(Long masterId);

    List<Order> getAllByMasterId(String masterIdStr, MultiValueMap<String, String> requestParams);
    List<Master> getMastersByOrderId(Long id);

    List<Order> checkRequestParamsAndGetAll(MultiValueMap<String, String> requestParams);
}
