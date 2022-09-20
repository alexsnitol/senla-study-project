package ru.senla.autoservice.service;

import ru.senla.autoservice.dto.JwtRequestDto;
import ru.senla.autoservice.dto.JwtResponseDto;

public interface IAuthService {

    JwtResponseDto createAuthToken(JwtRequestDto authRequest);

}
