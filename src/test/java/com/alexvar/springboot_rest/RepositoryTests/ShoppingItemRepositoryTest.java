package com.alexvar.springboot_rest.RepositoryTests;

import com.alexvar.springboot_rest.exception.ShoppingItemNotFoundException;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingItemRepositoryTest {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Test
    @Tag("By testing package invokes error")
    void check_Find_Shopping_Item_By_Name() {
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setName("USB-cable");
        shoppingItem.setPrice(500);
        shoppingItem.setImage(null);
        shoppingItem.setCreatedAt(LocalDateTime.now());
        shoppingItem.setUsers(new ArrayList<>());

        shoppingItemRepository.save(shoppingItem);

        ShoppingItem result = shoppingItemRepository.findShoppingItemByName(shoppingItem.getName()).orElseThrow(ShoppingItemNotFoundException::new);

        assertThat(result.getName()).isEqualTo(shoppingItem.getName());
    }
}