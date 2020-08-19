package com.nimai.splan.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.splan.model.NimaiFCoupenCountry;
import com.nimai.splan.model.NimaiFCoupenSubscription;
import com.nimai.splan.model.NimaiMMCoupen;
import com.nimai.splan.payload.GenericResponse;
import com.nimai.splan.service.ValidateCoupenService;

@RestController
public class ValidateCoupenController {

	@Autowired
	ValidateCoupenService validatecoupenService;

	
	  @RequestMapping(value ="/validateCoupen/{coupenId}/{countryName}/{subscriptionPlan}/{coupenfor}",produces = "application/json", method = RequestMethod.POST)
	  public ResponseEntity<Object> validateCoupen(@PathVariable("coupenId") String coupenId,@PathVariable("countryName") String countryName,@PathVariable("subscriptionPlan") String subscriptionPlan,@PathVariable("coupenfor") String coupenfor) {
	
	  GenericResponse response = new GenericResponse<>();
	  NimaiMMCoupen nmcoupen=new NimaiMMCoupen();
	  
	  
	  HashMap<String, String> outdata =
	  validatecoupenService.validateCoupen(coupenId, countryName,subscriptionPlan,coupenfor); 
	  response.setData(outdata);
	  return new ResponseEntity<Object>(response, HttpStatus.OK); 
	  }
	

}
