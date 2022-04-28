package autoservice.repository;

import autoservice.repository.model.Garage;

import java.util.List;

public interface IGarageRepository extends IAbstractRepository<Garage> {

    List<Garage> getPlacesFilteredByAvailability(boolean isTaken);
    Garage getByOrderId(Long orderId);

}
