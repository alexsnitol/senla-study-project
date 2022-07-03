package ru.senla.autoservice.service.impl;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IGarageRepository;
import ru.senla.autoservice.repository.IOrderGarageRepository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderGarage;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.util.EntityManagerUtil;
import ru.senla.autoservice.util.JsonUtil;
import ru.senla.autoservice.util.PropertyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.err;

@Singleton
@Slf4j
public class GarageServiceImpl extends AbstractServiceImpl<Garage, IGarageRepository> implements IGarageService {

    @Autowired
    private IGarageRepository garageRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IOrderGarageRepository orderGarageRepository;


    @PostConstruct
    public void init() {
        this.defaultRepository = garageRepository;
    }

    public void setGarageRepository(IGarageRepository garageRepository) {
        this.defaultRepository = garageRepository;
        this.garageRepository = garageRepository;
    }

    public Garage addPlace(Garage garage) throws Exception {
        try {
            PropertyUtil.getPropertyAddAndDeleteFreePlaces();
        } catch (Exception e) {
            err.println(e.getMessage());
        }

        List<Order> placesOfGarage = garage.getPlaces();

        placesOfGarage.add(null);
        garage.setPlaces(placesOfGarage);

        return garage;
    }

    public Garage addPlace(Garage garage, int number) throws Exception {
        try {
            PropertyUtil.getPropertyAddAndDeleteFreePlaces();
        } catch (Exception e) {
            err.println(e.getMessage());
        }

        List<Order> placesOfGarage = garage.getPlaces();

        for (int i = 0; i < number; i++) {
            placesOfGarage.add(null);
        }
        garage.setPlaces(placesOfGarage);

        return garage;
    }


    public Garage deleteLastPlace(Garage garage) {
        try {
            PropertyUtil.getPropertyAddAndDeleteFreePlaces();
        } catch (Exception e) {
            log.error(e.toString());
            return garage;
        }

        List<Order> placesOfGarage = garage.getPlaces();

        if (placesOfGarage.isEmpty()) {
            log.error("Garage with id {} is empty", garage.getId());
        } else {
            if (placesOfGarage.get(placesOfGarage.size() - 1) != null) {
                log.error("In garage with id {} last place is taken", garage.getId());
            } else {
                placesOfGarage.remove(placesOfGarage.size() - 1);
                garage.setPlaces(placesOfGarage);
                log.info("In garage with id {} last place successful deleted", garage.getId());
            }
        }

        return garage;
    }

    /**
     * @return
     * list with info where: index 0 - id garage, index 1 - index of taken place;
     */
    private List<Long> takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(Long orderId, Garage garage) {
        List<Order> places = garage.getPlaces();

        int indexOfFreePlace = places.indexOf(null);
        Order orderById = orderRepository.findById(orderId);

        OrderGarage orderGarage = new OrderGarage();
        orderGarage.setOrder(orderById);
        orderGarage.setGarage(garage);
        orderGarage.setPlace(indexOfFreePlace);

        try {
            orderGarageRepository.create(orderGarage);
        } catch (Exception e) {
            log.error(e.toString());

            return Collections.emptyList();
        }

        List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
        garagesIdAndNumberOfTakenPlace.add(garage.getId());
        garagesIdAndNumberOfTakenPlace.add(Integer.toUnsignedLong(indexOfFreePlace));

        log.info("Place in garage with id {} successful taken on index {}",
                garagesIdAndNumberOfTakenPlace.get(0), garagesIdAndNumberOfTakenPlace.get(1));

        return garagesIdAndNumberOfTakenPlace;
    }

