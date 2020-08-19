package com.nimai.splan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.splan.payload.SPlanApprovalBean;
import com.nimai.splan.payload.SubscriptionBean;
import com.nimai.splan.service.SubscriptionPlanService;
import com.nimai.splan.service.SubscriptionPlanServiceImpl;

@RestController
public class SubscriptionPlanController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionPlanServiceImpl.class);

	@Autowired
	private SubscriptionPlanService sPlanService;
	
	@CrossOrigin("*")
	@PostMapping("/saveTempSplan")
	public ResponseEntity<?> saveSubscriptionDetails(@RequestBody SubscriptionBean subscriptionRequest) {
			LOGGER.info(" ================ Subscription Plan Controller -> saveSubscriptionDetails ================");

		return sPlanService.saveOrUpdateSubscriptionPlanDetails(subscriptionRequest);

	}

	@CrossOrigin("*")
	@GetMapping("/viewTempSplan")
	public ResponseEntity<?> findAllSubscriptionPlanDetails() {
		LOGGER.info(
				" ================ Subscription Plan Controller -> findAllSubscriptionPlanDetails ================");

		return sPlanService.findAllSPlanDetails();
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/confirmSplanDetails", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> confirmSplanDetails(@RequestBody SPlanApprovalBean sPlanEntity) {
		LOGGER.info(" ================ Subscription Plan Controller -> confirmSplanDetails ================");

		return sPlanService.saveSplanDetails(sPlanEntity);
	}

	@CrossOrigin("*")
	@GetMapping("/viewSPlanToUser/{userId}")
	public ResponseEntity<?> viewSPlanToUser(@PathVariable("userId") String userId) {
		LOGGER.info(" ================ Subscription Plan Controller -> viewSPlanToUser ================");
		return sPlanService.findMSPlanDetails(userId);
	}
	
	@CrossOrigin("*")
	@GetMapping("/getSPlans")
	public ResponseEntity<?> getAllSPlanDetails() {
		LOGGER.info(" ================ Subscription Plan Controller -> getAllSPlanDetails ================");

		return sPlanService.getAllMasterSPlanDetails();
	}

	@CrossOrigin("*")
	@PostMapping("/saveUserSubscriptionPlan/{userId}")
	public ResponseEntity<?> saveUserSubscriptionPlan(@PathVariable("userId") String userID,
			@RequestBody SubscriptionBean subscriptionRequest) {
		LOGGER.info(" ================ Subscription Plan Controller -> saveUserSubscriptionPlan ================");

		return sPlanService.saveUserSubscriptionPlan(subscriptionRequest, userID);
	}
	@CrossOrigin("*")
	@GetMapping("/list/{userId}")
	public ResponseEntity<?> findAllSPlanDetailsByUserId(@PathVariable("userId") String userId) {
		LOGGER.info(" ================ Subscription Plan Controller -> findAllSPlanDetailsByUserId ================");

		return sPlanService.findSPlanDetailsByUserId(userId);
	}

	@CrossOrigin("*")
	@DeleteMapping("/subscriptionPlan/{subscriptionId}")
	public ResponseEntity<?> deleteSPlan(@PathVariable("subscriptionId") String subscriptionId) {
		LOGGER.info(" ================ Subscription Plan Controller -> deleteSPlan ================");

		return sPlanService.deleteBySubscriptionId(subscriptionId);
	}

	@CrossOrigin("*")
	@GetMapping("/getSPlan/{userId}")
	public ResponseEntity<?> getSPlanByUserId(@PathVariable("userId") String userId) {
		LOGGER.info(" ================ Subscription Plan Controller -> getSPlanByUserId ================");

		return sPlanService.getSPlanByUserId(userId);
	}
	
	

	
}
