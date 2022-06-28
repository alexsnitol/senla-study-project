package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.Singleton;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;

@Singleton
public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements IOrderRepository {

    @Override
    public boolean deleteById(Long id) {
        this.getById(id).setStatus(OrderStatusEnum.DELETED);
        return true;
    }

}