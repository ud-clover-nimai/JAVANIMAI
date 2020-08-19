package com.nimai.splan.service;

import java.util.HashMap;

public interface ValidateCoupenService {
	
	public HashMap<String, String> validateCoupen(String coupenId,String countryName,String subscriptionPlan,String coupenfor);
	
}
