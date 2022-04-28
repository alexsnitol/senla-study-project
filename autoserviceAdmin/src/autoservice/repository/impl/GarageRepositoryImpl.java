package autoservice.repository.impl;

import autoservice.repository.IGarageRepository;
import autoservice.repository.model.Garage;

import java.util.ArrayList;
import java.util.List;

public class GarageRepositoryImpl extends AbstractRepositoryImpl<Garage> implements IGarageRepository {

    public List<Garage> getPlacesFilteredByAvailability(boolean isTaken) {
        List<Garage> garagesWithNumbersOfFilteredPlaces = new ArrayList<>(this.repository.size());
        List<Long> numbersOfFilteredPlaces;

        for (Garage garage : this.repository) {
            numbersOfFilteredPlaces = new ArrayList<>(garage.getSize());

            for (Integer i = 0; i < garage.getSize(); i++) {
                if (isTaken) {
                    if (garage.getPlaces().get(i) >= 0) {
                        numbersOfFilteredPlaces.add((i.longValue()));
                    }
                } else {
                    if (garage.getPlaces().get(i) == null) {
                        numbersOfFilteredPlaces.add((i.longValue()));
                    }
                }
            }

            if (numbersOfFilteredPlaces.size() != 0) {
                Garage garageWithNumbersOfFilteredPlaces = new Garage();
                garageWithNumbersOfFilteredPlaces.setId(garage.getId());
                garageWithNumbersOfFilteredPlaces.setPlaces(numbersOfFilteredPlaces);

                garagesWithNumbersOfFilteredPlaces.add(garageWithNumbersOfFilteredPlaces);
            }
        }

        return garagesWithNumbersOfFilteredPlaces;
    }

    public Garage getByOrderId(Long orderId) {
        return repository.stream().filter(g -> g.findByOrderId(orderId) != null).findFirst().orElse(null);
    }
}