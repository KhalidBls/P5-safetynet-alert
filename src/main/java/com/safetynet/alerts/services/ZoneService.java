package com.safetynet.alerts.services;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.safetynet.alerts.models.Person;
import org.springframework.stereotype.Service;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Service
public class ZoneService {

	private List<Person> persons = new ArrayList<Person>();
	private int numberOfAdults = 0;
	private int numberOfChilds = 0;
	


	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public int getNumberOfChilds() {
		return numberOfChilds;
	}

	public void setNumberOfChilds(int numberOfChilds) {
		this.numberOfChilds = numberOfChilds;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}


}
