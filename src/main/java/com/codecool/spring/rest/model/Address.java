package com.codecool.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private long zipCode;
    private String city;

    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private Set<Person> persons = new HashSet<>();

    public Address() {}

    public Address(long zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public Address(long zipCode, String city, Set<Person> persons) {
        this.zipCode = zipCode;
        this.city = city;
        this.persons = persons;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getZipCode() {
        return zipCode;
    }

    public void setZipCode(long zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (zipCode != address.zipCode) return false;
        if (!city.equals(address.city)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * (int) (zipCode ^ (zipCode >>> 32));
        result = 31 * result + city.hashCode();
        return result;
    }
}
