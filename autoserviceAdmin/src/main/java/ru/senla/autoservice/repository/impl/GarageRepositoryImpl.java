package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.model.Garage;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class GarageRepositoryImpl extends AbstractRepositoryImpl<Garage> implements IGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(Garage.class);
    }

    public List<Garage> getPlacesFilteredByAvailability(boolean isTaken) {
        List<Garage> garagesWithNumbersOfFilteredPlaces = new ArrayList<>(this.repository.size());
        List<Long> numbersOfFilteredPlaces;

        for (Garage garage : this.repository) {
            numbersOfFilteredPlaces = new ArrayList<>(garage.getSize());

            for (int i = 0; i < garage.getSize(); i++) {
                if ((garage.getPlaces().get(i) != null) == isTaken) {
                    numbersOfFilteredPlaces.add(Integer.toUnsignedLong(i));
                }
            }

            if (!numbersOfFilteredPlaces.isEmpty()) {
                Garage garageWithNumbersOfFilteredPlaces = new Garage();
                garageWithNumbersOfFilteredPlaces.setId(garage.getId());
                // TODO
                //garageWithNumbersOfFilteredPlaces.setPlaces(numbersOfFilteredPlaces);
                garageWithNumbersOfFilteredPlaces.setPlaces(null);

                garagesWithNumbersOfFilteredPlaces.add(garageWithNumbersOfFilteredPlaces);
            }
        }

        return garagesWithNumbersOfFilteredPlaces;
    }

    public Garage findByOrderId(Long orderId) {
        return repository.stream().filter(g -> g.getIndexOfPlaceByOrderId(orderId) != null).findFirst().orElse(null);
    }

    @Override
    public List<Garage> findAllSorted(String sortType) {
        return findAll();
    }
}