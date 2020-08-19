package com.nimai.kyc.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimai.kyc.model.NimaiKyc;

@Repository
public interface NimaiKycBase64Repository extends JpaRepository<NimaiKyc, Integer>, JpaSpecificationExecutor<NimaiKyc> {

	@Query("FROM NimaiKyc n where n.custUserId.userid = :userid")
	List<NimaiKyc> getKycDetailsByUserId(String userid);

	@Modifying
	@Transactional
	@Query("update NimaiKyc u set u.country = ?1,u.title = ?2,u.encodedFileContent = ?3,u.custUserId = ?4,u.modifiedDate = ?5,u.kycStatus = ?6 where u.kycId = ?7")
	void update(String country, String title, String encodedFileContent, Object setCustUserId, Date modifiedDate,
			String status, Integer kycId);

}