    /**
     * Take first free place in all garages.
     *
     * @return emptyList - all garages is full;
     * list with info where: index 0 - id garage, index 1 - index of taken place;
     */
    public List<Long> takePlace(Long orderId) {
        for (Garage garage : this.garageRepository.findAll()) {
            if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
                return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage);
            }
        }

        log.error("All garages is full");
        return Collections.emptyList();
    }

    /**
     * Take first free place in garage by id.
     *
     * @return emptyList - all places in garage is taken;
     * list with info where: index 0 - id garage, index 1 - index of taken place;
     */
    public List<Long> takePlaceByGarageId(Long garageId, Long orderId) {
        Garage garage = this.garageRepository.findById(garageId);

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage);
        }

        log.error("All places in garage with id {} is taken", garageId);
        return Collections.emptyList();
    }

    /**
     * Take free place by index in garage by id.
     *
     * @return emptyList - all places in garage is taken or place is taken;
     * list with info where: index 0 - id garage, index 1 - index of taken place;
     */
    public List<Long> takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId) {
        Garage garage = getById(garageId);
        List<Order> places = garage.getPlaces();

        if (indexOfPlace >= garage.getSize() || indexOfPlace < 0) {
            log.error("Index {} out of range {}", indexOfPlace, garage.getSize() - 1);
            return Collections.emptyList();
        }

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            Long takenOrderId = garage.getPlaces().get(indexOfPlace).getId();
            if (takenOrderId != null) {
                log.error("Place with index {} in garage with id {} is taken by order with id {}",
                        indexOfPlace, garageId, takenOrderId);
                return Collections.emptyList();
            }

            Order orderById = orderRepository.findById(orderId);

            OrderGarage orderGarage = new OrderGarage();
            orderGarage.setOrder(orderById);
            orderGarage.setGarage(garage);
            orderGarage.setPlace(indexOfPlace);

            EntityManager entityManager = EntityManagerUtil.getEntityManager();
            EntityTransaction transaction =  entityManager.getTransaction();
            try {
                transaction.begin();

                orderGarageRepository.create(orderGarage);

                transaction.commit();
            } catch (Exception e) {
                log.error(e.toString());
                transaction.rollback();

                return Collections.emptyList();
            } finally {
                entityManager.close();
            }

            List<Long> garagesIdAndNumberOfTakenPlace = new ArrayList<>(2);
            garagesIdAndNumberOfTakenPlace.add(orderId);
            garagesIdAndNumberOfTakenPlace.add(indexOfPlace.longValue());

            log.info("Place in garage with id {} successful taken on index {}",
                    garagesIdAndNumberOfTakenPlace.get(0), garagesIdAndNumberOfTakenPlace.get(1));

            return garagesIdAndNumberOfTakenPlace;
        }

        log.error("All places in garage with id {} is taken", garageId);
        return Collections.emptyList();
    }

    public void freePlaceByOrderId(Long orderId) {
        Garage garage = getByOrderId(orderId);
        Integer indexOfPlace = garage.getIndexOfPlaceByOrderId(orderId);

        List<Order> places = garage.getPlaces();
        places.set(indexOfPlace, null);
        garage.setPlaces(places);

        OrderGarage orderGarage = orderGarageRepository.findByOrderId(orderId);
        orderGarageRepository.delete(orderGarage);

        log.info("Place with index {} in garage with id {} successful freed", indexOfPlace, garage.getId());
    }

    private Garage setupPlaces(Garage garage) {
        Query query = EntityManagerUtil.getEntityManager()
                .createQuery(
                        "from OrderGarage where garage.id = " + garage.getId()
                                + " and (order.status = '" + OrderStatusEnum.IN_PROCESS + "'"
                                + " or order.status = '" + OrderStatusEnum.POSTPONED + "')"
                );
        List<OrderGarage> orderGarageList = query.getResultList();

        try {
            garage = addPlace(garage, garage.getSize());
        } catch (Exception e) {
            log.error(e.toString());
        }
        List<Order> places = garage.getPlaces();

        for (OrderGarage orderGarage : orderGarageList) {
            places.set(orderGarage.getPlace(), orderGarage.getOrder());
        }

        garage.setPlaces(places);
        garage.setNumberOfTakenPlaces(orderGarageList.size());

        return garage;
    }

    @Override
    public Garage getById(Long id) {
        Garage garageById = garageRepository.findById(id);
        garageById = setupPlaces(garageById);
        return garageById;
    }

    @Override
    public List<Garage> getAll() {
        List<Garage> garages = garageRepository.findAll();
        for (Garage garage : garages) {
            garage = setupPlaces(garage);
        }
        return garages;
    }

    public Garage getByOrderId(Long orderId) {
        Garage garageByOrderId = garageRepository.findByOrderId(orderId);
        garageByOrderId = setupPlaces(garageByOrderId);
        return garageByOrderId;
    }

    public List<List<Long>> getPlacesFilteredByAvailability(boolean isTaken) {
        List<List<Long>> garagesIdAndNumbersOfFilteredPlaces = new ArrayList<>(size());
        List<Long> numbersOfFilteredPlaces;

        for (Garage garage : getAll()) {
            numbersOfFilteredPlaces = new ArrayList<>(garage.getSize());

            for (int i = 0; i < garage.getSize(); i++) {
                if ((garage.getPlaces().get(i) != null) == isTaken) {
                    numbersOfFilteredPlaces.add(Integer.toUnsignedLong(i));
                }
            }

            if (!numbersOfFilteredPlaces.isEmpty()) {
                List<Long> garageIdWithNumbersOfFilteredPlaces = new ArrayList<>(numbersOfFilteredPlaces.size());

                garageIdWithNumbersOfFilteredPlaces.add(garage.getId());
                garageIdWithNumbersOfFilteredPlaces.addAll(numbersOfFilteredPlaces);

                garagesIdAndNumbersOfFilteredPlaces.add(garageIdWithNumbersOfFilteredPlaces);
            }
        }

        return garagesIdAndNumbersOfFilteredPlaces;
    }

    @Override
    public List<Garage> getSorted(List<Garage> garages, String sortType) {
        return garageRepository.findAll();
    }

    public void exportGarageToJsonFile(Long garageId, String fileName) throws IOException {
        Garage garageById = getById(garageId);
        JsonUtil.exportModelToJsonFile(garageById, fileName);
        log.info("Garage with id {} successful exported", garageId);
    }

    public void importGarageFromJsonFile(String path) throws IOException {
        Garage garageJson = JsonUtil.importModelFromJsonFile(new Garage(), path);

        if (garageRepository.isExist(garageJson)) {
            update(garageJson);
        } else {
            add(garageJson);
        }
        log.info("Garage successful imported");
    }

    public void exportAllGaragesToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(garageRepository.findAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "garageList");
        log.info("All garages successful exported");
    }

    public void importAllGaragesFromJsonFile() throws IOException {
        List<Garage> garageList = JsonUtil.importModelListFromJsonFile(new Garage(),
                JsonUtil.JSON_CONFIGURATION_PATH + "garageList.json");
        garageRepository.setRepository(garageList);
        log.info("All garages successful imported");
    }

}
