package ru.senla.autoservice.repo;

import ru.senla.autoservice.model.Master;

import java.util.List;

public interface IMasterRepository extends IAbstractRepository<Master> {

    List<Master> findMastersByOrderId(Long orderId);

}
