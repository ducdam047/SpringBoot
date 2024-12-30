package com.example.Backend.controller;

import com.example.Backend.dto.request.user.UserAddRequest;
import com.example.Backend.dto.request.user.UserSignupRequest;
import com.example.Backend.dto.request.user.UserUpdateRequest;
import com.example.Backend.dto.response.ApiResponse;
import com.example.Backend.entity.User;
import com.example.Backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    ApiResponse<User> signupUser(@RequestBody UserSignupRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Registered successfully");
        apiResponse.setResult(userService.signupUser(request));
        return apiResponse;
    }

    @PostMapping("/addUser")
    ApiResponse<User> addUser(@RequestBody UserAddRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Add user successfully");
        apiResponse.setResult(userService.addUser(request));
        return apiResponse;
    }

    @PutMapping("/updateUser/{userId}")
    User updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/delete/{userId}")
    String deleteUser(@PathVariable("userId") String useId) {
        userService.deleteUser(useId);
        return "User has been deleted";
    }

    @GetMapping("/users")
    List<User> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return userService.getUsers();
    }

    @GetMapping("/myInfo")
    User getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/me/{userId}")
    User updateMe(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateMe(userId, request);
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/reloadUser")
    User reloadUser() {
        return userService.reloadUser();
    }
}
