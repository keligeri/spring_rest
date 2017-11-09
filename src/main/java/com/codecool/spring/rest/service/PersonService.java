package com.codecool.spring.rest.service;

import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.dao.PersonDaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonDaoInterface personDao;

    @Autowired
    public PersonService(PersonDaoInterface personDao) {
        this.personDao = personDao;
    }

    public List<Person> findAll() {
        return personDao.getAll();
    }

    public Person findById(long id) {
        return personDao.getById(id);
    }

    public void save(Person person) {
        personDao.savePerson(person);
    }

    public void update(long id, Person updatedPerson) {
        updatedPerson.setId(id);
        personDao.updatePerson(id, updatedPerson);
    }

    public void delete(long id) {
        personDao.deletePerson(id);
    }

}
