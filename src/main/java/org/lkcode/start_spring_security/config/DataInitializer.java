package org.lkcode.start_spring_security.config;

import org.lkcode.start_spring_security.models.UserProfile;
import org.lkcode.start_spring_security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, "user1", List.of("WRITE"));
            createUserIfMissing(userRepository, passwordEncoder, "user2", List.of("USER"));
            createUserIfMissing(userRepository, passwordEncoder, "user3", List.of("ADMIN", "WRITE"));
        };
    }

    private void createUserIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String username,
            List<String> roles
    ) {
        if (!userRepository.existsByUsername(username)) {
            String encodedPassword = passwordEncoder.encode("pass1");
            userRepository.save(new UserProfile(username, encodedPassword, roles));
        }
    }
}
