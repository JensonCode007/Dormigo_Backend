package org.example.dormigobackend.service;


import org.example.dormigobackend.Entity.User;
import org.example.dormigobackend.Repository.UserRepository;
import org.example.dormigobackend.dto.response.UserResponse;
import org.example.dormigobackend.exception.ResourceNotFoundException;
import org.example.dormigobackend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "'all'")
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

    @CachePut(value = "users", key = "'id' + #id")
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

    @CachePut(value = "users", key = "'id' + #id")
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

    @Cacheable(value = "users", key = "'id' + #id")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserMapper.toResponse(user);

    }
}
