package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address findById(long id) {
        return addressRepository.findOne(id);
    }

    public Iterable<Address> findAll(){
        return addressRepository.findAll();
    }

    public void save(Address address) {
        Set<Person> persons = address.getPersons();
        persons.forEach(person -> person.setAddress(address));
        addressRepository.save(address);
    }

    public void delete(long id) {
        Address address = addressRepository.findOne(id);

        address.getPersons().forEach(person -> person.setAddress(null));
        addressRepository.delete(address);
    }

    // Doesn't work well
    public void update(long id, Address address) {
//        Address prevAddress = addressRepository.findOne(id);
//        prevAddress.getPersons().forEach(person -> person.setAddress(null));    // person still exists without address

        address.setId(id);
        address.getPersons().forEach(person -> person.setAddress(address));
        address.getPersons().forEach(person -> System.out.println(person.getName()));

        addressRepository.save(address);
    }

}
