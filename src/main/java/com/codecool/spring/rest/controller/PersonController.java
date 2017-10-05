package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.exception.PersonNotFoundException;
import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.PersonRepository;
import com.codecool.spring.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

    private static final String statusOk = "{\"status\": \"ok\"}";
    private final PersonRepository personRepository;
    private final PersonService personService;

    @Autowired
    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping(value = {"/", ""})
    public Iterable<Person> helloWorld() {
        return personRepository.findAll();
    }

    @GetMapping("/{personId}")
    public Person getPersonById(@PathVariable Long personId) throws PersonNotFoundException {
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new PersonNotFoundException("Person with id: " + personId + " not found!");
        }

        return person;
    }

    @PostMapping(value = "/add", produces = "application/json")
    public String savePerson(@RequestBody Person person) {
        personService.savePerson(person);
        return statusOk;
    }

    @ExceptionHandler(PersonNotFoundException.class)
    void handlePersonNotFound(PersonNotFoundException exception,
                              HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
