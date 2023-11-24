package com.dtvn.springbootproject.services.interfaces;

import com.dtvn.springbootproject.requests.AuthenticationRequest;
import com.dtvn.springbootproject.responses.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
}
