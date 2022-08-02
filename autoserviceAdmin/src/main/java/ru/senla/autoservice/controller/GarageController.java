package ru.senla.autoservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.autoservice.dto.GarageWithPlaceNumbersDto;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.service.IGarageService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/garages")
public class GarageController extends AbstractController<Garage, IGarageService> {

    private final IGarageService garageService;


    @PostConstruct
    public void init() {
        this.defaultService = garageService;
    }


    @Override
    @GetMapping
    public List<Garage> getAll(@RequestParam(required = false) MultiValueMap<String, String> requestParams) {
        return super.getAll(requestParams);
    }

    @Override
    @GetMapping("/{id}")
    public Garage getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PutMapping("/{id}/update")
    public Garage update(@PathVariable Long id, Garage changedModel) {
        return super.update(id, changedModel);
    }

    @Override
    @GetMapping("/size")
    public Integer size() {
        return super.size();
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(Garage model) {
        return super.delete(model);
    }

    @Override
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long garageId) {
        log.info("Deleting garage with id {}", garageId);

        return super.deleteById(garageId);
    }

    @Override
    @PutMapping("/add")
    public Garage add(@RequestBody Garage newGarage) {
        log.info("Adding new garage: {}", newGarage);

        return super.add(newGarage);
    }

    @PostMapping("/{id}/add-place")
    public ResponseEntity<String> addPlace(@PathVariable("id") Long garageId) {
        log.info("Adding new place in garage with id {}", garageId);
        garageService.addPlaceInGarageByIdAndUpdate(garageId);

        return ResponseEntity.ok("Place added");
    }

    @DeleteMapping("/{id}/delete-last-place")
    public ResponseEntity<String> deleteLastPlace(@PathVariable("id") Long garageId) {
        log.info("Deleting last place in garage with id {}", garageId);
        garageService.deleteLastPlaceInGarageByIdAndUpdate(garageId);

        return ResponseEntity.ok("Last place deleted");
    }

    @GetMapping("/get-free-places")
    public List<GarageWithPlaceNumbersDto> getFreePlaces() {
        return garageService.getPlacesFilteredByAvailability(false);
    }

    @GetMapping("/get-taken-places")
    public List<GarageWithPlaceNumbersDto> getTakenPlaces() {
        return garageService.getPlacesFilteredByAvailability(true);
    }

    @PostMapping("/get-number-of-free-places-by-date")
    public Integer getNumberOfFreePlacesByDate(@RequestBody LocalDate date) {
        return garageService.getNumberOfFreePlacesByDate(date);
    }

    @GetMapping("/get-nearest-date")
    public LocalDate getNearestDate() {
        return garageService.getNearestDate();
    }

}
