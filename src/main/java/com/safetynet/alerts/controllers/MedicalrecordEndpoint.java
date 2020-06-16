package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.services.MedicalrecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class MedicalrecordEndpoint {

    private final Logger logger = LoggerFactory.getLogger(MedicalrecordEndpoint.class);

    @Autowired
    MedicalrecordService medicalrecordService;

    @GetMapping(value = "/medicalRecord")
    public MappingJacksonValue afficherMedicalrecord() throws Exception {
        logger.info("HTTP GET request received at /medicalRecord URL");

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue medicalFiltres = new MappingJacksonValue( medicalrecordService.getMedicalrecords());
        medicalFiltres.setFilters(listDeNosFiltres);

        return medicalFiltres;
    }



    @PutMapping(value = "/medicalRecord")
    public MappingJacksonValue updateMedicalrecord(@RequestBody Medicalrecord medicalrecord) throws Exception {
        logger.info("HTTP PUT request received at /medicalRecord URL");

        Medicalrecord existingMedicalrecord = medicalrecordService.findMedicalrecordByName(medicalrecord.getFirstName(),medicalrecord.getLastName());

        if (existingMedicalrecord!=null) {
            existingMedicalrecord.setFirstName(medicalrecord.getFirstName());
            existingMedicalrecord.setLastName(medicalrecord.getLastName());
            existingMedicalrecord.setBirthdate(medicalrecord.getBirthdate());
            existingMedicalrecord.setMedications(medicalrecord.getMedications());
            existingMedicalrecord.setAllergies(medicalrecord.getAllergies());
        }
        return afficherMedicalrecord();
    }

    @PostMapping(value = "/medicalRecord")
    public ResponseEntity<Void> addMedicalrecord(@RequestBody Medicalrecord medicalrecord) throws Exception {
        logger.info("HTTP POST request received at /medicalRecord URL");
        Medicalrecord addedMedicalrecord = medicalrecordService.save(medicalrecord);

        if(addedMedicalrecord == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/personInfo")
                .queryParam("firstName",addedMedicalrecord.getFirstName())
                .queryParam("lastName",addedMedicalrecord.getLastName())
                .build()
                .toUri();


        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/medicalRecord")
    public MappingJacksonValue deletePersons(@RequestBody Medicalrecord medicalrecord) throws Exception {
        logger.info("HTTP DELETE request received at /medicalRecord URL");
        medicalrecordService.deleteByName(medicalrecord.getFirstName(),medicalrecord.getLastName());

        return afficherMedicalrecord();
    }

}
