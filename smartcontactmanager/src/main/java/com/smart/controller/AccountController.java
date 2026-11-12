package com.smart.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.smart.dao.UserRepository;
import com.smart.models.PasswordChangeForm;
import com.smart.models.ProfileForm;
import com.smart.models.User;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class AccountController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser(principal);
        model.addAttribute("title", "Your profile | Smart Contact Manager");
        model.addAttribute("email", user.getEmail());
        model.addAttribute("profileForm", profileForm(user));
        return "normal/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @ModelAttribute("profileForm") ProfileForm profileForm,
            BindingResult bindingResult,
            Model model) {
        User user = currentUser(principal);
        model.addAttribute("title", "Your profile | Smart Contact Manager");
        model.addAttribute("email", user.getEmail());
        if (bindingResult.hasErrors()) {
            return "normal/profile";
        }
        user.setName(profileForm.getName().trim());
        user.setAbout(profileForm.getAbout().trim());
        userRepository.save(user);
        return "redirect:/user/profile?updated";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "Account settings | Smart Contact Manager");
        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        return "normal/settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @ModelAttribute("passwordChangeForm") PasswordChangeForm form,
            BindingResult bindingResult,
            Model model) {
        model.addAttribute("title", "Account settings | Smart Contact Manager");
        User user = currentUser(principal);

        if (!bindingResult.hasErrors() && !passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "password.invalid", "Current password is incorrect.");
        }
        if (!bindingResult.hasErrors() && !form.getNewPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "The new passwords do not match.");
        }
        if (bindingResult.hasErrors()) {
            return "normal/settings";
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        return "redirect:/user/settings?passwordChanged";
    }

    private User currentUser(UserDetails principal) {
        return userRepository.findByEmailIgnoreCase(principal.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private ProfileForm profileForm(User user) {
        ProfileForm form = new ProfileForm();
        form.setName(user.getName());
        form.setAbout(user.getAbout());
        return form;
    }
}
