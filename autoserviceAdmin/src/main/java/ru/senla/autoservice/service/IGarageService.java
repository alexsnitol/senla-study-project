package ru.senla.autoservice.service;

import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.model.Garage;

import java.io.IOException;
import java.util.List;

public interface IGarageService extends IAbstractService<Garage> {

    void setGarageRepository(IGarageRepository garageRepository);

    Garage addPlace(Garage garage) throws Exception;
    Garage addPlace(Garage garage, int number) throws Exception;
    Garage addPlaceInGarageByIdAndUpdate(Long garageId) throws Exception;

    Garage deleteLastPlace(Garage garage);
    Garage deleteLastPlaceInGarageByIdAndUpdate(Long garageId);

    List<Long> takePlace(Long orderId);

    List<Long> takePlaceByGarageId(Long garageId, Long orderId);

    List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId);
    Garage getByOrderId(Long orderId);

    void freePlaceByOrderId(Long orderId);

    /**
    * @return list of list where index 0 is garage id and other index is place number
    **/
    List<List<Long>> getPlacesFilteredByAvailability(boolean isTaken);

    void exportGarageToJsonFile(Long garageId, String fileName) throws IOException;

    void importGarageFromJsonFile(String path) throws IOException;

    void exportAllGaragesToJsonFile() throws IOException;

    void importAllGaragesFromJsonFile() throws IOException;
}
