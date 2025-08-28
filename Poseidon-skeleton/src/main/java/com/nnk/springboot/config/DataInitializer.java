package com.nnk.springboot.config;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = User.builder()
            .fullName("Administrator")
            .password(passwordEncoder.encode("admin1234"))
            .role("ADMIN")
            .username("admin")
            .build();
        userRepository.save(admin);

        User user = User.builder()
            .fullName("User")
            .password(passwordEncoder.encode("user1234"))
            .role("USER")
            .username("user")
            .build();
        userRepository.save(user);

    }
}