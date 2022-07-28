package ru.senla.autoservice.service;

import ru.senla.autoservice.dto.GarageWithPlaceNumbersDto;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.model.Garage;

import java.time.LocalDate;
import java.util.List;

public interface IGarageService extends IAbstractService<Garage> {

    Garage addPlace(Garage garage);
    Garage addPlace(Garage garage, int number);
    Garage addPlaceInGarageByIdAndUpdate(Long garageId);

    Garage deleteLastPlace(Garage garage);
    Garage deleteLastPlaceInGarageByIdAndUpdate(Long garageId);

    TakenPlaceDto takePlace(Long orderId);
    TakenPlaceDto takePlaceByGarageId(Long garageId, Long orderId);
    TakenPlaceDto takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId);

    Garage getByOrderId(Long orderId);

    void freePlaceByOrderId(Long orderId);

    List<GarageWithPlaceNumbersDto> getPlacesFilteredByAvailability(boolean isTaken);

    Integer getNumberOfFreePlacesByDate(LocalDate date);
    LocalDate getNearestDate();

}
