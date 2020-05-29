package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.MedicalrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalrecordEndpoint {

    @Autowired
    MedicalrecordService medicalrecordService;

    @GetMapping(value = "/medicalRecord")
    public MappingJacksonValue afficherMedicalrecord() throws Exception {
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue medicalFiltres = new MappingJacksonValue( medicalrecordService.getMedicalrecords());
        medicalFiltres.setFilters(listDeNosFiltres);

        return medicalFiltres;
    }

    @PostMapping(value = "/medicalRecord")
    public MappingJacksonValue addOrUpdateMedicalRecord(@RequestBody Medicalrecord medicalrecord) throws Exception {
        Medicalrecord existingMedialRecord = medicalrecordService.findMedicalrecordByName(medicalrecord.getFirstName(),medicalrecord.getLastName());
        if(existingMedialRecord == null)
            medicalrecordService.save(medicalrecord);
        else{
            existingMedialRecord.setFirstName(medicalrecord.getFirstName());
            existingMedialRecord.setLastName(medicalrecord.getLastName());
            existingMedialRecord.setBirthdate(medicalrecord.getBirthdate());
            existingMedialRecord.setAllergies(medicalrecord.getAllergies());
            existingMedialRecord.setMedications(medicalrecord.getMedications());
        }
        return afficherMedicalrecord();
    }

    @DeleteMapping(value = "/medicalRecord")
    public MappingJacksonValue deletePersons(@RequestBody Medicalrecord medicalrecord) throws Exception {
        medicalrecordService.deleteByName(medicalrecord.getFirstName(),medicalrecord.getLastName());

        return afficherMedicalrecord();
    }

}
