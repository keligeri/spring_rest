package com.codecool.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String role;

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<User> users;

    public Role() {}

    public Role(String role, List<User> users) {
        this.role = role;
        this.users = users;
    }

    public Role(String role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
