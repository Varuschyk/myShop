package com.alexvar.springboot_rest.controller;

import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public String getAll(Model model){
        model.addAttribute("users", userService.getAll());
        return "all-users";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult result){
        if(result.hasErrors()){
            return "user-create";
        }
        userService.create(user);
        return "redirect:/users/all";
    }

    @GetMapping("/read/{id}")
    public String read(Model model, @PathVariable(value="id") long id){
        model.addAttribute("user", userService.readById(id));
        return "user-info";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(value="id") long id, Model model){
        User oldUser = userService.readById(id);
        model.addAttribute("user", oldUser);
        model.addAttribute("roles", Role.values());
        return "user-update";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute(value = "user") @Valid User user, BindingResult result,
                         @PathVariable(value="id") long id, Model model){
        User oldUser = userService.readById(id);
        if(result.hasErrors()){
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", Role.values());
            return "user-update";
        }
        userService.update(user);
        return "redirect:/users/read/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value="id") long id){
        userService.delete(id);
        return "redirect:/users/all";
    }

}