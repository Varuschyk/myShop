package com.alexvar.springboot_rest.controller;

import com.alexvar.springboot_rest.configuration.security.SecurityConfig;
import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import com.alexvar.springboot_rest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ShoppingItemService shoppingItemService;

    @Autowired
    public UserController(UserService userService, ShoppingItemService shoppingItemService) {
        this.userService = userService;
        this.shoppingItemService = shoppingItemService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('developer:write')")
    public String getAll(Model model, Principal principal){
        model.addAttribute("users", userService.getAll());
        log.info("Successfully got list all users by admin {}", principal.getName());
        return "all-users";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('developer:write')")
    public String create(Model model){
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('developer:write')")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult result, Principal principal){
        if(result.hasErrors()){
            log.error("User wasn't created due to error by admin {}", principal.getName());
            return "user-create";
        }
        userService.create(user);
        log.info("User is successfully created by admin {}", principal.getName());
        return "redirect:/users/all";
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAuthority('developer:read')")
    public String read(Model model, @PathVariable(value="id") long id) {
        SecurityConfig.checkOwner(id);
        model.addAttribute("user", userService.readById(id));
        return "user-info";
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("hasAuthority('developer:read')")
    public String read(@PathVariable(value="id") long id, Model model){
        SecurityConfig.checkOwner(id);
        model.addAttribute("user", userService.readById(id));
        model.addAttribute("items", shoppingItemService.getAll());
        return "user-items";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public String update(@PathVariable(value="id") long id, Model model){
        SecurityConfig.checkOwner(id);
        User oldUser = userService.readById(id);
        model.addAttribute("user", oldUser);
        model.addAttribute("roles", Role.values());
        return "user-update";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public String update(@ModelAttribute(value = "user") @Valid User user, BindingResult result,
                         @PathVariable(value="id") long id, Model model, Principal principal){
        SecurityConfig.checkOwner(id);
        User oldUser = userService.readById(id);
        if(result.hasErrors()){
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", Role.values());
            log.error("User wasn't updated due to error by admin {}", principal.getName());
            return "user-update";
        }

        userService.update(user);
        log.info("User is successfully updated by admin {}", principal.getName());
        return "redirect:/users/read/{id}";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public String delete(@PathVariable(value="id") long id, Principal principal){
        SecurityConfig.checkOwner(id);
        userService.delete(id);
        log.info("User is successfully deleted by admin {}", principal.getName());
        return "redirect:/users/all";
    }

    @GetMapping("/{id}/add")
    @PreAuthorize("hasAuthority('developer:read')")
    public String addShoppingItem(@PathVariable(value="id") long id, @RequestParam(required = false, value = "item_id") long itemId){
        SecurityConfig.checkOwner(id);
        User user = userService.readById(id);

        user.getStuffList().add(shoppingItemService.readById(itemId));

        userService.update(user);

        log.info("User {} added item {} to cart", user.getEmail(), shoppingItemService.readById(itemId).getName());
        return "redirect:/users/{id}/items";
    }

    @GetMapping("/{id}/remove")
    @PreAuthorize("hasAuthority('developer:read')")
    public String removeShoppingItem(@PathVariable(value = "id") long id, @RequestParam(required = false, value = "item_id") long itemId){
        SecurityConfig.checkOwner(id);
        User user = userService.readById(id);

        user.getStuffList().remove(shoppingItemService.readById(itemId));

        userService.update(user);
        log.info("User {} removed item {} from cart", user.getEmail(), shoppingItemService.readById(itemId).getName());
        return "redirect:/users/{id}/items";
    }

}
