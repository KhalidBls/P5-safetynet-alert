package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private EntitiesRepository repo;

    public List<Person> getPersons(){
        return repo.getPersons();
    }

    public Person save(Person person) {
        repo.getPersons().add(person);
        return person;
    }

    public void deleteByName(String firstName,String lastName){
        repo.getPersons().removeIf(person -> (person.getFirstName().equals(firstName))
                && (person.getLastName().equals(lastName)));
    }

    public Person findPersonByName(String firstName,String lastName){
        return repo.findPersonByName(firstName,lastName);
    }


}
