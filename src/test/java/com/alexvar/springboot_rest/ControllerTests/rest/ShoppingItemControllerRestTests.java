package com.alexvar.springboot_rest.ControllerTests.rest;

import com.alexvar.springboot_rest.exception.ApiShoppingItemException;
import com.alexvar.springboot_rest.exception.ApiUserException;
import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.security.SecurityUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ShoppingItemControllerRestTests {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository; //by default has 2 items in DB

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private User setupAdminUser(){
        User user = new User(1, "Bogdan", "Rud",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "bogdanRud@gmail.com", Role.ADMIN, new HashSet<>());
        return userRepository.save(user);
    }

    @Test
    void return_All_Items_List() throws Exception {
        mockMvc.perform(get("/api/items")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Microwave")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Fridge")));
    }

    @Test
    void check_Create_Item() throws Exception {
        mockMvc.perform(post("/api/items")
                        .with(user(new SecurityUser(setupAdminUser())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"USB\", \"price\": \"500\"}"))
                .andExpect(status().isCreated());

        assertThat(shoppingItemRepository.findShoppingItemByName("USB")).isPresent();
    }

    @Test
    void check_Fail_Create_Item() throws Exception {
        mockMvc.perform(post("/api/items")
                        .with(user(new SecurityUser(setupAdminUser())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"USB\", \"price\": \"-1\"}"))
                .andExpect(status().isBadRequest());

        assertThat(shoppingItemRepository.findShoppingItemByName("USB")).isNotPresent();
    }

    @Test
    void check_Read_Item() throws Exception {
        mockMvc.perform(get("/api/items/4")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.is("Microwave")))
                .andExpect(jsonPath("$.price", Matchers.is(50.0)));
    }

    @Test
    void check_Fail_Read_Item() throws Exception {
        mockMvc.perform(get("/api/items/999")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isNotFound());
    }

    @Test
    void check_Update_Item() throws Exception {
        mockMvc.perform(patch("/api/items/4")
                        .with(user(new SecurityUser(setupAdminUser())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Microwave 3000\", \"price\": \"500\"}"))
                .andExpect(status().isOk());

        assertThat(shoppingItemRepository.findShoppingItemByName("Microwave 3000")).isPresent();
        assertEquals("Microwave 3000", shoppingItemRepository.findShoppingItemByName("Microwave 3000").orElseThrow(ApiShoppingItemException::new).getName());
    }

    @Test
    void check_Fail_Update_Item() throws Exception {
        mockMvc.perform(patch("/api/items/4")
                        .with(user(new SecurityUser(setupAdminUser())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Microwave 3000\", \"price\": \"-1\"}"))
                .andExpect(status().isBadRequest());

        assertThat(shoppingItemRepository.findShoppingItemByName("Microwave")).isPresent();
    }

    @Test
    void check_Delete_Item() throws Exception {
        mockMvc.perform(delete("/api/items/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isOk());

        assertThat(shoppingItemRepository.findShoppingItemByName("Fridge")).isNotPresent();
    }

    @Test
    void check_Fail_Delete_Item() throws Exception {
        mockMvc.perform(delete("/api/items/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isNotFound());
    }

}