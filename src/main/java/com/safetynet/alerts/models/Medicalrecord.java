package com.safetynet.alerts.models;

import com.fasterxml.jackson.annotation.JsonFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@JsonFilter("monFiltreDynamique")
public class Medicalrecord {

	private String firstName;
	private String lastName;
	private String birthdate;
	private List<String> medications = new ArrayList<String>();
	private List<String> allergies = new ArrayList<String>();
	


	public Medicalrecord(String firstName, String lastName, String birthdate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
	}

	public Medicalrecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.medications = medications;
		this.allergies = allergies;
	}

	public Medicalrecord(){}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public List<String> getMedications() {
		return medications;
	}
	public void setMedications(List<String> medications) {
		this.medications = medications;
	}
	public List<String> getAllergies() {
		return allergies;
	}
	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}
	
	
	public void addMedications(String medication) {
		medications.add(medication);
	}

	public void addAllergies(String allergie) {
		allergies.add(allergie);
	}

	
	
}
