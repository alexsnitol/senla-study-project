package ru.senla.autoservice.controller;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.util.JsonUtil;

import java.io.IOException;
import java.util.List;

@Singleton
@Slf4j
public class GarageController extends AbstractController<Garage, IGarageService> {

    public static GarageController instance;
    @Autowired
    private IGarageService garageService;

    @PostConstruct
    public void setInstance() {
        instance = this;
    }

    public static GarageController getInstance() {
        return instance;
    }

    @PostConstruct
    public void init() {
        this.defaultService = garageService;
    }

    public void setGarageService(IGarageService garageService) {
        this.defaultService = garageService;
        this.garageService = garageService;
    }

    public void deleteById(Long garageId) {
        log.info("Deleting garage with id {}", garageId);
        garageService.deleteById(garageId);
    }

    public void add(Garage newGarage) {
        log.info("Adding new garage with id {}", newGarage.getId());
        garageService.add(newGarage);
    }

    public void addPlace(Long garageId) throws Exception {
        log.info("Adding new place in garage with id {}", garageId);
        Garage garage = garageService.getById(garageId);
        garage = garageService.addPlace(garage);
        garageService.update(garage);
    }

    public void deleteLastPlace(Long garageId) {
        log.info("Deleting last place in garage with id {}", garageId);
        Garage garage;
        try {
            garage = garageService.getById(garageId);
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
        garage = garageService.deleteLastPlace(garage);
        garageService.update(garage);
    }

    public List<List<Long>> getFreePlaces() {
        return garageService.getPlacesFilteredByAvailability(false);
    }

    public void exportGarageToJsonFile(Long garageId, String fileName) throws IOException {
        log.info("Export garage with id {} to json file: {}", garageId, fileName);
        garageService.exportGarageToJsonFile(garageId, fileName);
    }

    public void importGarageFromJsonFile(String path) throws IOException {
        log.info("Import garage from json file: {}", path);
        garageService.importGarageFromJsonFile(path);
    }

    public void exportAllGaragesToJsonFile() throws IOException {
        log.info("Export all garages to json file: {}", JsonUtil.JSON_CONFIGURATION_PATH + "garageList.json");
        garageService.exportAllGaragesToJsonFile();
    }

//    @PostConstruct
    public void importAllGaragesFromJsonFile() throws IOException {
        log.info("Import all garages from json file: {}", JsonUtil.JSON_CONFIGURATION_PATH + "garageList.json");
        garageService.importAllGaragesFromJsonFile();
    }

}
