package ru.senla.autoservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.dto.GarageWithPlaceNumbersDto;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.exception.BusinessRuntimeException;
import ru.senla.autoservice.exception.garage.AllGaragesIsFullException;
import ru.senla.autoservice.exception.garage.AllPlacesInGarageIsTakenException;
import ru.senla.autoservice.exception.garage.GarageIsEmptyException;
import ru.senla.autoservice.exception.garage.LastPlaceInGarageIsTakenException;
import ru.senla.autoservice.exception.garage.PlaceInGarageIsTakenException;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderGarage;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IGarageRepository;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderGarageRepository;
import ru.senla.autoservice.repo.IOrderMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.helper.EntityHelper;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class GarageServiceImpl extends AbstractServiceImpl<Garage, IGarageRepository> implements IGarageService {

    private final IGarageRepository garageRepository;
    private final IMasterRepository masterRepository;
    private final IOrderRepository orderRepository;
    private final IOrderGarageRepository orderGarageRepository;
    private final IOrderMasterRepository orderMasterRepository;


    @PostConstruct
    public void init() {
        this.clazz = Garage.class;
        this.defaultRepository = garageRepository;
    }

    // adding null in places of garage
    public void addPlace(@NonNull Garage garage) {
        List<Order> placesOfGarage = garage.getPlaces();

        placesOfGarage.add(null);
        garage.setPlaces(placesOfGarage);
    }

    // adding null in places of garage n times
    public void addPlace(@NonNull Garage garage, int number) {
        List<Order> placesOfGarage = garage.getPlaces();

        for (int i = 0; i < number; i++) {
            placesOfGarage.add(null);
        }
        garage.setPlaces(placesOfGarage);
    }

    @Transactional
    public void addPlaceInGarageByIdAndUpdate(@NonNull Long garageId) {
        Garage garage = getById(garageId);
        addPlace(garage);
        garageRepository.update(garage);
    }


    public void deleteLastPlace(@NonNull Garage garage) throws GarageIsEmptyException,
            LastPlaceInGarageIsTakenException {

        List<Order> placesOfGarage = garage.getPlaces();

        String message = "";
        if (placesOfGarage.isEmpty()) {
            message = String.format("Garage with id %s is empty", garage.getId());
            log.error(message);
            throw new GarageIsEmptyException(message);
        } else {
            if (placesOfGarage.get(placesOfGarage.size() - 1) != null) {
                message = String.format("In garage with id %s last place is taken", garage.getId());
                log.error(message);
                throw new LastPlaceInGarageIsTakenException(message);
            } else {
                placesOfGarage.remove(placesOfGarage.size() - 1);
                garage.setPlaces(placesOfGarage);
                message = String.format("In garage with id %s last place successful deleted", garage.getId());
                log.info(message);
            }
        }
    }

    @Override
    public void deleteLastPlaceInGarageByIdAndUpdate(@NonNull Long garageId) {
        Garage garage = getById(garageId);

        try {
            deleteLastPlace(garage);
        } catch (Exception e) {
            log.error("Deleting last place in garage with id " + garageId + " failed");
            throw new BusinessRuntimeException(e.getMessage());
        }

        garageRepository.update(garage);
    }

    /**
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     */
    private TakenPlaceDto takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(
            @NonNull Long orderId,
            @NonNull Garage garage,
            @NonNull Optional<Integer> indexOfPlaceOptional)
            throws AllPlacesInGarageIsTakenException, PlaceInGarageIsTakenException {

        List<Order> places = garage.getPlaces();

        Integer indexOfFreePlace = null;
        if (indexOfPlaceOptional.isEmpty()) {
            indexOfFreePlace = places.indexOf(null);
            if (indexOfFreePlace == -1) {
                String message = String.format("All place in garage with id %s are taken", garage.getId());
                log.error(message);
                throw new AllPlacesInGarageIsTakenException(message);
            }
        } else {
            Integer indexOfPlace = indexOfPlaceOptional.get();
            if (indexOfPlace < 0 || indexOfPlace >= garage.getSize()) {
                String message = String.format("Index %s out of range %s", indexOfPlace, garage.getSize() - 1);
                log.error(message);
                throw new IndexOutOfBoundsException(message);
            }

            Order orderInPlaceByIndex = garage.getPlaces().get(indexOfPlace);
            if (orderInPlaceByIndex != null) {
                String message = String.format(
                        "Place with index %s in garage with id %s is taken by order with id %s",
                        indexOfPlace, garage.getId(), orderInPlaceByIndex.getId()
                );
                log.error(message);
                throw new PlaceInGarageIsTakenException(message);
            }

            indexOfFreePlace = indexOfPlace;
        }

        Order orderById = orderRepository.findById(orderId);
        EntityHelper.checkEntityOnNullAfterFindedById(orderById, Order.class, orderId);

        OrderGarage orderGarage = new OrderGarage();
        orderGarage.setOrder(orderById);
        orderGarage.setGarage(garage);
        orderGarage.setPlace(indexOfFreePlace);

        orderGarageRepository.create(orderGarage);

        places.set(indexOfFreePlace, orderById);
        garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() + 1);

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
     */
    public TakenPlaceDto takePlace(@NonNull Long orderId) {
        for (Garage garage : getAll()) {
            try {
                return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage, Optional.empty());
            } catch (AllPlacesInGarageIsTakenException e) {
                continue;
            } catch (Exception e) {
                String message = String.format("Taking place in garage with id %s failed", garage.getId());
                log.error(message);
                throw new BusinessRuntimeException(e.getMessage());
            }
        }

        String message = "All garages is full";
        log.error(message);
        throw new AllGaragesIsFullException(message);
    }

    /**
     * Take first free place in garage by id.
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     */
    public TakenPlaceDto takePlaceByGarageId(@NonNull Long garageId, @NonNull Long orderId)
            throws AllPlacesInGarageIsTakenException {

        Garage garage = getById(garageId);

        try {
            return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage, Optional.empty());
        } catch (AllPlacesInGarageIsTakenException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Taking place in garage with id %s failed", garage.getId());
            log.error(message);
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    /**
     * Take free place by index in garage by id.
     *
     * @return
     * Object of class TakenPlaceDto if place has been successfully taken.
     */
    public TakenPlaceDto takePlaceByGarageIdAndPlaceIndex(@NonNull Long garageId, @NonNull Integer indexOfPlace,
                                                          @NonNull Long orderId)
            throws PlaceInGarageIsTakenException {

        Garage garage = getById(garageId);

        try {
            return takePlaceAndGetListOfGarageIdAndNumberOfTakenPlace(orderId, garage, Optional.of(indexOfPlace));
        } catch (PlaceInGarageIsTakenException | IndexOutOfBoundsException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Taking place in garage with id %s failed", garage.getId());
            log.error(message);
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    private int getIndexOfPlaceByOrderId(@NonNull Garage garage, @NonNull Long orderId) {
        List<Order> places = garage.getPlaces();
        for (int i = 0; i < places.size(); i++) {
            if (places.get(i) != null) {
                if (places.get(i).getId().equals(orderId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void freePlaceByOrderId(@NonNull Long orderId) {
        Garage garage = getByOrderId(orderId);

        if (garage == null) {
            String message = String.format("Order with id %s is not in any garage", orderId);
            log.warn(message);
            return;
        }

        int indexOfPlace = getIndexOfPlaceByOrderId(garage, orderId);
        if (indexOfPlace == -1) {
            String message = String.format("Free place by order with id %s failed", orderId);
            log.error(message);
            throw new BusinessRuntimeException(message);
        }

        List<Order> places = garage.getPlaces();
        places.set(indexOfPlace, null);
        garage.setPlaces(places);
        garage.setNumberOfTakenPlaces(garage.getNumberOfTakenPlaces() - 1);

        OrderGarage orderGarage = orderGarageRepository.findByOrderId(orderId);
        orderGarageRepository.delete(orderGarage);

        log.info("Place with index {} in garage with id {} successful freed", indexOfPlace, garage.getId());
    }

    private void setupPlaces(@NonNull Garage garage) {
        List<OrderGarage> orderGarageList = orderGarageRepository.findAllByGarageIdAndOrderStatusList(
                garage.getId(),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED)
        );

        int size = garage.getSize();
        garage.setPlaces(new ArrayList<>(garage.getSize()));
        addPlace(garage, size);


        List<Order> places = garage.getPlaces();

        for (OrderGarage orderGarage : orderGarageList) {
            places.set(orderGarage.getPlace(), orderGarage.getOrder());
        }

        garage.setPlaces(places);
        garage.setNumberOfTakenPlaces(orderGarageList.size());
    }

    private List<Garage> setupPlaces(@NonNull List<Garage> garages) {
        for (Garage garage : garages) {
            setupPlaces(garage);
        }
        return garages;
    }

    @Override
    public Garage getById(@NonNull Long id) {
        Garage garageById = super.getById(id);
        setupPlaces(garageById);
        return garageById;
    }

    @Override
    public List<Garage> getAll() {
        return setupPlaces(super.getAll());
    }

    @Override
    public List<Garage> getAll(@NonNull MultiValueMap<String, String> requestParams) {
        return setupPlaces(super.getAll(requestParams));
    }

    public Garage getByOrderId(Long orderId) {
        Garage garageByOrderId = garageRepository.findByOrderId(orderId);
        if (garageByOrderId != null) {
            setupPlaces(garageByOrderId);
        }
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

    public Integer getNumberOfFreePlacesByDate(@NonNull LocalDate date) {
        LocalDateTime from = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
        LocalDateTime to = from.plusDays(1);

        List<OrderStatusEnum> orderStatusList = List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED);

        List<Master> mastersOnAllActiveOrdersByDate = orderMasterRepository
                .findAllMastersByTimeOfCompletionAndOrderStatusList(from, to, orderStatusList);

        List<Order> activeOrdersByDate = orderRepository
                .findAllByTimeOfCompletionAndStatuses(from, to, orderStatusList);

        int numberOfFreePlacesByDate = getNumberOfPlacesInAllGarages() - activeOrdersByDate.size();

        int numberOfFreeMastersByDate = masterRepository.findAll().size() - mastersOnAllActiveOrdersByDate.size();

        return Math.min(numberOfFreePlacesByDate, numberOfFreeMastersByDate);
    }

    private int getNumberOfPlacesInAllGarages() {
        return garageRepository.sumOfSizeOfAllGarages();
    }

    public LocalDate getNearestDate() {
        LocalDate tmpDate = LocalDate.now();
        while (getNumberOfFreePlacesByDate(tmpDate) == 0) {
            tmpDate = tmpDate.plusDays(1);
        }

        return tmpDate;
    }

}
