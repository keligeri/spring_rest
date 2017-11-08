package com.codecool.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.persistence.Id;

@Entity
@NamedQueries({
        @NamedQuery(name = "findAllPerson", query = "select p from Person p"),
        @NamedQuery(name = "findPersonByName", query = "select p from Person p where p.name = :personName"),
        @NamedQuery(name = "findPersonsByBetweenTwoAge",
                query = "select p from Person p where p.age between :personAge1 and :personAge2")
})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String name;
    private int age;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Address address;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
