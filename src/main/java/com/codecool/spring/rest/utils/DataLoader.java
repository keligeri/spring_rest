package com.codecool.spring.rest.utils;

import com.codecool.spring.rest.Application;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;

    @Autowired
    public DataLoader(AddressRepository addressRepository, PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Person gergo = new Person("Gergő", 24, new Address(8900L, "Zalaegerszeg"));
        Person pisti = new Person("Pista", 20, new Address(1146L, "Budapest"));

        Address zeg = new Address(8900L, "Zalaegerszeg");
        Address pest = new Address(1146L, "Budapest");
//        addressRepository.save(zeg);
//        addressRepository.save(pest);

        gergo.setAddress(zeg);
        pisti.setAddress(pest);

        personRepository.save(gergo);
        personRepository.save(pisti);

    }
}
