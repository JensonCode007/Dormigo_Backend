package com.example.demo.service;


import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public @Nullable List<UserResponse> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toResponse).collect(Collectors.toList());

    }


    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findByIsActiveFalse().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }
    public List<UserResponse> getAllInactiveActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(UserMapper::toResponse)
                . collect(Collectors.toList());
    }

    @Transactional
    public UserResponse deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        if(!user.getIsActive()){
            throw new IllegalStateException("User already deactivated");
        }
        user.setIsActive(false);

        User deactivatedUser = userRepository.save(user);
        return UserMapper.toResponse(deactivatedUser);
    }

    @Transactional
    public UserResponse activateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        if(user.getIsActive()){
            throw new IllegalStateException("User is already activated");
        }
        user.setIsActive(true);
        User activatedUser = userRepository.save(user);
        return UserMapper.toResponse(activatedUser);
    }

    public void deleteUser(Long id) {
        if(userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserMapper.toResponse(user);

    }
}
