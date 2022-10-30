package com.alexvar.springboot_rest.RepositoryTests;

import com.alexvar.springboot_rest.model.*;
import com.alexvar.springboot_rest.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkFindUserByEmail() {
        User user = new User(1, "Bogdan", "Rud",
                "$2a$12$KsVy57HckgKGZ63rIYwrLODhkHSSv1nWx5K.gIdUyJrBlYjqbGnFa",
                "bogdanRud@gmail.com", Role.USER, new HashSet<>());
        userRepository.save(user);


        User result = userRepository.findUserByEmail(user.getEmail()).get();


        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }
}