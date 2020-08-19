package com.nimai.kyc.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.kyc.model.NimaiMCustomer;

public interface NimaiCustomerRepository extends JpaRepository<NimaiMCustomer, String>, JpaSpecificationExecutor<NimaiMCustomer> {
	
	@Query("FROM NimaiMCustomer r where r.userid = :userid")
    Optional<NimaiMCustomer> findByUserId(@Param("userid") String userid);

	@Query("FROM NimaiMCustomer r where r.userid = :userid")
    NimaiMCustomer findByUId(@Param("userid") String userid);

	@Modifying
	@Transactional
	@Query("update NimaiMCustomer c set c.kycStatus= ?1 WHERE c.userid= ?2")
	void updateKycStatus(String kycStatus,String userId);
 
}