package com.alexvar.springboot_rest.services;

import com.alexvar.springboot_rest.model.ShoppingItem;

import java.util.List;

public interface ShoppingItemService {
    ShoppingItem create(ShoppingItem shoppingItem);
    ShoppingItem readById(long id);
    ShoppingItem update(ShoppingItem shoppingItem);
    void delete(long id);
    List<ShoppingItem> getAll();
}
