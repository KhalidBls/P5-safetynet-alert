package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirestationService {

    @Autowired
    private EntitiesRepository repo;

    public EntitiesRepository getRepo() {
        return repo;
    }

    public List<Firestation> getFirestations(){
        return repo.getFirestations();
    }

    public Firestation findAll(String address) {
        return repo.findFirestation(address);
    }

    public void save(Firestation firestation) {
        repo.getFirestations().add(firestation);
    }

    public void deleteStation(String address, String station) {
        repo.getFirestations().removeIf(firestation -> (firestation.getAddress().equals(address))
                && (firestation.getStation().equals(station)));
    }
}
