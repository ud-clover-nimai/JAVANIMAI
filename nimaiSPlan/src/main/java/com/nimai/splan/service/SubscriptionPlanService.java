package com.nimai.splan.service;

import org.springframework.http.ResponseEntity;

import com.nimai.splan.payload.SplanRequest;
import com.nimai.splan.payload.SubscriptionBean;

public interface SubscriptionPlanService {

	ResponseEntity<?> saveUserSubscriptionPlan(SubscriptionBean subscriptionRequest, String userID);

	ResponseEntity<?> findSPlanDetailsByUserId(String userId);

	ResponseEntity<?> getSPlanByUserId(String userId);
	
	ResponseEntity<?> findMSPlanDetails(String userId);

	ResponseEntity<?> findCustomerSPlanDetails(SplanRequest splanRequest);

}
