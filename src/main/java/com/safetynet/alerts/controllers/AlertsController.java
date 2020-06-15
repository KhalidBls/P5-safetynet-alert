package com.safetynet.alerts.controllers;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.models.*;
import com.safetynet.alerts.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertsController {

	@Autowired
	private DataInitialization repo;
	@Autowired
	private FirestationService firestationService;
	@Autowired
	private PersonService personService;
	@Autowired
	private MedicalrecordService medicalrecordService;



	@GetMapping("/personInfo")
	public MappingJacksonValue afficherLesPersonne(@RequestParam(name="firstName", required = true)String firstName
			,@RequestParam(name="lastName", required = true)String lastName) throws Exception {
		List<Person> ourPersonList = new ArrayList<>();
		for(int i = 0;i<repo.getPersons().size(); i++) {
			if( repo.getPersons().get(i).getLastName().equals(lastName) )
				ourPersonList.add(repo.getPersons().get(i));
		}
		for (Person person : ourPersonList) {
			person.setAllergies(medicalrecordService.findMedicalrecordByName(person.getFirstName(),person.getLastName()).getAllergies());
			person.setMedications(medicalrecordService.findMedicalrecordByName(person.getFirstName(),person.getLastName()).getMedications());
		}

		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("firstName","city","zip","phone","birthdate");

		FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		MappingJacksonValue personsFiltres = new MappingJacksonValue(ourPersonList);
		personsFiltres.setFilters(listDeNosFiltres);

		return personsFiltres;
	}


	@GetMapping("/firestation")
	public MappingJacksonValue afficherPersonnesDeZone(@RequestParam(name="stationNumber", required = true)String number) throws Exception {
		String[] tab = {"city","zip","email","birthdate","medications","allergies"};
		return firestationService.filter(tab,firestationService.sortPersonByStation(number));
	}

	
	@GetMapping("/childAlert")
	public MappingJacksonValue afficherEnfant(@RequestParam(name="address", required = true)String address) {
		String[] tab = {"address","city","zip","email","phone","birthdate","medications","allergies"};
		return personService.filter(tab,personService.sortChildrenAndAdultByAddress(address));
	}

	@GetMapping("/flood/stations")
	public MappingJacksonValue afficherHabitantsParStations(@RequestParam(name="stations", required = true)List<String> listOfStations) {
		String[] tab = {"address","city","zip","email","birthdate"};
		return firestationService.filter(tab,firestationService.sortPersonByListOfStations(listOfStations));
	}

	@GetMapping("/fire")
	public MappingJacksonValue afficherHabitants(@RequestParam(name="address", required = true)String address) {
		String[] tab = {"firstName","address","city","zip","email","birthdate"};
		return firestationService.filter(tab,firestationService.sortPersonByAddress(address));
	}
	
	@GetMapping("/phoneAlert")
	public List<String> afficherNumberByFirestation(@RequestParam(name="firestation", required = true)String firestation) {
		List<String> phoneNumber = new ArrayList<String>();

		Firestation ourFirestation = firestationService.findByNumber(firestation);

		List<Person> localPerson = repo.getPersons()
				.stream()
				.filter(c -> c.getAddress().equals(ourFirestation.getAddress()))
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

	
}
