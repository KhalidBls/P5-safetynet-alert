package com.safetynet.alerts.services;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.models.Person;
import org.springframework.stereotype.Service;

@Service
public class ChildrenService {

	private List<Person> childrens = new ArrayList<Person>();
	private List<Person> personFamily = new ArrayList<Person>();
	
	public List<Person> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<Person> childrens) {
		this.childrens = childrens;
	}
	public List<Person> getPersonFamily()
	{
		return personFamily;
	}

	public void setPersonFamily(List<Person> personFamily) {
		this.personFamily = personFamily;
	}
	
	

}
