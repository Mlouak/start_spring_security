package org.lkcode.start_spring_security.controllers;

import org.lkcode.start_spring_security.model.UserProfile;
import org.lkcode.start_spring_security.services.CustomUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;;

    private final PasswordEncoder passwordEncoder;

    public UserController(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Handles GET requests to "/register".
     * @return The view name "register" for user registration page.
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * Handles Post requests to /register.
     * Registers a new user with the provided username and password.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param model The Spring MVC Model object for storing attributes.
     * @return The view name "register" if registration fails, "login" if successful.
     */
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        // Implementation for user registration
        if (customUserDetailsService.userExists(username)) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        UserProfile user =  new UserProfile(username, passwordEncoder.encode(password), List.of("USER"));
        user.setEnabled(true);
        customUserDetailsService.saveUser(user);
        return "login";
    }

}
