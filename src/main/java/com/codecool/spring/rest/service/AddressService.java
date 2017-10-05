package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final PersonService personService;

    public AddressService(AddressRepository addressRepository, PersonService personService) {
        this.addressRepository = addressRepository;
        this.personService = personService;
    }

    public void deleteDependency(Address address) {
        personService.deleteAddress(address.getPersons());
        address.setPersons(null);
    }

    public void update(long id, Address address) {
        Address updatedAddress = addressRepository.findOne(id);

    }
}
