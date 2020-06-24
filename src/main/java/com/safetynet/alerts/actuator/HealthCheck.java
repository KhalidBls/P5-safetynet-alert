package com.safetynet.alerts.actuator;

import com.safetynet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {

    private PersonService personService;

    @Autowired
    public HealthCheck(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public Health health() {
        try {
            if(personService.getPersons().isEmpty())
                return Health.down().withDetail("Cause","Récupération des données impossible").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Health.up().build();
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository()
    {
        return new InMemoryHttpTraceRepository();
    }
}
