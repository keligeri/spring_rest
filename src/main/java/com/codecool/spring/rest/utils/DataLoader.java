package com.codecool.spring.rest.utils;

import com.codecool.spring.rest.model.Address;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.model.Role;
import com.codecool.spring.rest.model.User;
import com.codecool.spring.rest.repository.AddressRepository;
import com.codecool.spring.rest.repository.PersonRepository;
import com.codecool.spring.rest.repository.RoleRepository;
import com.codecool.spring.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(AddressRepository addressRepository, PersonRepository personRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        User admin = new User("admin", "admin", adminRole);
        User user = new User("user", "user", userRole);
        userRepository.save(admin);
        userRepository.save(user);

        Address zeg = new Address(8900L, "Zalaegerszeg");
        Address pest = new Address(1146L, "Budapest");

        Person gergo = new Person("Gergő", 24, zeg, admin);
        Person pisti = new Person("Pista", 20, pest, user);

//        addressRepository.save(zeg);
//        addressRepository.save(pest);

        gergo.setAddress(zeg);
        pisti.setAddress(pest);

        personRepository.save(gergo);
        personRepository.save(pisti);

    }
}
