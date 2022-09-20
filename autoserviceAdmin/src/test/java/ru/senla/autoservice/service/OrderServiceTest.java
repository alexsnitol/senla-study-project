package ru.senla.autoservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.config.TestConfig;
import ru.senla.autoservice.dto.TakenPlaceDto;
import ru.senla.autoservice.exception.BusinessRuntimeException;
import ru.senla.autoservice.exception.order.MasterInOrderNotFoundException;
import ru.senla.autoservice.exception.order.TimeOfBeginInOrderNotSetException;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
class OrderServiceTest {

    @Autowired
    IOrderService orderService;
    @Autowired
    IOrderRepository mockedOrderRepository;
    @Autowired
    IMasterRepository mockedMasterRepository;
    @Autowired
    @Qualifier("mockedGarageService")
    IGarageService mockedGarageService;
    
    Order order;
    List<Master> masterList;
    LocalDateTime TIME_OF_CREATED = LocalDateTime.now();

    List<OrderStatusEnum> ACTIVE_STATUSES = new ArrayList<>(List.of(
            OrderStatusEnum.IN_PROCESS,
            OrderStatusEnum.POSTPONED
    ));

    List<OrderStatusEnum> INACTIVE_STATUSES = new ArrayList<>(List.of(
            OrderStatusEnum.CANCELLED,
            OrderStatusEnum.COMPLETED,
            OrderStatusEnum.PAUSED
    ));
    
    @BeforeEach
    void init() {
        order = new Order();
        order.setTimeOfCreated(TIME_OF_CREATED);
        
        masterList = List.of(mock(Master.class), mock(Master.class));
        
        order.setMasters(masterList);

        ACTIVE_STATUSES = new ArrayList<>(List.of(
                OrderStatusEnum.IN_PROCESS,
                OrderStatusEnum.POSTPONED
        ));

        INACTIVE_STATUSES = new ArrayList<>(List.of(
                OrderStatusEnum.CANCELLED,
                OrderStatusEnum.COMPLETED,
                OrderStatusEnum.PAUSED
        ));
    }

    @AfterEach
    void clearInvocationsInMocked() {
        Mockito.clearInvocations(
                mockedOrderRepository,
                mockedMasterRepository,
                mockedGarageService
        );
    }
    
    @Test
    void whenAddAndTakePlaceCalled_ThenAddOrderAndTakePlaceAndReturnTakenPlaceDto() {
        // test order
        Order testOrder = new Order();
        testOrder.setId(1L);
        
        // test dto
        TakenPlaceDto testTakenPlaceDto = mock(TakenPlaceDto.class);
        
        // expected dto
        TakenPlaceDto expectedTakenPlaceDto = testTakenPlaceDto;

        // test
        when(mockedGarageService.takePlace(testOrder.getId())).thenReturn(testTakenPlaceDto);
        
        TakenPlaceDto resultDto = orderService.addAndTakePlace(testOrder);
        
        verify(mockedOrderRepository, times(1)).create(testOrder);
        verify(mockedGarageService, times(1)).takePlace(testOrder.getId());
        
        assertEquals(expectedTakenPlaceDto, resultDto);
    }
    
