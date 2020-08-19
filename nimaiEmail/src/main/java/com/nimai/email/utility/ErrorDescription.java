package com.nimai.email.utility;

import java.util.HashMap;
import java.util.Map;

public class ErrorDescription {

	private static Map<String, String> codeToDescriptionMap = new HashMap<String, String>();

	static {
		codeToDescriptionMap.put("NIM000", "SUCCESS");
		codeToDescriptionMap.put("NIM001", "FAILURE");
		
		codeToDescriptionMap.put("EXE000", "Exception Occurred : ");
				
	
		codeToDescriptionMap.put("ASA001", "User id does not exist.");
		codeToDescriptionMap.put("ASA002", "Email Send Succefully");
		codeToDescriptionMap.put("ASA003","Password Already Reset!");
		codeToDescriptionMap.put("ASA004","Setting password link has expired!");
		codeToDescriptionMap.put("ASA005","Email Address is not registred!");
		codeToDescriptionMap.put("ASA006","Domain Name does not match!");
		codeToDescriptionMap.put("ASA007","Subsidiary Account Registration link expired!");
		codeToDescriptionMap.put("ASA008", "Refer Account Registration link expired!");
        codeToDescriptionMap.put("ASA009", "Branch user passcode expired!");

	}
	
	public static String getDescription(String code) 
	{
		String description = codeToDescriptionMap.get(code);
		if (description == null) 
		{
			description = "Invalid Error Code!";
		}
		return description;
	}

}

