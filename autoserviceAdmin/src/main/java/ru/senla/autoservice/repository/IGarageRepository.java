package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.Garage;

public interface IGarageRepository extends IAbstractRepository<Garage> {

    Garage findByOrderId(Long orderId);

}
