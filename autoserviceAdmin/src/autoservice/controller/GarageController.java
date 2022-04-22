package autoservice.controller;

import autoservice.repository.model.Garage;
import autoservice.service.IGarageService;
import autoservice.service.impl.GarageServiceImpl;

import java.io.IOException;
import java.util.List;

public class GarageController extends AbstractController<Garage, IGarageService> {

    private static GarageController instance;
    private IGarageService garageService;


    private GarageController() {
        super(new GarageServiceImpl());
    }

    public static GarageController getInstance() {
        if (instance == null) {
            instance = new GarageController();
        }
        return instance;
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

    public void addPlace(Long garageId) {
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

}
