package com.safetynet.alerts.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.safetynet.alerts.models.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
