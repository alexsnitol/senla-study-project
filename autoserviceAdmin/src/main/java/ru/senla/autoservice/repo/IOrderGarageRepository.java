package ru.senla.autoservice.repo;

import ru.senla.autoservice.model.OrderGarage;
import ru.senla.autoservice.model.OrderStatusEnum;

import java.util.List;

public interface IOrderGarageRepository extends IAbstractRepository<OrderGarage> {

    OrderGarage findByOrderId(Long orderId);
    List<OrderGarage> findByGarageId(Long garageId);
    OrderGarage findByOrderIdAndByGarageId(Long orderId, Long garageId);
    List<OrderGarage> findAllByGarageIdAndOrderStatusList(Long garageId, List<OrderStatusEnum> orderStatusList);

}
