package com.alexvar.springboot_rest.ControllerTests;

import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.security.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ShoppingItemControllerTests {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        setupShoppingItems();
    }

    private User setupSimpleUser(){
        User user = new User(1, "Bogdan", "Rud",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "bogdanRud@gmail.com", Role.USER, new HashSet<>());
        return userRepository.save(user);
    }

    private void setupShoppingItems(){
        ShoppingItem item1 = new ShoppingItem();
        item1.setId(1L);
        item1.setName("Fridge");
        item1.setPrice(500);
        item1.setCreatedAt(LocalDateTime.now());

        shoppingItemRepository.save(item1);

        ShoppingItem item2 = new ShoppingItem();
        item2.setId(2L);
        item2.setName("Microwave");
        item2.setPrice(500);
        item2.setCreatedAt(LocalDateTime.now());

        shoppingItemRepository.save(item2);
    }

    @Test
    void get_All_Shopping_Items_Success_2xx() throws Exception {
        mockMvc.perform(get("/shopping_items/all")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeExists("items"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("all-items"));
    }

    @Test
    void fail_Get_All_Shopping_Items_401() throws Exception {
        mockMvc.perform(get("/shopping_items/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void go_To_Create_Shopping_Item_Page_2xx() throws Exception {
        mockMvc.perform(get("/shopping_items/create")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("item-create"));
    }

    @Test
    void fail_go_To_Create_Shopping_Item_Page_401() throws Exception {
        mockMvc.perform(get("/shopping_items/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_Shopping_Item_Post_Success_3xx() throws Exception {
        mockMvc.perform(post("/shopping_items/create")
                .param("name", "Phone")
                .param("price", "500")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/shopping_items/all"));
    }

    @Test
    void create_Shopping_Item_Post_Validation_Error() throws Exception {
        mockMvc.perform(post("/shopping_items/create")
                        .param("name", "")
                        .param("price", "-1")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeHasErrors())
                .andExpect(model().hasErrors())
                .andExpect(view().name("item-create"));
    }


    @Test
    void read_Shopping_Item_Get_Success_2xx() throws Exception{
        mockMvc.perform(get("/shopping_items/read/1")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("item-info"));
    }

    @Test
    void fail_Read_Shopping_Item_Get_404() throws Exception{
        mockMvc.perform(get("/shopping_items/read/999")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeDoesNotExist("item"))
                .andExpect(view().name("404"));
    }

    @Test
    void fail_Read_Shopping_Item_Get_401() throws Exception{
        mockMvc.perform(get("/shopping_items/read/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_Shopping_Item_Get_Page_Success_2xx() throws Exception{
        mockMvc.perform(get("/shopping_items/update/1")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("item-update"));
    }

    @Test
    void fail_Update_Shopping_Item_Get_Page_401() throws Exception{
        mockMvc.perform(get("/shopping_items/update/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Update_Shopping_Item_Post_401() throws Exception{
        mockMvc.perform(post("/shopping_items/update/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_Shopping_Item_Post_3xx() throws Exception{
        mockMvc.perform(post("/shopping_items/update/2")
                        .param("name", "Microwave 3000")
                        .param("price", "550")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/shopping_items/read/{id}"));
    }

    @Test
    void update_Shopping_Item_Post_Validation_Error() throws Exception{
        mockMvc.perform(post("/shopping_items/update/2")
                        .param("name", "Microwave 3000")
                        .param("price", "-1")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeHasErrors())
                .andExpect(model().hasErrors())
                .andExpect(view().name("item-update"));
    }

    @Test
    void delete_Shopping_Item_Success_3xx() throws Exception {
        mockMvc.perform(get("/shopping_items/delete/1")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/shopping_items/all"));
    }

    @Test
    void fail_Delete_Shopping_Item_401() throws Exception{
        mockMvc.perform(get("/shopping_items/delete/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Delete_Shopping_Item_404() throws Exception{
        mockMvc.perform(get("/shopping_items/delete/999")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404"));
    }

}