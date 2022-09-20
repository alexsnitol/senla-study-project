package ru.senla.autoservice.repo;

import org.springframework.stereotype.Repository;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.OrderMaster;
import ru.senla.autoservice.model.OrderStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IOrderMasterRepository extends IAbstractRepository<OrderMaster> {

    List<Master> findAllMastersByTimeOfCompletionAndOrderStatusList(LocalDateTime from, LocalDateTime to,
                                                                    List<OrderStatusEnum> orderStatusList);

}
