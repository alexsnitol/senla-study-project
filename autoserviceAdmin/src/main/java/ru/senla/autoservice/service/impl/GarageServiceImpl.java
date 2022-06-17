package ru.senla.autoservice.service.impl;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.util.JsonUtil;
import ru.senla.autoservice.util.PropertyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.err;

@Singleton
@Slf4j
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
     * @return 1 - garage is empty;
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
            log.error("Garage with id {} is empty", garageId);
            return 1;
        }

        if (placesOfGarage.get(placesOfGarage.size() - 1) != null) {
            log.error("In garage with id {} last place is taken", garageId);
            return -1;
        } else {
            placesOfGarage.remove(placesOfGarage.size() - 1);
            garage.setPlaces(placesOfGarage);
            log.info("In garage with id {} last place successful deleted", garageId);
            return 0;
        }
    }

    private List<Long> takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(Long orderId, Garage garage) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);

        int indexOfFreePlace = garage.getPlaces().indexOf(null);

        garage.getPlaces().set(indexOfFreePlace, orderId);
        garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() + 1);

        garagesIdAndNumberOfTakenPlace.add(garage.getId());
        garagesIdAndNumberOfTakenPlace.add(Integer.toUnsignedLong(indexOfFreePlace));

        log.info("Place in garage with id {} successful taken on index {}",
                garagesIdAndNumberOfTakenPlace.get(0), garagesIdAndNumberOfTakenPlace.get(1));

        return garagesIdAndNumberOfTakenPlace;
    }

    /**
     * Take first free place in all garages.
     *
     * @return emptyList - all garages is full;
     * list with info where: index 0 - id garage, index 1 - index taken place;
     */
    public List<Long> takePlace(Long orderId) {
        for (Garage garage : this.garageRepository.getAll()) {
            if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
                return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage);
            }
        }

        log.error("All garages is full");
        return Collections.emptyList();
    }

    /**
     * Take first free place in garage by id.
     *
     * @return emptyList - all places in garage is taken;
     * list with info where: index 0 - id garage, index 1 - index taken place;
     */
    public List<Long> takePlaceByGarageId(Long garageId, Long orderId) {
        Garage garage = this.garageRepository.getById(garageId);

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage);
        }

        log.error("All places in garage with id {} is taken", garageId);
        return Collections.emptyList();
    }

    /**
     * Take free place by index in garage by id.
     *
     * @return emptyList - all places in garage is taken or place is taken;
     * list with info where: index 0 - id garage, index 1 - index taken place;
     */
    public List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId) {
        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
        Garage garage = this.garageRepository.getById(garageId);

        if (indexOfPlace >= garage.getSize() || indexOfPlace < 0) {
            log.error("Index {} out of range {}", indexOfPlace, garage.getSize());
            return Collections.emptyList();
        }

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {

            Long takenOrderId = garage.getPlaces().get(indexOfPlace);
            if (takenOrderId != null) {
                log.error("Place with index {} in garage with id {} is taken by order with id {}",
                        indexOfPlace, garageId, takenOrderId);
                return Collections.emptyList();
            }

            garage.getPlaces().set(indexOfPlace, orderId);
            garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() + 1);

            garagesIdAndNumberOfTakenPlace.add(orderId);
            garagesIdAndNumberOfTakenPlace.add(indexOfPlace.longValue());

            log.info("Place in garage with id {} successful taken on index {}",
                    garagesIdAndNumberOfTakenPlace.get(0), garagesIdAndNumberOfTakenPlace.get(1));

            return garagesIdAndNumberOfTakenPlace;
        }

        log.error("All places in garage with id {} is taken", garageId);
        return Collections.emptyList();
    }

    public void freePlaceByOrderId(Long orderId) {
        Garage garage = garageRepository.getByOrderId(orderId);
        Integer indexOfPlace = garage.findByOrderId(orderId);

        garage.getPlaces().set(indexOfPlace, null);

        log.info("Place with index {} in garage with id {} successful freed", indexOfPlace, garage.getId());
    }

    public List<Garage> getPlacesFilteredByAvailability(boolean isTaken) {
        return this.garageRepository.getPlacesFilteredByAvailability(isTaken);
    }

    @Override
    public List<Garage> getSorted(String sortType) {
        return null;
    }

    @Override
    public List<Garage> getSorted(List<Garage> garages, String sortType) {
        return null;
    }

    public void exportGarageToJsonFile(Long garageId, String fileName) throws IOException {
        Garage garageById = getById(garageId);
        JsonUtil.exportModelToJsonFile(garageById, fileName);
        log.info("Garage with id {} successful exported", garageId);
    }

    public void importGarageFromJsonFile(String path) throws IOException {
        Garage garageJson = JsonUtil.importModelFromJsonFile(new Garage(), path);
        Garage garageByJsonId = getById(garageJson.getId());

        if (garageByJsonId != null) {
            update(garageByJsonId, garageJson);
        } else {
            add(garageJson);
        }
        log.info("Garage successful imported");
    }

    public void exportAllGaragesToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(garageRepository.getAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "garageList");
        log.info("All garages successful exported");
    }

    public void importAllGaragesFromJsonFile() throws IOException {
        List<Garage> garageList = JsonUtil.importModelListFromJsonFile(new Garage(),
                JsonUtil.JSON_CONFIGURATION_PATH + "garageList.json");
        garageRepository.setRepository(garageList);
        log.info("All garages successful imported");
    }

}
