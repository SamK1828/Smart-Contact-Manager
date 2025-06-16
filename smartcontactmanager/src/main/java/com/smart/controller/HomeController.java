package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    // @Autowired
    // private UserRepository userRepository;

    // @GetMapping("/test")
    // @ResponseBody
    // public String test() {
    // User user = new User();
    // user.setName("Samarth Kalegaonkar");
    // user.setEmail("kalegaonkarsamarth@gmailom");
    // user.setRole("Engineer");
    // Contact contact = new Contact();
    // user.getContacts().add(contact);
    // userRepository.save(user);
    // return "Working";
    // }
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }
}
