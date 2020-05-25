package com.safetynet.alerts.repositories;

import com.safetynet.alerts.models.PersonOfAddress;

import java.util.ArrayList;
import java.util.List;

public class AddressEntity {

    private List<PersonOfAddress> personOfAddress = new ArrayList<PersonOfAddress>();
    private String firestationNumber;

    public List<PersonOfAddress> getPersonOfAddress() {
        return personOfAddress;
    }

    public void setFirestationNumber(String firestationNumber) {
        this.firestationNumber = firestationNumber;
    }

    public void setPersonOfAddress(List<PersonOfAddress> personOfAddress) {
        this.personOfAddress = personOfAddress;
    }

    public String getFirestationNumber() {
        return firestationNumber;
    }
}
