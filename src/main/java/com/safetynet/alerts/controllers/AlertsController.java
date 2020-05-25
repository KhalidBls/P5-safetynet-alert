package com.safetynet.alerts.controllers;



import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.safetynet.alerts.repositories.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.models.Children;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.repositories.ChildrenEntity;
import com.safetynet.alerts.repositories.EntitiesRepository;
import com.safetynet.alerts.repositories.PersonsOfStations;

@RestController
public class AlertsController {
	
	EntitiesRepository repo;
	List<Person> personList;

	@Autowired
	public AlertsController(EntitiesRepository repo) throws ParseException {
		super();
		this.repo = repo;
		repo.parsing();
		personList = repo.getPersons();
		
	}

	@GetMapping(value = "/person")
	public List<Person> afficherPersonnes() throws Exception {
		
		
		System.out.println(personList.get(1).getBirthdate());
		return personList;
	
	}
	
	@GetMapping("/personInfo")
	public Person afficherLaPersonne(@RequestParam(name="firstName", required = true)String firstName
			,@RequestParam(name="lastName", required = true)String lastName) throws Exception {
		for(int i = 0;i<personList.size(); i++) {
			if(personList.get(i).getFirstName().equals(firstName) && personList.get(i).getLastName().equals(lastName) )
				return personList.get(i);
		}
		return null;
	}
	
	@PostMapping(value = "/person")
	public List<Person> ajouterPersonnes(@RequestBody Person person) throws Exception {
			repo.save(person);
			personList = repo.getPersons();
			
			return personList;
	}
	
	@GetMapping("/firestation")
	public PersonsOfStations afficherPersonnesDeZone(@RequestParam(name="stationNumber", required = true)String number) throws Exception {

		List<Firestation> firestationObj = repo.getFirestations()
				  .stream()
				  .filter(c -> c.getStation().equals(number))
				  .collect(Collectors.toList());
		

		PersonsOfStations personsToSave = new PersonsOfStations();
		
		for(int i = 0;i<firestationObj.size();i++) {
			for(int j = 0;j<firestationObj.get(i).getPersonToSave().size();j++) 
			personsToSave.addPerson(firestationObj.get(i).getPersonToSave().get(j)
					,repo.findByName(firestationObj.get(i).getPersonToSave().get(j).getFirstName(),firestationObj.get(i).getPersonToSave().get(j).getLastName())
					.getAge());
		}
		return  personsToSave;
	}
	
	
	@GetMapping("/childAlert")
	public ChildrenEntity afficherEnfant(@RequestParam(name="address", required = true)String address) {
		
		ChildrenEntity childrenEntity = new ChildrenEntity();
		List<Person> childPerson = repo.getPersons()
				.stream()
				.filter(c -> c.getAge() < 18 && c.getAddress().equals(address))
				.collect(Collectors.toList());
		
		List<Person> adultPerson = repo.getPersons()
				.stream()
				.filter(c -> c.getAge() > 18 && c.getAddress().equals(address))
				.collect(Collectors.toList());
		
		for (Person person : adultPerson) {
			childrenEntity.getPersonFamily().add(person);
		}
		
		for(int i =0;i < childPerson.size(); i++) {
			childrenEntity.getChildrens().add(new Children(childPerson.get(i).getFirstName()
					,childPerson.get(i).getLastName(),childPerson.get(i).getAge()));
		}
		
		return childrenEntity;
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

	//A METTRE EN PLACE
	@GetMapping("/fire")
	public AddressEntity afficherHabitants(@RequestParam(name="address", required = true)String address) {

		AddressEntity addressEntity = new AddressEntity();
		List<Person> personFromAddress = repo.getPersons()
				.stream()
				.filter(c -> c.getAddress().equals(address))
				.collect(Collectors.toList());

		for (Person person : personFromAddress) {
			addressEntity.getPersonOfAddress().add(new Person(person.getLastName(),person.getPhone(),
					person.getAge(),person.getMedications(),person.getAllergies()));
			addressEntity.setFirestationNumber(person.getFirestationNumber());
		}

		return addressEntity;
	}
	
	
	@GetMapping(value = "/medicalrecord")
	public List<Medicalrecord> afficherMedicalrecord() throws Exception {
		return repo.getMedicalrecords();
	}
	
}
