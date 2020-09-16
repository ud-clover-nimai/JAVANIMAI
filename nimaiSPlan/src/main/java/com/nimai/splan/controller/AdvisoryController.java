package com.nimai.splan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.splan.model.NimaiAdvisory;
import com.nimai.splan.payload.GenericResponse;
import com.nimai.splan.service.NimaiAdvisoryService;
import com.nimai.splan.utility.ErrorDescription;




@RestController
public class AdvisoryController {
	
	@Autowired   
	NimaiAdvisoryService advisoryService;
	
	GenericResponse response = new GenericResponse<>();
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewAdvisory", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<?> viewAdvisory()
	{
			List<NimaiAdvisory> nadvisory= advisoryService.viewAdvisory();
			if(nadvisory.isEmpty())
			{
				response.setErrMessage("No Records Found");;
				response.setStatus("Failure");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
			else
			{
				response.setData(nadvisory);
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
    }
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/getAdvisoryListByCountry", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAdvisoryByCountryName(@RequestBody NimaiAdvisory nimaiAdvisory) {
		GenericResponse response = new GenericResponse<>();
		try {
			String country_name=nimaiAdvisory.getCountry_name();
			List<NimaiAdvisory> outdata1 =  (List<NimaiAdvisory>) advisoryService.viewAdvisoryByCountry(country_name);
			response.setData(outdata1);
			response.setStatus("Success");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
			
		}
		catch(Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
