package com.example.demo.dto.response;

import com.example.demo.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
