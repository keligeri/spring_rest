package com.codecool.spring.rest.controller;

import com.codecool.spring.rest.exception.EntityNotFoundException;
import com.codecool.spring.rest.model.Person;
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
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(value = {"/", ""})
    public Iterable<Person> read() {
        return personService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Person getById(@PathVariable Long id) throws EntityNotFoundException {
        isValidPerson(id);

        return personService.findById(id);
    }

    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.POST)
    public String save(@RequestBody Person person) {
        personService.save(person);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable long id, @RequestBody Person updatedPerson) throws EntityNotFoundException {
        isValidPerson(id);

        personService.update(id, updatedPerson);
        return statusOk;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable long id) throws EntityNotFoundException {
        isValidPerson(id);

        personService.delete(id);
        return statusOk;
    }

    private void isValidPerson(long id) throws EntityNotFoundException {
        Person person = personService.findById(id);
        if (person == null) {
            throw new EntityNotFoundException("Person with id: " + id + " not found!");
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    void handlePersonNotFound(EntityNotFoundException exception,
                              HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
