package com.dtvn.springbootproject.services.interfaces;

import com.dtvn.springbootproject.dto.requestDtos.Auth.AuthenticationRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Auth.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO login(AuthenticationRequestDTO request);
}
