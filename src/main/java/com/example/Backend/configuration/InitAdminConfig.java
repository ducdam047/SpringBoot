package com.example.Backend.configuration;

import com.example.Backend.enums.UserRole;
import com.example.Backend.entity.User;
import com.example.Backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class InitAdminConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByEmail("admin@gmail.com").isEmpty() && userRepository.findByUsername("admin").isEmpty()) {
                var role = UserRole.ADMIN.name();
                User user = User.builder()
                        .email("admin@gmail.com")
                        .username("admin")
                        .password(passwordEncoder.encode("220903"))
                        .role(role)
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: Admin, please change it");
            }
        };
    }
}
