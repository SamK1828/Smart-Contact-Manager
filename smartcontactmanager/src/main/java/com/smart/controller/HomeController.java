package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.models.User;

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

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Log in - Smart Contact Manager");
        return "login";
    }

    @PostMapping("/do_register")
    // @RequestMapping(value="/do_register",method=RequestMethod.POST)
    // Handler for Registering User
    public String registerUser(@ModelAttribute("user") User user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement) {
                user.setEnabled(true);
                user.setRole("ROLE_USER");
                System.out.println("Agreement "+ agreement);
                System.out.println("User "+ user);
        return "signup";
    }
}
