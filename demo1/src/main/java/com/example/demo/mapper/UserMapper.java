package com.example.demo.mapper;

import com.example.demo.Entity.User;
import com.example.demo.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())  // Enum to String
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
