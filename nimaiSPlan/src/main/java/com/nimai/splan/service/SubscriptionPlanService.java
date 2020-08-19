package com.nimai.splan.service;


import org.springframework.http.ResponseEntity;

import com.nimai.splan.payload.SPlanApprovalBean;
import com.nimai.splan.payload.SubscriptionBean;

public interface SubscriptionPlanService {

	ResponseEntity<Object> saveOrUpdateSubscriptionPlanDetails(SubscriptionBean subscriptionRequest);

	ResponseEntity<?> findAllSPlanDetails();

	ResponseEntity<?> saveUserSubscriptionPlan(SubscriptionBean subscriptionRequest, String userID);

	ResponseEntity<?> findSPlanDetailsByUserId(String userId);

	ResponseEntity<?> deleteBySubscriptionId(String subscriptionId);

	ResponseEntity<?> getSPlanByUserId(String userId);

	ResponseEntity<?> saveSplanDetails(SPlanApprovalBean sPlanEntity);

	ResponseEntity<?> findMSPlanDetails();

	ResponseEntity<?> findMSPlanDetails(String userId);

	ResponseEntity<?> getAllMasterSPlanDetails();
	
}
