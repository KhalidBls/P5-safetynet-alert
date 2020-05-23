package com.safetynet.alerts.repositories;


import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.models.Person;




public class PersonsOfStations {

	private List<Person> persons = new ArrayList<Person>();
	private  Integer numberOfAdults = 0;
	private  Integer numberOfChilds = 0;
	


	public Integer getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(Integer numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public Integer getNumberOfChilds() {
		return numberOfChilds;
	}

	public void setNumberOfChilds(Integer numberOfChilds) {
		this.numberOfChilds = numberOfChilds;
	}
	

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public void addPerson(Person person, int age) {
		if(age > 18  )
			this.numberOfAdults++;
		else
			this.numberOfChilds++;
		persons.add(person);
	}
	
}
