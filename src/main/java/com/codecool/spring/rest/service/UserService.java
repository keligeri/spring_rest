package com.codecool.spring.rest.service;

import com.codecool.spring.rest.authentication.LoggedInChecker;
import com.codecool.spring.rest.exception.EntityAlreadyExistsException;
import com.codecool.spring.rest.exception.EntityNotFoundException;
import com.codecool.spring.rest.model.User;
import com.codecool.spring.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoggedInChecker loggedInChecker;

    @Autowired
    public UserService(UserRepository userRepository, LoggedInChecker loggedInChecker) {
        this.userRepository = userRepository;
        this.loggedInChecker = loggedInChecker;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(long id) {
        return userRepository.findOne(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void update(long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    public void save(User user) throws EntityNotFoundException {
        if (findByUsername(user.getUsername()) != null) {
            throw new EntityAlreadyExistsException("Enttiy already exists!");
        }

        userRepository.save(user);
    }

    public void delete(long id) {
        userRepository.delete(id);
    }

    public User getCurrentUser() {
        return loggedInChecker.getLoggedInUser();
    }

}
