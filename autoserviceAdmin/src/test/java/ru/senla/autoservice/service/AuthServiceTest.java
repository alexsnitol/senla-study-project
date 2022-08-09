package ru.senla.autoservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.senla.autoservice.config.TestConfig;
import ru.senla.autoservice.dto.JwtRequestDto;
import ru.senla.autoservice.dto.JwtResponseDto;
import ru.senla.autoservice.util.JwtTokenUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class AuthServiceTest {

    @Autowired
    IAuthService authService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserDetailsService mockedUserDetailsService;
    @Autowired
    AuthenticationManager mockedAuthenticationManager;

    @AfterEach
    void clearInvocationsInMocked() {
        Mockito.clearInvocations(
                mockedUserDetailsService,
                mockedAuthenticationManager
        );
    }

    @Test
    void whenCreateAuthTokenCalled_thenAuthenticateUserAndReturnJwtResponseDto() {
        // test authRequest
        JwtRequestDto authRequest = new JwtRequestDto("testUser", "testPassword");

        // test userDetails
        UserDetails testUserDetails = new org.springframework.security.core.userdetails.User(
                "testUser",
                "testPassword",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("testRole"), new SimpleGrantedAuthority("testAuthority"))
        );

        // expected jwt response
        JwtResponseDto expectedJwtResponse = new JwtResponseDto(
                jwtTokenUtil.generateToken(testUserDetails)
        );

        // test
        when(mockedUserDetailsService.loadUserByUsername("testUser")).thenReturn(testUserDetails);
        assertEquals(expectedJwtResponse, authService.createAuthToken(authRequest));
    }

    @Test
    void whenCreateAuthTokenCalledInCaseOfIfUserNotExist_thenThrowBadCredentialsException() {
        // test username password token
        UsernamePasswordAuthenticationToken testUsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                "testUsername",
                "testPassword"
        );

        // test
        when(mockedAuthenticationManager.authenticate(testUsernamePasswordAuthenticationToken))
                .thenThrow(BadCredentialsException.class);
        assertThrows(AuthorizationServiceException.class, () -> {
            authService.createAuthToken(new JwtRequestDto("testUsername", "testPassword"));
        });
    }


}
