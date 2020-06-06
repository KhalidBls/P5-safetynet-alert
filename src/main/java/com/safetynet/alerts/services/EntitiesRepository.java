package com.safetynet.alerts.services;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Medicalrecord;
import com.safetynet.alerts.models.Person;
import javax.annotation.PostConstruct;

@org.springframework.stereotype.Service
public class EntitiesRepository {

	private JSONObject jsonObj;

	private List<Person> persons = new ArrayList<>();
	private List<Firestation> firestations = new ArrayList<>();
	private List<Medicalrecord> medicalrecords = new ArrayList<>();


	public EntitiesRepository() throws Exception {
		jsonObj = recupererInfos();
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<Firestation> getFirestations() {
		return firestations;
	}

	public void setFirestations(List<Firestation> firestations) {
		this.firestations = firestations;
	}

	public void setMedicalrecords(List<Medicalrecord> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}

	@PostConstruct
	public void init() throws ParseException {
		parsing();
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
				}
			}
		}	
	}

	public void parseJsonToMedicalrecordObject() throws ParseException {
		JSONArray arr = (JSONArray) jsonObj.get("medicalrecords");
		
		for(int i = 0;i<arr.size();i++) {		
			//Store the JSON objects in an array
			//Get the index of the JSON object and print the values as per the index
			JSONObject jsonobj = (JSONObject)arr.get(i);
			medicalrecords.add(new Medicalrecord((String )jsonobj.get("firstName"), (String )jsonobj.get("lastName")
					,(String )jsonobj.get("birthdate")));

			findPersonByName((String )jsonobj.get("firstName"),(String )jsonobj.get("lastName")).setBirthdate((String )jsonobj.get("birthdate"));
			
			JSONArray arr2 = (JSONArray) jsonobj.get("medications");
			for(int j = 0; j < arr2.size(); j++) {
				medicalrecords.get(i).addMedications((String)arr2.get(j));
				//findPersonByName((String )jsonobj.get("firstName"),(String )jsonobj.get("lastName")).getMedications().add((String)arr2.get(j));
			}
			
			JSONArray arr3 = (JSONArray) jsonobj.get("allergies");
			for(int j = 0; j < arr3.size(); j++) {
				medicalrecords.get(i).addAllergies((String)arr3.get(j));
				//findPersonByName((String )jsonobj.get("firstName"),(String )jsonobj.get("lastName")).getAllergies().add((String)arr3.get(j));
			}
		}		
		
	}

	public Person findPersonByName(String firstName,String lastName) {
		for (Person person : persons) {
			if(person.getFirstName().equals(firstName) && person.getLastName().contentEquals(lastName))
				return person;
		}
		return null;
	}



	public void parsing() throws ParseException {
		parseJsonToPersonObject();
		parseJsonToMedicalrecordObject();
		parseJsonToFirestationObject();

	}


	//************************* POUR LE LIRE DEPUIS UNE URL ***************************************
	/*public JSONObject recupererInfos() throws Exception {
		URL apiUrl = new URL("https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json");
		String inline = "";
		HttpURLConnection conn = (HttpURLConnection)apiUrl.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		int responsecode = conn.getResponseCode();
		if(responsecode != 200)
			throw new RuntimeException("HttpResponseCode: " +responsecode);
		else{

			Scanner sc = new Scanner(apiUrl.openStream());
			while(sc.hasNext())
			{
				inline+=sc.nextLine();
			}
			sc.close();
		}
		JSONParser parse = new JSONParser();
		JSONObject  jobj = (JSONObject)parse.parse(inline);

		return jobj;
	}*/

	// ******************LE LIRE DEPUIS UN FICHIER***************************
	public JSONObject recupererInfos() throws Exception {
		String filepath = "src/data.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(filepath));
			JSONObject  jobj  = (JSONObject)obj;
			return jobj;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Firestation findFirestation(String address) {
		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equals(address))
				return firestation;
		}
		return null;
	}

	public Firestation findFirestationByNumber(String number) {
		for (Firestation firestation : firestations) {
			if (firestation.getStation().equals(number))
				return firestation;
		}
		return null;
	}

	public Medicalrecord findMedicalrecordByName(String firstName, String lastName) {
		for (Medicalrecord medicalrecord : medicalrecords) {
			if(medicalrecord.getFirstName().equals(firstName) && medicalrecord.getLastName().equals(lastName))
				return medicalrecord;
		}
		return null;
	}
}
