package com.nimai.splan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.splan.payload.GenericResponse;
import com.nimai.splan.payload.SplanRequest;
import com.nimai.splan.payload.SubscriptionBean;
import com.nimai.splan.service.SubscriptionPlanService;
import com.nimai.splan.utility.ErrorDescription;

@RestController
public class SubscriptionPlanController {

	private static Logger logger = LoggerFactory.getLogger(SubscriptionPlanController.class);

	@Autowired
	private SubscriptionPlanService sPlanService;

	/**
	 * Dhiraj Code
	 *  on the basis of customer type and country splan is view to user
	 * @param userID
	 * @param subscriptionRequest
	 * @return
	 */
//	@CrossOrigin("*")
//	@PostMapping("/viewCustomerSPlan")
//	public ResponseEntity<?> ViewCustomerSPlans(@RequestBody SplanRequest sPLanRequest) {
//		logger.info(" ================ Send ViewCustomerSPlans API is Invoked ================:"
//				+ sPLanRequest.getUserId());
//		GenericResponse response = new GenericResponse<>();
//		if (!sPLanRequest.getUserId().substring(0, 2).equalsIgnoreCase("RE")) {
//			return sPlanService.findCustomerSPlanDetails(sPLanRequest);
//		}
//		response.setErrCode("ASA014");
//		response.setErrMessage(ErrorDescription.getDescription("ASA014"));
//		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
//
//	}

	@CrossOrigin("*")
	@PostMapping("/saveUserSubscriptionPlan/{userId}")
	public ResponseEntity<?> saveCustomerSPlan(@PathVariable("userId") String userID,
			@RequestBody SubscriptionBean subscriptionRequest) {
		logger.info(" ================ Send saveCustomerSPlan API is Invoked ================:" + userID);
		GenericResponse response = new GenericResponse<>();
		if (!userID.substring(0, 2).equalsIgnoreCase("RE")) {
			return sPlanService.saveUserSubscriptionPlan(subscriptionRequest, userID);
		}
		response.setErrCode("ASA014");
		response.setErrMessage(ErrorDescription.getDescription("ASA014"));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

	}

	@CrossOrigin("*")
	@GetMapping("/viewSPlanToUser/{userId}")
	public ResponseEntity<?> viewSPlanToCustomer(@PathVariable("userId") String userId) {
		logger.info(" ================ Send viewSPlanToCustomer API is Invoked ================:" + userId);
		return sPlanService.findMSPlanDetails(userId);
	}

	@CrossOrigin("*")
	@GetMapping("/list/{userId}")
	public ResponseEntity<?> findAllSPlanDetailsByUserId(@PathVariable("userId") String userId) {
		logger.info(" ================ Send findAllSPlanDetailsByUserId API is Invoked ================:" + userId);
		return sPlanService.findSPlanDetailsByUserId(userId);
	}

	@CrossOrigin("*")
	@GetMapping("/getSPlan/{userId}")
	public ResponseEntity<?> getSPlanByUserId(@PathVariable("userId") String userId) {
		logger.info(" ================ Send getSPlanByUserId API is Invoked ================:" + userId);
		return sPlanService.getSPlanByUserId(userId);
	}
	
	@CrossOrigin("*")
	@PostMapping("/viewCustomerSPlan")
	public ResponseEntity<?> ViewCustomerSPlans(@RequestBody SplanRequest sPLanRequest) {
		logger.info(" ================ Send ViewCustomerSPlans API is Invoked ================:"
				+ sPLanRequest.getUserId());
		GenericResponse response = new GenericResponse<>();
		if (!sPLanRequest.getUserId().substring(0, 2).equalsIgnoreCase("RE")) {
			return sPlanService.findCustomerSPlanDetails(sPLanRequest);
		}
		response.setErrCode("ASA014");
		response.setErrMessage(ErrorDescription.getDescription("ASA014"));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

	}


}
