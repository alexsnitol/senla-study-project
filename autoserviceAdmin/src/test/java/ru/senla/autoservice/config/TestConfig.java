package ru.senla.autoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.senla.autoservice.repo.IGarageRepository;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderGarageRepository;
import ru.senla.autoservice.repo.IOrderMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;
import ru.senla.autoservice.service.impl.GarageServiceImpl;
import ru.senla.autoservice.service.impl.MasterServiceImpl;
import ru.senla.autoservice.service.impl.OrderServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean
    public IGarageRepository garageRepository() {
        return mock(IGarageRepository.class);
    }

    @Bean
    public IMasterRepository masterRepository() {
        return mock(IMasterRepository.class);
    }

    @Bean
    public IOrderRepository orderRepository() {
        return mock(IOrderRepository.class);
    }

    @Bean
    public IOrderGarageRepository orderGarageRepository() {
        return mock(IOrderGarageRepository.class);
    }

    @Bean
    public IOrderMasterRepository orderMasterRepository() {
        return mock(IOrderMasterRepository.class);
    }

    @Bean
    public IGarageService garageService() {
        return new GarageServiceImpl(garageRepository(), masterRepository(), orderRepository(),
                orderGarageRepository(), orderMasterRepository());
    }

    @Bean
    public IGarageService mockedGarageService() {
        return mock(IGarageService.class);
    }

    @Bean
    public IMasterService masterService() {
        return new MasterServiceImpl(masterRepository(), orderRepository());
    }

    @Bean
    public IOrderService orderService() {
        return new OrderServiceImpl(orderRepository(), masterRepository(), mockedGarageService());
    }

}
