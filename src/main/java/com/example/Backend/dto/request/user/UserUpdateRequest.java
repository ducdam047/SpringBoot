package com.example.Backend.dto.request.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String username;
    String password;
    String fullName;
    String sex;
    String address;
    String phone;
    String cid;
}
