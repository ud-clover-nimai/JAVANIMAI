package com.nimai.splan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nimai.splan.model.NimaiMCustomer;

public interface NimaiMCustomerRepository extends JpaRepository<NimaiMCustomer, String>  {

	@Query("FROM NimaiMCustomer r where r.userid = :userId")
	Optional<NimaiMCustomer> findByUserId(String userId);

}
