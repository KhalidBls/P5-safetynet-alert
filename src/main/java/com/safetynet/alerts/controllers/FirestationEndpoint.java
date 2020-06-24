package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.services.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class FirestationEndpoint {

    private final Logger logger = LoggerFactory.getLogger(FirestationEndpoint.class);

    @Autowired
    FirestationService firestationService;

    @GetMapping(value = "/firestations")
    public MappingJacksonValue afficherStations() throws Exception {
        logger.info("HTTP GET request received at /firestations URL");
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("personToSave");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue firestationFiltres = new MappingJacksonValue( firestationService.getFirestations());
        firestationFiltres.setFilters(listDeNosFiltres);

        return firestationFiltres;
    }



    @PutMapping(value = "/firestation")
    public MappingJacksonValue updateFirestation(@RequestBody Firestation firestation) throws Exception {
        logger.info("HTTP PUT request received at /firestation URL");
        Firestation existingFirestation = firestationService.findAll(firestation.getAddress());

        if (existingFirestation!=null) {
            existingFirestation.setAddress(firestation.getAddress());
            existingFirestation.setStation(firestation.getStation());
        }
        return afficherStations();
    }

    @PostMapping(value = "/firestation")
    public ResponseEntity<Void> addFirestation(@RequestBody Firestation firestation) throws Exception {
        logger.info("HTTP POST request received at /firestation URL");
        Firestation addedFirestation = firestationService.save(firestation);

        if(addedFirestation == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/firestation")
                .build()
                .toUri();


        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/firestation")
    public MappingJacksonValue deleteFirestation(@RequestBody Firestation firestation) throws Exception {
        logger.info("HTTP DELETE request received at /firestation URL");
        firestationService.deleteStation(firestation.getAddress(),firestation.getStation());
        return afficherStations();
    }

}
