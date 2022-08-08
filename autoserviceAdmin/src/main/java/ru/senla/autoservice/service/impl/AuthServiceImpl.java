package ru.senla.autoservice.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.senla.autoservice.dto.JwtRequestDto;
import ru.senla.autoservice.dto.JwtResponseDto;
import ru.senla.autoservice.service.IAuthService;
import ru.senla.autoservice.util.JwtTokenUtil;

@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    public AuthServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                           JwtTokenUtil jwtTokenUtil,
                           AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    private void authenticate(@NonNull String username, @NonNull String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public JwtResponseDto createAuthToken(@NonNull JwtRequestDto authRequest) throws Exception {
        try {
            authenticate(authRequest.getUsername(), authRequest.getPassword());
        } catch (BadCredentialsException ex) {
            String message = "Incorrect username or password";
            log.error(message);
            throw new Exception(message);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        String token = jwtTokenUtil.generateToken(userDetails);

        return new JwtResponseDto(token);
    }
}
