package com.safetynet.alerts.controllers;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.*;
import com.safetynet.alerts.services.FireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.services.ChildrenService;
import com.safetynet.alerts.services.EntitiesRepository;
import com.safetynet.alerts.services.ZoneService;

@RestController
public class AlertsController {

	@Autowired
	private EntitiesRepository repo;
	@Autowired
	private ChildrenService childrenService;
	@Autowired
	private FireService fireService;
	@Autowired
	private ZoneService zoneService;


	
	@GetMapping("/personInfo")
	public MappingJacksonValue afficherLaPersonne(@RequestParam(name="firstName", required = true)String firstName
			,@RequestParam(name="lastName", required = true)String lastName) throws Exception {
		Person ourPerson = null;
		for(int i = 0;i<repo.getPersons().size(); i++) {
			if(repo.getPersons().get(i).getFirstName().equals(firstName) && repo.getPersons().get(i).getLastName().equals(lastName) )
				ourPerson = repo.getPersons().get(i);
		}
		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("firstName","address","city","zip","phone","birthdate","age",
				"firestationNumber");

		FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		MappingJacksonValue produitsFiltres = new MappingJacksonValue(ourPerson);
		produitsFiltres.setFilters(listDeNosFiltres);

		return produitsFiltres;
	}

	
	@GetMapping("/firestation")
	public MappingJacksonValue afficherPersonnesDeZone(@RequestParam(name="stationNumber", required = true)String number) throws Exception {

		zoneService.setPersons(repo.getPersons()
				  .stream()
				  .filter(c -> c.getFirestationNumber().equals(number))
				  .collect(Collectors.toList()));

		for (Person person : zoneService.getPersons()) {
			if(person.getAge()>=18)
				zoneService.increaseAdult();
			else
				zoneService.increaseChild();
		}

		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("city","zip","email"
				,"birthdate","age","firestationNumber","medications","allergies");
		FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		MappingJacksonValue personFiltres = new MappingJacksonValue(zoneService);
		personFiltres.setFilters(listDeNosFiltres);

		return  personFiltres;
	}
	
	
	@GetMapping("/childAlert")
	public MappingJacksonValue afficherEnfant(@RequestParam(name="address", required = true)String address) {
		

		childrenService.setChildrens(repo.getPersons()
				.stream()
				.filter(c -> c.getAge() < 18 && c.getAddress().equals(address))
				.collect(Collectors.toList()));
		
		childrenService.setPersonFamily(repo.getPersons()
				.stream()
				.filter(c -> c.getAge() > 18 && c.getAddress().equals(address))
				.collect(Collectors.toList()));

		if(childrenService.getChildrens().size() == 0)
			return null;

		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("address","city","zip","email","phone"
				,"birthdate","firestationNumber","medications","allergies");
		FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		MappingJacksonValue personFiltres = new MappingJacksonValue(childrenService);
		personFiltres.setFilters(listDeNosFiltres);

		return personFiltres;
	}

	@GetMapping("/fire")
	public MappingJacksonValue afficherHabitants(@RequestParam(name="address", required = true)String address) {

		fireService.setPersons( repo.getPersons()
				.stream()
				.filter(c -> c.getAddress().equals(address))
				.collect(Collectors.toList()));

		if(fireService.getPersons().size() > 0)
			fireService.setFirestation(fireService.getPersons().get(0).getFirestationNumber());

		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("firstName","address","city","zip","email"
				,"birthdate","firestationNumber");
		FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		MappingJacksonValue personFiltres = new MappingJacksonValue(fireService);
		personFiltres.setFilters(listDeNosFiltres);


		return personFiltres;
	}
	
	@GetMapping("/phoneAlert")
	public List<String> afficherNumberByFirestation(@RequestParam(name="firestation", required = true)String firestation) {
		List<String> phoneNumber = new ArrayList<String>();
		List<Person> localPerson = repo.getPersons()
				.stream()
				.filter(c -> c.getFirestationNumber().equals(firestation))
				.collect(Collectors.toList());

		for (Person person : localPerson) {
			phoneNumber.add(person.getPhone());
		}
		return phoneNumber;
		
	}


	@GetMapping("/communityEmail")
	public List<String> afficherEmailOfCity(@RequestParam(name="city", required = true)String city) {
		List<String> emailCommunity = new ArrayList<>();
		List<Person> personFromCity = repo.getPersons()
				.stream()
				.filter(c -> c.getCity().equals(city))
				.collect(Collectors.toList());

		for (Person person : personFromCity) {
			emailCommunity.add(person.getEmail());
		}
		return emailCommunity;
	}
	
	@GetMapping(value = "/medicalrecord")
	public List<Medicalrecord> afficherMedicalrecord() throws Exception {
		return repo.getMedicalrecords();
	}
	
}
