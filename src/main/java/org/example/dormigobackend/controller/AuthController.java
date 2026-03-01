package org.example.dormigobackend.controller;

import org.example.dormigobackend.dto.request.LoginRequest;
import org.example.dormigobackend.dto.request.RegisterRequest;
import org.example.dormigobackend.dto.response.AuthResponse;
import org.example.dormigobackend.dto.response.UserResponse;
import org.example.dormigobackend.service.AuthService;
import org.example.dormigobackend.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody RegisterRequest registerRequest){
        UserResponse userResponse = authService.signUp(registerRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    )
    {

        String ipAddress = RequestUtils.getClientIpAddress(request);
        String device = RequestUtils.getDeviceInfo(request);
        AuthResponse authResponse = authService.login(loginRequest, ipAddress, device);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
