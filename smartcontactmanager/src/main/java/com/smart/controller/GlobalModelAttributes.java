package com.smart.controller;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute
    public void addAuthenticationState(Principal principal, Model model) {
        model.addAttribute("authenticatedEmail", principal == null ? null : principal.getName());
    }
}
