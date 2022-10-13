package com.alexvar.springboot_rest.controller;

import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import com.alexvar.springboot_rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ShoppingItemService shoppingItemService;

    @Autowired
    public UserController(UserService userService, ShoppingItemService shoppingItemService) {
        this.userService = userService;
        this.shoppingItemService = shoppingItemService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('developer:write')")
    public String getAll(Model model){
        model.addAttribute("users", userService.getAll());
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
    public String create(@ModelAttribute("user") @Valid User user, BindingResult result){
        if(result.hasErrors()){
            return "user-create";
        }
        userService.create(user);
        return "redirect:/users/all";
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAuthority('developer:read')")
    public String read(Model model, @PathVariable(value="id") long id){
        model.addAttribute("user", userService.readById(id));
        return "user-info";
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("hasAuthority('developer:read')")
    public String read(@PathVariable(value="id") long id, Model model){
        model.addAttribute("user", userService.readById(id));
        model.addAttribute("items", shoppingItemService.getAll());
        return "user-items";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public String update(@PathVariable(value="id") long id, Model model){
        User oldUser = userService.readById(id);
        model.addAttribute("user", oldUser);
        model.addAttribute("roles", Role.values());
        return "user-update";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
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
    @PreAuthorize("hasAuthority('developer:write')")
    public String delete(@PathVariable(value="id") long id){
        userService.delete(id);
        return "redirect:/users/all";
    }

    @GetMapping("/{id}/add")
    @PreAuthorize("hasAuthority('developer:read')")
    public String addShoppingItem(@PathVariable(value="id") long id, @RequestParam(required = false, value = "item_id") long itemId){
        User user = userService.readById(id);

        user.getStuffList().add(shoppingItemService.readById(itemId));

        userService.update(user);
        return "redirect:/users/all";
    }

    @GetMapping("/{id}/remove")
    @PreAuthorize("hasAuthority('developer:read')")
    public String removeShoppingItem(@PathVariable(value = "id") long id, @RequestParam(required = false, value = "item_id") long itemId){
        User user = userService.readById(id);

        user.getStuffList().remove(shoppingItemService.readById(itemId));

        userService.update(user);
        return "redirect:/users/all";
    }

}
