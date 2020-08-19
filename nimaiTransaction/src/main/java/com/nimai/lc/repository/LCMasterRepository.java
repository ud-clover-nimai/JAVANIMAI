package com.nimai.lc.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.nimai.lc.entity.NimaiLCMaster;

@Transactional
public interface LCMasterRepository extends JpaRepository<NimaiLCMaster, String> {
	@Query(value = "SELECT * from get_all_transaction", nativeQuery = true)
	List<NimaiLCMaster> findAllTransaction();

	@Query(value = "SELECT * from get_all_transaction where transaction_status=(:status)", nativeQuery = true)
	List<NimaiLCMaster> findAllTransactionByStatus(@Param("status") String status);

	// @Query(value="SELECT * from get_all_active_transaction alltr", nativeQuery =
	// true )
	// List<NimaiLCMaster> findAllActiveTransaction();

	@Query(value = "SELECT * from get_all_transaction where transaction_id=(:transid)", nativeQuery = true)
	NimaiLCMaster findSpecificTransactionById(@Param("transid") String transid);

	@Query(value = "SELECT * from get_all_transaction where user_id=(:userid)", nativeQuery = true)
	List<NimaiLCMaster> findByTransactionByUserId(@Param("userid") String userid);

	@Query(value = "SELECT * from get_all_transaction where user_id=(:userid) and transaction_status=(:status)", nativeQuery = true)
	List<NimaiLCMaster> findByTransactionByUserIdAndStatus(@Param("userid") String userid,
			@Param("status") String status);

	@Procedure("quote_calculation")
	public void getQuote(@Param("inp_transaction_id") String transId);

	@Procedure(name = "move_to_historytbl")
	public void insertIntoHistory(@Param("inp_transaction_id") String transId, @Param("inp_userid") String userId);

	@Modifying
	@Query(value = "update nimai_mm_transaction set modified_date=now(), transaction_status='Accepted', quotation_accepted='Yes', accepted_on=now(), status_reason='Transaction Accepted' where transaction_id=(:transId)", nativeQuery = true)
	void updateTransactionStatusToAccept(String transId);

	@Modifying
	@Query(value = "update nimai_mm_transaction set modified_date=now(), transaction_status='Rejected', status_reason=(:statusReason), quotation_received=ifnull(quotation_received,0)-1 where transaction_id=(:transId)", nativeQuery = true)
	void updateTransactionStatusToReject(String transId, @Param("statusReason") String statusReason);

	@Query(value = "SELECT email_address from nimai_m_customer where userid=(:userid)", nativeQuery = true)
	String getEmailAddress(@Param("userid") String userid);

	@Query(value = "SELECT * from get_all_transaction where user_id=(:userid) and branch_user_email=(:branchEmailId)", nativeQuery = true)
	List<NimaiLCMaster> findByTransactionByUserIdAndBranchEmail(@Param("userid") String userId,
			@Param("branchEmailId") String branchEmailId);

	@Query(value = "SELECT * from get_all_transaction where user_id=(:userid) and transaction_status=(:status) and branch_user_email=(:branchEmailId)", nativeQuery = true)
	List<NimaiLCMaster> findByTransactionByUserIdStatusBranchEmail(@Param("userid") String userId,
			@Param("status") String status, @Param("branchEmailId") String branchEmailId);

	@Query(value = "select * from nimai_mm_transaction where transaction_status='Accepted' and quotation_accepted='Yes'and transaction_id=(:transId)", nativeQuery = true)
	NimaiLCMaster getTransactionByTransIdTrStatusAndQuoteStatus(String transId);

	@Modifying
	@Query(value = "update nimai_mm_transaction set modified_date=now(), transaction_status='Active', status_reason='Active after Reopen' where transaction_id=(:transactionId) and user_id=(:userId)", nativeQuery = true)
	void updateTransactionStatusToActive(String transactionId, String userId);

}
