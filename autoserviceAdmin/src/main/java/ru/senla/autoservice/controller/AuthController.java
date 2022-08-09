package ru.senla.autoservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.autoservice.dto.JwtRequestDto;
import ru.senla.autoservice.dto.JwtResponseDto;
import ru.senla.autoservice.service.IAuthService;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private IAuthService authService;

    @PostMapping
    public ResponseEntity<JwtResponseDto> createAuthToken(@RequestBody JwtRequestDto authRequest) {
        return ResponseEntity.ok(authService.createAuthToken(authRequest));
    }

}
