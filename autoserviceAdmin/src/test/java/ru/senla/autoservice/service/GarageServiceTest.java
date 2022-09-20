package ru.senla.autoservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.senla.autoservice.config.TestConfig;
import ru.senla.autoservice.dto.GarageWithPlaceNumbersDto;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.exception.BusinessRuntimeException;
import ru.senla.autoservice.exception.garage.AllGaragesIsFullException;
import ru.senla.autoservice.exception.garage.AllPlacesInGarageIsTakenException;
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
import ru.senla.autoservice.service.impl.GarageServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
class GarageServiceTest {

    @Autowired
    IGarageRepository mockedGarageRepository;
    @Autowired
    IMasterRepository mockedMasterRepository;
    @Autowired
    IOrderRepository mockedOrderRepository;
    @Autowired
    IOrderGarageRepository mockedOrderGarageRepository;
    @Autowired
    IOrderMasterRepository mockedOrderMasterRepository;

    @Autowired
    @Qualifier("garageService")
    IGarageService garageService;

    @Autowired
    @Qualifier("mockedGarageService")
    IGarageService mockedGarageService = mock(GarageServiceImpl.class);

    final Garage mockedGarage = mock(Garage.class);
    final List<OrderGarage> mockedOrderGarageList = mock(List.class);
    int size;
    List<Order> places;
    List<OrderGarage> orderGarageList;

    @BeforeAll
    static void setup() {
    }


    @BeforeEach
    void init() {
        size = 2;


        Order testMockedOrder1 = mock(Order.class);
        Order testMockedOrder2 = mock(Order.class);

        places = new ArrayList<>(List.of(testMockedOrder1, testMockedOrder2));


        OrderGarage testOrderGarage1 = new OrderGarage();
        testOrderGarage1.setPlace(0);
        testOrderGarage1.setOrder(testMockedOrder1);

        OrderGarage testOrderGarage2 = new OrderGarage();
        testOrderGarage2.setPlace(1);
        testOrderGarage2.setOrder(testMockedOrder2);

        orderGarageList = new ArrayList<>(List.of(testOrderGarage1, testOrderGarage2));
    }

    @AfterEach
    void clearInvocationsInMocked() {
        Mockito.clearInvocations(
                mockedGarageRepository,
                mockedMasterRepository,
                mockedOrderRepository,
                mockedOrderGarageRepository,
                mockedOrderMasterRepository,
                mockedGarageService
        );
    }

    @Test
    void whenAddPlaceCalledWithoutNumber_ThenInGarageWillAddOneNullPlaceAndIncreaseItSizeByOne() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setPlaces(places);
        testGarage.setSize(size);

        // expected result
        List<Order> placesWithNewPlace = new ArrayList<>(size);
        placesWithNewPlace.addAll(places);
        placesWithNewPlace.add(null);

        int sizeWithNewPlace = size + 1;

        // test
        garageService.addPlace(testGarage);

