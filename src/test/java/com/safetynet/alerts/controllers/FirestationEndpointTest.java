package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controllers.FirestationEndpoint;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.services.FirestationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void testCreateFirestationWithGoodArgument() throws Exception {
        //GIVEN
        Firestation firestation;
        ObjectMapper mapper = new ObjectMapper();
        String firestationJSON = "{\n" +
                "        \"address\": \"100 Rue de paris\",\n" +
                "        \"station\": \"3\"\n" +
                "    }" ;

        firestation = mapper.readValue(firestationJSON, Firestation.class);

        when(firestationService.save(Mockito.any(Firestation.class))).thenReturn(firestation);

        //WHEN & THEN
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJSON))
                .andExpect(status().is(201));
    }

    @Test
    public void testUpdatePersonsWhenFirestationExist() throws Exception {
        //GIVEN
        Firestation firestation = new Firestation("999 Rue New York","18");

        when(firestationService.findAll("999 Rue New York")).thenReturn(firestation);

        String firestationJSON = "{\n" +
                "        \"address\": \"999 Rue New York\",\n" +
                "        \"station\": \"25\"\n" +
                "    }" ;

        //WHEN
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJSON))
                .andExpect(status().is(200));

        //THEN
        assertEquals(firestation.getAddress(),"999 Rue New York");
        assertEquals(firestation.getStation(),"25");
    }


    @Test
    public void testDeleteWhenFirestationExist() throws Exception {
        String firestationJSON = "{\n" +
                "        \"address\": \"999 Rue New York\",\n" +
                "        \"station\": \"25\"\n" +
                "    }" ;

        doNothing().when(firestationService).deleteStation("999 Rue New York","25");

        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJSON))
                .andExpect(status().isOk());

        verify(firestationService,times(1)).deleteStation("999 Rue New York","25");
    }

}
