package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.Garage;

import java.util.List;

public interface IGarageRepository extends IAbstractRepository<Garage> {

    List<Garage> getPlacesFilteredByAvailability(boolean isTaken);

    Garage findByOrderId(Long orderId);

}
