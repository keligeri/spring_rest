package com.codecool.spring.rest.repository;

import com.codecool.spring.rest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
