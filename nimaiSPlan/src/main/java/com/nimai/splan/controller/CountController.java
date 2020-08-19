package com.nimai.splan.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.splan.model.NimaiMCustomer;
import com.nimai.splan.payload.GenericResponse;
import com.nimai.splan.service.GetCountService;
import com.nimai.splan.utility.ErrorDescription;

@RestController
public class CountController {

	private static final Logger logger = LoggerFactory.getLogger(CountController.class);

	@Autowired
	GetCountService getCountService;

	/*
	 * @RequestMapping(value ="/getCount/{userid}/{emailAddress}", produces =
	 * "application/json", method = RequestMethod.POST) public
	 * ResponseEntity<Object> getCount(@PathVariable("userid") String
	 * userid,@PathVariable("emailAddress") String emailAddress) {
	 * logger.info("======== Getting details and count details =========");
	 * GenericResponse response = new GenericResponse<>(); HashMap<String, Object>
	 * outdata1 = (HashMap<String, Object>)
	 * getCountService.getCount(userid,emailAddress); response.setData(outdata1);
	 * return new ResponseEntity<Object>(response, HttpStatus.OK);
	 * 
	 * }
	 */

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getCount", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<Object> getCount(@RequestBody NimaiMCustomer nimaicustomer) {
		logger.info("======== Getting personal details and count details =========");
		GenericResponse response = new GenericResponse<>();
		try {

			String userid = nimaicustomer.getUserid();
			String emailAddress = nimaicustomer.getEmailAddress();
			if (nimaicustomer.getUserid() != null && nimaicustomer.getEmailAddress() == null) {
				HashMap<String, Object> outdata1 = (HashMap<String, Object>) getCountService.getCount(userid,
						emailAddress);
				response.setData(outdata1);
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				HashMap<String, Object> outdata1 = (HashMap<String, Object>) getCountService.getCount(userid,
						emailAddress);
				response.setData(outdata1);
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
