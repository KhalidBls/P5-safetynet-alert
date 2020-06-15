package com.safetynet.alerts.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    @Autowired
    private DataInitialization repo;
    @Autowired
    PersonService personService;
    @Autowired
    MedicalrecordService medicalrecordService;

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

    public JSONObject sortPersonByAddress(String address){
        Firestation ourFirestation = findAll(address);
        List<Person> persons =  repo.getPersons()
                .stream()
                .filter(c -> c.getAddress().equals(address))
                .collect(Collectors.toList());

        for (Person person : repo.getPersons()) {
            person.setAge(personService.ageCalculation(person.getBirthdate()));
            person.setAllergies(medicalrecordService.findMedicalrecordByName(person.getFirstName(),person.getLastName()).getAllergies());
            person.setMedications(medicalrecordService.findMedicalrecordByName(person.getFirstName(),person.getLastName()).getMedications());
        }
        JSONObject json = new JSONObject();
        json.put("persons",persons);
        json.put("stations",ourFirestation.getStation());

        return json;
    }
    public JSONObject sortPersonByStation(String number){
        int numberOfChilds = 0;
        int numberOfAdults = 0;
        Firestation ourFirestation = findByNumber(number);
        List<Person> persons = repo.getPersons()
                .stream()
                .filter(c -> c.getAddress().equals(ourFirestation.getAddress()))
                .collect(Collectors.toList());
        for (Person person : persons) {
            person.setAge(personService.ageCalculation(person.getBirthdate()));
        }
        for (Person person : persons) {
            if(person.getAge()>=18)
                numberOfAdults++;
            else
                numberOfChilds++;
        }
        JSONObject json = new JSONObject();
        json.put("persons",persons);
        json.put("childrens",numberOfChilds);
        json.put("adults",numberOfAdults);

        return json;
    }

    public JSONObject sortPersonByListOfStations(List<String> listOfStations){
        Firestation ourFirestation;
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        for(int i = 0;i < listOfStations.size();i++){
            ourFirestation = findByNumber(listOfStations.get(i));
            for (Person person : ourFirestation.getPersonToSave()) {
                person.setAge(personService.ageCalculation(person.getBirthdate()));
                person.setAllergies(medicalrecordService.findMedicalrecordByName(person.getFirstName(),person.getLastName()).getAllergies());
                person.setMedications(medicalrecordService.findMedicalrecordByName(person.getFirstName(),person.getLastName()).getMedications());
            }
            json.put(ourFirestation.getAddress(),ourFirestation.getPersonToSave());
            json2.put(ourFirestation.getStation(),json);
        }
        return json2;
    }

    public MappingJacksonValue filter(String[] tab,JSONObject json){
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept(tab);
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue personFiltres = new MappingJacksonValue(json);
        personFiltres.setFilters(listDeNosFiltres);
        return personFiltres;
    }

}
