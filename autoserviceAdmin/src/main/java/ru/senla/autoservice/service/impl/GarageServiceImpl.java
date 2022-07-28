package ru.senla.autoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.dto.GarageWithPlaceNumbersDto;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderGarage;
import ru.senla.autoservice.model.OrderMaster;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IGarageRepository;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderGarageRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.helper.EntityHelper;
import ru.senla.autoservice.util.PropertyUtil;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;

@Slf4j
@Service
public class GarageServiceImpl extends AbstractServiceImpl<Garage, IGarageRepository> implements IGarageService {

    private final IGarageRepository garageRepository;
    private final IMasterRepository masterRepository;
    private final IOrderRepository orderRepository;
    private final IOrderGarageRepository orderGarageRepository;

    @Autowired
    public GarageServiceImpl(IGarageRepository garageRepository, IMasterRepository masterRepository, IOrderRepository orderRepository,
                             IOrderGarageRepository orderGarageRepository) {
        this.garageRepository = garageRepository;
        this.masterRepository = masterRepository;
        this.orderRepository = orderRepository;
        this.orderGarageRepository = orderGarageRepository;
    }

    @PostConstruct
    public void init() {
        this.clazz = Garage.class;
        this.defaultRepository = garageRepository;
    }

    public Garage addPlace(Garage garage) {
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

    public Garage addPlace(Garage garage, int number) {
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

    public Garage addPlaceInGarageByIdAndUpdate(Long garageId) {
        Garage garage = getById(garageId);
        garage = addPlace(garage);
        garageRepository.update(garage);

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

    @Override
    public Garage deleteLastPlaceInGarageByIdAndUpdate(Long garageId) {
        Garage garage;
        try {
            garage = getById(garageId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        garage = deleteLastPlace(garage);
        garageRepository.update(garage);

        return garage;
    }

    /**
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     * Null if current garage is full.
     */
    private TakenPlaceDto takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(Long orderId, Garage garage) {
        List<Order> places = garage.getPlaces();

        int indexOfFreePlace = places.indexOf(null);
        if (indexOfFreePlace == -1) {
            log.error("All place in garage with id {} are taken", garage.getId());
            return null;
        }

        Order orderById = orderRepository.findById(orderId);
        EntityHelper.checkEntity(orderById, Order.class, orderId);

        OrderGarage orderGarage = new OrderGarage();
        orderGarage.setOrder(orderById);
        orderGarage.setGarage(garage);
        orderGarage.setPlace(indexOfFreePlace);

        try {
            orderGarageRepository.create(orderGarage);
        } catch (Exception e) {
            log.error(e.toString());

            return null;
        }

        TakenPlaceDto takenPlaceDto = new TakenPlaceDto(garage.getId(), indexOfFreePlace);

        log.info("Place in garage with id {} successful taken on index {}",
                takenPlaceDto.getGarageId(), takenPlaceDto.getPlaceNumber());

        return takenPlaceDto;
    }

    /**
     * Take first free place in all garages.
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     * Null if all garages is full.
     */
    public TakenPlaceDto takePlace(Long orderId) {
        for (Garage garage : getAll()) {
            if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
                return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage);
            }
        }

        log.error("All garages is full");
        return null;
    }

    /**
     * Take first free place in garage by id.
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     * Null if current garage is full.
     */
    public TakenPlaceDto takePlaceByGarageId(Long garageId, Long orderId) {
        Garage garage = getById(garageId);

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage);
        }

        log.error("All places in garage with id {} is taken", garageId);
        return null;
    }

    /**
     * Take free place by index in garage by id.
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     * Null if all garage is full.
     */
    public TakenPlaceDto takePlaceByGarageIdAndPlaceIndex(Long garageId, Integer indexOfPlace, Long orderId) {
        Garage garage = getById(garageId);

        if (indexOfPlace >= garage.getSize() || indexOfPlace < 0) {
            log.error("Index {} out of range {}", indexOfPlace, garage.getSize() - 1);
            return null;
        }

        if (garage.getNumberOfTakenPlaces() < garage.getSize()) {
            Long takenOrderId = garage.getPlaces().get(indexOfPlace).getId();
            if (takenOrderId != null) {
                log.error("Place with index {} in garage with id {} is taken by order with id {}",
                        indexOfPlace, garageId, takenOrderId);
                return null;
            }

            Order orderById = orderRepository.findById(orderId);
            EntityHelper.checkEntity(orderById, Order.class, orderId);

            OrderGarage orderGarage = new OrderGarage();
            orderGarage.setOrder(orderById);
            orderGarage.setGarage(garage);
            orderGarage.setPlace(indexOfPlace);

            orderGarageRepository.create(orderGarage);

            TakenPlaceDto takenPlaceDto = new TakenPlaceDto(garage.getId(), indexOfPlace);

            log.info("Place in garage with id {} successful taken on index {}",
                    takenPlaceDto.getGarageId(), takenPlaceDto.getPlaceNumber());

            return takenPlaceDto;
        }

        log.error("All places in garage with id {} is taken", garageId);
        return null;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderGarage> criteriaQuery = criteriaBuilder.createQuery(OrderGarage.class);
        Root<OrderGarage> orderGarageRoot = criteriaQuery.from(OrderGarage.class);
        orderGarageRoot.fetch("order", JoinType.LEFT).fetch("masters", JoinType.LEFT);
        Join<OrderGarage, Garage> garageJoin = orderGarageRoot.join("garage");
        Join<OrderGarage, Order> orderJoin = orderGarageRoot.join("order");

        criteriaQuery.select(orderGarageRoot)
                .where(criteriaBuilder.equal(garageJoin.get("id"), garage.getId()),
                        criteriaBuilder.or(
                                criteriaBuilder.equal(orderJoin.get("status"), OrderStatusEnum.IN_PROCESS),
                                criteriaBuilder.equal(orderJoin.get("status"), OrderStatusEnum.POSTPONED)
                        )
                );

        List<OrderGarage> orderGarageList = entityManager.createQuery(criteriaQuery).getResultList();

        try {
            int size = garage.getSize();
            garage.setPlaces(new ArrayList<>(garage.getSize()));
            garage = addPlace(garage, size);
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

    public List<Garage> setupPlaces(List<Garage> garages) {
        for (Garage garage : garages) {
            garage = setupPlaces(garage);
        }
        return garages;
    }

    @Override
    public Garage getById(Long id) {
        Garage garageById = super.getById(id);

        garageById = setupPlaces(garageById);
        return garageById;
    }

    @Override
    public List<Garage> getAll() {
        return setupPlaces(super.getAll());
    }

    @Override
    public List<Garage> getAll(MultiValueMap<String, String> requestParams) {
        return setupPlaces(super.getAll(requestParams));
    }

    public Garage getByOrderId(Long orderId) {
        Garage garageByOrderId = garageRepository.findByOrderId(orderId);
        garageByOrderId = setupPlaces(garageByOrderId);
        return garageByOrderId;
    }

    public List<GarageWithPlaceNumbersDto> getPlacesFilteredByAvailability(boolean isTaken) {
        List<GarageWithPlaceNumbersDto> listOfGarageWithPlaceNumbersDto = new ArrayList<>(size());
        List<Integer> numbersOfFilteredPlaces;

        for (Garage garage : getAll()) {
            numbersOfFilteredPlaces = new ArrayList<>(garage.getSize());

            for (int i = 0; i < garage.getSize(); i++) {
                if ((garage.getPlaces().get(i) != null) == isTaken) {
                    numbersOfFilteredPlaces.add(i);
                }
            }

            if (!numbersOfFilteredPlaces.isEmpty()) {
                listOfGarageWithPlaceNumbersDto.add(
                        new GarageWithPlaceNumbersDto(garage.getId(), numbersOfFilteredPlaces)
                );
            }
        }

        return listOfGarageWithPlaceNumbersDto;
    }

    @Override
    public List<Garage> getSorted(List<Garage> garages, String sortType) {
        return getAll();
    }

    public Integer getNumberOfFreePlacesByDate(LocalDate date) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Master> criteriaQuery = criteriaBuilder.createQuery(Master.class);
        Root<OrderMaster> orderMasterRoot = criteriaQuery.from(OrderMaster.class);
        Join<OrderMaster, Order> orderJoin = orderMasterRoot.join("order");
        Join<OrderMaster, Master> masterJoin = orderMasterRoot.join("master");

        LocalDateTime from = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
        LocalDateTime to = from.plusDays(1);

        criteriaQuery.select(masterJoin)
                .where(criteriaBuilder.or(
                                criteriaBuilder.equal(orderJoin.get("status"), OrderStatusEnum.POSTPONED),
                                criteriaBuilder.equal(orderJoin.get("status"), OrderStatusEnum.IN_PROCESS)
                        ),
                        criteriaBuilder.between(orderJoin.get("timeOfCompletion"), from, to)
                )
                .groupBy(masterJoin.get("id"));

        List<Master> mastersOnAllActiveOrdersByDate = entityManager.createQuery(criteriaQuery).getResultList();


        int numberOfFreePlaces = getPlacesFilteredByAvailability(false)
                .stream().mapToInt(g -> g.getPlaceNumbers().size()).sum();

        int numberOfFreeMasters = masterRepository.findAll().size() - mastersOnAllActiveOrdersByDate.size();

        return Math.min(numberOfFreePlaces, numberOfFreeMasters);
    }

    public LocalDate getNearestDate() {
        LocalDate tmpDate = LocalDate.now();
        while (getNumberOfFreePlacesByDate(tmpDate) == 0) {
            tmpDate.plusDays(1);
        }

        return tmpDate;
    }

}
