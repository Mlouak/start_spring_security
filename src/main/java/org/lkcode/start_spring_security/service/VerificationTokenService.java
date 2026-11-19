package org.lkcode.start_spring_security.service;


import org.lkcode.start_spring_security.model.User;
import org.lkcode.start_spring_security.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

    private final UserRepository userRepository;

    public VerificationTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
    }

    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalidToken";
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return "valid";
    }
}
