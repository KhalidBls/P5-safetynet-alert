package com.safetynet.alerts;

import com.safetynet.alerts.repositories.EntitiesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest
@RunWith(SpringRunner.class)
public class AlertsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EntitiesRepository repo;

    @Test
    public void testShowPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd"))
                .andExpect(status().is2xxSuccessful());
    }

}
