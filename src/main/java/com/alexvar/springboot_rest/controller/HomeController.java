package com.alexvar.springboot_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {

    final static Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("home")
    public String home(Principal principal){
        log.info("User {} has entered in account", principal.getName());
        return "home";
    }
}
