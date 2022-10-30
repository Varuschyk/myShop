package com.alexvar.springboot_rest.ControllerTests;

import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.ShoppingItem;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.ShoppingItemRepository;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.security.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private User setupAdminUser(){
        User user = new User(1, "Bogdan", "Rud",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "bogdanRud@gmail.com", Role.ADMIN, new HashSet<>());
        return userRepository.save(user);
    }

    private User setupSimpleUser(){
     User user = new User(1, "Bogdan", "Rud",
             "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
             "bogdanRud@gmail.com", Role.USER, new HashSet<>());
     return userRepository.save(user);
    }

    @Test
    void go_To_Get_All_Users_Page_2xx() throws Exception {
        mockMvc.perform(get("/users/all")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(model().attributeExists("users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("all-users"));
    }

    @Test
    void fail_Go_To_Get_All_Users_Page_401() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Go_To_Get_All_Users_Page_403() throws Exception {
      mockMvc.perform(get("/users/all")
                      .with(user(new SecurityUser(setupSimpleUser()))))
              .andExpect(model().attributeDoesNotExist("users"))
              .andExpect(status().isForbidden())
              .andExpect(view().name("403"));
    }

    @Test
    void go_To_Create_User_Page_2xx() throws Exception {
       mockMvc.perform(get("/users/create")
               .with(user(new SecurityUser(setupAdminUser()))))
               .andExpect(model().attributeExists("user"))
               .andExpect(status().is2xxSuccessful())
               .andExpect(view().name("user-create"));
    }

    @Test
    void fail_Go_To_Create_User_Page_401() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Go_To_Create_User_Page_403() throws Exception {
        mockMvc.perform(get("/users/create")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeDoesNotExist("user"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("403"));
    }

    @Test
    void create_User_Post_Success_3xx() throws Exception {
       mockMvc.perform(post("/users/create")
                       .param("name", "Alexandro")
                       .param("surname", "Brice")
                       .param("email", "alexandroBrice@gmail.com")
                       .param("password", "1234")
               .with(user(new SecurityUser(setupAdminUser()))))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(model().hasNoErrors())
               .andExpect(status().is3xxRedirection());
    }

    @Test
    void fail_create_User_Post_Validation_Error() throws Exception {
        mockMvc.perform(post("/users/create")
                .param("name", "alexandro")
                .param("surname", "brice")
                .param("email", "alexandroBrice")
                .param("password", "1234")
                .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().hasErrors())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void read_User_Get_Success_2xx() throws Exception{
        mockMvc.perform(get("/users/read/1")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user-info"));
    }

    @Test
    void fail_Read_User_Get_403() throws Exception{
        User user = new User(2, "Michel", "Jackson",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "michelJackson@gmail.com", Role.ADMIN, new HashSet<>());
        userRepository.save(user);

        mockMvc.perform(get("/users/read/2")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeDoesNotExist("user"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("403"));
    }

    @Test
    void fail_Read_User_Get_401() throws Exception{
        mockMvc.perform(get("/users/read/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Read_User_Get_404() throws Exception{
        mockMvc.perform(get("/users/read/999")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404"));
    }

    @Test
    void update_User_Get_Page_Success_2xx() throws Exception{
        mockMvc.perform(get("/users/update/1")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void fail_Update_User_Get_Page_403() throws Exception{
        User user = new User(2, "Michel", "Jackson",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "michelJackson@gmail.com", Role.ADMIN, new HashSet<>());
        userRepository.save(user);

        mockMvc.perform(get("/users/update/2")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().isForbidden())
                .andExpect(model().attributeDoesNotExist("user"))
                .andExpect(view().name("403"));
    }

    @Test
    void fail_Update_User_Get_Page_401() throws Exception{
        mockMvc.perform(get("/users/update/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Update_User_Get_Page_404() throws Exception{
        mockMvc.perform(get("/users/update/999")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404"));
    }

    @Test
    void update_User_Post_Success_3xx() throws Exception{
       mockMvc.perform(post("/users/update/1")
                       .param("name", "Michel")
                       .param("surname", "Jackson")
                       .param("password", "12345678")
               .with(user(new SecurityUser(setupSimpleUser()))))
               .andExpect(model().attributeHasNoErrors())
               .andExpect(model().hasNoErrors())
               .andExpect(status().is3xxRedirection());
    }

    @Test
    void fail_Update_User_Post_Page_404() throws Exception{
        mockMvc.perform(post("/users/update/999")
                        .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404"));
    }

    @Test
    void fail_update_User_Post_Validation_Error() throws Exception {
        mockMvc.perform(post("/users/update/1")
                        .param("name", "michel")
                        .param("surname", "jackson")
                        .param("password", "12345678")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().hasErrors())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void delete_User_Success_3xx() throws Exception {
        mockMvc.perform(get("/users/delete/1")
                .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void fail_Delete_User_403() throws Exception {
        mockMvc.perform(get("/users/delete/1")
                        .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(status().isForbidden())
                .andExpect(view().name("403"));
    }

    @Test
    void fail_Delete_User_401() throws Exception{
        mockMvc.perform(get("/users/delete/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fail_Delete_User_404() throws Exception{
        mockMvc.perform(get("/users/delete/999")
                .with(user(new SecurityUser(setupAdminUser()))))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404"));
    }

    @Test
    @Disabled("something wrong")
    void read_User_Shopping_Items_Success_2xx() throws Exception {
//        User user = new User(2, "Michel", "Jackson",
//                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
//                "michelJackson@gmail.com", Role.ADMIN, new HashSet<>());
//
//        userRepository.save(user);

        mockMvc.perform(get("/users/1/items")
                .with(user(new SecurityUser(setupSimpleUser()))))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().hasNoErrors())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void add_Shopping_Item_Success_3xx() throws Exception {
        ShoppingItem item = new ShoppingItem();
        item.setId(1);
        item.setName("Fork");
        item.setPrice(500);
        item.setCreatedAt(LocalDateTime.now());

        shoppingItemRepository.save(item);

        mockMvc.perform(get("/users/1/add")
                        .with(user(new SecurityUser(setupSimpleUser())))
                        .param("item_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/{id}/items"));
    }

    @Test
    void remove_Shopping_Item_Success_3xx() throws Exception {
        ShoppingItem item = new ShoppingItem();
        item.setId(1);
        item.setName("Fork");
        item.setPrice(500);
        item.setCreatedAt(LocalDateTime.now());

        shoppingItemRepository.save(item);

        mockMvc.perform(get("/users/1/remove")
                        .with(user(new SecurityUser(setupSimpleUser())))
                        .param("item_id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/{id}/items"));
    }
}