package com.example.demo.service;

import com.example.demo.Entity.User;
import com.example.demo.Enums.Role;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceAlredyExistsException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Transactional
    public UserResponse signUp(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ResourceAlredyExistsException("Email Already Exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.STUDENT);

        try{
         emailService.sendWelcomeEmail(user);
        }
        catch (Exception e){
            log.error("Failed to send welcome email, but registration succeeded", e);

        }
        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request, String ipAddress, String device){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceAlredyExistsException("User not found"));


        emailService.sendLoginNotification(user, ipAddress, device, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        return new AuthResponse(jwt, user.getId(),
                user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getRole());
    }

}
