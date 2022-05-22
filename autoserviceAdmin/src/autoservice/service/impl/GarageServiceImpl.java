package autoservice.service.impl;

import autoservice.repository.IGarageRepository;
import autoservice.repository.impl.GarageRepositoryImpl;
import autoservice.repository.model.Garage;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.service.IGarageService;
import autoservice.util.JsonUtil;
import autoservice.util.PropertyUtil;
import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.err;

@Singleton
public class GarageServiceImpl extends AbstractServiceImpl<Garage, IGarageRepository> implements IGarageService {

    @Autowired
    private IGarageRepository garageRepository;


    @PostConstruct
    public void init() {
        this.defaultRepository = garageRepository;
    }

    public void setGarageRepository(IGarageRepository garageRepository) {
        this.defaultRepository = garageRepository;
        this.garageRepository = garageRepository;
    }

    @Override
    public void addPlace(Long garageId) throws Exception {
        try {
            PropertyUtil.getPropertyAddAndDeleteFreePlaces();
        } catch (Exception e) {
            err.println(e.getMessage());
        }

        Garage garage = garageRepository.getById(garageId);
        List<Long> placesOfGarage = garage.getPlaces();

        placesOfGarage.add(null);
        garage.setPlaces(placesOfGarage);
    }


    /**
     * @return
     * 1 - garage is empty;
     * 0 - place is successful delete;
     * -1 - place is taken;
     */
    @Override
    public int deleteLastPlace(Long garageId) {
        try {
            PropertyUtil.getPropertyAddAndDeleteFreePlaces();
        } catch (Exception e) {
            err.println(e.getMessage());
            return 1;
        }

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

    /**
     * Take first free place in all garages.
     * @return
     * emptyList - all garages is full;
     * list with info where: index 0 - id garage, index 1 - index taken place;
     */
    public List<Long> takePlace(Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);

        for (Garage garage : this.garageRepository.getAll()) {
            if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
                garage.getPlaces().set(garage.getNumberOfTakenPlaces(), orderId);
                garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() + 1);
                garagesIdAndNumberOfTakenPlace.add(garage.getId());
                garagesIdAndNumberOfTakenPlace.add(garage.getNumberOfTakenPlaces().longValue());
                return garagesIdAndNumberOfTakenPlace;
            }
        }

        return Collections.emptyList();
    }

    /**
     * Take first free place in garage by id.
     * @return
     * emptyList - all garages is full;
     * list with info where: index 0 - id garage, index 1 - index taken place;
     */
    public List<Long> takePlaceByGarageId(Long garageId, Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
        Garage garage = this.garageRepository.getById(garageId);

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            garage.getPlaces().set(garage.getNumberOfTakenPlaces(), orderId);
            garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() + 1);
            garagesIdAndNumberOfTakenPlace.add(garage.getId());
            garagesIdAndNumberOfTakenPlace.add(garage.getNumberOfTakenPlaces().longValue());
            return garagesIdAndNumberOfTakenPlace;
        }

        return Collections.emptyList();
    }

    /**
     * Take free place by index in garage by id.
     * @return
     * emptyList - all garages is full;
     * list with info where: index 0 - id garage, index 1 - index taken place;
     */
    public List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
        Garage garage = this.garageRepository.getById(garageId);

        if (indexOfPlace >= garage.getSize() || indexOfPlace < 0)
            return Collections.emptyList();

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            garage.getPlaces().set(indexOfPlace, orderId);
            garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() + 1);
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

    public void exportGarageToJsonFile(Long garageId, String fileName) throws IOException {
        Garage garageById = getById(garageId);
        JsonUtil.exportModelToJsonFile(garageById, fileName);
    }

    public void importGarageFromJsonFile(String path) throws IOException {
        Garage garageJson = JsonUtil.importModelFromJsonFile(new Garage(), path);
        Garage garageByJsonId = getById(garageJson.getId());

        if (garageByJsonId != null) {
            update(garageByJsonId, garageJson);
        } else {
            add(garageJson);
        }
    }

    public void exportAllGaragesToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(garageRepository.getAll(), JsonUtil.JSON_CONFIGURATION_PATH + "garageList");
    }

    public void importAllGaragesFromJsonFile() throws IOException {
        List<Garage> garageList = JsonUtil.importModelListFromJsonFile(new Garage(), JsonUtil.JSON_CONFIGURATION_PATH + "garageList.json");
        garageRepository.setRepository(garageList);
    }

}
