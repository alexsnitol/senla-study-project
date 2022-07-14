package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.Master;

import java.util.List;

public interface IMasterRepository extends IAbstractRepository<Master> {

    List<Master> findMastersByOrderId(Long orderId);

}
