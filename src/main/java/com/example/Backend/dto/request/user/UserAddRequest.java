package com.example.Backend.dto.request.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddRequest {

    String email;
    String username;
    String password;
    String role;
}
