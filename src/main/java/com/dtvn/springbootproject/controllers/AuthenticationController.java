package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.requestDtos.Auth.AuthenticationRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Auth.AuthenticationResponseDTO;
import com.dtvn.springbootproject.services.implementations.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        return ResponseEntity.ok(authenticationServiceImpl.login(request));
    }
}
