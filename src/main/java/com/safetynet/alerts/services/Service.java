package com.safetynet.alerts.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;




@org.springframework.stereotype.Service
public class Service {

	JSONObject jobj;
	
	
	public Service() throws Exception {
		super();
		recupererInfos();
	}
	
	public JSONObject getJobj() {
		return jobj;
	}



	public JSONObject recupererInfos() throws Exception {
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
		jobj = (JSONObject)parse.parse(inline);
		
		return jobj;
	}
	
	
	
}
