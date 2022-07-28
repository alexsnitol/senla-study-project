package ru.senla.autoservice.service;

import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends IAbstractService<Order> {

    TakenPlaceDto addAndTakePlace(Order order);
    void deleteByIdAndFreePlace(Long orderId);

    Order setTimeOfCompletion(Order order, int minutes);
    Order setTimeOfCompletionInOrderByIdAndUpdate(Long orderId, int minutes);
    Order setStatus(Order order, OrderStatusEnum newStatus);
    Order setStatusInOrderByIdAndUpdate(Long orderId, OrderStatusEnum newStatus);
    Order setPrice(Order order, float price);
    Order setPriceInOrderByIdAndUpdate(Long orderId, float price);

    Order assignMasterById(Order order, Long masterId);
    Order assignMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId);
    Order removeMasterById(Order order, Long masterId);
    Order removeMasterByIdInOrderByIdAndUpdate(Long orderId, Long masterId);
    Order reducedNumberOfActiveOrdersOfMastersByOrder(Order order);
    Order shiftTimeOfCompletion(Order order, int shiftMinutes);
    Order shiftTimeOfCompletionInOrderById(Long orderId, int shiftMinutes);
    String getInfoOfOrder(Order order);

    List<Order> getAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to);
    List<Order> getAllByTimeOfCompletion(List<Order> orders, LocalDateTime from, LocalDateTime to);
    List<Order> getAllByStatus(OrderStatusEnum status);
    List<Order> getAllByStatus(List<Order> orders, OrderStatusEnum status);
    List<Order> getAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId);
    List<Order> getAllByMasterId(Long masterId);

    List<Order> getAllByMasterId(List<Order> orders, Long masterId);
    List<Order> getAllByMasterId(String masterIdStr, MultiValueMap<String, String> requestParams);
    List<Master> getMastersByOrderId(Long id);

    List<Order> checkRequestParamsAndGetAll(MultiValueMap<String, String> requestParams);
}
