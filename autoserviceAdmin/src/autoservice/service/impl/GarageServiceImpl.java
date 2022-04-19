package autoservice.service.impl;

import autoservice.repository.IGarageRepository;
import autoservice.repository.model.Garage;
import autoservice.service.IGarageService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GarageServiceImpl extends AbstractServiceImpl<Garage, IGarageRepository> implements IGarageService {
    private final IGarageRepository garageRepository;

    public GarageServiceImpl(IGarageRepository defaultRepository) {
        super(defaultRepository);
        this.garageRepository = defaultRepository;
    }

    /*
    * variants of return:
    * emptyList - all garages is full
    * list with info where: index 0 - id garage, index 1 - index taken place
     */

    @Override
    public void addPlace(Long garageId) {
        Garage garage = garageRepository.getById(garageId);
        List<Long> placesOfGarage = garage.getPlaces();

        placesOfGarage.add(null);
        garage.setPlaces(placesOfGarage);
    }


    /*
    * variants of return:
    * 1 - garage is empty
    * 0 - place is successful delete
    * -1 - place is taken
     */
    @Override
    public int deleteLastPlace(Long garageId) {
        Garage garage = garageRepository.getById(garageId);

        List<Long> placesOfGarage = garage.getPlaces();

        if (placesOfGarage.isEmpty()) {
            return 1;
        }

        if (placesOfGarage.get(placesOfGarage.size() - 1) != null) {
            return -1;
        } else {
            placesOfGarage.remove(placesOfGarage.size() - 1);
            garage.setPlaces(placesOfGarage);
            return 0;
        }
    }

    // take first free place in all garages
    public List<Long> takePlace(Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);

        for (Garage garage : this.garageRepository.getAll()) {
            if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
                garage.getPlaces().set(garage.getNumberOfTakenPlaces(), orderId);
                garagesIdAndNumberOfTakenPlace.add(garage.getId());
                garagesIdAndNumberOfTakenPlace.add(garage.getNumberOfTakenPlaces().longValue());
                return garagesIdAndNumberOfTakenPlace;
            }
        }

        return Collections.emptyList();
    }

    // take first free place in garage by id
    public List<Long> takePlaceByGarageId(Long garageId, Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
        Garage garage = this.garageRepository.getById(garageId);

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            garage.getPlaces().set(garage.getNumberOfTakenPlaces(), orderId);
            garagesIdAndNumberOfTakenPlace.add(garage.getId());
            garagesIdAndNumberOfTakenPlace.add(garage.getNumberOfTakenPlaces().longValue());
            return garagesIdAndNumberOfTakenPlace;
        }

        return Collections.emptyList();
    }

    // take free place by index in garage by id
    public List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
        Garage garage = this.garageRepository.getById(garageId);

        if (indexOfPlace >= garage.getSize() || indexOfPlace < 0)
            return Collections.emptyList();

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            garage.getPlaces().set(indexOfPlace, orderId);
            garagesIdAndNumberOfTakenPlace.add(orderId);
            garagesIdAndNumberOfTakenPlace.add(indexOfPlace.longValue());
            return garagesIdAndNumberOfTakenPlace;
        }

        return Collections.emptyList();
    }

    public void freePlaceByOrderId(Long orderId) {
        Garage garage = garageRepository.getByOrderId(orderId);
        Integer indexOfPlace = garage.findByOrderId(orderId);

        garage.getPlaces().set(indexOfPlace, null);
    }

    public List<Garage> getPlacesFilteredByAvailability(boolean isTaken) {
        return this.garageRepository.getPlacesFilteredByAvailability(isTaken);
    }

    @Override
    public List<Garage> getSorted(String sortType) {
        return null;
    }

    @Override
    public List<Garage> getSorted(List<Garage> garages,String sortType) {
        return null;
    }
}
