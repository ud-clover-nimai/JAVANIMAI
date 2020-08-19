package com.nimai.kyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.kyc.model.NimaiFKyc;


@Repository
public interface NimaiKycRepository extends JpaRepository<NimaiFKyc, String>, JpaSpecificationExecutor<NimaiFKyc> {

//	@Query(value = "SELECT SEQ_KYC_ID.nextval FROM dual", nativeQuery = true)
//	Long getNextSeriesId();

	@Query("FROM NimaiFKyc n where n.userid.userid = :userid")
	List<NimaiFKyc> getKycDetailsByUserId(String userid);
	
	
	 @Query("update NimaiFKyc c set c.approvalDate = :approvalDate,c.reason= :reason,c.status= :status,c.checkedBy=:checkedBy WHERE c.userid.userid=:userid AND c.kycid=:kycid")
     NimaiFKyc updateNimaiFKyc(@Param("approvalDate") String approvalDate, @Param("reason") String reason,@Param("status")String status,@Param("checkedBy") String checkedBy,@Param("userid") String userId,@Param("kycid") String kycId);
	
}
