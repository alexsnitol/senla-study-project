package ru.senla.autoservice.repo;

import ru.senla.autoservice.model.Garage;

public interface IGarageRepository extends IAbstractRepository<Garage> {

    Garage findByOrderId(Long orderId);

}
