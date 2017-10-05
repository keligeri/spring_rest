package com.codecool.spring.rest;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner runner(AddressRepository addressRepository,
                                     PersonRepository personRepository) {
        return args -> {
            Person gergo = new Person("Gergő", 24);
            Person pisti = new Person("Pista", 20);

            Address zeg = new Address(8900L, "Zalaegerszeg");
            Address pest = new Address(1146L, "Budapest");
            addressRepository.save(zeg);
            addressRepository.save(pest);

            gergo.setAddress(zeg);
            pisti.setAddress(pest);

            personRepository.save(gergo);
            personRepository.save(pisti);
        };
    }
}
