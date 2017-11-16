package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.exception.EntityAlreadyExistsException;
import com.codecool.spring.rest.exception.EntityNotFoundException;
import com.codecool.spring.rest.model.User;
import com.codecool.spring.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final String statusOk = "{\"status\": \"ok\"}";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", ""})
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public User getById(@PathVariable long id) throws EntityNotFoundException {
        isValidUserId(id);
        return userService.findOne(id);
    }

    @PostMapping(value = {"/", ""})
    public String saveUser(@RequestBody User user) throws EntityAlreadyExistsException {
        userService.save(user);
        return statusOk;
    }

    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable long id) {
        isValidUserId(id);
        userService.delete(id);
        return statusOk;
    }

    @PutMapping(value = "/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user) {
        isValidUserId(id);
        userService.update(id, user);
        return statusOk;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public void handleUserNotFound(EntityNotFoundException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public void handleUserAlreadyExists(EntityAlreadyExistsException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    private void isValidUserId(long id) throws EntityNotFoundException {
        User user = userService.findOne(id);
        if (user == null) {
            throw new EntityNotFoundException("User with " + id + "not found!");
        }
    }
}
