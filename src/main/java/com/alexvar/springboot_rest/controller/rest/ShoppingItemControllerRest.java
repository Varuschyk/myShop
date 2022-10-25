package com.alexvar.springboot_rest.controller.rest;

import com.alexvar.springboot_rest.dto.ShoppingItemDto;
import com.alexvar.springboot_rest.exception.ApiShoppingItemException;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ShoppingItemControllerRest {

        private final ShoppingItemService shoppingItemService;

        @Autowired
        public ShoppingItemControllerRest(ShoppingItemService shoppingItemService) {
            this.shoppingItemService = shoppingItemService;
        }

        @GetMapping("/items")
        @PreAuthorize("hasAuthority('developer:write')")
        public List<ShoppingItemDto> readAll(){
            return shoppingItemService.getAll()
                    .stream()
                    .map(ShoppingItemDto::new)
                    .toList();
        }

        @PostMapping("/items")
        @PreAuthorize("hasAuthority('developer:write')")
        public ResponseEntity<ShoppingItemDto> create(@RequestBody @Valid ShoppingItem item, BindingResult result) throws ApiShoppingItemException {

            if(result.hasErrors()){
                throw new ApiShoppingItemException("Something wrong by create item");
            }

            shoppingItemService.create(item);

            return new ResponseEntity<>(new ShoppingItemDto(item), HttpStatus.CREATED);
        }

        @GetMapping("/items/{id}")
        @PreAuthorize("hasAuthority('developer:write')")
        public ShoppingItemDto read(@PathVariable(value = "id") long id){
            return new ShoppingItemDto(shoppingItemService.readById(id));
        }

        @PatchMapping("/items/{id}")
        @PreAuthorize("hasAuthority('developer:write')")
        public ResponseEntity<ShoppingItem> update(@PathVariable(value = "id") long id, @RequestBody @Valid ShoppingItem item, BindingResult result) throws ApiShoppingItemException {

            ShoppingItem oldItem = shoppingItemService.readById(id);

            if(result.hasErrors()){
                throw new ApiShoppingItemException("Something wrong by update user");
            }

            item.setId(id);
            item.setImage(oldItem.getImage());
            item.setCreatedAt(oldItem.getCreatedAt());
            shoppingItemService.update(item);

            return new ResponseEntity<>(item, HttpStatus.OK);
        }

        @DeleteMapping("/items/{id}")
        @PreAuthorize("hasAuthority('developer:write')")
        public ResponseEntity<Object> delete(@PathVariable(value = "id") long id){

            shoppingItemService.delete(id);

            return new ResponseEntity<>("Item is deleted successfully", HttpStatus.OK);
        }
}
