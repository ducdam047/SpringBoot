package com.example.Backend.controller;

import com.example.Backend.dto.request.authentication.AuthenticationRequest;
import com.example.Backend.dto.request.authentication.TokenValidationRequest;
import com.example.Backend.dto.response.ApiResponse;
import com.example.Backend.dto.response.AuthenticationResponse;
import com.example.Backend.dto.response.TokenValidationResponse;
import com.example.Backend.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                .message("Valid information")
                .result(result)
                .build();
    }

    @PostMapping("/verify")
    ApiResponse<TokenValidationResponse> authenticate(@RequestBody TokenValidationRequest request) throws ParseException, JOSEException {
        var result = authenticationService.tokenValid(request);
        return ApiResponse.<TokenValidationResponse>builder()
                .code(200)
                .message("Login successfully")
                .result(result)
                .build();
    }
}
