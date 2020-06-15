package com.safetynet.alerts.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class FirestationServiceTest {

    @Mock
    DataInitialization repo;

    @InjectMocks
    FirestationService firestationService;
    @Mock
    MedicalrecordService medicalrecordService;
    @Mock
    PersonService personService;

    @Test
    public void testGetFirestationsShouldReturnAllFirestations(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        List<Firestation> ourListOfFirestations = new ArrayList<>();
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourListOfFirestations);

        //WHEN
        List<Firestation> result = firestationService.getFirestations();

        //THEN
        assertTrue(result.size() == 2);
        assertTrue(result.get(0).getAddress().equals("avenue de Paris"));
        assertTrue(result.get(1).getAddress().equals("avenue de Marseille"));
    }

    @Test
    public void testFindAllByAddressWithGoodAddress(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","7");
        List<Firestation> ourList = new ArrayList<>();
        ourList.add(firestation1);
        ourList.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourList);

        //WHEN
        Firestation result = firestationService.findAll("avenue de Marseille");

        //THEN
        assertTrue(result.getAddress().equals("avenue de Marseille"));
        assertTrue(result.getStation().equals("7"));
    }

    @Test
    public void testFindFirestationByNumberWithGoodNumberShouldReturnGoodFirestation(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","7");
        List<Firestation> ourList = new ArrayList<>();
        ourList.add(firestation1);
        ourList.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourList);

        //WHEN
        Firestation result = firestationService.findByNumber("2");

        //THEN
        assertTrue(result.getAddress().equals("avenue de Paris"));
        assertTrue(result.getStation().equals("2"));
    }

    @Test
    public void testSaveFirestationShouldReturnFirestation(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        Firestation firestation3 = new Firestation("avenue de Brest","8");
        List<Firestation> ourListOfFirestations = new ArrayList<>();
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourListOfFirestations);
        assertTrue(ourListOfFirestations.size()==2);

        //WHEN
        Firestation result = firestationService.save(firestation3);

        //THEN
        assertTrue(ourListOfFirestations.size()==3);
        assertTrue(result.getStation().equals("8"));
        assertTrue(result.getAddress().equals("avenue de Brest"));
    }

    @Test
    public void testDeleteFirestation(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        Firestation firestation3 = new Firestation("avenue de Brest","8");
        List<Firestation> ourListOfFirestations = new ArrayList<>();
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);
        ourListOfFirestations.add(firestation3);

        when(repo.getFirestations()).thenReturn(ourListOfFirestations);
        assertTrue(ourListOfFirestations.size()==3);

        //WHEN
        firestationService.deleteStation("avenue de Brest","8");

        //THEN
        assertTrue(ourListOfFirestations.size()==2);
    }

    @Test
    public void testSortPersonByAddress() throws JsonProcessingException {
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Person person1 = new Person("Bob","Bobby","avenue de Paris","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue de Paris","Paris"
                ,"75000","0123456788","jacky@mail.com");
        person1.setBirthdate("01/10/1999");
        person2.setBirthdate("01/10/1999");

        when(repo.getFirestations()).thenReturn(Arrays.asList(firestation1));
        when(repo.getPersons()).thenReturn(Arrays.asList(person1, person2));
        when(personService.ageCalculation(anyString())).thenReturn(20);
        when(medicalrecordService.findMedicalrecordByName(anyString(),anyString())).thenReturn(new Medicalrecord());
        when(medicalrecordService.findMedicalrecordByName(anyString(),anyString())).thenReturn(new Medicalrecord());

        JSONObject json = new JSONObject();
        json.put("persons",Arrays.asList(person1, person2));
        json.put("stations",firestation1.getStation());

        //WHEN
        JSONObject result = firestationService.sortPersonByAddress("avenue de Paris");

        //THEN
       assertTrue(result.toString().equals(json.toString()));
    }

    @Test
    public void testSortPersonByStation(){
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Person person1 = new Person("Bob","Bobby","avenue de Paris","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue de Paris","Paris"
                ,"75000","0123456788","jacky@mail.com");
        person1.setBirthdate("01/10/1999");
        person2.setBirthdate("01/10/2015");

        when(repo.getFirestations()).thenReturn(Arrays.asList(firestation1));
        when(repo.getPersons()).thenReturn(Arrays.asList(person1, person2));
        when(personService.ageCalculation("01/10/1999")).thenReturn(21);
        when(personService.ageCalculation("01/10/2015")).thenReturn(5);

        JSONObject json = new JSONObject();
        json.put("persons",Arrays.asList(person1, person2));
        json.put("childrens",1);
        json.put("adults",1);

        //WHEN
        JSONObject result = firestationService.sortPersonByStation("2");

        //THEN
        assertTrue(result.toString().equals(json.toString()));
    }

    @Test
    public void testSortPersonByListOfStations(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        Firestation firestation3 = new Firestation("avenue de Brest","8");
        Person person1 = new Person("Bob","Bobby","avenue de Paris","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue de Paris","Paris"
                ,"75000","0123456788","jacky@mail.com");
        Person person3 = new Person("Bob","Bobby","avenue de Marseille","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person4 = new Person("Jack","Jacky","avenue de Brest","Paris"
                ,"75000","0123456788","jacky@mail.com");
        Person person5 = new Person("Bob","Bobby","avenue de Brest","Paris"
                ,"75000","0123456789","bob@mail.com");

        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();

        firestation1.setPersonToSave(Arrays.asList(person1, person2));
        firestation2.setPersonToSave(Arrays.asList(person3));
        firestation3.setPersonToSave(Arrays.asList(person4, person5));

        when(repo.getFirestations()).thenReturn(Arrays.asList(firestation1, firestation2,firestation3));
        when(medicalrecordService.findMedicalrecordByName(anyString(),anyString())).thenReturn(new Medicalrecord());
        when(medicalrecordService.findMedicalrecordByName(anyString(),anyString())).thenReturn(new Medicalrecord());
        json.put("avenue de Paris",Arrays.asList(person1, person2));
        json2.put("2",json);
        json.put("avenue de Brest",Arrays.asList(person4, person5));
        json2.put("8",json);

        //WHEN
        JSONObject result = firestationService.sortPersonByListOfStations(Arrays.asList("2", "8"));

        //THEN
        assertTrue(result.toString().equals(json2.toString()));

    }

}
