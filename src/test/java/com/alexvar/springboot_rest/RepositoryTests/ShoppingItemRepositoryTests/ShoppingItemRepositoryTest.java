package com.alexvar.springboot_rest.RepositoryTests.ShoppingItemRepositoryTests;

import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingItemRepositoryTest {
    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Test
    void check_Find_Shopping_Item_By_Name() {
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setName("USB-cable");
        shoppingItem.setPrice(500);
        shoppingItem.setImage(null);
        shoppingItem.setCreatedAt(LocalDateTime.now());
        shoppingItem.setUsers(new ArrayList<>());

        shoppingItemRepository.save(shoppingItem);

        ShoppingItem result = shoppingItemRepository.findShoppingItemByName(shoppingItem.getName()).get();

        assertThat(result.getName()).isEqualTo(shoppingItem.getName());
    }
}