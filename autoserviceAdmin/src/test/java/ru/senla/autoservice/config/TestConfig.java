package ru.senla.autoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.senla.autoservice.repo.IGarageRepository;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderGarageRepository;
import ru.senla.autoservice.repo.IOrderMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.repo.IUserRepository;
import ru.senla.autoservice.service.IAuthService;
import ru.senla.autoservice.service.IGarageService;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.IOrderService;
import ru.senla.autoservice.service.impl.AuthServiceImpl;
import ru.senla.autoservice.service.impl.GarageServiceImpl;
import ru.senla.autoservice.service.impl.MasterServiceImpl;
import ru.senla.autoservice.service.impl.OrderServiceImpl;
import ru.senla.autoservice.service.impl.UserDetailsServiceImpl;
import ru.senla.autoservice.util.JwtTokenUtil;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    public static final String TEST_SECURE_KEY = "examplesupersecurekeyforencodinganddecodingtheHS256algorithm";

    @Bean
    public IGarageRepository mockedGarageRepository() {
        return mock(IGarageRepository.class);
    }

    @Bean
    public IMasterRepository mockedMasterRepository() {
        return mock(IMasterRepository.class);
    }

    @Bean
    public IOrderRepository mockedOrderRepository() {
        return mock(IOrderRepository.class);
    }

    @Bean
    public IOrderGarageRepository mockedOrderGarageRepository() {
        return mock(IOrderGarageRepository.class);
    }

    @Bean
    public IOrderMasterRepository mockedOrderMasterRepository() {
        return mock(IOrderMasterRepository.class);
    }

    @Bean
    public IUserRepository mockedUserRepository() {
        return mock(IUserRepository.class);
    }

    @Bean
    public IGarageService garageService() {
        return new GarageServiceImpl(mockedGarageRepository(), mockedMasterRepository(), mockedOrderRepository(),
                mockedOrderGarageRepository(), mockedOrderMasterRepository());
    }

    @Bean
    public IGarageService mockedGarageService() {
        return mock(IGarageService.class);
    }

    @Bean
    public IMasterService masterService() {
        return new MasterServiceImpl(mockedMasterRepository(), mockedOrderRepository());
    }

    @Bean
    public IOrderService orderService() {
        return new OrderServiceImpl(mockedOrderRepository(), mockedMasterRepository(), mockedGarageService());
    }

    @Bean
    public IAuthService authService() {
        return new AuthServiceImpl(mockedUserDetailsService(), jwtTokenUtil(), mockedAuthenticationManager());
    }

    @Bean
    public AuthenticationManager mockedAuthenticationManager() {
        return mock(AuthenticationManager.class);
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil(TEST_SECURE_KEY);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(mockedUserRepository());
    }

    @Bean
    public UserDetailsService mockedUserDetailsService() {
        return mock(UserDetailsServiceImpl.class);
    }

}
