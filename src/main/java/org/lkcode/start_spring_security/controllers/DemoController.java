package org.lkcode.start_spring_security.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @PostMapping("/public")
    public String postPublic() {
        return "Public POST method granted";
    }

    @GetMapping("/public")
    public String getPublic() {
        return "Public GET method granted";
    }

    @GetMapping("/secured")
    public String getSecured() {
        return "Secured GET method granted";
    }

    @GetMapping("/user")
    public String getUser(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        System.out.println("Authenticated user: " + userDetails.getUsername());
        System.out.println("Authorities: " + userDetails.getAuthorities());
        return "Access to 'user' method granted";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Access to 'admin' method granted";
    }
}
