package org.lkcode.start_spring_security.controllers;

import org.lkcode.start_spring_security.services.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class PasswordController {

    private final CustomUserDetailsService customUserDetailsService;

    public PasswordController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/update-password")
    public String password() {
        return "update-password";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Principal principal, Model model

    ) {
        try {
            if (!newPassword.equals(confirmNewPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "update-password";
            }
            customUserDetailsService.updateUserPassword(principal.getName(), currentPassword, newPassword);
            model.addAttribute("message", "Password updated successfully");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the password");
        }
        return "update-password";
    }

}
