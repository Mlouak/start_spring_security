package org.lkcode.start_spring_security.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/home")
    public String home() {
        return "userHome";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
