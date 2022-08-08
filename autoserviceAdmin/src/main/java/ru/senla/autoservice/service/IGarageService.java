package ru.senla.autoservice.service;

import ru.senla.autoservice.dto.GarageWithPlaceNumbersDto;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.exception.garage.AllPlacesInGarageIsTakenException;
import ru.senla.autoservice.exception.garage.GarageIsEmptyException;
import ru.senla.autoservice.exception.garage.LastPlaceInGarageIsTakenException;
import ru.senla.autoservice.exception.garage.PlaceInGarageIsTakenException;
import ru.senla.autoservice.model.Garage;

import java.time.LocalDate;
import java.util.List;

public interface IGarageService extends IAbstractService<Garage> {

    void addPlace(Garage garage);
    void addPlace(Garage garage, int number);
    void addPlaceInGarageByIdAndUpdate(Long garageId);

    void deleteLastPlace(Garage garage) throws GarageIsEmptyException, LastPlaceInGarageIsTakenException;
    void deleteLastPlaceInGarageByIdAndUpdate(Long garageId);

    TakenPlaceDto takePlace(Long orderId);
    TakenPlaceDto takePlaceByGarageId(Long garageId, Long orderId) throws AllPlacesInGarageIsTakenException;
    TakenPlaceDto takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId)
            throws PlaceInGarageIsTakenException, AllPlacesInGarageIsTakenException;

    Garage getByOrderId(Long orderId);

    void freePlaceByOrderId(Long orderId);

    List<GarageWithPlaceNumbersDto> getPlacesFilteredByAvailability(boolean isTaken);

    Integer getNumberOfFreePlacesByDate(LocalDate date);
    LocalDate getNearestDate();

}
