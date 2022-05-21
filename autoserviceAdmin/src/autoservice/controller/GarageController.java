package autoservice.controller;

import autoservice.repository.model.Garage;
import autoservice.service.IGarageService;
import autoservice.service.impl.GarageServiceImpl;
import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;

import java.io.IOException;
import java.util.List;

@Singleton
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
        garageService.deleteById(garageId);
    }

    public void add(Garage newGarage) {
        garageService.add(newGarage);
    }

    public void addPlace(Long garageId) throws Exception {
        garageService.addPlace(garageId);
    }

    public int deleteLastPlace(Long garageId) {
        return garageService.deleteLastPlace(garageId);
    }

    public List<Garage> getFreePlaces() {
        return garageService.getPlacesFilteredByAvailability(false);
    }

    public void exportGarageToJsonFile(Long garageId, String fileName) throws IOException {
        garageService.exportGarageToJsonFile(garageId, fileName);
    }

    public void importGarageFromJsonFile(String path) throws IOException {
        garageService.importGarageFromJsonFile(path);
    }

    public void exportAllGaragesToJsonFile() throws IOException {
        garageService.exportAllGaragesToJsonFile();
    }

    @PostConstruct
    public void importAllGaragesFromJsonFile() throws IOException {
        garageService.importAllGaragesFromJsonFile();
    }

}
