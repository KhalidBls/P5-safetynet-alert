package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DataInitializationTest {

    private DataInitialization repo;

    @Test
    public void  testParseJsonToPersonObject() throws Exception {
        //GIVEN
        repo = new DataInitialization();
        Person person1 = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Person person2 = new Person("Jacob","Boyd","1509 Culver St","Culver","97451","841-874-6513","drk@email.com");

        //WHEN
        repo.parseJsonToPersonObject();

        //THEN
        assertTrue(repo.getPersons().get(0).getFirstName().equals(person1.getFirstName()));
        assertTrue(repo.getPersons().get(1).getFirstName().equals(person2.getFirstName()));
    }

    @Test
    public void parseJsonToFirestationObject() throws Exception {
        //GIVEN
        repo = new DataInitialization();
        Firestation firestation1 = new Firestation("1509 Culver St","3");
        Firestation firestation2 = new Firestation("29 15th St","2");

        //WHEN
        repo.parseJsonToFirestationObject();

        //THEN
        assertTrue(repo.getFirestations().get(0).getAddress().equals(firestation1.getAddress()));
        assertTrue(repo.getFirestations().get(1).getAddress().equals(firestation2.getAddress()));
        assertTrue(repo.getFirestations().get(0).getStation().equals(firestation1.getStation()));
        assertTrue(repo.getFirestations().get(1).getStation().equals(firestation2.getStation()));

    }

    @Test
    public void testParseJsonToMedicalrecordObject() throws Exception {
        //GIVEN
        repo = new DataInitialization();
        List<String> medications1 = new ArrayList<>();
        medications1.add("aznol:350mg");
        medications1.add("hydrapermazol:100mg");
        List<String> medications2 = new ArrayList<>();
        medications2.add("pharmacol:5000mg");
        medications2.add("terazine:10mg");
        medications2.add("noznazol:250mg");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("nillacilan");
        List<String> allergies2= new ArrayList<>();


        Medicalrecord medicalrecord1 = new Medicalrecord("John","Boyd","03/06/1984",medications1,allergies1);
        Medicalrecord medicalrecord2 = new Medicalrecord("Jacob","Boyd","03/06/1989",medications2,allergies2);

        //WHEN
        repo.parseJsonToPersonObject();
        repo.parseJsonToMedicalrecordObject();

        //THEN
        assertTrue(repo.getMedicalrecords().get(0).getFirstName().equals(medicalrecord1.getFirstName()));
    }

    @Test
    public void testParsing() throws Exception {
        //GIVEN
        repo = new DataInitialization();
        Person person1 = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Firestation firestation1 = new Firestation("1509 Culver St","3");

        List<String> medications1 = new ArrayList<>();
        medications1.add("aznol:350mg");
        medications1.add("hydrapermazol:100mg");
        List<String> allergies1 = new ArrayList<>();
        allergies1.add("nillacilan");
        Medicalrecord medicalrecord1 = new Medicalrecord("John","Boyd","03/06/1984",medications1,allergies1);

        //WHEN
        repo.parsing();

        //THEN
        assertTrue(repo.getPersons().get(0).getFirstName().equals(person1.getFirstName()));
        assertTrue(repo.getFirestations().get(0).getStation().equals(firestation1.getStation()));
        assertTrue(repo.getMedicalrecords().get(0).getBirthdate().equals(medicalrecord1.getBirthdate()));
    }

}