    @Test
    void whenDeleteByIdAndFreePlaceCalledForOrderIsExistAndStatusIsActive_ThenSetDeletedStatusInOrderAndFreePlaceInGarageAndReducedNumberOfActiveOrdersOfMastersByOrder() {
        Order testOrder = new Order();

        ACTIVE_STATUSES.forEach(activeStatus -> {
            // test order
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);

            Master testMaster1 = new Master();
            testMaster1.setNumberOfActiveOrders(1);

            Master testMaster2 = new Master();
            testMaster2.setNumberOfActiveOrders(2);

            testOrder.setMasters(
                    List.of(testMaster1, testMaster2)
            );

            testOrder.setStatus(activeStatus);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);

            Master expectedMaster1 = new Master();
            expectedMaster1.setNumberOfActiveOrders(0);

            Master expectedMaster2 = new Master();
            expectedMaster2.setNumberOfActiveOrders(1);

            expectedOrder.setMasters(
                    List.of(expectedMaster1, expectedMaster2)
            );

            expectedOrder.setStatus(OrderStatusEnum.DELETED);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

            orderService.deleteByIdAndFreePlace(1L);

            assertEquals(expectedOrder, testOrder);
        });

        verify(mockedOrderRepository, times(2)).update(testOrder);
        verify(mockedGarageService, times(2)).freePlaceByOrderId(1L);
    }

    @Test
    void whenDeleteByIdAndFreePlaceCalledForOrderIsExistAndStatusIsInactive_ThenSetDeletedStatusInOrderAndFreePlaceInGarageAndReducedNumberOfActiveOrdersOfMastersByOrder() {
        Order testOrder = new Order();

        INACTIVE_STATUSES.forEach(inactiveStatus -> {
            // test order
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);

            Master testMaster1 = new Master();
            testMaster1.setNumberOfActiveOrders(1);

            Master testMaster2 = new Master();
            testMaster2.setNumberOfActiveOrders(2);

            testOrder.setMasters(
                    List.of(testMaster1, testMaster2)
            );

            testOrder.setStatus(inactiveStatus);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);

            Master expectedMaster1 = new Master();
            expectedMaster1.setNumberOfActiveOrders(1);

            Master expectedMaster2 = new Master();
            expectedMaster2.setNumberOfActiveOrders(2);

            expectedOrder.setMasters(
                    List.of(expectedMaster1, expectedMaster2)
            );

            expectedOrder.setStatus(OrderStatusEnum.DELETED);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

            orderService.deleteByIdAndFreePlace(1L);

            assertEquals(expectedOrder, testOrder);
        });

        verify(mockedOrderRepository, times(3)).update(testOrder);
        verify(mockedGarageService, never()).freePlaceByOrderId(1L);
    }

    @Test
    void whenDeleteByIdAndFreePlaceCalledForOrderIsExistAndItStatusIsDeleted_ThenDoNothing() {
        // test order
        Order testOrder = new Order();
        testOrder.setTimeOfCreated(TIME_OF_CREATED);
        testOrder.setId(1L);

        Master testMaster1 = new Master();
        testMaster1.setNumberOfActiveOrders(1);

        Master testMaster2 = new Master();
        testMaster2.setNumberOfActiveOrders(2);

        testOrder.setMasters(
                List.of(testMaster1, testMaster2)
        );

        testOrder.setStatus(OrderStatusEnum.DELETED);

        // expected order
        Order expectedOrder = new Order();
        expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder.setId(1L);

        Master expectedMaster1 = new Master();
        expectedMaster1.setNumberOfActiveOrders(1);

        Master expectedMaster2 = new Master();
        expectedMaster2.setNumberOfActiveOrders(2);

        expectedOrder.setMasters(
                List.of(expectedMaster1, expectedMaster2)
        );

        expectedOrder.setStatus(OrderStatusEnum.DELETED);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        orderService.deleteByIdAndFreePlace(1L);

        verify(mockedGarageService, never()).freePlaceByOrderId(1L);
        verify(mockedOrderRepository, never()).update(testOrder);
        assertEquals(expectedOrder, testOrder);
    }
    
    @Test
    void whenDeleteByIdAndFreePlaceCalledForOrderIsNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.deleteByIdAndFreePlace(1L));
    }

    @Test
    void whenSetTimeOfCompletionCalledWithMinutesAndTimeOfBeginIsExist_ThenPlusMinutesToTimeOfBeginAndSetItInTimeOfCompletion() throws TimeOfBeginInOrderNotSetException {
        // test order
        Order testOrder = new Order();
        testOrder.setTimeOfCreated(TIME_OF_CREATED);
        testOrder.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));

        // expected order
        Order expectedOrder = new Order();
        expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));
        expectedOrder.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 1, 0));

        // test
        orderService.setTimeOfCompletion(testOrder, 60);

        assertEquals(expectedOrder, testOrder);
    }

    @Test
    void whenSetTimeOfCompletionCalledWithMinutesAndTimeOfBeginIsNotExist_ThenThrowTimeOfBeginInOrderNotSetException() {
        // test order
        Order testOrder = new Order();
        testOrder.setTimeOfBegin(null);

        // test
        assertThrows(TimeOfBeginInOrderNotSetException.class, () -> orderService.setTimeOfCompletion(testOrder, 60));
    }

    @Test
    void whenSetTimeOfCompletionCalledWithNegativeMinutesAndTimeOfBeginIsExist_ThenThrowIllegalArgumentException() {
        // test order
        Order testOrder = new Order();
        testOrder.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));

        // test
        assertThrows(IllegalArgumentException.class, () -> orderService.setTimeOfCompletion(testOrder, -60));
    }

    @Test
    void whenSetTimeOfCompletionInOrderByIdAndUpdateCalledWithMinutesAndTimeOfBeginIsExist_ThenPlusMinutesToTimeOfBeginAndSetItInTimeOfCompletionAndUpdate() throws TimeOfBeginInOrderNotSetException {
        // test order
        Order testOrder = new Order();
        testOrder.setTimeOfCreated(TIME_OF_CREATED);
        testOrder.setId(1L);
        testOrder.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));

        // expected order
        Order expectedOrder = new Order();
        expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder.setId(1L);
        expectedOrder.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));
        expectedOrder.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 1, 0));

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        orderService.setTimeOfCompletionInOrderByIdAndUpdate(1L, 60);

        verify(mockedOrderRepository, times(1)).update(testOrder);
        assertEquals(expectedOrder, testOrder);
    }

    @Test
    void whenSetTimeOfCompletionInOrderByIdAndUpdateCalledWithMinutesAndTimeOfBeginIsNotExist_ThenThrowBusinessRuntimeException() {
        // test order
        Order testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setTimeOfBegin(null);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        assertThrows(BusinessRuntimeException.class, () -> orderService.setTimeOfCompletionInOrderByIdAndUpdate(1L, 60));
    }

    @Test
    void whenSetTimeOfCompletionInOrderByIdAndUpdateCalledWithNegativeMinutesAndTimeOfBeginIsExist_ThenThrowIllegalArgumentException() {
        // test order
        Order testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        assertThrows(IllegalArgumentException.class, () -> orderService.setTimeOfCompletionInOrderByIdAndUpdate(1L, -60));
    }

    @Test
    void whenSetTimeOfCompletionInOrderByIdAndUpdateCalledInCaseOrderIsNotExist_ThenThrowEntityNotFoundException() {
        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.setTimeOfCompletionInOrderByIdAndUpdate(1L, 60));
    }

    @Test
    void whenSetStatusCalledInCaseOfIfSettingInactiveStatusOfOrderToActive_ThenTakePlaceInGarageForItOrderAndIncreaseNumberOfActiveOrdersOfMastersByOrderByOneAndSetActiveStatus() {
        INACTIVE_STATUSES.add(OrderStatusEnum.DELETED);

        Order testOrder = new Order();

        INACTIVE_STATUSES.forEach(inactiveStatus -> {
            ACTIVE_STATUSES.forEach(activeStatus -> {
                // test order
                testOrder.setTimeOfCreated(TIME_OF_CREATED);
                testOrder.setId(1L);

                Master testMaster1 = new Master();
                testMaster1.setNumberOfActiveOrders(0);

                Master testMaster2 = new Master();
                testMaster2.setNumberOfActiveOrders(1);

                testOrder.setMasters(
                        List.of(testMaster1, testMaster2)
                );

                testOrder.setStatus(inactiveStatus);

                // expected order
                Order expectedOrder = new Order();
                expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
                expectedOrder.setId(1L);

                Master expectedMaster1 = new Master();
                expectedMaster1.setNumberOfActiveOrders(1);

                Master expectedMaster2 = new Master();
                expectedMaster2.setNumberOfActiveOrders(2);

                expectedOrder.setMasters(
                        List.of(expectedMaster1, expectedMaster2)
                );

                expectedOrder.setStatus(activeStatus);

                // test
                orderService.setStatus(testOrder, activeStatus);

                assertEquals(expectedOrder, testOrder);
            });
        });

        verify(mockedGarageService, times(INACTIVE_STATUSES.size() * ACTIVE_STATUSES.size())).takePlace(1L);
    }

    @Test
    void whenSetStatusCalledInCaseOfIfSettingActiveStatusOfOrderToInactive_ThenFreePlaceInGarageByOrderIdAndReduceNumberOfActiveOrdersOfMastersByOrderByOneAndSetInactiveStatus() {
        Order testOrder = new Order();

        ACTIVE_STATUSES.forEach(activeStatus -> {
            INACTIVE_STATUSES.forEach(inactiveStatus -> {
                // test order
                testOrder.setTimeOfCreated(TIME_OF_CREATED);
                testOrder.setId(1L);

                Master testMaster1 = new Master();
                testMaster1.setNumberOfActiveOrders(1);

                Master testMaster2 = new Master();
                testMaster2.setNumberOfActiveOrders(2);

                testOrder.setMasters(
                        List.of(testMaster1, testMaster2)
                );

                testOrder.setStatus(activeStatus);

                // expected order
                Order expectedOrder = new Order();
                expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
                expectedOrder.setId(1L);

                Master expectedMaster1 = new Master();
                expectedMaster1.setNumberOfActiveOrders(0);

                Master expectedMaster2 = new Master();
                expectedMaster2.setNumberOfActiveOrders(1);

                expectedOrder.setMasters(
                        List.of(expectedMaster1, expectedMaster2)
                );

                expectedOrder.setStatus(inactiveStatus);

                // test
                orderService.setStatus(testOrder, inactiveStatus);

                assertEquals(expectedOrder, testOrder);
            });
        });

        verify(mockedGarageService, times(ACTIVE_STATUSES.size() * INACTIVE_STATUSES.size())).freePlaceByOrderId(1L);
    }

    @Test
    void whenSetStatusCalledInCaseOfIfSettingAnyStatusOfOrderToDeleted_ThenThrowBusinessRuntimeException() {
        List<OrderStatusEnum> allStatuses = Arrays.stream(OrderStatusEnum.values()).collect(Collectors.toList());

        Order testOrder = new Order();

        allStatuses.forEach(status -> {
            testOrder.setStatus(status);
            assertThrows(BusinessRuntimeException.class, () -> orderService.setStatus(testOrder, OrderStatusEnum.DELETED));
        });
    }


    @Test
    void whenSetStatusInOrderByIdAndUpdateCalledInCaseOfIfSettingInactiveStatusOfOrderToActive_ThenTakePlaceInGarageForItOrderAndIncreaseNumberOfActiveOrdersOfMastersByOrderByOneAndSetActiveStatusAndUpdate() {
        INACTIVE_STATUSES.add(OrderStatusEnum.DELETED);

        Order testOrder = new Order();

        INACTIVE_STATUSES.forEach(inactiveStatus -> {
            ACTIVE_STATUSES.forEach(activeStatus -> {
                // test order
                testOrder.setTimeOfCreated(TIME_OF_CREATED);
                testOrder.setId(1L);

                Master testMaster1 = new Master();
                testMaster1.setNumberOfActiveOrders(0);

                Master testMaster2 = new Master();
                testMaster2.setNumberOfActiveOrders(1);

                testOrder.setMasters(
                        List.of(testMaster1, testMaster2)
                );

                testOrder.setStatus(inactiveStatus);

                // expected order
                Order expectedOrder = new Order();
                expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
                expectedOrder.setId(1L);

                Master expectedMaster1 = new Master();
                expectedMaster1.setNumberOfActiveOrders(1);

                Master expectedMaster2 = new Master();
                expectedMaster2.setNumberOfActiveOrders(2);

                expectedOrder.setMasters(
                        List.of(expectedMaster1, expectedMaster2)
                );

                expectedOrder.setStatus(activeStatus);

                // test
                when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

                orderService.setStatusInOrderByIdAndUpdate(1L, activeStatus);

                assertEquals(expectedOrder, testOrder);
            });
        });

        verify(mockedGarageService, times(INACTIVE_STATUSES.size() * ACTIVE_STATUSES.size())).takePlace(1L);
        verify(mockedOrderRepository, times(INACTIVE_STATUSES.size() * ACTIVE_STATUSES.size())).update(testOrder);
    }

    @Test
    void whenSetStatusInOrderByIdAndUpdateCalledInCaseOfIfSettingActiveStatusOfOrderToInactive_ThenFreePlaceInGarageByOrderIdAndReduceNumberOfActiveOrdersOfMastersByOrderByOneAndSetInactiveStatusAndUpdate() {
        Order testOrder = new Order();

        ACTIVE_STATUSES.forEach(activeStatus -> {
            INACTIVE_STATUSES.forEach(inactiveStatus -> {
                // test order
                testOrder.setTimeOfCreated(TIME_OF_CREATED);
                testOrder.setId(1L);

                Master testMaster1 = new Master();
                testMaster1.setNumberOfActiveOrders(1);

                Master testMaster2 = new Master();
                testMaster2.setNumberOfActiveOrders(2);

                testOrder.setMasters(
                        List.of(testMaster1, testMaster2)
                );

                testOrder.setStatus(activeStatus);

                // expected order
                Order expectedOrder = new Order();
                expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
                expectedOrder.setId(1L);

                Master expectedMaster1 = new Master();
                expectedMaster1.setNumberOfActiveOrders(0);

                Master expectedMaster2 = new Master();
                expectedMaster2.setNumberOfActiveOrders(1);

                expectedOrder.setMasters(
                        List.of(expectedMaster1, expectedMaster2)
                );

                expectedOrder.setStatus(inactiveStatus);

                // test
                when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

                orderService.setStatusInOrderByIdAndUpdate(1L, inactiveStatus);

                assertEquals(expectedOrder, testOrder);
            });
        });

        verify(mockedGarageService, times(ACTIVE_STATUSES.size() * INACTIVE_STATUSES.size())).freePlaceByOrderId(1L);
        verify(mockedOrderRepository, times(ACTIVE_STATUSES.size() * INACTIVE_STATUSES.size())).update(testOrder);
    }

    @Test
    void whenSetStatusInOrderByIdAndUpdateCalledInCaseOfIfSettingAnyStatusOfOrderToDeleted_ThenThrowBusinessRuntimeException() {
        List<OrderStatusEnum> allStatuses = Arrays.stream(OrderStatusEnum.values()).collect(Collectors.toList());

        Order testOrder = new Order();

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        allStatuses.forEach(status -> {
            testOrder.setStatus(status);
            assertThrows(BusinessRuntimeException.class, () -> orderService.setStatusInOrderByIdAndUpdate(1L, OrderStatusEnum.DELETED));
            verify(mockedOrderRepository, never()).update(testOrder);
        });
    }

    @Test
    void whenSetStatusInOrderByIdAndUpdateCalledInCaseOfIfOrderNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.setStatusInOrderByIdAndUpdate(1L, OrderStatusEnum.COMPLETED));
    }

    @Test
    void whenAssignMasterByIdCalledForOrderWithActiveStatus_ThenIncreaseNumberOfActiveOrdersOfMasterByOneAndSetMasterByIdToOrder() {
        ACTIVE_STATUSES.forEach(activeStatus -> {
            // test master
            Master testMaster = new Master();
            testMaster.setNumberOfActiveOrders(0);

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setNumberOfActiveOrders(1);

            List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster));

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setStatus(activeStatus);
            testOrder.setMasters(new ArrayList<>());

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(activeStatus);

            // test
            when(mockedMasterRepository.findById(1L)).thenReturn(testMaster);

            orderService.assignMasterById(testOrder, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
        });
    }

    @Test
    void whenAssignMasterByIdCalledForOrderWithInactiveStatus_ThenSetMasterByIdToOrder() {
        INACTIVE_STATUSES.add(OrderStatusEnum.DELETED);

        INACTIVE_STATUSES.forEach(inactiveStatus -> {
            // test master
            Master testMaster = new Master();
            testMaster.setNumberOfActiveOrders(0);

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setNumberOfActiveOrders(0);

            List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster));

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setMasters(new ArrayList<>());
            testOrder.setStatus(inactiveStatus);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(inactiveStatus);

            // test
            when(mockedMasterRepository.findById(1L)).thenReturn(testMaster);

            orderService.assignMasterById(testOrder, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
        });
    }

    @Test
    void whenAssignMasterByIdCalledInCaseOfIfMasterNotExist_ThenThrowEntityNotFoundException() {
        when(mockedMasterRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.assignMasterById(mock(Order.class), 1L));
    }


    @Test
    void whenAssignMasterByIdInOrderByIdAndUpdateCalledForOrderWithActiveStatus_ThenIncreaseNumberOfActiveOrdersOfMasterByOneAndSetMasterByIdToOrderAndUpdate() {
        for (OrderStatusEnum activeStatus : ACTIVE_STATUSES) {
            // test master
            Master testMaster = new Master();
            testMaster.setNumberOfActiveOrders(0);

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setNumberOfActiveOrders(1);

            List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster));

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setStatus(activeStatus);
            testOrder.setMasters(new ArrayList<>());

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(activeStatus);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);
            when(mockedMasterRepository.findById(1L)).thenReturn(testMaster);

            orderService.assignMasterByIdInOrderByIdAndUpdate(1L, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
            verify(mockedOrderRepository, times(1)).update(testOrder);
        }
    }

    @Test
    void whenAssignMasterByIdInOrderByIdAndUpdateCalledForOrderWithInactiveStatus_ThenSetMasterByIdToOrderAndUpdate() {
        INACTIVE_STATUSES.add(OrderStatusEnum.DELETED);

        for (OrderStatusEnum inactiveStatus : INACTIVE_STATUSES) {
            // test master
            Master testMaster = new Master();
            testMaster.setNumberOfActiveOrders(0);

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setNumberOfActiveOrders(0);

            List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster));

            // test order
            Order testOrder = new Order();
            testOrder.setId(1L);
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setMasters(new ArrayList<>());
            testOrder.setStatus(inactiveStatus);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(inactiveStatus);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);
            when(mockedMasterRepository.findById(1L)).thenReturn(testMaster);

            orderService.assignMasterByIdInOrderByIdAndUpdate(1L, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
            verify(mockedOrderRepository, times(1)).update(testOrder);
        }
    }

    @Test
    void whenAssignMasterByIdInOrderByIdAndUpdateCalledInCaseOfIfMasterNotExist_ThenThrowEntityNotFoundException() {
        when(mockedMasterRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.assignMasterByIdInOrderByIdAndUpdate(1L, 1L));
    }

    @Test
    void whenAssignMasterByIdInOrderByIdAndUpdateCalledInCaseOfIfOrderNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.assignMasterByIdInOrderByIdAndUpdate(1L, 1L));
    }


    @Test
    void whenRemoveMasterByIdCalledForOrderWithActiveStatus_ThenReduceNumberOfActiveOrdersOfMasterByOneAndRemoveMasterByIdFromOrder() throws MasterInOrderNotFoundException {
        for (OrderStatusEnum activeStatus : ACTIVE_STATUSES) {
            // test master
            Master testMaster = new Master();
            testMaster.setId(1L);
            testMaster.setNumberOfActiveOrders(1);

            List<Master> testMasterList = new ArrayList<>(List.of(testMaster));

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setId(1L);
            expectedMaster.setNumberOfActiveOrders(0);

            List<Master> expectedMasterList = new ArrayList<>();

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setStatus(activeStatus);
            testOrder.setMasters(testMasterList);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(activeStatus);

            // test
            when(mockedMasterRepository.findById(1L)).thenReturn(testMaster);

            orderService.removeMasterById(testOrder, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
        }
    }

    @Test
    void whenRemoveMasterByIdCalledForOrderWithInactiveStatus_ThenRemoveMasterByIdFromOrder() throws MasterInOrderNotFoundException {
        INACTIVE_STATUSES.add(OrderStatusEnum.DELETED);

        for (OrderStatusEnum inactiveStatus : INACTIVE_STATUSES) {
            // test master
            Master testMaster = new Master();
            testMaster.setId(1L);
            testMaster.setNumberOfActiveOrders(0);

            List<Master> testMasterList = new ArrayList<>(List.of(testMaster));

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setId(1L);
            expectedMaster.setNumberOfActiveOrders(0);

            List<Master> expectedMasterList = new ArrayList<>();

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setMasters(testMasterList);
            testOrder.setStatus(inactiveStatus);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(inactiveStatus);

            // test
            orderService.removeMasterById(testOrder, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
        }
    }

    @Test
    void whenRemoveMasterByIdCalledInCaseOfIfMasterNotExistInOrder_ThenThrowMasterInOrderNotFoundException() {
        // test master
        Master testMaster = new Master();
        testMaster.setId(2L);

        List<Master> testMasterList = new ArrayList<>();
        testMasterList.add(testMaster);

        // test order
        Order testOrder = new Order();
        testOrder.setMasters(testMasterList);

        // test
        assertThrows(MasterInOrderNotFoundException.class, () -> orderService.removeMasterById(testOrder, 1L));
    }


    @Test
    void whenRemoveMasterByIdInOrderByIdAndUpdateCalledForOrderWithActiveStatus_ThenReduceNumberOfActiveOrdersOfMasterByOneAndRemoveMasterByIdFromOrderAndUpdate() throws MasterInOrderNotFoundException {
        for (OrderStatusEnum activeStatus : ACTIVE_STATUSES) {
            // test master
            Master testMaster = new Master();
            testMaster.setId(1L);
            testMaster.setNumberOfActiveOrders(1);

            List<Master> testMasterList = new ArrayList<>(List.of(testMaster));

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setId(1L);
            expectedMaster.setNumberOfActiveOrders(0);

            List<Master> expectedMasterList = new ArrayList<>();

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setStatus(activeStatus);
            testOrder.setMasters(testMasterList);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(activeStatus);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);
            when(mockedMasterRepository.findById(1L)).thenReturn(testMaster);

            orderService.removeMasterByIdInOrderByIdAndUpdate(1L, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
            verify(mockedOrderRepository, times(1)).update(testOrder);
        }
    }

    @Test
    void whenRemoveMasterByIdInOrderByIdAndUpdateCalledForOrderWithInactiveStatus_ThenRemoveMasterByIdFromOrderAndUpdate() throws MasterInOrderNotFoundException {
        INACTIVE_STATUSES.add(OrderStatusEnum.DELETED);

        for (OrderStatusEnum inactiveStatus : INACTIVE_STATUSES) {
            // test master
            Master testMaster = new Master();
            testMaster.setId(1L);
            testMaster.setNumberOfActiveOrders(0);

            List<Master> testMasterList = new ArrayList<>(List.of(testMaster));

            // expected master
            Master expectedMaster = new Master();
            expectedMaster.setId(1L);
            expectedMaster.setNumberOfActiveOrders(0);

            List<Master> expectedMasterList = new ArrayList<>();

            // test order
            Order testOrder = new Order();
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setId(1L);
            testOrder.setMasters(testMasterList);
            testOrder.setStatus(inactiveStatus);

            // expected order
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setId(1L);
            expectedOrder.setMasters(expectedMasterList);
            expectedOrder.setStatus(inactiveStatus);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

            orderService.removeMasterByIdInOrderByIdAndUpdate(1L, 1L);

            assertEquals(testMaster, expectedMaster);
            assertEquals(testOrder, expectedOrder);
            verify(mockedOrderRepository, times(1)).update(testOrder);
        }
    }

    @Test
    void whenRemoveMasterByIdInOrderByIdAndUpdateCalledInCaseOfIfOrderNotExist_ThenThrowEntityNotFoundException() {
        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.removeMasterByIdInOrderByIdAndUpdate(1L, 1L));
    }

    @Test
    void whenRemoveMasterByIdInOrderByIdAndUpdateCalledInCaseOfIfMasterNotExistInOrder_ThenThrowBusinessRunTimeException() {
        // test master
        Master testMaster = new Master();
        testMaster.setId(2L);

        List<Master> testMasterList = new ArrayList<>();
        testMasterList.add(testMaster);

        // test order
        Order testOrder = new Order();
        testOrder.setMasters(testMasterList);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        assertThrows(BusinessRuntimeException.class, () -> orderService.removeMasterByIdInOrderByIdAndUpdate(1L, 1L));
        verify(mockedOrderRepository, never()).update(testOrder);
    }
    
    @Test
    void whenShiftTimeOfCompletionWithUpdateCalled_ThenShiftTimeOfCompletionInOrderAndShiftTimeOfBeginAndTimeOfCompletionOfAllOrdersWithStatusPostponedOfMastersOfItOrder() {
        // test masters
        Master testMaster1 = mock(Master.class);
        Master testMaster2 = mock(Master.class);
        Master testMaster3 = mock(Master.class);
        Master testMaster4 = mock(Master.class);
        
        List<Master> testMasterList1 = new ArrayList<>(List.of(testMaster1, testMaster2, testMaster3));
        List<Master> testMasterList2 = new ArrayList<>(List.of(testMaster1));
        List<Master> testMasterList3 = new ArrayList<>(List.of(testMaster2, testMaster3));
        List<Master> testMasterList4 = new ArrayList<>(List.of(testMaster4));
        
        // test orders
        Order testOrder1 = new Order();
        testOrder1.setTimeOfCreated(TIME_OF_CREATED);
        testOrder1.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder1.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 0, 0));
        testOrder1.setMasters(testMasterList1);
        
        Order testOrder2 = new Order();
        testOrder2.setTimeOfCreated(TIME_OF_CREATED);
        testOrder2.setStatus(OrderStatusEnum.POSTPONED);
        testOrder2.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));
        testOrder2.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 1, 0));
        testOrder2.setMasters(testMasterList2);

        Order testOrder3 = new Order();
        testOrder3.setTimeOfCreated(TIME_OF_CREATED);
        testOrder3.setStatus(OrderStatusEnum.POSTPONED);
        testOrder3.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 0));
        testOrder3.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 0));
        testOrder3.setMasters(testMasterList3);

        Order testOrder4 = new Order();
        testOrder4.setTimeOfCreated(TIME_OF_CREATED);
        testOrder4.setStatus(OrderStatusEnum.POSTPONED);
        testOrder4.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 0));
        testOrder4.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 0));
        testOrder4.setMasters(testMasterList4);

        List<Order> testOrderList = new ArrayList<>(List.of(testOrder1, testOrder2, testOrder3, testOrder4));
        List<Order> testPostponedOrderList = new ArrayList<>(List.of(testOrder2, testOrder3, testOrder4));

        // expected orders
        Order expectedOrder1 = new Order();
        expectedOrder1.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder1.setStatus(OrderStatusEnum.IN_PROCESS);
        expectedOrder1.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 0, 30));
        expectedOrder1.setMasters(testMasterList1);

        Order expectedOrder2 = new Order();
        expectedOrder2.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder2.setStatus(OrderStatusEnum.POSTPONED);
        expectedOrder2.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 30));
        expectedOrder2.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 1, 30));
        expectedOrder2.setMasters(testMasterList2);

        Order expectedOrder3 = new Order();
        expectedOrder3.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder3.setStatus(OrderStatusEnum.POSTPONED);
        expectedOrder3.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 30));
        expectedOrder3.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 30));
        expectedOrder3.setMasters(testMasterList3);

        Order expectedOrder4 = new Order();
        expectedOrder4.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder4.setStatus(OrderStatusEnum.POSTPONED);
        expectedOrder4.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 0));
        expectedOrder4.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 0));
        expectedOrder4.setMasters(testMasterList4);

        List<Order> expectedOrderList = new ArrayList<>(List.of(expectedOrder1, expectedOrder2, expectedOrder3, expectedOrder4));

        // test
        when(mockedOrderRepository.findAllByStatus(OrderStatusEnum.POSTPONED)).thenReturn(testPostponedOrderList);

        orderService.shiftTimeOfCompletionWithUpdate(testOrder1, 30);

        assertEquals(expectedOrderList, testOrderList);
        verify(mockedOrderRepository, times(1)).update(testOrder1);
        verify(mockedOrderRepository, times(1)).update(testOrder2);
        verify(mockedOrderRepository, times(1)).update(testOrder3);
        verify(mockedOrderRepository, never()).update(testOrder4);
    }

    @Test
    void whenShiftTimeOfCompletionWithUpdateCalledInCaseOfIfShiftMinutesIsNegative_ThenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> orderService.shiftTimeOfCompletionWithUpdate(mock(Order.class), -1));
    }

    @Test
    void whenShiftTimeOfCompletionInOrderByIdWithUpdateCalled_ThenShiftTimeOfCompletionInOrderAndShiftTimeOfBeginAndTimeOfCompletionOfAllOrdersWithStatusPostponedOfMastersOfItOrder() {
        // test masters
        Master testMaster1 = mock(Master.class);
        Master testMaster2 = mock(Master.class);
        Master testMaster3 = mock(Master.class);
        Master testMaster4 = mock(Master.class);

        List<Master> testMasterList1 = new ArrayList<>(List.of(testMaster1, testMaster2, testMaster3));
        List<Master> testMasterList2 = new ArrayList<>(List.of(testMaster1));
        List<Master> testMasterList3 = new ArrayList<>(List.of(testMaster2, testMaster3));
        List<Master> testMasterList4 = new ArrayList<>(List.of(testMaster4));

        // test orders
        Order testOrder1 = new Order();
        testOrder1.setTimeOfCreated(TIME_OF_CREATED);
        testOrder1.setStatus(OrderStatusEnum.IN_PROCESS);
        testOrder1.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 0, 0));
        testOrder1.setMasters(testMasterList1);

        Order testOrder2 = new Order();
        testOrder2.setTimeOfCreated(TIME_OF_CREATED);
        testOrder2.setStatus(OrderStatusEnum.POSTPONED);
        testOrder2.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 0));
        testOrder2.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 1, 0));
        testOrder2.setMasters(testMasterList2);

        Order testOrder3 = new Order();
        testOrder3.setTimeOfCreated(TIME_OF_CREATED);
        testOrder3.setStatus(OrderStatusEnum.POSTPONED);
        testOrder3.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 0));
        testOrder3.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 0));
        testOrder3.setMasters(testMasterList3);

        Order testOrder4 = new Order();
        testOrder4.setTimeOfCreated(TIME_OF_CREATED);
        testOrder4.setStatus(OrderStatusEnum.POSTPONED);
        testOrder4.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 0));
        testOrder4.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 0));
        testOrder4.setMasters(testMasterList4);

        List<Order> testOrderList = new ArrayList<>(List.of(testOrder1, testOrder2, testOrder3, testOrder4));
        List<Order> testPostponedOrderList = new ArrayList<>(List.of(testOrder2, testOrder3, testOrder4));

        // expected orders
        Order expectedOrder1 = new Order();
        expectedOrder1.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder1.setStatus(OrderStatusEnum.IN_PROCESS);
        expectedOrder1.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 0, 30));
        expectedOrder1.setMasters(testMasterList1);

        Order expectedOrder2 = new Order();
        expectedOrder2.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder2.setStatus(OrderStatusEnum.POSTPONED);
        expectedOrder2.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 0, 30));
        expectedOrder2.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 1, 30));
        expectedOrder2.setMasters(testMasterList2);

        Order expectedOrder3 = new Order();
        expectedOrder3.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder3.setStatus(OrderStatusEnum.POSTPONED);
        expectedOrder3.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 30));
        expectedOrder3.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 30));
        expectedOrder3.setMasters(testMasterList3);

        Order expectedOrder4 = new Order();
        expectedOrder4.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder4.setStatus(OrderStatusEnum.POSTPONED);
        expectedOrder4.setTimeOfBegin(LocalDateTime.of(2020, 1, 1, 1, 0));
        expectedOrder4.setTimeOfCompletion(LocalDateTime.of(2020, 1, 1, 2, 0));
        expectedOrder4.setMasters(testMasterList4);

        List<Order> expectedOrderList = new ArrayList<>(List.of(expectedOrder1, expectedOrder2, expectedOrder3, expectedOrder4));

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder1);
        when(mockedOrderRepository.findAllByStatus(OrderStatusEnum.POSTPONED)).thenReturn(testPostponedOrderList);

        orderService.shiftTimeOfCompletionInOrderByIdWithUpdate(1L, 30);

        assertEquals(expectedOrderList, testOrderList);
        verify(mockedOrderRepository, times(1)).update(testOrder1);
        verify(mockedOrderRepository, times(1)).update(testOrder2);
        verify(mockedOrderRepository, times(1)).update(testOrder3);
        verify(mockedOrderRepository, never()).update(testOrder4);
    }

    @Test
    void whenShiftTimeOfCompletionInOrderByIdWithUpdateCalledInCaseOfIfShiftMinutesIsNegative_ThenThrowIllegalArgumentException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(mock(Order.class));
        assertThrows(IllegalArgumentException.class, () -> orderService.shiftTimeOfCompletionInOrderByIdWithUpdate(1L, -1));
    }

    @Test
    void whenShiftTimeOfCompletionInOrderByIdWithUpdateCalledInCaseOfIfOrderNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.shiftTimeOfCompletionInOrderByIdWithUpdate(1L, 30));
    }

    @Test
    void whenSetPriceCalled_ThenSetPrice() {
        // test order
        Order testOrder = new Order();
        testOrder.setPrice(0);

        // expected order
        Order expectedOrder = new Order();
        expectedOrder.setPrice(100);

        // test
        orderService.setPrice(testOrder, 100);

        assertEquals(expectedOrder, testOrder);
    }

    @Test
    void whenSetPriceCalledInCaseOfIfPriceIsNegative_ThenThrowIllegalArgumentException() {
        // test
        assertThrows(IllegalArgumentException.class, () -> orderService.setPrice(mock(Order.class), -1));
    }

    @Test
    void whenSetPriceInOrderByIdAndUpdateCalled_ThenSetPrice() {
        // test order
        Order testOrder = new Order();
        testOrder.setPrice(0);

        // expected order
        Order expectedOrder = new Order();
        expectedOrder.setPrice(100);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        orderService.setPriceInOrderByIdAndUpdate(1L, 100);

        assertEquals(expectedOrder, testOrder);
        verify(mockedOrderRepository, times(1)).update(testOrder);
    }

    @Test
    void whenSetPriceInOrderByIdAndUpdateCalledInCaseOfIfPriceIsNegative_ThenThrowIllegalArgumentException() {
        // test order
        Order testOrder = new Order();
        testOrder.setPrice(0);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        assertThrows(IllegalArgumentException.class, () -> orderService.setPriceInOrderByIdAndUpdate(1L, -1));
        verify(mockedOrderRepository, never()).update(testOrder);
    }

    @Test
    void whenSetPriceInOrderByIdAndUpdateCalledInCaseOfIfOrderNotExist_ThenEntityNotFoundException() {
        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.setPriceInOrderByIdAndUpdate(1L, 30));
    }
    
    @Test
    void whenDeleteByIdCalledForActiveOrder_ThenReduceNumberOfActiveOrdersOfMastersByOrderAndSetOrderStatusToDeleted() {
        ACTIVE_STATUSES.forEach(activeStatus -> {
            // test masters
            Master testMaster1 = new Master();
            testMaster1.setNumberOfActiveOrders(1);
            
            Master testMaster2 = new Master();
            testMaster2.setNumberOfActiveOrders(2);
            
            List<Master> testMasterList = new ArrayList<>(List.of(testMaster1, testMaster2));
            
            // test orders
            Order testOrder = new Order();
            testOrder.setStatus(activeStatus);
            testOrder.setMasters(testMasterList);
            
            // expected masters
            Master expectedMaster1 = new Master();
            expectedMaster1.setNumberOfActiveOrders(0);

            Master expectedMaster2 = new Master();
            expectedMaster2.setNumberOfActiveOrders(1);

            List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster1, expectedMaster2));

            // expected orders
            Order expectedOrder = new Order();
            expectedOrder.setStatus(OrderStatusEnum.DELETED);
            expectedOrder.setMasters(expectedMasterList);
            
            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

            orderService.deleteById(1L);

            assertEquals(expectedOrder, testOrder);
            verify(mockedOrderRepository, times(1)).update(testOrder);
        });
    }

    @Test
    void whenDeleteByIdCalledForInactiveOrder_ThenSetOrderStatusToDeleted() {
        Order testOrder = new Order();

        INACTIVE_STATUSES.forEach(inactiveStatus -> {
            // test masters
            Master testMaster1 = new Master();
            testMaster1.setNumberOfActiveOrders(1);

            Master testMaster2 = new Master();
            testMaster2.setNumberOfActiveOrders(2);

            List<Master> testMasterList = new ArrayList<>(List.of(testMaster1, testMaster2));

            // test orders
            testOrder.setTimeOfCreated(TIME_OF_CREATED);
            testOrder.setStatus(inactiveStatus);
            testOrder.setMasters(testMasterList);

            // expected masters
            Master expectedMaster1 = new Master();
            expectedMaster1.setNumberOfActiveOrders(1);

            Master expectedMaster2 = new Master();
            expectedMaster2.setNumberOfActiveOrders(2);

            List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster1, expectedMaster2));

            // expected orders
            Order expectedOrder = new Order();
            expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
            expectedOrder.setStatus(OrderStatusEnum.DELETED);
            expectedOrder.setMasters(expectedMasterList);

            // test
            when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

            orderService.deleteById(1L);

            assertEquals(expectedOrder, testOrder);
        });

        verify(mockedOrderRepository, times(INACTIVE_STATUSES.size())).update(testOrder);
    }

    @Test
    void whenDeleteByIdCalledForDeletedOrder_ThenDoNothing() {
        // test masters
        Master testMaster1 = new Master();
        testMaster1.setNumberOfActiveOrders(1);

        Master testMaster2 = new Master();
        testMaster2.setNumberOfActiveOrders(2);

        List<Master> testMasterList = new ArrayList<>(List.of(testMaster1, testMaster2));

        // test orders
        Order testOrder = new Order();
        testOrder.setTimeOfCreated(TIME_OF_CREATED);
        testOrder.setStatus(OrderStatusEnum.DELETED);
        testOrder.setMasters(testMasterList);

        // expected masters
        Master expectedMaster1 = new Master();
        expectedMaster1.setNumberOfActiveOrders(1);

        Master expectedMaster2 = new Master();
        expectedMaster2.setNumberOfActiveOrders(2);

        List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster1, expectedMaster2));

        // expected orders
        Order expectedOrder = new Order();
        expectedOrder.setTimeOfCreated(TIME_OF_CREATED);
        expectedOrder.setStatus(OrderStatusEnum.DELETED);
        expectedOrder.setMasters(expectedMasterList);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        orderService.deleteById(1L);

        assertEquals(expectedOrder, testOrder);
        verify(mockedOrderRepository, never()).update(testOrder);
    }

    @Test
    void whenDeleteByIdCalledInCauseOfIfOrderNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.deleteById(1L));
    }

    @Test
    void whenReducedNumberOfActiveOrdersOfMastersByOrderCalled_ThenReduceNumberOfActiveOrdersOfMastersByOrder() {
        // test masters
        Master testMaster1 = new Master();
        testMaster1.setNumberOfActiveOrders(1);

        Master testMaster2 = new Master();
        testMaster2.setNumberOfActiveOrders(2);

        List<Master> testMasterList = new ArrayList<>(List.of(testMaster1, testMaster2));

        // test orders
        Order testOrder = new Order();
        testOrder.setMasters(testMasterList);

        // expected masters
        Master expectedMaster1 = new Master();
        expectedMaster1.setNumberOfActiveOrders(0);

        Master expectedMaster2 = new Master();
        expectedMaster2.setNumberOfActiveOrders(1);

        List<Master> expectedMasterList = new ArrayList<>(List.of(expectedMaster1, expectedMaster2));

        // test
        orderService.reducedNumberOfActiveOrdersOfMastersByOrder(testOrder);

        assertEquals(expectedMasterList, testMasterList);
    }

    @Test
    void whenGetAllByTimeOfCompletionCalled_ThenGetAllByTimeOfCompletion() {
        // test orders
        List<Order> testOrderList = new ArrayList<>(List.of(mock(Order.class)));

        // test time of completion
        LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2020, 1, 2, 0, 0);

        // test
        when(mockedOrderRepository.findAllByTimeOfCompletion(from, to)).thenReturn(testOrderList);

        assertEquals(testOrderList, orderService.getAllByTimeOfCompletion(from, to));
    }

    @Test
    void whenGetAllByTimeOfCompletionCalledInCaseOfFromParameterAfterToParameter_ThenThrowIllegalArgumentException() {
        // test time of completion
        LocalDateTime from = LocalDateTime.of(2020, 1, 2, 0, 0);
        LocalDateTime to = LocalDateTime.of(2020, 1, 1, 0, 0);

        // test
        assertThrows(IllegalArgumentException.class, () -> orderService.getAllByTimeOfCompletion(from, to));
    }

    @Test
    void whenGetAllByStatusCalled_ThenGetAllByStatus() {
        // test orders
        List<Order> testOrderList = new ArrayList<>(List.of(mock(Order.class)));

        // test
        when(mockedOrderRepository.findAllByStatus(any(OrderStatusEnum.class))).thenReturn(testOrderList);

        assertEquals(testOrderList, orderService.getAllByStatus(OrderStatusEnum.IN_PROCESS));
    }

    @Test
    void whenGetAllByStatusAndMasterIdCalled_ThenGetAllByStatusAndMasterId() {
        // test orders
        List<Order> testOrderList = new ArrayList<>(List.of(mock(Order.class)));

        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(mock(Master.class));
        when(mockedOrderRepository.findAllByStatusAndMasterId(any(OrderStatusEnum.class), eq(1L))).thenReturn(testOrderList);

        assertEquals(testOrderList, orderService.getAllByStatusAndMasterId(OrderStatusEnum.IN_PROCESS, 1L));
    }

    @Test
    void whenGetAllByStatusAndMasterIdCalledInCaseOfIfMasterNotExist_ThenThrowEntityNotFoundException() {
        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> orderService.getAllByStatusAndMasterId(OrderStatusEnum.IN_PROCESS, 1L));
    }

    @Test
    void whenGetAllByMasterIdCalled_ThenGetAllMasterId() {
        // test orders
        List<Order> testOrderList = new ArrayList<>(List.of(mock(Order.class)));

        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(mock(Master.class));
        when(mockedOrderRepository.findAllByMasterId(1L)).thenReturn(testOrderList);

        assertEquals(testOrderList, orderService.getAllByMasterId(1L));
    }

    @Test
    void whenGetAllByMasterIdCalledInCaseOfIfMasterNotExist_ThenThrowEntityNotFoundException() {
        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> orderService.getAllByMasterId(1L));
    }

    @Test
    void whenGetAllByMasterIdCalledWithRequestParams_ThenCheckMasterIdInParamsAndGetAllMasterId() {
        // test orders
        List<Order> testOrderList = new ArrayList<>(List.of(mock(Order.class)));

        // test request params
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(mock(Master.class));
        when(mockedOrderRepository.findAll(requestParams)).thenReturn(testOrderList);

        assertEquals(testOrderList, orderService.getAllByMasterId("1", requestParams));
        assertTrue(requestParams.containsKey("masterId"));
    }

    @Test
    void whenGetAllByMasterIdCalledWithRequestParamsInCaseOfIfMasterNotExist_ThenThrowEntityNotFoundException() {
        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> orderService.getAllByMasterId("1", new LinkedMultiValueMap<>()));
    }

    @Test
    void whenGetMastersByOrderIdCalled_ThenGetMasterByOrderId() {
        // test masters
        List<Master> testMasterList = new ArrayList<>(List.of(mock(Master.class)));

        // test order
        Order testOrder = new Order();
        testOrder.setMasters(testMasterList);

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(testOrder);

        assertEquals(testMasterList, orderService.getMastersByOrderId(1L));
    }

    @Test
    void whenGetMastersByOrderIdCalledInCaseOfIfOrderNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderService.getMastersByOrderId(1L));
    }

    @Test
    void whenCheckRequestParamsAndGetAllCalled_ThenCheckOfNotExistMasterIdParameterInParametersAndGetAllByParameters() {
        // test orders
        List<Order> testOrderList = new ArrayList<>(List.of(mock(Order.class)));

        // test request params
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("masterId", "1");

        // test
        when(mockedOrderRepository.findAll(requestParams)).thenReturn(testOrderList);

        assertEquals(testOrderList, orderService.checkRequestParamsAndGetAll(requestParams));
        assertFalse(requestParams.containsKey("masterId"));
    }

}