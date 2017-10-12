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
    public Iterable<Person> read() {
        return personRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Person getById(@PathVariable Long id) throws PersonNotFoundException {
        isValidPerson(id);
        return personRepository.findOne(id);
    }

    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.POST)
    public String save(@RequestBody Person person) {
        personService.savePerson(person);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable long id, @RequestBody Person updatedPerson) throws PersonNotFoundException {
        isValidPerson(id);

        personService.update(id, updatedPerson);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable long id) throws PersonNotFoundException {
        isValidPerson(id);

        Person person = personRepository.findOne(id);
        personRepository.delete(person);
        return statusOk;
    }

    private void isValidPerson(long id) throws PersonNotFoundException {
        Person person = personRepository.findOne(id);
        if (person == null) {
            throw new PersonNotFoundException("Person with id: " + id + " not found!");
        }
    }

    @ExceptionHandler(PersonNotFoundException.class)
    void handlePersonNotFound(PersonNotFoundException exception,
                              HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
