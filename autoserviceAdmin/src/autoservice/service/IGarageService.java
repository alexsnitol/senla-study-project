package autoservice.service;

import autoservice.repository.model.Garage;

import java.util.List;

public interface IGarageService extends IAbstractService<Garage> {
    void addPlace(Long garageId);
    int deleteLastPlace(Long garageId);
    List<Long> takePlace(Long orderId);
    List<Long> takePlaceByGarageId(Long garageId, Long orderId);
    List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId);
    void freePlaceByOrderId(Long orderId);
    List<Garage> getPlacesFilteredByAvailability(boolean isTaken);
}
