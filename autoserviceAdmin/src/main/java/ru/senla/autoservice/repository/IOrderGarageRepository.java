package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.OrderGarage;

import java.util.List;

public interface IOrderGarageRepository extends IAbstractRepository<OrderGarage> {

    OrderGarage findByOrderId(Long orderId);
    List<OrderGarage> findByGarageId(Long garageId);
    OrderGarage findByOrderIdAndByGarageId(Long orderId, Long garageId);

}
