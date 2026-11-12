package com.smart.controller;

import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.models.User;

import jakarta.validation.Valid;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @InitBinder("user")
    void restrictUserBinding(WebDataBinder binder) {
        binder.setAllowedFields("name", "email", "password", "about");
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About | Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Create account | Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String registered,
            @RequestParam(required = false) String logout,
            Model model) {
        model.addAttribute("title", "Sign in | Smart Contact Manager");
        model.addAttribute("loginError", error != null);
        model.addAttribute("registered", registered != null);
        model.addAttribute("loggedOut", logout != null);
        return "login";
    }

    @PostMapping("/do_register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
            Model model) {
        model.addAttribute("title", "Create account | Smart Contact Manager");

        if (!agreement) {
            bindingResult.reject("agreement.required", "Accept the terms to create an account.");
        }

        user.setEmail(user.getEmail() == null ? null : user.getEmail().trim().toLowerCase(Locale.ROOT));
        if (user.getEmail() != null && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            bindingResult.rejectValue("email", "email.duplicate", "An account with this email already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setImageUrl("default.png");

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.rejectValue("email", "email.duplicate", "An account with this email already exists.");
            return "signup";
        }

        return "redirect:/login?registered";
    }
}
