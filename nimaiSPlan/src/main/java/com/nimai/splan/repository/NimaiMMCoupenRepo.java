package com.nimai.splan.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.nimai.splan.model.NimaiMMCoupen;


@Repository
public interface NimaiMMCoupenRepo extends JpaRepository<NimaiMMCoupen, String>{
	
	
	@Procedure("VALIDATE_COUPEN")
	 void validCoupen(String coupenId,String countryName,String subscriptionPlan,String coupenfor);


}
