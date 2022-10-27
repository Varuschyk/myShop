package com.alexvar.springboot_rest.services.Impl;

import com.alexvar.springboot_rest.exception.NullEntityException;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingItemServiceImpl implements ShoppingItemService {

    final static Logger log = LoggerFactory.getLogger(ShoppingItemServiceImpl.class);

    private final ShoppingItemRepository shoppingItemRepository;

    @Autowired
    public ShoppingItemServiceImpl(ShoppingItemRepository shoppingItemRepository){
        this.shoppingItemRepository = shoppingItemRepository;
    }

    @Override
    public ShoppingItem create(ShoppingItem shoppingItem) {
        log.trace("Item with id {} saved", shoppingItem.getId());
        return shoppingItemRepository.save(shoppingItem);
    }

    @Override
    public ShoppingItem readById(long id) {
        if(shoppingItemRepository.existsById(id)) {
            log.trace("Get item with id {}", id);
            return shoppingItemRepository.findById(id).get();
        }else{
            log.warn("Item with id {} not found!", id);
            throw new NullEntityException("Item not found!");
        }
    }

    @Override
    public ShoppingItem update(ShoppingItem shoppingItem) {
        if(shoppingItem == null){
            log.warn("Item with id {} not found!", shoppingItem.getId());
            throw new NullEntityException("Item is null!");
        }

        System.out.println(shoppingItem);

        ShoppingItem newItem = shoppingItemRepository.findById(shoppingItem.getId()).get();
        newItem.setName(shoppingItem.getName());
        newItem.setPrice(shoppingItem.getPrice());
        newItem.setCreatedAt(LocalDateTime.now());

        log.trace("Item with id {} updated", shoppingItem.getId());
        return shoppingItemRepository.save(newItem);
    }

    @Override
    public void delete(long id) {
        log.trace("Item with id {} successfully deleted", id);
        shoppingItemRepository.deleteById(id);
    }

    @Override
    public List<ShoppingItem> getAll() {
        log.trace("Get all items from the DB");
        return shoppingItemRepository.findAll();
    }
}
