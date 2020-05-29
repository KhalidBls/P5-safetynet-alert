package com.safetynet.alerts;

import com.safetynet.alerts.controllers.PersonEndpoint;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(PersonEndpoint.class)
@RunWith(SpringRunner.class)
public class PersonEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonService personService;

    @Test
    public void testAfficherPersonnes() throws Exception {

        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
        ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/person")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Bob")))
                .andExpect(jsonPath("$[0].lastName", is("Bobby")))
                .andExpect(jsonPath("$[0].address", is("avenue des Bob")))
                .andExpect(jsonPath("$[0].city", is("Paris")))
                .andExpect(jsonPath("$[0].zip", is("75000")))
                .andExpect(jsonPath("$[0].phone", is("0123456789")))
                .andExpect(jsonPath("$[0].email", is("bob@mail.com")))
                .andExpect(jsonPath("$[1].firstName", is("Jack")))
                .andExpect(jsonPath("$[1].lastName", is("Jacky")))
                .andExpect(jsonPath("$[1].address", is("avenue des Jack")))
                .andExpect(jsonPath("$[1].city", is("Paris")))
                .andExpect(jsonPath("$[1].zip", is("75000")))
                .andExpect(jsonPath("$[1].phone", is("0123456788")))
                .andExpect(jsonPath("$[1].email", is("jacky@mail.com")));

        verify(personService,times(1)).getPersons();

    }
}
