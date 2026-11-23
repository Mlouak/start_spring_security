package org.lkcode.start_spring_security.services;

import org.lkcode.start_spring_security.model.SecurityUser;
import org.lkcode.start_spring_security.model.UserProfile;
import org.lkcode.start_spring_security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityUser(
            userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username))
        );
    }

    /**
     * Updates the password for a given user if the current password is correct.
     *
     * @param username The username of the user.
     * @param currentPassword The current password of the user.
     * @param newPassword The new password to be set.
     * @throws Exception if the current password is incorrect or the user is not found.
     */
    public void updateUserPassword(String username, String currentPassword, String newPassword) throws Exception {
        UserProfile user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Saves a new user profile to the repository.
     *
     * @param user The user profile to save.
     */
    public void saveUser(UserProfile user) {
        userRepository.save(user);
    }
}
