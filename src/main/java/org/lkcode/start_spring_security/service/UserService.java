package org.lkcode.start_spring_security.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.lkcode.start_spring_security.dtos.UserRegistrationDto;
import org.lkcode.start_spring_security.model.User;
import org.lkcode.start_spring_security.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    public User registerNewUserAccount(UserRegistrationDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(false);
        String verificationToken = generateVerificationToken();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        //generate verification link
        String verificationLink = "http://localhost:8080/verify-email?token=" + verificationToken;
        //send verification email
        emailService.sendEmail(user.getEmail(), "Email Verification", "Please click the following link to verify your email: " + verificationLink);
        return user;
    }

    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    private String generateVerificationToken() {
        // Generate a random verification token (you can use UUID or any other method)
        return java.util.UUID.randomUUID().toString();
    }
}
