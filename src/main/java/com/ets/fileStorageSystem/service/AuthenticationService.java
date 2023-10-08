package com.ets.fileStorageSystem.service;

import com.ets.fileStorageSystem.model.dao.request.SignUpRequest;
import com.ets.fileStorageSystem.model.dao.request.SigninRequest;
import com.ets.fileStorageSystem.model.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}