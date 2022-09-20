package ru.senla.autoservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.senla.autoservice.config.TestConfig;
import ru.senla.autoservice.model.Authority;
import ru.senla.autoservice.model.Role;
import ru.senla.autoservice.model.User;
import ru.senla.autoservice.repo.IUserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class UserDetailsServiceTest {

    @Autowired
    IUserRepository mockedUserRepository;
    @Autowired
    UserDetailsService userDetailsService;

    @AfterEach
    void clearInvocationsInMocked() {
        Mockito.clearInvocations(
                mockedUserRepository
        );
    }

    @Test
    void whenLoadByUsernameCalled_ThenFindUserAndConvertHisInUserDetailsAndReturn() {
        // test user
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setEnabled(true);

        Role testRole = new Role();
        testRole.setName("testRole");

        Authority testAuthority = new Authority();
        testAuthority.setName("testAuthority");

        testRole.setAuthorities(List.of(testAuthority));

        testUser.setRoles(List.of(testRole));

        // expected user details
        UserDetails expectedUserDetails = new org.springframework.security.core.userdetails.User(
                "testUser",
                "testPassword",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("testRole"), new SimpleGrantedAuthority("testAuthority"))
        );

        // test
        when(mockedUserRepository.findByUsername("testUser")).thenReturn(testUser);

        UserDetails resultUserDetails = userDetailsService.loadUserByUsername("testUser");

        assertEquals(expectedUserDetails, resultUserDetails);
    }

    @Test
    void whenLoadByUsernameCalledInCaseOfIfUserNotExist_ThenThrowUsernameNotFoundException() {
        when(mockedUserRepository.findByUsername("notExistUser")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("notExistUser"));
    }

}
