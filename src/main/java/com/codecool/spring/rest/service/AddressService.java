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

    public Address saveOrUpdateAddress(Person person) {
        Address current = person.getAddress();
        Address address = addressRepository.findByCityAndZipCode(current.getCity(), current.getZipCode());
        if (address == null) {
            addressRepository.save(current);
            return current;
        }
        return address;
    }

    public void deleteDependency(Address address) {
        personService.deleteAddress(address.getPersons());
        address.setPersons(null);
    }

}
