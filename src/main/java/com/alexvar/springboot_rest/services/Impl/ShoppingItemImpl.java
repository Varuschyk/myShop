package com.alexvar.springboot_rest.services.Impl;

import com.alexvar.springboot_rest.exception.NullEntityException;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if(shoppingItemRepository.existsById(id)) {
            return shoppingItemRepository.findById(id).get();
        }else{
            throw new NullEntityException("Item not found!");
        }
    }

    @Override
    public ShoppingItem update(ShoppingItem shoppingItem) {
        if(shoppingItem == null){
            throw new NullEntityException("Item is null!");
        }

        System.out.println(shoppingItem);

        ShoppingItem newItem = shoppingItemRepository.findById(shoppingItem.getId()).get();
        newItem.setName(shoppingItem.getName());
        newItem.setPrice(shoppingItem.getPrice());
        newItem.setCreatedAt(LocalDateTime.now());

        return shoppingItemRepository.save(newItem);
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
