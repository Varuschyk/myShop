package com.alexvar.springboot_rest.services.Impl;

import com.alexvar.springboot_rest.exception.NullEntityException;
import com.alexvar.springboot_rest.exception.UserExistsException;
import com.alexvar.springboot_rest.exception.UserNotFoundException;
import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserExistsException("User already exists!");
        }else if(user == null){
            throw new NullEntityException("User is null!");
        }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User not found!");
        }
        return userRepository.getReferenceById(id);
    }

    @Override
    public User update(User user) {
        if(user == null){
            throw new NullEntityException("User is null!");
        }
        User newEmp = userRepository.findById(user.getId()).get();
        newEmp.setName(user.getName());
        newEmp.setSurname(user.getSurname());
        newEmp.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(newEmp);
    }

    @Override
    public void delete(long id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User not found!");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        if(userRepository.findAll().isEmpty()){
            throw new UserNotFoundException("Users not found!");
        }
        return userRepository.findAll();
    }
}
