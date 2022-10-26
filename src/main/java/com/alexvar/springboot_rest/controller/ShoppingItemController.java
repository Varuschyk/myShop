package com.alexvar.springboot_rest.controller;

import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/shopping_items")
public class ShoppingItemController{

    private final ShoppingItemService shoppingItemService;

//    @Value("${upload.path}")
//    private String uploadPath;

    @Autowired
    public ShoppingItemController(ShoppingItemService shoppingItemService){
        this.shoppingItemService = shoppingItemService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('developer:read')")
    public String getAll(Model model){
        model.addAttribute("items", shoppingItemService.getAll());
        return "all-items";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('developer:read')")
    public String create(Model model){
        model.addAttribute("item", new ShoppingItem());
        return "item-create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('developer:read')")
    public String create(@ModelAttribute("item") @Valid ShoppingItem item, BindingResult result) throws IOException {

//        if(image != null && !image.getOriginalFilename().isEmpty()){
//            File uploadDir = new File(uploadPath);
//
//            if(!uploadDir.exists()){
//                uploadDir.mkdir();
//            }
//
//            String uuidImage = UUID.randomUUID().toString();
//            String resultImageName = uuidImage + "." + image.getOriginalFilename();
//
//            image.transferTo(new File(uploadPath + "/" + resultImageName));
//
//            item.setImage(resultImageName);
//        }

//        File convFile = new File(image.getOriginalFilename());
//        convFile.createNewFile();
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(image.getBytes());
//        fos.close();
//
//
//        item.setImage(convFile);

        if(result.hasErrors()){
            return "item-create";
        }

        shoppingItemService.create(item);
        return "redirect:/shopping_items/all";
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAuthority('developer:read')")
    public String read(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("item", shoppingItemService.readById(id));
        return "item-info";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('developer:read')")
    public String update(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("item", shoppingItemService.readById(id));
        return "item-update";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('developer:read')")
    public String update(@ModelAttribute("item") @Valid ShoppingItem item, BindingResult result){

        if(result.hasErrors()){
            return "item-update";
        }

        shoppingItemService.update(item);
        return "redirect:/shopping_items/read/{id}";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('developer:read')")
    public String delete(@PathVariable(value = "id") long id){
        shoppingItemService.delete(id);
        return "redirect:/shopping_items/all";
    }
}
