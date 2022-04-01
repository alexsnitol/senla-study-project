package autoservice.Controllers;

import autoservice.Repositories.GarageRepository;
import autoservice.Services.GarageService;

import java.util.List;

public class GarageController {
    public static List<Integer> getFreePlaces(GarageRepository garage) {
        return GarageService.filterGarage(garage, 0, false);
    }
}
