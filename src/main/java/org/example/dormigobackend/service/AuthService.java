package org.example.dormigobackend.service;

import org.example.dormigobackend.Entity.User;
import org.example.dormigobackend.Enums.Role;
import org.example.dormigobackend.Repository.UserRepository;
import org.example.dormigobackend.dto.request.LoginRequest;
import org.example.dormigobackend.dto.request.RegisterRequest;
import org.example.dormigobackend.dto.response.AuthResponse;
import org.example.dormigobackend.dto.response.UserResponse;
import org.example.dormigobackend.exception.ResourceAlredyExistsException;
import org.example.dormigobackend.mapper.UserMapper;
import org.example.dormigobackend.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        log.info("🔐 Login attempt for: {}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

        );
        log.info("✅ Authentication successful");
        log.info("🔍 Authentication class: {}", authentication.getClass().getName());
        log.info("🔍 Principal class: {}", authentication.getPrincipal().getClass().getName());
        log.info("🔍 Authorities from authentication: {}", authentication.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        log.info("✅ JWT generated");

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceAlredyExistsException("User not found"));

        log.info("🔍 User from database - Role: {}", user.getRole());
        log.info("🔍 User from database - ID: {}", user.getId());
        String roleString = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                                .findFirst()
                                        .orElse("ROLE_STUDENT");
        log.info("🔍 Role string from authentication: {}", roleString);
        Role role = roleString.replace("ROLE_", "").equals("ADMIN")
                ? Role.ADMIN
                : Role.STUDENT;
        log.info("🔍 Final role enum: {}", role);

        emailService.sendLoginNotification(user, ipAddress, device, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        log.info("📤 Returning AuthResponse with role: {}", role);
        return new AuthResponse(jwt, user.getId(),
                user.getEmail(), user.getFirstName(),
                user.getLastName(), role);
    }

}
