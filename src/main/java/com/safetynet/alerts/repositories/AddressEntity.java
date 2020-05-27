package com.safetynet.alerts.repositories;

import com.safetynet.alerts.models.Person;

import java.util.ArrayList;
import java.util.List;

public class AddressEntity {

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
