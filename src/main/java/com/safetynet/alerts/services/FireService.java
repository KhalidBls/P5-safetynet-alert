package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireService {

    private List<Person> persons = new ArrayList<Person>();
    private String firestation;

    public List<Person> getPersons() {
        return persons;
    }

    public void setFirestation(String firestation) {
        this.firestation = firestation;
    }

    public void setPersons(List<Person> personOfAddress) {
        this.persons = personOfAddress;
    }

    public String getFirestationNumber() {
        return firestation;
    }
}
