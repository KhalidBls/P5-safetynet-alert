package com.safetynet.alerts.repositories;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.Service;
import org.springframework.stereotype.Repository;

@Repository
public class EntitiesRepository {

	Service service;
	JSONObject jsonObj;
	
	List<Person> persons = new ArrayList<>();
	List<Firestation> firestations = new ArrayList<>();
	List<Medicalrecord> medicalrecords = new ArrayList<>();

	@Autowired
	public EntitiesRepository(Service service) throws ParseException {
		super();
		this.service = service;
		jsonObj = service.getJobj();
		
	}

	
	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public List<Person> getPersons() {
		return persons;
	}
	
	public List<Medicalrecord> getMedicalrecords() {
		return medicalrecords;
	}
	
	public void parseJsonToPersonObject() {
		JSONArray arr = (JSONArray) jsonObj.get("persons");
		
		for(int i = 0;i<arr.size();i++) {
			//Store the JSON objects in an array
			//Get the index of the JSON object and print the values as per the index
			JSONObject jsonobj = (JSONObject)arr.get(i);
			persons.add(new Person((String )jsonobj.get("firstName"), (String )jsonobj.get("lastName"),
					(String )jsonobj.get("address"), (String )jsonobj.get("city"),
					(String )jsonobj.get("zip"), (String )jsonobj.get("phone")
					, (String )jsonobj.get("email")));
		}

	}
	
	public void parseJsonToFirestationObject() {
		JSONArray arr = (JSONArray) jsonObj.get("firestations");
		
		for(int i = 0;i<arr.size();i++) {		
			JSONObject jsonobj = (JSONObject)arr.get(i);
			firestations.add(new Firestation((String )jsonobj.get("address"), (String )jsonobj.get("station")));
			
			String address = (String) jsonobj.get("address");
			for(int j =0;j<persons.size();j++) {
				if(address.equals(persons.get(j).getAddress())) {
					firestations.get(i).addPerson(new Person(persons.get(j).getFirstName(),persons.get(j).getLastName()
							,persons.get(j).getAddress(),persons.get(j).getPhone()));
					persons.get(j).setFirestationNumber(firestations.get(i).getStation());
				}
			}
		}	
	}
	
	public List<Firestation> getFirestations() {
		return firestations;
	}


	public void setFirestations(List<Firestation> firestations) {
		this.firestations = firestations;
	}


	public void parseJsonToMedicalrecordObject() throws ParseException {
		JSONArray arr = (JSONArray) jsonObj.get("medicalrecords");
		
		for(int i = 0;i<arr.size();i++) {		
			//Store the JSON objects in an array
			//Get the index of the JSON object and print the values as per the index
			JSONObject jsonobj = (JSONObject)arr.get(i);
			medicalrecords.add(new Medicalrecord((String )jsonobj.get("firstName"), (String )jsonobj.get("lastName")
					,(String )jsonobj.get("birthdate")));
			
			findByName((String )jsonobj.get("firstName"),(String )jsonobj.get("lastName")).setBirthdate((String )jsonobj.get("birthdate"));
			
			JSONArray arr2 = (JSONArray) jsonobj.get("medications");
			for(int j = 0; j < arr2.size(); j++) {
				medicalrecords.get(i).addMedications((String)arr2.get(j)); 
			}
			
			JSONArray arr3 = (JSONArray) jsonobj.get("allergies");
			for(int j = 0; j < arr3.size(); j++) {
				medicalrecords.get(i).addAllergies((String)arr3.get(j)); 
			}
		}		
		
	}


	public Person findByName(String firstName,String lastName) {
		for (Person person : persons) {
			if(person.getFirstName().equals(firstName) && person.getLastName().contentEquals(lastName))
				return person;
		}
		return null;
	}
	
	
	
	public Person save(Person person) {
		persons.add(person);
		return person;
	}
	
	public void parsing() throws ParseException {
		parseJsonToPersonObject();
		parseJsonToMedicalrecordObject();
		parseJsonToFirestationObject();

	}
	
}
