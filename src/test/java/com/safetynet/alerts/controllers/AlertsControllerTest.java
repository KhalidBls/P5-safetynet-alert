package com.safetynet.alerts.controllers;

import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertsController.class)
@RunWith(SpringRunner.class)
public class AlertsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;
    @MockBean
    private MedicalrecordService medicalrecordService;
    @MockBean
    private PersonService personService;


    @Test
    public void testAfficherLaPersonnesWhenExist() throws Exception {
        //GIVEN
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");

        Medicalrecord medicalrecord1 = new Medicalrecord("Peu","importe","01/10/1997",medications1,allergies1);

        when(medicalrecordService.findMedicalrecordByName(anyString(),anyString())).thenReturn(medicalrecord1);
        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/personInfo")
                .contentType(APPLICATION_JSON)
                .param("firstName","Jack")
                .param("lastName","Jacky"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName",is("Jacky")))
                .andExpect(jsonPath("$[0].address",is("avenue des Jack")))
                .andExpect(jsonPath("$[0].email",is("jacky@mail.com")));
    }

    @Test
    public void testAfficherLaPersonnesWhenDontExist() throws Exception {
        //GIVEN
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/personInfo")
                .contentType(APPLICATION_JSON)
                .param("firstName","Inconnu")
                .param("lastName","Inconnu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    public void testAfficherNumberByFirestationWhenGoodArgument() throws Exception {
        Person person1 = new Person("Bob","Bobby","avenue des Jack&Bob","Paris"
                ,"75000","1000000","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack&Bob","Paris"
                ,"75000","200000","jacky@mail.com");
        Person person3 = new Person("Louis","Bill","avenue des nimporteOu","Paris"
                ,"75000","5000000","jacky@mail.com");
        Firestation firestation = new Firestation("avenue des Jack&Bob","33");

        when(firestationService.findByNumber("33")).thenReturn(firestation);
        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2,person3));

        mockMvc.perform(get("/phoneAlert")
                .contentType(APPLICATION_JSON)
                .param("firestation","33"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0]",is("1000000")))
                .andExpect(jsonPath("$[1]",is("200000")));
    }

    @Test
    public void testAfficherEmailByCityWhenGoodArgument() throws Exception {
        Person person1 = new Person("Bob","Bobby","avenue des Jack&Bob","Paris"
                ,"75000","1000000","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack&Bob","Marseille"
                ,"75000","200000","jacky@mail.com");
        Person person3 = new Person("Louis","Bill","avenue des nimporteOu","Paris"
                ,"75000","5000000","Louis@mail.com");

        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2,person3));

        mockMvc.perform(get("/communityEmail")
                .contentType(APPLICATION_JSON)
                .param("city","Marseille"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0]",is("jacky@mail.com")));
    }


}
