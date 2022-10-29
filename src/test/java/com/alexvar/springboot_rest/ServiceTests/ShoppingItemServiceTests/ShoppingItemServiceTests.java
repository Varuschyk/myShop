package com.alexvar.springboot_rest.ServiceTests.ShoppingItemServiceTests;

import com.alexvar.springboot_rest.exception.ShoppingItemExistsException;
import com.alexvar.springboot_rest.exception.ShoppingItemNotFoundException;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.services.ShoppingItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingItemServiceTests {

    private final ShoppingItemService shoppingItemService;
    private final ShoppingItemRepository shoppingItemRepository;

    @Autowired
    public ShoppingItemServiceTests(ShoppingItemService shoppingItemService, ShoppingItemRepository shoppingItemRepository) {
        this.shoppingItemService = shoppingItemService;
        this.shoppingItemRepository = shoppingItemRepository;
    }

    @BeforeEach
    void setUp() {
        shoppingItemRepository.deleteAll();
    }

    @Test
    @Transactional
    void check_Success_Create_Item() {
        ShoppingItem item = new ShoppingItem();
        item.setName("USB-cable");
        item.setPrice(500);
        item.setImage(null);
        item.setCreatedAt(LocalDateTime.now());
        item.setUsers(new ArrayList<>());

        shoppingItemService.create(item);

        assertEquals(item, shoppingItemRepository.getReferenceById(item.getId()));
    }

    @Test
    void check_Fail_Create_Item_Exists(){
        ShoppingItem item1 = new ShoppingItem();
        item1.setName("USB-cable");
        item1.setPrice(500);
        item1.setImage(null);
        item1.setCreatedAt(LocalDateTime.now());
        item1.setUsers(new ArrayList<>());

        shoppingItemRepository.save(item1);

        ShoppingItem item2 = new ShoppingItem();
        item2.setName("USB-cable");
        item2.setPrice(500);
        item2.setImage(null);
        item2.setCreatedAt(LocalDateTime.now());
        item2.setUsers(new ArrayList<>());

        ShoppingItemExistsException thrown = assertThrows(ShoppingItemExistsException.class, () -> shoppingItemService.create(item2));
        assertEquals("Item already exists!", thrown.getMessage());
        assertFalse(shoppingItemRepository.existsById(item2.getId()));
    }

    @Test
    @Transactional
    void check_Success_Read_Item_By_Id() {
        ShoppingItem item = new ShoppingItem();
        item.setName("USB-cable");
        item.setPrice(500);
        item.setImage(null);
        item.setCreatedAt(LocalDateTime.now());
        item.setUsers(new ArrayList<>());
        shoppingItemRepository.save(item);

        assertEquals(item, shoppingItemService.readById(item.getId()));
    }

    @Test
    void check_Fail_Read_Item_By_Id_Not_Exists(){
        ShoppingItemNotFoundException thrown = assertThrows(ShoppingItemNotFoundException.class, () -> shoppingItemService.readById(999L));
        assertEquals("Item not found!", thrown.getMessage());
    }

    @Test
    @Transactional
    void check_Success_Update_Item() {
        ShoppingItem oldItem = new ShoppingItem();
        oldItem.setName("USB-cable");
        oldItem.setPrice(500);
        oldItem.setImage(null);
        oldItem.setCreatedAt(LocalDateTime.now());
        oldItem.setUsers(new ArrayList<>());
        shoppingItemRepository.save(oldItem);

        ShoppingItem newItem = shoppingItemRepository.getReferenceById(oldItem.getId());
        oldItem.setName("USB-cable");
        oldItem.setPrice(900);
        shoppingItemService.update(newItem);

        assertEquals(newItem, shoppingItemRepository.getReferenceById(oldItem.getId()));
    }


    @Test
    void check_Success_delete_Item_By_Id() {
        ShoppingItem item = new ShoppingItem();
        item.setName("USB-cable");
        item.setPrice(500);
        item.setImage(null);
        item.setCreatedAt(LocalDateTime.now());
        item.setUsers(new ArrayList<>());
        shoppingItemRepository.save(item);

        shoppingItemService.delete(item.getId());

        assertFalse(shoppingItemRepository.existsById(item.getId()));
    }

    @Test
    void check_Fail_Delete_Item_By_Id_Not_Exists(){
        ShoppingItemNotFoundException thrown = assertThrows(ShoppingItemNotFoundException.class, () -> shoppingItemService.delete(999L));
        assertEquals("Item not found!", thrown.getMessage());
    }

    @Test
    void check_Success_Get_All_Items() {
        ShoppingItem item1 = new ShoppingItem();
        item1.setName("USB-cable");
        item1.setPrice(500);
        item1.setImage(null);
        item1.setCreatedAt(LocalDateTime.now());
        item1.setUsers(new ArrayList<>());
        shoppingItemRepository.save(item1);

        ShoppingItem item2 = new ShoppingItem();
        item2.setName("Type-C cable");
        item2.setPrice(500);
        item2.setImage(null);
        item2.setCreatedAt(LocalDateTime.now());
        item2.setUsers(new ArrayList<>());
        shoppingItemRepository.save(item2);

        ShoppingItem item3 = new ShoppingItem();
        item3.setName("Mouse");
        item3.setPrice(500);
        item3.setImage(null);
        item3.setCreatedAt(LocalDateTime.now());
        item3.setUsers(new ArrayList<>());
        shoppingItemRepository.save(item3);

        assertThat(shoppingItemService.getAll()).hasSize(3);
    }

    @Test
    void check_Fail_Get_All_Items_Not_Exists() {
        ShoppingItemNotFoundException thrown = assertThrows(ShoppingItemNotFoundException.class, shoppingItemService::getAll);
        assertEquals("Items not found!", thrown.getMessage());
    }
}