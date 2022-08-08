package ru.senla.autoservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.config.TestConfig;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
class MasterServiceTest {

    @Autowired
    IMasterRepository mockedMasterRepository;
    @Autowired
    IOrderRepository mockedOrderRepository;
    @Autowired
    IMasterService masterService;

    Master master;
    List<Order> testOrderList;

    @BeforeEach
    void init() {
        master = new Master();
        master.setFirstName("firstName");
        master.setLastName("lastName");
        master.setPatronymic("patronymic");

        Order testOrder = new Order();
        testOrderList = new ArrayList<>(1);
        testOrderList.add(testOrder);

        master.setOrders(testOrderList);
    }

    @AfterEach
    void clearInvocationsInMocked() {
        Mockito.clearInvocations(
                mockedMasterRepository,
                mockedOrderRepository
        );
    }


    @Test
    void whenGetOrdersByMasterIdCalledInCaseOfMasterIsExist_ThenReturnOrders() {
        // test master
        master.setId(1L);

        // expected orders
        List<Order> expectedOrders = new ArrayList<>(1);
        expectedOrders.addAll(testOrderList);

        // test
        when(mockedMasterRepository.findById(1L)).thenReturn(master);

        List<Order> resultOrderList = masterService.getOrdersByMasterId(1L);

        assertEquals(expectedOrders, resultOrderList);
    }

    @Test
    void whenGetOrderByMasterIdCalledInCaseOfMasterIsNotExist_ThenThrowEntityNotFoundException() {
        when(mockedMasterRepository.findById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> masterService.getOrdersByMasterId(1L));
    }

    @Test
    void whenGetAllByOrderIdCalledInCaseOfOrderIsExist_ThenReturnMasters() {
        // test masters
        List<Master> testMasterList = new ArrayList<>(1);
        testMasterList.add(master);

        // expected masters
        List<Master> expectedMasterList = new ArrayList<>(1);
        expectedMasterList.add(master);

        // test params
        MultiValueMap<String, String> testParams = new LinkedMultiValueMap<>();

        // test
        when(mockedOrderRepository.findById(1L)).thenReturn(mock(Order.class));
        when(mockedMasterRepository.findAll(testParams)).thenReturn(testMasterList);

        List<Master> resultMasterList = masterService.getAllByOrderId("1", testParams);

        assertEquals(expectedMasterList, resultMasterList);
    }

    @Test
    void whenGetAllByOrderIdCalledInCaseOfOrderIsNotExist_ThenThrowEntityNotFoundException() {
        when(mockedOrderRepository.findById(1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> masterService.getAllByOrderId("1", new LinkedMultiValueMap<>()));
    }

    @Test
    void whenGetFullNameCalled_ThenReturnFullName() {
        assertEquals("lastName firstName patronymic", masterService.getFullName(master));
    }

    @Test
    void whenGetFullNameWithIdCalled_ThenReturnFullNameWithId() {
        master.setId(1L);
        assertEquals("lastName firstName patronymic [id: 1]", masterService.getFullNameWithId(master));
    }

    @Test
    void whenCheckRequestParamsAndGetAllCalled_ThenInRequestParamsNotShouldBeOrderIdAndReturnAllMastersByParameters() {
        // test masters
        List<Master> testMasterList = new ArrayList<>(1);
        testMasterList.add(master);

        // expected masters
        List<Master> expectedMasterList = new ArrayList<>(1);
        expectedMasterList.add(master);

        // test params
        MultiValueMap<String, String> testParams = new LinkedMultiValueMap();
        testParams.add("orderId", "1");

        // expected params
        MultiValueMap<String, String> expectedParams = new LinkedMultiValueMap<>();

        // test
        when(mockedMasterRepository.findAll(testParams)).thenReturn(testMasterList);

        List<Master> resultMasterList = masterService.checkRequestParamsAndGetAll(testParams);

        assertEquals(expectedMasterList, resultMasterList);
        assertEquals(expectedParams, testParams);
    }

}
