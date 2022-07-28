package ru.senla.autoservice.service;

import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;

import java.util.List;

public interface IMasterService extends IAbstractService<Master> {

    List<Order> getOrdersByMasterId(Long id);
    List<Master> getAllByOrderId(String orderIdStr, MultiValueMap<String, String> requestParams);

    String getFullName(Master master);

    String getFullNameWithId(Master master);

    List<Master> checkRequestParamsAndGetAll(MultiValueMap<String, String> requestParams);
}
