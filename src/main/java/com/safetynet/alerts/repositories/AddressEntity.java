package com.safetynet.alerts.repositories;

import com.safetynet.alerts.models.Person;

import java.util.ArrayList;
import java.util.List;

public class AddressEntity {

    private List<Person> personOfAddress = new ArrayList<Person>();
    private String firestationNumber;

    public List<Person> getPersonOfAddress() {
        return personOfAddress;
    }

    public void setPersonOfAddress(List<Person> personOfAddress) {
        this.personOfAddress = personOfAddress;
    }

    public String getFirestationNumber() {
        return firestationNumber;
    }

    public void setFirestationNumber(String firestationNumber) {
        this.firestationNumber = firestationNumber;
    }

}
