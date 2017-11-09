package com.codecool.spring.rest.repository.dao;

import com.codecool.spring.rest.model.Person;

import java.util.List;

/**
 * Created by keli on 2017.11.08..
 */
public interface PersonDaoInterface {

    List<Person> getAll();

    Person getById(long id);

    void savePerson(Person person);

    void updatePerson(long id, Person person);

    void deletePerson(long id);

}
