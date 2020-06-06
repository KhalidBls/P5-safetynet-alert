package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.MedicalrecordService;
import com.safetynet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
public class PersonEndpoint {

    @Autowired
    PersonService personService;

    @GetMapping(value = "/person")
    public MappingJacksonValue afficherPersonnes() throws Exception {
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("birthdate","age"
                ,"firestationNumber","medications","allergies");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);


        MappingJacksonValue personFiltres = new MappingJacksonValue( personService.getPersons());
        personFiltres.setFilters(listDeNosFiltres);

        return personFiltres;
    }


    @PutMapping(value = "/person")
    public MappingJacksonValue updatePersons(@RequestBody Person person) throws Exception {
        Person existingPerson = personService.findPersonByName(person.getFirstName(),person.getLastName());

        if (existingPerson!=null) {
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setAddress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setZip(person.getZip());
            existingPerson.setPhone(person.getPhone());
            existingPerson.setEmail(person.getEmail());
        }
        return afficherPersonnes();
    }

    @PostMapping(value = "/person")
    public ResponseEntity<Void> addPersons(@RequestBody Person person) throws Exception {
        Person addedPerson = personService.save(person);

        if(addedPerson == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/personInfo")
                .queryParam("firstName",addedPerson.getFirstName())
                .queryParam("lastName",addedPerson.getLastName())
                .build()
                .toUri();


        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/person")
    public MappingJacksonValue deletePersons(@RequestBody Person person) throws Exception {
        personService.deleteByName(person.getFirstName(),person.getLastName());

        return afficherPersonnes();
    }
}
