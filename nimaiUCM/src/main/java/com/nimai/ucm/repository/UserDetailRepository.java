package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.entity.NimaiCustomer;

@Repository
public interface UserDetailRepository extends JpaRepository<NimaiCustomer, String>,  JpaSpecificationExecutor<NimaiCustomer>{
	
	boolean existsByEmailAddress(String emailAddress);
	
	 @Query("from NimaiCustomer where emailAddress= :emailId")
	 NimaiCustomer isEmailExsists(String emailId);
	
}
