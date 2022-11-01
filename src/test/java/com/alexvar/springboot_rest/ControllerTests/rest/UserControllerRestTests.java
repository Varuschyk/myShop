package com.alexvar.springboot_rest.ControllerTests.rest;

import com.alexvar.springboot_rest.controller.rest.UserControllerRest;
import com.alexvar.springboot_rest.exception.ApiUserException;
import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.security.SecurityUser;
import com.alexvar.springboot_rest.services.UserService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerRestTests {

    @Autowired
    private UserRepository userRepository; //by default has 4 users in DB

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = setupAdminUser();
    }

    private User setupAdminUser(){
            User user = new User(1, "Bogdan", "Rud",
                    "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                    "bogdanRud@gmail.com", Role.ADMIN, new HashSet<>());
            return userRepository.save(user);
    }

    @Test
    void return_All_Users_List() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(user(new SecurityUser(user))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Matchers.is("Alex")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Ruslana")))
                .andExpect(jsonPath("$[2].name", Matchers.is("Misha")))
                .andExpect(jsonPath("$[3].name", Matchers.is("Kirill")));

    }

    @Test
    void check_Create_User() throws Exception {
        mockMvc.perform(post("/api/users")
                .with(user(new SecurityUser(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Richard\", \"surname\": \"Cro\", \"email\": \"richardCro@gmail.com\"" +
                                ", \"password\": \"1234\"}"))
                .andExpect(status().isCreated());

        assertThat(userRepository.findUserByEmail("richardCro@gmail.com")).isPresent();
    }

    @Test
    void check_Fail_Create_User() throws Exception {
        mockMvc.perform(post("/api/users")
                        .with(user(new SecurityUser(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"richard\", \"surname\": \"cro\", \"email\": \"richardCro@gmail.com\"" +
                                ", \"password\": \"1234\"}"))
                .andExpect(status().isBadRequest());

        assertThat(userRepository.findUserByEmail("richardCro@gmail.com")).isNotPresent();
    }

    @Test
    void check_Read_User() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .with(user(new SecurityUser(user))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.is("Bogdan")))
                .andExpect(jsonPath("$.surname", Matchers.is("Rud")))
                .andExpect(jsonPath("$.email", Matchers.is("bogdanRud@gmail.com")));
    }

    @Test
    void check_Fail_Read_User() throws Exception {
        mockMvc.perform(get("/api/users/999")
                        .with(user(new SecurityUser(user))))
                .andExpect(status().isNotFound());
    }

    @Test
    void check_Update_User() throws Exception {
        mockMvc.perform(patch("/api/users/9")
                        .with(user(new SecurityUser(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Richard\", \"surname\": \"Cro\"" +
                                ", \"password\": \"123456789\"}"))
                .andExpect(status().isOk());

        assertThat(userRepository.findUserByEmail("mishaKos@gmail.com")).isPresent();
        assertEquals("Richard Cro", userRepository.findUserByEmail("mishaKos@gmail.com").orElseThrow(ApiUserException::new).getFullName());
    }

    @Test
    void check_Fail_Update_User() throws Exception {
        mockMvc.perform(patch("/api/users/1")
                        .with(user(new SecurityUser(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"richard\", \"surname\": \"cro\"" +
                                ", \"password\": \"123456789\"}"))
                .andExpect(status().isBadRequest());

        assertThat(userRepository.findUserByEmail("bogdanRud@gmail.com")).isPresent();
        assertEquals("Bogdan Rud", userRepository.findUserByEmail("bogdanRud@gmail.com").orElseThrow(ApiUserException::new).getFullName());
    }

    @Test
    //@Order(20)
    void check_Delete_User() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new SecurityUser(user))))
                .andExpect(status().isOk());

        assertThat(userRepository.findUserByEmail("bogdanRud@gmail.com")).isNotPresent();
    }

    @Test
    void check_Fail_Delete_User() throws Exception {
        mockMvc.perform(delete("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new SecurityUser(user))))
                .andExpect(status().isNotFound());
    }
}