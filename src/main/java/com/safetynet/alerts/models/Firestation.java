package com.safetynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.ArrayList;
import java.util.List;


@JsonFilter("monFiltreDynamique")
public class Firestation {

	private String address;
	private String station ;
	
	private List<Person> personToSave = new ArrayList<Person>();

	public Firestation(String address, String station) {
		super();
		this.address = address;
		this.station = station;
	}
	
	public  void addPerson(Person person) {
		personToSave.add(person);
	}

	public List<Person> getPersonToSave() {
		return personToSave;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	

}
