package org.lkcode.start_spring_security.controller;

import org.lkcode.start_spring_security.dtos.UserRegistrationDto;
import org.lkcode.start_spring_security.event.OnRegistrationCompleteEvent;
import org.lkcode.start_spring_security.model.User;
import org.lkcode.start_spring_security.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    public RegistrationController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userRegistrationDto, Model model) {
        try {
            User registered = userService.registerNewUserAccount(userRegistrationDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
            return "redirect:/verify-email";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while registering the user.");
            return "registration";
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", "Your account has been verified successfully.");
        } else {
            model.addAttribute("invalidToken", true);
            model.addAttribute("message", "Invalid verification token.");
        }
        return "verify-email";
    }
}
