package com.example.demo.dto.response;

import com.example.demo.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String email;
    private Role role;

}
