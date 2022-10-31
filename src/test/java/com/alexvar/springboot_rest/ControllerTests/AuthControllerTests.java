package com.alexvar.springboot_rest.ControllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWebMvc
@AutoConfigureMockMvc
class AuthControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void get_Login_Page_Success_2xx() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"));
    }

    @Test
    void get_Register_Page_Success_2xx() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));
    }

    @Test
    void post_Register_Page_Success_2xx() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .param("name", "Andrei")
                        .param("surname", "Vol")
                        .param("email", "andreiVol@gmail.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void post_Register_Page_Validation_Error() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .param("name", "andrei")
                        .param("surname", "vol")
                        .param("email", "andreiVolgmail.com")
                        .param("password", ""))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeHasErrors())
                .andExpect(model().hasErrors())
                .andExpect(view().name("register"));
    }
}