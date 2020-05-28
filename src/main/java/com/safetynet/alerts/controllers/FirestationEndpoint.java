package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.FirestationService;
import com.safetynet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RestController
public class FirestationEndpoint {

    @Autowired
    FirestationService firestationService;

    @GetMapping(value = "/firestations")
    public MappingJacksonValue afficherStations() throws Exception {
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("personToSave");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue firestationFiltres = new MappingJacksonValue( firestationService.getFirestations());
        firestationFiltres.setFilters(listDeNosFiltres);

        return firestationFiltres;
    }

    @PostMapping(value = "/firestation")
    public MappingJacksonValue addOrupdateFirestation(@RequestBody Firestation firestation) throws Exception {
        Firestation existingFirestation = firestationService.findAll(firestation.getAddress());
        if(existingFirestation == null)
            firestationService.save(firestation);
        else {
            existingFirestation.setStation(firestation.getAddress());
            existingFirestation.setStation(firestation.getStation());
        }
        return afficherStations();
    }

    @DeleteMapping(value = "/firestation")
    public MappingJacksonValue deletePersons(@RequestBody Firestation firestation) throws Exception {
        firestationService.deleteStation(firestation.getAddress(),firestation.getStation());
        return afficherStations();
    }

}
