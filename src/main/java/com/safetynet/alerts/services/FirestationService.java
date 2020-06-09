package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirestationService {

    @Autowired
    private DataInitialization repo;

    public List<Firestation> getFirestations(){
        return repo.getFirestations();
    }

    public Firestation findAll(String address) {
        for (Firestation firestation : repo.getFirestations()) {
            if (firestation.getAddress().equals(address))
                return firestation;
        }
        return null;
    }

    public Firestation findByNumber(String number) {
        for (Firestation firestation : repo.getFirestations()) {
            if (firestation.getStation().equals(number))
                return firestation;
        }
        return null;
    }

    public Firestation save(Firestation firestation) {
        repo.getFirestations().add(firestation);
        return firestation;
    }

    public void deleteStation(String address, String station) {
        repo.getFirestations().removeIf(firestation -> (firestation.getAddress().equals(address))
                && (firestation.getStation().equals(station)));
    }
}
