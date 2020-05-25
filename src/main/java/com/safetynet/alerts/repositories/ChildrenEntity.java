package com.safetynet.alerts.repositories;

import java.util.ArrayList;
import java.util.List;

import com.safetynet.alerts.models.Children;
import com.safetynet.alerts.models.Person;

public class ChildrenEntity {

	private List<Children> childrens = new ArrayList<Children>();
	private List<Person> personFamily = new ArrayList<Person>();
	
	public List<Children> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<Children> childrens) {
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
