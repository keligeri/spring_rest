package com.codecool.spring.rest.repository;

import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.abstraction.PersonDaoInterface;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class PersonDao implements PersonDaoInterface {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Person> getAll() {
        return em.createNamedQuery("findAllPerson").getResultList();
    }

    @Override
    public Person getById(long id) {
        return em.find(Person.class, id);
    }

    @Override
    public void savePerson(Person person) {
        em.persist(person);
    }

    @Override
    public void updatePerson(long id, Person person) {
        Person pers = getById(person.getId());
        pers.setName(person.getName());
        pers.setAge(person.getAge());
        pers.setAddress(person.getAddress());

        em.flush();
    }

    @Override
    public void deletePerson(long id) {
        em.remove(getById(id));
    }
}
