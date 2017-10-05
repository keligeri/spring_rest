package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public  PersonService(PersonRepository personRepository, AddressRepository addressRepository) {
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
    }

    public void savePerson(Person person) {
        Address address = saveOrUpdateAddress(person);
        person.setAddress(address);
        personRepository.save(person);
    }

    private Address saveOrUpdateAddress(Person person) {
        Address current = person.getAddress();
        Address address = addressRepository.findByCityAndZipCode(current.getCity(), current.getZipCode());
        if (address == null) {
            addressRepository.save(current);
            return current;
        }
        return address;
    }

    void deleteAddress(Set<Person> persons) {
        persons.forEach(person -> person.setAddress(null));
    }

    public void update(long id, Person updatedPerson) {
        updatedPerson.setId(id);
        Address address = saveOrUpdateAddress(updatedPerson);
        updatedPerson.setAddress(address);

        personRepository.save(updatedPerson);
    }
}
