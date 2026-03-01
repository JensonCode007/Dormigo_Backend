package org.example.dormigobackend.dto.response;

import org.example.dormigobackend.Enums.Role;
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
    private Long userId;
    private String type = "Bearer";
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public AuthResponse(String token, Long userId, String email, String firstName, String lastName, Role role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
