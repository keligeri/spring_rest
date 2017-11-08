package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final AddressService addressService;

    @Autowired
    public  PersonService(PersonRepository personRepository, AddressService addressService) {
        this.personRepository = personRepository;
        this.addressService = addressService;
    }

    public List<Person> findAll() { return personRepository.findAll(); }

    public Person findById(long id) { return personRepository.findOne(id); }

    public void save(Person person) {
        if (person.getAddress() != null) {
            Address address = addressService.saveOrUpdateAddress(person);
            person.setAddress(address);
        }

        personRepository.save(person);
    }

    public void update(long id, Person updatedPerson) {
        updatedPerson.setId(id);
        Address address = addressService.saveOrUpdateAddress(updatedPerson);
        updatedPerson.setAddress(address);

        personRepository.save(updatedPerson);
    }

    public void delete(long id) {
        personRepository.delete(id);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }
}
