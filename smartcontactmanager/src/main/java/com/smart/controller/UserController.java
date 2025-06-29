package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    // This is the user dashboard
    @RequestMapping("/dashboard")
    public String dashboard() {
        return "normal/user_dashboard";
    }

    // This is the user profile
    @RequestMapping("/profile")
    public String profile() {
        return "normal/profile";
    }

    // This is the user settings
    @RequestMapping("/settings")
    public String settings() {
        return "normal/settings";
    }
}
