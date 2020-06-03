package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.services.MedicalrecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalrecordEndpoint.class)
@RunWith(SpringRunner.class)
public class MedicalrecordEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MedicalrecordService medicalrecordService;

    @Test
    public void testGetMedicalredord() throws Exception {
        //GIVEN
        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        Medicalrecord medicalrecord1 = new Medicalrecord("Louis","Funes","01/10/1997",medications1,allergies1);
        Medicalrecord medicalrecord2 = new Medicalrecord("Franck","Francky","14/02/1966",medications2,allergies2);

        //WHEN
        when(medicalrecordService.getMedicalrecords()).thenReturn(Arrays.asList(medicalrecord1, medicalrecord2));

        mockMvc.perform(get("/medicalRecord")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Louis")))
                .andExpect(jsonPath("$[0].lastName", is("Funes")))
                .andExpect(jsonPath("$[0].birthdate", is("01/10/1997")))
                .andExpect(jsonPath("$[0].medications[0]", is("dodoli")))
                .andExpect(jsonPath("$[0].medications[1]", is("dodol")))
                .andExpect(jsonPath("$[0].allergies[0]", is("lait")))

                .andExpect(jsonPath("$[1].firstName", is("Franck")))
                .andExpect(jsonPath("$[1].lastName", is("Francky")))
                .andExpect(jsonPath("$[1].birthdate", is("14/02/1966")))
                .andExpect(jsonPath("$[1].medications[0]", is("dodoli500")))
                .andExpect(jsonPath("$[1].medications[1]", is("dodol1000")))
                .andExpect(jsonPath("$[1].allergies[0]", is("chat")))
                .andExpect(jsonPath("$[1].allergies[1]", is("blabla")));

        verify(medicalrecordService,times(1)).getMedicalrecords();
    }

    @Test
    public void testCreateMedicalrecordWithGoodArgument() throws Exception {
        //GIVEN
        Medicalrecord medicalrecord;
        ObjectMapper mapper = new ObjectMapper();
        String medicalrecordJSON = "{\n" +
                "        \"firstName\": \"Louis\",\n" +
                "        \"lastName\": \"Funes\",\n" +
                "        \"birthdate\": \"01/10/1997\",\n" +
                "        \"medications\": [\n" +
                "            \"aznol:350mg\",\n" +
                "            \"hydrapermazol:100mg\"\n" +
                "        ],\n" +
                "        \"allergies\": [\n" +
                "            \"nillacilan\"\n" +
                "        ]\n" +
                "    }";

        medicalrecord = mapper.readValue(medicalrecordJSON, Medicalrecord.class);

        when(medicalrecordService.save(Mockito.any(Medicalrecord.class))).thenReturn(medicalrecord);

        //WHEN & THEN
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicalrecordJSON))
                .andExpect(status().is(201));
    }

    @Test
    public void testUpdatePersonsWhenMedicalrecordExist() throws Exception {
        //GIVEN
        List<String> medications = new ArrayList<>();
        medications.add("dodoli");


        List<String> allergies = new ArrayList<>();
        allergies.add("lait");
        allergies.add("dodol");

        Medicalrecord medicalrecord = new Medicalrecord("Louis","Funes","01/10/1997",medications,allergies);


        when(medicalrecordService.findMedicalrecordByName("Louis","Funes")).thenReturn(medicalrecord);

        String medicalrecordJSON = "{\n" +
                "        \"firstName\": \"Louis\",\n" +
                "        \"lastName\": \"Funes\",\n" +
                "        \"birthdate\": \"01/10/1997\",\n" +
                "        \"medications\": [\n" +
                "            \"aznol:350mg\",\n" +
                "            \"hydrapermazol:100mg\"\n" +
                "        ],\n" +
                "        \"allergies\": [\n" +
                "            \"nillacilan\"\n" +
                "        ]\n" +
                "    }";

        //WHEN
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicalrecordJSON))
                .andExpect(status().is(200));

        //THEN
        assertEquals(medicalrecord.getMedications().size(),2);
        assertEquals(medicalrecord.getAllergies().size(),1);
    }

    @Test
    public void testDeleteWhenFirestationExist() throws Exception {
        String medicalrecordJSON = "{\n" +
                "        \"firstName\": \"Bobby\",\n" +
                "        \"lastName\": \"Bob\"\n" +
                "    }" ;

        doNothing().when(medicalrecordService).deleteByName("Bobby","Bob");

        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicalrecordJSON))
                .andExpect(status().isOk());

        verify(medicalrecordService,times(1)).deleteByName("Bobby","Bob");
    }

}
