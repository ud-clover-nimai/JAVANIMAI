package com.nimai.splan.service;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.splan.model.NimaiMMCoupen;
import com.nimai.splan.repository.NimaiMMCoupenRepo;

@Service
public class ValidateCoupenServiceImpl implements ValidateCoupenService{
	
	@Autowired
	EntityManagerFactory emFactory;
	
	@Autowired
	NimaiMMCoupenRepo nimaimmRepo;

	@Override
	public HashMap<String, String> validateCoupen(String coupenId,String countryName,String subscriptionPlan,String coupenfor) {
		EntityManager entityManager = emFactory.createEntityManager();
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("VALIDATE_COUPEN",NimaiMMCoupen.class);

		storedProcedureQuery.registerStoredProcedureParameter("inp_coupen_id", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_country_name", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_subsciption_plan", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_user_type", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("out_coupen_status", String.class, ParameterMode.OUT);
		storedProcedureQuery.registerStoredProcedureParameter("out_total_amount", float.class, ParameterMode.OUT);
		
		storedProcedureQuery.setParameter("inp_coupen_id", coupenId);
		storedProcedureQuery.setParameter("inp_country_name",countryName );
		storedProcedureQuery.setParameter("inp_subsciption_plan",subscriptionPlan );
		storedProcedureQuery.setParameter("inp_user_type",coupenfor );
		storedProcedureQuery.execute();
		
		String out_coupen_status = (String) storedProcedureQuery.getOutputParameterValue("out_coupen_status");
		Float out_total_amount = (Float) storedProcedureQuery.getOutputParameterValue("out_total_amount");
		
		HashMap<String, String> outputdata=new HashMap<String, String>();
		outputdata.put("coupenstatus", out_coupen_status);
		outputdata.put("totalamount", out_total_amount.toString());
		return outputdata;
		
	}
	
}	
	
	


