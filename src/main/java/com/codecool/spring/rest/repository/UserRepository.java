package com.codecool.spring.rest.repository;

import com.codecool.spring.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
