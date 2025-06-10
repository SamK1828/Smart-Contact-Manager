package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
public class HomeController {
    private UserRepository userRepository;
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        User user=new User();
        user.setName("Samarth Kalegaonkar");
        user.setEmail("kalegaonkarsamarth@gmail.com");
        user.setRole("Engineer");
        Contact contact = new Contact();
        user.getContacts().add(contact);
        userRepository.save(user);
        return "Working";
    }
}
