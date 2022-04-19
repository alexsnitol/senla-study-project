package autoservice.repository.impl;

import autoservice.repository.IOrderRepository;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.repository.model.Order;

import java.util.*;
import java.util.stream.Collectors;

public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements IOrderRepository {
    @Override
    public void deleteById(Long id) {
        this.getById(id).setStatus(OrderStatusEnum.DELETED);
    }
}