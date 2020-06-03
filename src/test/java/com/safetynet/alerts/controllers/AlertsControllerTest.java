package com.safetynet.alerts.controllers;

import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.ChildrenService;
import com.safetynet.alerts.services.EntitiesRepository;
import com.safetynet.alerts.services.FireService;
import com.safetynet.alerts.services.ZoneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
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
    private EntitiesRepository repo;
    @MockBean
    private ChildrenService childrenService;
    @MockBean
    private FireService fireService;
    @MockBean
    private ZoneService zoneService;

    @Test
    public void testAfficherLaPersonnesWhenExist() throws Exception {
        //GIVEN
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        when(repo.getPersons()).thenReturn(Arrays.asList(person1, person2));

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

        when(repo.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/personInfo")
                .contentType(APPLICATION_JSON)
                .param("firstName","Inconnu")
                .param("lastName","Inconnu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

}
