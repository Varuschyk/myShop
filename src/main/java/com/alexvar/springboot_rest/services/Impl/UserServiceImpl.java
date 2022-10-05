package com.alexvar.springboot_rest.services.Impl;

import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
//import com.alexvar.springboot_rest.repositories.RoleRepository;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new EntityNotFoundException("User already exists!");
        }
            user.setRole(Role.USER);
            return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public User update(User user) {
        User newEmp = userRepository.findById(user.getId()).get();
        newEmp.setName(user.getName());
        newEmp.setSurname(user.getSurname());
        newEmp.setPassword(user.getPassword());
        newEmp.setRole(user.getRole());

        return userRepository.save(newEmp);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
