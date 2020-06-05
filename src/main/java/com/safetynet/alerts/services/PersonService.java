package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private EntitiesRepository repo;

    public List<Person> getPersons(){
        return repo.getPersons();
    }

    public Person save(Person person) {
        repo.getPersons().add(person);
        return person;
    }

    public void deleteByName(String firstName,String lastName){
        repo.getPersons().removeIf(person -> (person.getFirstName().equals(firstName))
                && (person.getLastName().equals(lastName)));
    }

    public Person findPersonByName(String firstName,String lastName){
        return repo.findPersonByName(firstName,lastName);
    }

    public int ageCalculation(String birthdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDateFormat = null;
        try {
            birthDateFormat = sdf.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar now = Calendar.getInstance();
        now.setTime(birthDateFormat);

        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE);

        LocalDate localD = LocalDate.of(year, month, date);
        LocalDate nowDate = LocalDate.now();
        Period diff1 = Period.between(localD, nowDate);
        return diff1.getYears();
    }

}
