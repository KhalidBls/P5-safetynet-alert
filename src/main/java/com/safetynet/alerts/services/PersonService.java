package com.safetynet.alerts.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.Person;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private DataInitialization repo;

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
        for (Person person : repo.getPersons()) {
            if(person.getFirstName().equals(firstName) && person.getLastName().contentEquals(lastName))
                return person;
        }
        return null;
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

    public JSONObject sortChildrenAndAdultByAddress(String address){
        List<Person> personFamily =  repo.getPersons()
                .stream()
                .filter(c -> c.getAddress().equals(address))
                .collect(Collectors.toList());

        for (Person person : personFamily) {
            person.setAge(ageCalculation(person.getBirthdate()));
        }
        List<Person> childrens = personFamily
                .stream()
                .filter(c -> c.getAge() < 18)
                .collect(Collectors.toList());
        List<Person> adult = personFamily
                .stream()
                .filter(c -> c.getAge() >= 18)
                .collect(Collectors.toList());
        JSONObject json = new JSONObject();
        json.put("childrens",childrens);
        json.put("adults",adult);
        JSONObject json2 = new JSONObject();
        json2.put(address,json);

        return json2;
    }

    public MappingJacksonValue filter(String[] tab,JSONObject json){
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept(tab);
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue personFiltres = new MappingJacksonValue(json);
        personFiltres.setFilters(listDeNosFiltres);
        return personFiltres;
    }

}
