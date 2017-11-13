package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.User;
import com.codecool.spring.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(long id) {
        return userRepository.findOne(id);
    }

    public void update(long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(long id) {
        userRepository.delete(id);
    }

}
