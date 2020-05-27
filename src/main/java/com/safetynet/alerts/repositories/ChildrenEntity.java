package com.safetynet.alerts.repositories;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.models.Person;

public class ChildrenEntity {

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
