package autoservice.controller;

import autoservice.repository.model.Garage;
import autoservice.service.IGarageService;

import java.util.List;

public class GarageController extends AbstractController<Garage, IGarageService> {
    private IGarageService garageService;

    public GarageController(IGarageService defaultService) {
        super(defaultService);
        this.garageService = defaultService;
    }

    public void addPlace(Long garageId) {
        garageService.addPlace(garageId);
    }

    public int deleteLastPlace(Long garageId) {
        return garageService.deleteLastPlace(garageId);
    }

    public List<Garage> getFreePlaces() {
        return garageService.getPlacesFilteredByAvailability(false);
    }

    public List<Long> takePlace(Long orderId) {
        return garageService.takePlace(orderId);
    }

    public List<Long> takePlaceByGarageId(Long garageId, Long orderId) {
        return garageService.takePlaceByGarageId(garageId, orderId);
    }

    public List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId) {
        return garageService.takePlaceByGarageIdAndPlaceIndex(garageId, indexOfPlace, orderId);
    }

    public void freePlaceByOrderId(Long orderId) {
        garageService.freePlaceByOrderId(orderId);
    }

    @Override
    public List<Garage> getSorted(String sortType) {
        return null;
    }

}
