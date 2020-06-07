package com.safetynet.alerts.services;


import com.safetynet.alerts.models.Person;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FloodStationService {
    HashMap<String, List<Person>> foyer = new HashMap<String, List<Person>>();

    public HashMap<String, List<Person>> getFoyer() {
        return foyer;
    }

    public void setFoyer(HashMap<String, List<Person>> foyer) {
        this.foyer = foyer;
    }
}
