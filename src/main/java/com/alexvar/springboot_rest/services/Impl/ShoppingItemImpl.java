package com.alexvar.springboot_rest.services.Impl;

import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingItemImpl implements ShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;

    @Autowired
    public ShoppingItemImpl(ShoppingItemRepository shoppingItemRepository){
        this.shoppingItemRepository = shoppingItemRepository;
    }

    @Override
    public ShoppingItem create(ShoppingItem shoppingItem) {
        return shoppingItemRepository.save(shoppingItem);
    }

    @Override
    public ShoppingItem readById(long id) {
        return shoppingItemRepository.getReferenceById(id);
    }

    @Override
    public ShoppingItem update(ShoppingItem shoppingItem) {
        ShoppingItem newItem = new ShoppingItem();
        newItem.setName(shoppingItem.getName());
        newItem.setPrice(shoppingItem.getPrice());
        return newItem;
    }

    @Override
    public void delete(long id) {
        shoppingItemRepository.deleteById(id);
    }

    @Override
    public List<ShoppingItem> getAll() {
        return shoppingItemRepository.findAll();
    }
}
