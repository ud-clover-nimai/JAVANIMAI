package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.entity.Customer;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer , String>{
	
	@Query(value ="SELECT * FROM NIMAI_M_CUSTOMER WHERE userid =:userid" , nativeQuery = true)
	Customer findByUSERID(@Param("userid") String userid);
}
