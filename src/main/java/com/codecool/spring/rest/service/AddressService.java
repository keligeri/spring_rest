package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final PersonService personService;
    private Address address;

    @Autowired
    public AddressService(AddressRepository addressRepository, PersonService personService) {
        this.addressRepository = addressRepository;
        this.personService = personService;
    }

    public void delete(Address address) {
        deleteDependency(address);
        addressRepository.delete(address);
    }

    private void deleteDependency(Address address) {
        personService.deleteAddress(address.getPersons());
        address.setPersons(null);
    }

    public void update(long id, Address address) {
        address.setId(id);
        addressRepository.save(address);
    }

}
