package com.alexvar.springboot_rest.controller;

import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
//@Log4j
public class AuthController {

    Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result){

        if(result.hasErrors()){
            log.error("Register was interrupted by error");
            return "register";
        }

        log.info("User was successfully created");
        userService.create(user);
        return "redirect:/auth/login";
    }


}
