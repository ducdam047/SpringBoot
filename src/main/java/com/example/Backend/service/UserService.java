package com.example.Backend.service;

import com.example.Backend.dto.request.user.UserAddRequest;
import com.example.Backend.enums.UserRole;
import com.example.Backend.dto.request.user.UserSignupRequest;
import com.example.Backend.dto.request.user.UserUpdateRequest;
import com.example.Backend.entity.User;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import com.example.Backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signupUser(UserSignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .role(UserRole.USER.name())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if(!request.getPassword().equals(request.getConfirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_CONFIRM);

        return userRepository.save(user);
    }

    public User addUser(UserAddRequest request) {
        if(userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public User updateUser(String userId, UserUpdateRequest request) {
        User user = getUser(userId);

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setSex(request.getSex());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setCid(request.getCid());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        if(!userRepository.existsById(userId))
            throw new RuntimeException("User not found!");

        userRepository.deleteById(userId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow((() -> new RuntimeException("User not found!")));
    }

    @PostAuthorize("returnObject.email == authentication.name")
    public User updateMe(String userId, UserUpdateRequest request) {
        User user = getUser(userId);

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setSex(request.getSex());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setCid(request.getCid());

        return userRepository.save(user);
    }

    public User reloadUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("sub");
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isPresent())
                return optionalUser.get();
        }

        throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }
}
