package com.alexvar.springboot_rest.ServiceTests;

import com.alexvar.springboot_rest.exception.UserExistsException;
import com.alexvar.springboot_rest.exception.UserNotFoundException;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

//    @Mock
//    private BindingResult result;
    private final UserService userService;
    private final UserRepository userRepository;


    @Autowired
    public UserServiceTests(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void check_Success_Create_User() {
        User user = new User();
        user.setName("Bogdan");
        user.setSurname("Krav");
        user.setEmail("bogdanKrav@email.com");
        user.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        userService.create(user);

        assertEquals(user, userRepository.getReferenceById(user.getId()));
    }

    @Test
    void check_Fail_Create_User_Exists(){
        User user1 = new User();
        user1.setName("Bogdan");
        user1.setSurname("Krav");
        user1.setEmail("bogdanKrav@email.com");
        user1.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Bogdan");
        user2.setSurname("Krav");
        user2.setEmail("bogdanKrav@email.com");
        user2.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        UserExistsException thrown = assertThrows(UserExistsException.class, () -> userService.create(user2));
        assertEquals("User already exists!", thrown.getMessage());
        assertFalse(userRepository.existsById(user2.getId()));
    }

    @Test
    @Transactional
    void check_Success_Read_User_By_Id() {
        User user = new User();
        user.setName("Bogdan");
        user.setSurname("Krav");
        user.setEmail("bogdanKrav@email.com");
        user.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");
        userRepository.save(user);

        assertEquals(user, userService.readById(user.getId()));
    }

    @Test
    void check_Fail_Read_User_By_Id_Not_Exists(){
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.readById(999L));
        assertEquals("User not found!", thrown.getMessage());
    }

    @Test
    @Transactional
    void check_Success_Update_User() {
        User oldUser = new User();
        oldUser.setName("Bogdan");
        oldUser.setSurname("Krav");
        oldUser.setEmail("bogdanKrav@email.com");
        oldUser.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");
        userRepository.save(oldUser);

        User newUser = userRepository.getReferenceById(oldUser.getId());
        newUser.setPassword("12345");
        newUser.setName("Igor");
        newUser.setSurname("Vir");
        userService.update(newUser);

        assertEquals(newUser, userRepository.getReferenceById(oldUser.getId()));
    }


    @Test
    void check_Success_delete_User_By_Id() {
        User user = new User();
        user.setName("Bogdan");
        user.setSurname("Krav");
        user.setEmail("bogdanKrav@email.com");
        user.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");
        userRepository.save(user);

        userService.delete(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    void check_Fail_Delete_User_By_Id_Not_Exists(){
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> userService.delete(999L));
        assertEquals("User not found!", thrown.getMessage());
    }

    @Test
    void check_Success_Get_All_Users() {
        User user1 = new User();
        user1.setName("Bogdan");
        user1.setSurname("Krav");
        user1.setEmail("bogdanKrav@email.com");
        user1.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Vlad");
        user2.setSurname("Vlad");
        user2.setEmail("vlad@email.com");
        user2.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("Dima");
        user3.setSurname("Dima");
        user3.setEmail("dima@email.com");
        user3.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");
        userRepository.save(user3);

        assertThat(userService.getAll()).hasSize(3);
    }

    @Test
    void check_Fail_Get_All_Users_Not_Exists() {
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, userService::getAll);
        assertEquals("Users not found!", thrown.getMessage());
    }
}