package autoservice.service;

import autoservice.repository.IGarageRepository;
import autoservice.repository.model.Garage;

import java.io.IOException;
import java.util.List;

public interface IGarageService extends IAbstractService<Garage> {

    void setGarageRepository(IGarageRepository garageRepository);

    void addPlace(Long garageId) throws Exception;
    int deleteLastPlace(Long garageId);

    List<Long> takePlace(Long orderId);
    List<Long> takePlaceByGarageId(Long garageId, Long orderId);
    List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId);

    void freePlaceByOrderId(Long orderId);

    List<Garage> getPlacesFilteredByAvailability(boolean isTaken);

    void exportGarageToJsonFile(Long garageId, String fileName) throws IOException;
    void importGarageFromJsonFile(String path) throws IOException;
    void exportAllGaragesToJsonFile() throws IOException;
    void importAllGaragesFromJsonFile() throws IOException;

}