        assertEquals(placesWithNewPlace, testGarage.getPlaces());
        assertEquals(sizeWithNewPlace, testGarage.getSize());
    }

    @Test
    void whenAddPlaceCalledWithNumber_ThenInGarageWllAddSpecifiedNumberOfNullPlaceAndIncreaseItSizeByItNumber() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setPlaces(places);
        testGarage.setSize(size);

        // expected result
        List<Order> placesWithNewPlace = new ArrayList<>(size);
        placesWithNewPlace.addAll(places);
        for (int i = 0; i < 5; i++) {
            placesWithNewPlace.add(null);
        }

        int sizeWithNewPlace = size + 5;

        // test
        garageService.addPlace(testGarage, 5);

        assertEquals(placesWithNewPlace, testGarage.getPlaces());
        assertEquals(sizeWithNewPlace, testGarage.getSize());
    }

    @Test
    void whenAddPlaceInGarageByIdAndUpdateCalled_ThenInGarageByIdWillAddOneNullPlaceAndIncreaseItSizeByOneAndUpdateCalled() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setSize(size);

        // expected result
        List<Order> placesWithNewPlace = new ArrayList<>(size);
        placesWithNewPlace.addAll(places);
        placesWithNewPlace.add(null);

        int sizeWithNewPlace = size + 1;

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        garageService.addPlaceInGarageByIdAndUpdate(1L);

        assertEquals(placesWithNewPlace, testGarage.getPlaces());
        assertEquals(sizeWithNewPlace, testGarage.getSize());
        verify(mockedGarageRepository, times(1)).update(testGarage);
    }

    @Test
    void whenDeleteLastPlaceCalledForTheGarageWithTakenLastPlace_ThenWillThrownExceptionAndGarageStayNotChanged() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setPlaces(places);
        testGarage.setSize(size);

        // expected result
        List<Order> placesUnchanged = new ArrayList<>(size);
        placesUnchanged.addAll(places);

        int sizeUnchanged = size;

        // test
        assertThrows(LastPlaceInGarageIsTakenException.class, () -> garageService.deleteLastPlace(testGarage));
        assertEquals(placesUnchanged, testGarage.getPlaces());
        assertEquals(sizeUnchanged, testGarage.getSize());
    }

    @Test
    void whenDeleteLastPlaceCalledForTheGarageWithFreeLastPlace_ThenLastPlaceInGarageIsDeletedAndItSizeReducedByOne() throws Exception {
        // test garage
        Garage testGarage = new Garage();
        places.add(null);
        testGarage.setPlaces(places);
        testGarage.setSize(places.size());

        // expected result
        List<Order> placesAfterRemoveLastPlace = new ArrayList<>(places.size());
        placesAfterRemoveLastPlace.addAll(places);
        placesAfterRemoveLastPlace.remove(placesAfterRemoveLastPlace.size() - 1);

        int sizeBeforeRemoveLastPlace = placesAfterRemoveLastPlace.size();

        // test
        garageService.deleteLastPlace(testGarage);

        assertEquals(placesAfterRemoveLastPlace, testGarage.getPlaces());
        assertEquals(sizeBeforeRemoveLastPlace, testGarage.getSize());
    }

    @Test
    void whenDeleteLastPlaceInGarageByIdAndUpdateCalledForTheGarageWithTakenLastPlace_ThenWillThrownExceptionAndGarageStayNotChangedAndUpdateWillNotCalled() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setSize(size);

        // expected result
        List<Order> placesUnchanged = new ArrayList<>(size);
        placesUnchanged.addAll(places);

        int sizeUnchanged = size;

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        assertThrows(BusinessRuntimeException.class, () -> garageService.deleteLastPlaceInGarageByIdAndUpdate(1L));
        assertEquals(placesUnchanged, testGarage.getPlaces());
        assertEquals(sizeUnchanged, testGarage.getSize());
        verify(mockedGarageRepository, never()).update(testGarage);
    }

    @Test
    void whenDeleteLastPlaceInGarageByIdAndUpdateCalledForTheGarageWithFreeLastPlace_ThenInGarageByIdDeleteLastPlaceAndItSizeReducedByOneAndUpdateCalled() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        places.add(null);
        testGarage.setPlaces(places);
        testGarage.setSize(places.size());

        // expected result
        List<Order> placesAfterRemoveLastPlace = new ArrayList<>(places.size());
        placesAfterRemoveLastPlace.addAll(places);
        placesAfterRemoveLastPlace.remove(placesAfterRemoveLastPlace.size() - 1);

        int sizeBeforeRemoveLastPlace = placesAfterRemoveLastPlace.size();

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        garageService.deleteLastPlaceInGarageByIdAndUpdate(1L);

        assertEquals(placesAfterRemoveLastPlace, testGarage.getPlaces());
        assertEquals(sizeBeforeRemoveLastPlace, testGarage.getSize());
        verify(mockedGarageRepository, times(1)).update(testGarage);
    }

    @Test
    void whenTakePlaceCalledInCaseOfAllGaragesWithTakenPlaces_ThenWillThrowAllGaragesIsFullException() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage unchangedGarage = new Garage();
        unchangedGarage.setId(1L);
        List<Order> unchangedPlaces = new ArrayList<>(places.size());
        unchangedPlaces.addAll(places);
        unchangedGarage.setPlaces(unchangedPlaces);
        unchangedGarage.setNumberOfTakenPlaces(2);
        unchangedGarage.setSize(unchangedPlaces.size());
        List<Garage> unchangedGaragesList = List.of(unchangedGarage);

        // test
        when(mockedGarageRepository.findAll()).thenReturn(testGarageList);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(mock(Order.class));

        assertThrows(AllGaragesIsFullException.class, () -> garageService.takePlace(1L));
        assertEquals(unchangedGaragesList, testGarageList);
    }

    @Test
    void whenTakePlaceCalledInCaseOfExistGarageWithFreePlace_ThenWillInItPlacesSettingOrderById() {
        // test order
        Order testMockedOrder = mock(Order.class);

        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage garageWithTakenPlace = new Garage();
        garageWithTakenPlace.setId(1L);
        List<Order> placesWithTakenPlace = new ArrayList<>(places.size());
        placesWithTakenPlace.addAll(places);
        placesWithTakenPlace.add(testMockedOrder);
        garageWithTakenPlace.setPlaces(placesWithTakenPlace);
        garageWithTakenPlace.setNumberOfTakenPlaces(3);
        garageWithTakenPlace.setSize(placesWithTakenPlace.size());
        List<Garage> garageListWithTakenPlace = List.of(garageWithTakenPlace);

        TakenPlaceDto expectedTakenPlaceDto = new TakenPlaceDto();
        expectedTakenPlaceDto.setGarageId(1L);
        expectedTakenPlaceDto.setPlaceNumber(2);

        // additional settings for test garage
        places.add(null);
        testGarage.setSize(size + 1);

        // test
        when(mockedGarageRepository.findAll()).thenReturn(testGarageList);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(testMockedOrder);

        TakenPlaceDto resultTakenPlaceDto = garageService.takePlace(1L);

        assertEquals(garageListWithTakenPlace, testGarageList);
        assertEquals(expectedTakenPlaceDto, resultTakenPlaceDto);
    }

    @Test
    void whenTakePlaceByGarageIdCalledForGarageWhichFull_ThenThrowAllPlaceInGarageIsTakenException() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage unchangedGarage = new Garage();
        unchangedGarage.setId(1L);
        List<Order> unchangedPlaces = new ArrayList<>(places.size());
        unchangedPlaces.addAll(places);
        unchangedGarage.setPlaces(unchangedPlaces);
        unchangedGarage.setNumberOfTakenPlaces(2);
        unchangedGarage.setSize(unchangedPlaces.size());

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(mock(Order.class));

        assertThrows(AllPlacesInGarageIsTakenException.class, () -> garageService.takePlaceByGarageId(1L, 1L));
        assertEquals(unchangedGarage, testGarage);
    }

    @Test
    void whenTakePlaceByGarageIdCalledForGarageWhichExistFreePlace_ThenWillInItPlacesSettingOrderById() throws AllPlacesInGarageIsTakenException {
        // test order
        Order testMockedOrder = mock(Order.class);

        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage garageWithTakenPlace = new Garage();
        garageWithTakenPlace.setId(1L);
        List<Order> placesWithTakenPlace = new ArrayList<>(places.size());
        placesWithTakenPlace.addAll(places);
        placesWithTakenPlace.add(testMockedOrder);
        garageWithTakenPlace.setPlaces(placesWithTakenPlace);
        garageWithTakenPlace.setNumberOfTakenPlaces(3);
        garageWithTakenPlace.setSize(placesWithTakenPlace.size());

        TakenPlaceDto expectedTakenPlaceDto = new TakenPlaceDto();
        expectedTakenPlaceDto.setGarageId(1L);
        expectedTakenPlaceDto.setPlaceNumber(2);

        // additional settings for test garage
        places.add(null);
        testGarage.setSize(size + 1);

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(testMockedOrder);

        TakenPlaceDto resultTakenPlaceDto = garageService.takePlaceByGarageId(1L, 1L);

        assertEquals(garageWithTakenPlace, testGarage);
        assertEquals(expectedTakenPlaceDto, resultTakenPlaceDto);
    }

    @Test
    void whenTakePlaceByGarageIdAndPlaceIndexCalledInCaseOfIndexOutOfRange_ThenThrowIndexOutOfBoundsException() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage unchangedGarage = new Garage();
        unchangedGarage.setId(1L);
        List<Order> unchangedPlaces = new ArrayList<>(places.size());
        unchangedPlaces.addAll(places);
        unchangedGarage.setPlaces(unchangedPlaces);
        unchangedGarage.setNumberOfTakenPlaces(2);
        unchangedGarage.setSize(unchangedPlaces.size());

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(mock(Order.class));

        assertThrows(IndexOutOfBoundsException.class, () -> garageService.takePlaceByGarageIdAndPlaceIndex(1L, 10, 1L));
        assertEquals(unchangedGarage, testGarage);
    }

    @Test
    void whenTakePlaceByGarageIdAndPlaceIndexCalledForPlaceInGarageWhichIsTaken_ThenThrowPlaceIsInGarageIsTakenException() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage unchangedGarage = new Garage();
        unchangedGarage.setId(1L);
        List<Order> unchangedPlaces = new ArrayList<>(places.size());
        unchangedPlaces.addAll(places);
        unchangedGarage.setPlaces(unchangedPlaces);
        unchangedGarage.setNumberOfTakenPlaces(2);
        unchangedGarage.setSize(unchangedPlaces.size());

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(mock(Order.class));

        assertThrows(PlaceInGarageIsTakenException.class, () -> garageService.takePlaceByGarageIdAndPlaceIndex(1L, 1, 1L));
        assertEquals(unchangedGarage, testGarage);
    }

    @Test
    void whenTakePlaceByGarageIdAndPlaceIndexCalledForFreePlaceInGarage_ThenWillInItPlacesSettingOrderById()
            throws AllPlacesInGarageIsTakenException, PlaceInGarageIsTakenException {

        // test order
        Order testMockedOrder = mock(Order.class);

        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage garageWithTakenPlace = new Garage();
        garageWithTakenPlace.setId(1L);
        List<Order> placesWithTakenPlace = new ArrayList<>(places.size());
        placesWithTakenPlace.addAll(places);
        placesWithTakenPlace.add(testMockedOrder);
        garageWithTakenPlace.setPlaces(placesWithTakenPlace);
        garageWithTakenPlace.setNumberOfTakenPlaces(3);
        garageWithTakenPlace.setSize(placesWithTakenPlace.size());

        TakenPlaceDto expectedTakenPlaceDto = new TakenPlaceDto();
        expectedTakenPlaceDto.setGarageId(1L);
        expectedTakenPlaceDto.setPlaceNumber(2);

        // additional settings for test garage
        places.add(null);
        testGarage.setSize(size + 1);

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);
        when(mockedOrderRepository.findById(1L)).thenReturn(testMockedOrder);

        TakenPlaceDto resultTakenPlaceDto = garageService.takePlaceByGarageIdAndPlaceIndex(1L, 2, 1L);

        assertEquals(garageWithTakenPlace, testGarage);
        assertEquals(expectedTakenPlaceDto, resultTakenPlaceDto);
    }

    @Test
    void whenFreePlaceByOrderIdCalled_ThenPlaceWithOrderByIdSettingOnNull() {
        // test order
        Order testOrder = new Order();
        testOrder.setId(1L);

        // test places
        List<Order> testPlaces = new ArrayList<>(3);
        testPlaces.add(null);
        testPlaces.add(testOrder);
        testPlaces.add(null);

        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(testPlaces);
        testGarage.setNumberOfTakenPlaces(1);
        testGarage.setSize(testPlaces.size());

        OrderGarage testOrderGarage = new OrderGarage();
        testOrderGarage.setOrder(testOrder);
        testOrderGarage.setGarage(testGarage);
        testOrderGarage.setPlace(1);
        List<OrderGarage> testOrderGaragesList = List.of(testOrderGarage);

        // expected result
        Garage garageWithFreedPlace = new Garage();
        garageWithFreedPlace.setId(1L);
        List<Order> placesWithFreedPlace = new ArrayList<>(testPlaces.size());
        placesWithFreedPlace.addAll(testPlaces);
        placesWithFreedPlace.set(1, null);
        garageWithFreedPlace.setPlaces(placesWithFreedPlace);
        garageWithFreedPlace.setNumberOfTakenPlaces(0);
        garageWithFreedPlace.setSize(placesWithFreedPlace.size());

        // test
        when(mockedGarageRepository.findByOrderId(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(testOrderGaragesList);

        garageService.freePlaceByOrderId(1L);

        assertEquals(garageWithFreedPlace, testGarage);
    }

    @Test
    void whenGetByIdCalledInCaseOfExistingGarage_ThenGetGarageByIdWithSetUpPlaces() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        // expected result
        Garage expectedGarage = new Garage();
        expectedGarage.setId(1L);
        List<Order> placesWithTakenPlace = new ArrayList<>(places.size());
        placesWithTakenPlace.addAll(places);
        expectedGarage.setPlaces(placesWithTakenPlace);
        expectedGarage.setNumberOfTakenPlaces(2);
        expectedGarage.setSize(placesWithTakenPlace.size());

        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        Garage resultGarage = garageService.getById(1L);

        assertEquals(expectedGarage, resultGarage);
    }

    @Test
    void whenGetByIdCalledInCaseOfNotExistingGarage_ThenThrowEntityNotFoundException() {
        // test
        when(mockedGarageRepository.findById(1L)).thenReturn(null);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> garageService.getById(1L));
    }

    @Test
    void whenGetAllCalled_ThenGetAllGaragesWithSetUpPlaces() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        Garage expectedGarage = new Garage();
        expectedGarage.setId(1L);
        List<Order> expectedPlaces = new ArrayList<>(places.size());
        expectedPlaces.addAll(places);
        expectedGarage.setPlaces(expectedPlaces);
        expectedGarage.setNumberOfTakenPlaces(2);
        expectedGarage.setSize(expectedPlaces.size());

        List<Garage> expectedGarageList = List.of(expectedGarage);

        // test
        when(mockedGarageRepository.findAll()).thenReturn(testGarageList);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        List<Garage> resultGarageList = garageService.getAll();

        assertEquals(expectedGarageList, resultGarageList);
    }

    @Test
    void whenGetByOrderIdCalledInCaseOfExistingGarage_ThenGetGarageWithPlaceWhichContainedOrderByIdAndWithSetUpPlaces() {
        // test order
        Order testOrder = new Order();
        testOrder.setId(1L);

        // test places
        List<Order> testPlaces = new ArrayList<>(3);
        testPlaces.add(null);
        testPlaces.add(testOrder);
        testPlaces.add(null);

        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setPlaces(testPlaces);
        testGarage.setNumberOfTakenPlaces(1);
        testGarage.setSize(testPlaces.size());

        OrderGarage testOrderGarage = new OrderGarage();
        testOrderGarage.setOrder(testOrder);
        testOrderGarage.setGarage(testGarage);
        testOrderGarage.setPlace(1);
        List<OrderGarage> testOrderGaragesList = List.of(testOrderGarage);

        // expected result
        Garage expectedGarage = new Garage();
        expectedGarage.setId(1L);
        List<Order> expectedPlaces = new ArrayList<>(testPlaces.size());
        expectedPlaces.addAll(testPlaces);
        expectedGarage.setPlaces(expectedPlaces);
        expectedGarage.setNumberOfTakenPlaces(1);
        expectedGarage.setSize(expectedPlaces.size());

        // test
        when(mockedGarageRepository.findByOrderId(1L)).thenReturn(testGarage);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(testOrderGaragesList);

        Garage resultGarage = garageService.getByOrderId(1L);

        assertEquals(expectedGarage, resultGarage);
    }

    @Test
    void whenGetByOrderIdCalledInCaseOfNotExistingGarage_ThenGetNull() {
        // test
        when(mockedGarageRepository.findByOrderId(1L)).thenReturn(null);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(null);

        Garage resultGarage = garageService.getByOrderId(1L);

        assertNull(resultGarage);
    }

    @Test
    void whenGetPlacesFilteredByAvailableCalledForTakenPlaces_ThenGetPlacesFilteredByTakenPlaces() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        places.add(null);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size + 1);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        List<GarageWithPlaceNumbersDto> expectedDto = List.of(
                new GarageWithPlaceNumbersDto(1L, List.of(0, 1))
        );

        // test
        when(mockedGarageRepository.findAll()).thenReturn(testGarageList);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        List<GarageWithPlaceNumbersDto> resultDto = garageService.getPlacesFilteredByAvailability(true);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    void whenGetPlacesFilteredByAvailableCalledForFreedPlaces_ThenGetPlacesFilteredByFreedPlaces() {
        // test garage
        Garage testGarage = new Garage();
        testGarage.setId(1L);
        places.add(null);
        testGarage.setPlaces(places);
        testGarage.setNumberOfTakenPlaces(2);
        testGarage.setSize(size + 1);

        List<Garage> testGarageList = List.of(testGarage);

        // expected result
        List<GarageWithPlaceNumbersDto> expectedDto = List.of(
                new GarageWithPlaceNumbersDto(1L, List.of(2))
        );

        // test
        when(mockedGarageRepository.findAll()).thenReturn(testGarageList);
        when(mockedOrderGarageRepository.findAllByGarageIdAndOrderStatusList(eq(1L), anyList())).thenReturn(orderGarageList);

        List<GarageWithPlaceNumbersDto> resultDto = garageService.getPlacesFilteredByAvailability(false);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    void whenGetNumberOfFreePlacesByDateCalled_ThenGetIt() {
        // test orders
        Order testOrder1 = new Order();
        testOrder1.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder1.setTimeOfCompletion(LocalDateTime.of(2020, 1, 10, 0, 0));

        Order testOrder2 = new Order();
        testOrder2.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder2.setTimeOfCompletion(LocalDateTime.of(2020, 1, 10, 0, 0));

        Order testOrder3 = new Order();
        testOrder3.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder3.setTimeOfCompletion(LocalDateTime.of(2020, 1, 11, 0, 0));

        List<Order> testOrderList = List.of(
                testOrder1,
                testOrder2,
                testOrder3
        );

        List<Order> testActiveOrderListByDate = List.of(
                testOrder3
        );

        // test masters
        List<Master> testActiveMasterListByDate = List.of(
                mock(Master.class)
        );

        List<Master> testMasterList = List.of(
                mock(Master.class),
                mock(Master.class)
        );

        // test datetime
        LocalDateTime testDateTime = LocalDateTime.of(2020, 1, 11, 0, 0);

        // test
        when(mockedOrderMasterRepository.findAllMastersByTimeOfCompletionAndOrderStatusList(
                testDateTime,
                testDateTime.plusDays(1),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED))
        ).thenReturn(testActiveMasterListByDate);

        when(mockedOrderRepository.findAllByTimeOfCompletionAndStatuses(
                testDateTime,
                testDateTime.plusDays(1),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED))
        ).thenReturn(testActiveOrderListByDate);

        when(mockedGarageRepository.sumOfSizeOfAllGarages()).thenReturn(2);

        when(mockedMasterRepository.findAll()).thenReturn(testMasterList);

        int result = garageService.getNumberOfFreePlacesByDate(LocalDate.of(2020, 1, 11));

        assertEquals(1, result);
    }

    @Test
    void whenGetNearestDateCalled_ThenGetItDate() {
        // test orders
        Order testOrder1 = new Order();
        testOrder1.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder1.setTimeOfCompletion(LocalDateTime.now());

        Order testOrder2 = new Order();
        testOrder2.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder2.setTimeOfCompletion(LocalDateTime.now());

        Order testOrder3 = new Order();
        testOrder3.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder3.setTimeOfCompletion(LocalDateTime.now().plusDays(1));

        List<Order> testOrderList = List.of(
                testOrder1,
                testOrder2,
                testOrder3
        );

        List<Order> testActiveOrderListByDateWithoutFreePlace = List.of(
                testOrder1,
                testOrder2
        );

        List<Order> testActiveOrderListByDateWithFreePlace = List.of(
                testOrder3
        );

        // test masters
        List<Master> testActiveMasterListByDateWithoutFreePlace = List.of(
                mock(Master.class),
                mock(Master.class)
        );

        List<Master> testActiveMasterListByDateWithFreePlace = List.of(
                mock(Master.class)
        );

        List<Master> testMasterList = List.of(
                mock(Master.class),
                mock(Master.class)
        );

        // test datetime
        LocalDateTime testDateTimeWithoutFreePlace = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0, 0));
        LocalDateTime testDateTimeWithFreePlace = testDateTimeWithoutFreePlace.plusDays(1);

        // test
        when(mockedOrderMasterRepository.findAllMastersByTimeOfCompletionAndOrderStatusList(
                testDateTimeWithoutFreePlace,
                testDateTimeWithoutFreePlace.plusDays(1),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED))
        ).thenReturn(testActiveMasterListByDateWithoutFreePlace);

        when(mockedOrderRepository.findAllByTimeOfCompletionAndStatuses(
                testDateTimeWithoutFreePlace,
                testDateTimeWithoutFreePlace.plusDays(1),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED))
        ).thenReturn(testActiveOrderListByDateWithoutFreePlace);

        when(mockedOrderMasterRepository.findAllMastersByTimeOfCompletionAndOrderStatusList(
                testDateTimeWithFreePlace,
                testDateTimeWithFreePlace.plusDays(1),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED))
        ).thenReturn(testActiveMasterListByDateWithFreePlace);

        when(mockedOrderRepository.findAllByTimeOfCompletionAndStatuses(
                testDateTimeWithFreePlace,
                testDateTimeWithFreePlace.plusDays(1),
                List.of(OrderStatusEnum.IN_PROCESS, OrderStatusEnum.POSTPONED))
        ).thenReturn(testActiveOrderListByDateWithFreePlace);

        when(mockedGarageRepository.sumOfSizeOfAllGarages()).thenReturn(2);

        when(mockedMasterRepository.findAll()).thenReturn(testMasterList);

        LocalDate resultDate = garageService.getNearestDate();

        assertEquals(LocalDate.now().plusDays(1), resultDate);
    }

}
