package com.alexvar.springboot_rest.controller.rest;

import com.alexvar.springboot_rest.dto.UserDto;
import com.alexvar.springboot_rest.exception.ApiUserException;
import com.alexvar.springboot_rest.model.User;
import com.alexvar.springboot_rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserControllerRest {

    private final UserService userService;

    @Autowired
    public UserControllerRest(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('developer:write')")
    public List<UserDto> readAll(){
        return userService.getAll()
                .stream()
                .map(UserDto::new)
                .toList();
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('developer:write')")
    public ResponseEntity<UserDto> create(@RequestBody @Valid User user, BindingResult result) throws ApiUserException{

        if(result.hasErrors()){
            throw new ApiUserException("Something wrong by create user");
        }

        userService.create(user);

        return new ResponseEntity<>(new UserDto(user), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public UserDto read(@PathVariable(value = "id") long id){
        return new UserDto(userService.readById(id));
    }

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public ResponseEntity<User> update(@PathVariable(value = "id") long id, @RequestBody @Valid User user, BindingResult result) throws ApiUserException {

        User oldUser = userService.readById(id);

        if(result.hasErrors()){
            throw new ApiUserException("Something wrong by update user");
        }

        user.setId(id);
        user.setEmail(oldUser.getEmail());
        user.setRole(oldUser.getRole());
        user.setStuffList(oldUser.getStuffList());
        userService.update(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('developer:write')")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") long id){

        userService.delete(id);

        return new ResponseEntity<>("User is deleted successfully", HttpStatus.OK);
    }


}
