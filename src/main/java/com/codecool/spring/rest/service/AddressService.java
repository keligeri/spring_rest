package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    public void delete(long id) {
        Address address = addressRepository.findOne(id);
        address.getPersons().forEach(person -> person.setAddress(null));
        addressRepository.delete(address);
    }

    public void update(long id, Address address) {
        address.setId(id);
        addressRepository.save(address);
    }

    Address saveOrUpdateAddress(Person person) {
        Address current = person.getAddress();
        Address address = addressRepository.findByCityAndZipCode(current.getCity(), current.getZipCode());
        if (address == null) {
            addressRepository.save(current);
            return current;
        }
        return address;
    }

}
