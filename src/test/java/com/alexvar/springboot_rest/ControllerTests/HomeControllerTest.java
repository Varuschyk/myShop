package com.alexvar.springboot_rest.ControllerTests;

import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.security.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWebMvc
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private User setupSimpleUser(){
        User user = new User(1, "Bogdan", "Rud",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "bogdanRud@gmail.com", Role.USER, new HashSet<>());
        return userRepository.save(user);
    }

    @Test
    void get_Home_Page_Success_2xx() throws Exception {
        mockMvc.perform(get("/home")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"));
    }

    @Test
    void fail_Get_Home_Page_401() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isUnauthorized());
    }
}