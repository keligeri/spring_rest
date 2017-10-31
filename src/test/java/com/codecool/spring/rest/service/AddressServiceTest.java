package com.codecool.spring.rest.service;

import com.codecool.spring.rest.Application;
import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class AddressServiceTest {

    @Autowired private AddressService addressService;
    @Autowired private AddressRepository addressRepository;

    @Autowired private PersonRepository personRepository;

    private Address zalaegerszeg;
    private Address budapest;

    private Person geza;
    private Person sanyi;

    @Before
    public void setup() throws Exception {
        this.personRepository.deleteAll();
        this.addressRepository.deleteAll();

        updateDb();
    }

    private void updateDb() {
        this.budapest = new Address(1146, "Budapest");
        this.zalaegerszeg = new Address(8900, "Babosdöbréte");

        this.addressRepository.save(budapest);
        this.addressRepository.save(zalaegerszeg);

        this.sanyi = new Person("Sándorka", 44, budapest);
        this.geza = new Person("GézaFiam", 17, zalaegerszeg);

        this.personRepository.save(sanyi);
        this.personRepository.save(geza);
    }

    @Test
    public void save_AddPersonToo_IfPostAddress() {
        Address newAddress = new Address(2500, "Gyula");
        Person newPerson = new Person("új személy", 44);
//        addressService.save();

    }

    @Test
    public void update_CityEquals_IfUpdateAddressCity() {
        budapest.setCity("NemBudapest");
        long id = budapest.getId();
        addressService.update(id, budapest);
        assertEquals("NemBudapest", addressRepository.findOne(id).getCity());
    }

    @Test
    public void update_ZipCodeEquals_IfUpdateAddressZipCode() {
        budapest.setZipCode(5000);
        long id = budapest.getId();
        addressService.update(id, budapest);
        assertEquals(5000, addressRepository.findOne(id).getZipCode());
    }

}
