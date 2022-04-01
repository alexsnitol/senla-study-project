package autoservice.Services;

import autoservice.Repositories.GarageRepository;

import java.util.ArrayList;
import java.util.List;

public class GarageService {
    public static <T> List<Integer> filterGarage(GarageRepository garage, int filterType, T param) {
        List<Integer> result;

        switch (filterType) {
            case 0:
                result = garage.getPlacesFilteredByAvailability((boolean) param);

                return result;
        }

        return null;
    }
}
