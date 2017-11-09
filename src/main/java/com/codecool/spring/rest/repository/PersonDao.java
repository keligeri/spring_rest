package com.codecool.spring.rest.repository;

import com.codecool.spring.rest.model.Person;
import com.codecool.spring.rest.repository.dao.PersonDaoInterface;
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

    public List<Person> getByAge(int age1, int age2) {
        return em.createNamedQuery("findPersonsByBetweenTwoAge", Person.class)
                .setParameter("personAge1", age1)
                .setParameter("personAge2", age2).getResultList();
    }

    public Person getByName(String name){
        return em.createNamedQuery("findPersonByName", Person.class).setParameter("personName", name).getSingleResult();
    }

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
