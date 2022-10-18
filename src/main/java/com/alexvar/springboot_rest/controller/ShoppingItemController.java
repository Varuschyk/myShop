package com.alexvar.springboot_rest.controller;

import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/shopping_items")
public class ShoppingItemController {

    private final ShoppingItemService shoppingItemService;

    @Autowired
    public ShoppingItemController(ShoppingItemService shoppingItemService){
        this.shoppingItemService = shoppingItemService;
    }

    @GetMapping("/all")
    public String getAll(Model model){
        model.addAttribute("items", shoppingItemService.getAll());
        return "all-items";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("item", new ShoppingItem());
        return "item-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("item") ShoppingItem item){
        shoppingItemService.create(item);
        return "redirect:/shopping_items/all";
    }

    @GetMapping("/read/{id}")
    public String read(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("item", shoppingItemService.readById(id));
        return "item-info";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("item", shoppingItemService.readById(id));
        return "item-update";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("item") @Valid ShoppingItem item){
        shoppingItemService.update(item);
        return "redirect:/shopping_items/read/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") long id){
        shoppingItemService.delete(id);
        return "redirect:/shopping_items/all";
    }
}
