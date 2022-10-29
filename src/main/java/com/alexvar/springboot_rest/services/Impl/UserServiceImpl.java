package com.alexvar.springboot_rest.services.Impl;

import com.alexvar.springboot_rest.exception.NullEntityException;
import com.alexvar.springboot_rest.exception.UserExistsException;
import com.alexvar.springboot_rest.exception.UserNotFoundException;
import com.alexvar.springboot_rest.model.Role;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.repositories.UserRepository;
import com.alexvar.springboot_rest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static com.alexvar.springboot_rest.SpringbootRestApplication.passwordEncoder;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            log.warn("User with id {} already exists", user.getId());
            throw new UserExistsException("User already exists!");
        }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            log.trace("User with id {} saved", user.getId());
            return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
        if(!userRepository.existsById(id)){
            log.warn("User with id {} not found!", id);
            throw new UserNotFoundException("User not found!");
        }

        log.trace("Get user with id {}", id);
        return userRepository.getReferenceById(id);
    }

    @Override
    public User update(User user) {
        if(user == null){
            log.warn("User is null");
            throw new NullEntityException("User is null!");
        }
        User newEmp = userRepository.findById(user.getId()).get();
        newEmp.setName(user.getName());
        newEmp.setSurname(user.getSurname());
        newEmp.setPassword(passwordEncoder.encode(user.getPassword()));

        log.trace("User with id {} updated", user.getId());
        return userRepository.save(newEmp);
    }

    @Override
    public void delete(long id) {
        if(!userRepository.existsById(id)){
            log.warn("User with id {} not found", id);
            throw new UserNotFoundException("User not found!");
        }

        log.trace("User with id {} successfully deleted", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        if(userRepository.findAll().isEmpty()){
            log.warn("Users not found");
            throw new UserNotFoundException("Users not found!");
        }

        log.trace("Get all users from the DB");
        return userRepository.findAll();
    }
}
