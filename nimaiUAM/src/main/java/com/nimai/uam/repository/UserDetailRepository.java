package com.nimai.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nimai.uam.entity.NimaiClient;



@Repository
public interface UserDetailRepository extends JpaRepository<NimaiClient, String>,  JpaSpecificationExecutor<NimaiClient>{
	
	boolean existsByEmailAddress(String emailAddress);
	

	
}
