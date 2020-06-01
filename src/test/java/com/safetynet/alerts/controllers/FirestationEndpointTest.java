package com.safetynet.alerts.controllers;

import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.services.FirestationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationEndpoint.class)
@RunWith(SpringRunner.class)
public class FirestationEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FirestationService firestationService;

    @Test
    public void testGetFirestations() throws Exception {
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");

        //WHEN
        when(firestationService.getFirestations()).thenReturn(Arrays.asList(firestation1, firestation2));

        mockMvc.perform(get("/firestations")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].address", is("avenue de Paris")))
                .andExpect(jsonPath("$[0].station", is("2")))
                .andExpect(jsonPath("$[1].address", is("avenue de Marseille")))
                .andExpect(jsonPath("$[1].station", is("5")));

        verify(firestationService,times(1)).getFirestations();

    }


}
