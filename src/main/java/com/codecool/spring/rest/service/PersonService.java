package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void deleteAddress(Set<Person> persons) {
        for (Person person : persons) {
            person.setAddress(null);
        }
    }

}
