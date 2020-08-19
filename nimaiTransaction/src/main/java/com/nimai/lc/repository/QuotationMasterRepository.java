package com.nimai.lc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.lc.entity.QuotationMaster;

@Repository
@Transactional
public interface QuotationMasterRepository extends JpaRepository<QuotationMaster, Integer>
{
	@Query(value="SELECT * from get_all_quotation where userid=(:userId) and quotation_status=(:status)", nativeQuery = true )
	List<QuotationMaster> findAllQuotation(@Param("userId") String userId,@Param("status") String status);
	
	@Query(value="SELECT transaction_id from get_all_quotation where quotation_id=(:qId)", nativeQuery = true )
	String findTransactionIdByQid(@Param("qId") Integer qId);
	
	
	@Query(value="SELECT * from get_all_quotation where userid=(:userId)", nativeQuery = true )
	List<QuotationMaster> findAllQuotationByUserId(@Param("userId") String userId);
	
	@Query(value="SELECT * from get_all_quotation where userid=(:userId) and transaction_id=(:transactionId)", nativeQuery = true )
	List<QuotationMaster> findAllQuotationByUserIdAndTransactionId(@Param("userId") String userId,@Param("transactionId") String transactionId);

	@Query(value="SELECT * from get_all_quotation where quotation_id=(:quotationId)", nativeQuery = true )
	List<QuotationMaster> findAllQuotationByQuotationId(Integer quotationId);

	@Modifying
	@Query(value= "update nimai_m_quotation set quotation_status='Rejected' where quotation_id=(:quotationId)", nativeQuery = true)
	void updateQuotationStatusToReject(Integer quotationId);
	
	@Modifying
	@Query(value= "update nimai_m_quotation set quotation_status='Accepted' where quotation_id=(:quotationId)", nativeQuery = true)
	void updateQuotationStatusToAccept(Integer quotationId);
	
	@Modifying
	@Query(value= "update nimai_m_quotation set quotation_status='Expired' where transaction_id=(:transId) and quotation_id!=(:quotationId)", nativeQuery = true)
	void updateQuotationStatusToExpired(String transId,Integer quotationId);

	@Query(value="SELECT * from get_all_quotation where bank_userid=(:bankUserId)", nativeQuery = true )
	List<QuotationMaster> findAllQuotationBybankUserId(String bankUserId);

	/*@Query(value="SELECT * from get_trans_quote_for_bank where quotation_placed=(:quotationPlaced) and transaction_status=(:transactionStatus) and bank_userid=(:bankUserId)", nativeQuery = true )
	List findTransQuotationBybankUserIdAndStatus(String bankUserId,String quotationPlaced,String transactionStatus);
	*/
	@Query(value="SELECT * from get_trans_quote_for_bank where quotation_status=(:quotationStatus) and bank_userid=(:bankUserId)", nativeQuery = true )
	List findTransQuotationBybankUserIdAndStatus(String bankUserId,String quotationStatus);
	
	
	@Query(value="SELECT first_name,last_name,email_address,mobile_number,country_name,bank_name,branch_name,swift_code,telephone from nimai_m_customer where userid=(:bankUserId)", nativeQuery = true )
	List findBankDetailsBybankUserId(String bankUserId);

	@Query(value="SELECT bank_userid from nimai_m_quotation where quotation_id=(:quotationId) ", nativeQuery = true )
	String findBankUserIdByQuotationId(Integer quotationId);

	@Query(value= "select * from nimai_m_quotation where quotation_status='Accepted' and transaction_id=(:transId) and userid=(:userId)", nativeQuery = true)
	QuotationMaster findAcceptedTransByTransIdUserId(String transId, String userId);

	@Query(value="SELECT quotation_id from nimai_m_quotation where transaction_id=(:transactionId) and quotation_status='Rejected'", nativeQuery = true )
	Integer findRejectedQuotationByTransId(String transactionId);

	@Modifying
	@Query(value= "update nimai_m_quotation set quotation_status='Placed' where transaction_id=(:transId) and quotation_id=(:qid)", nativeQuery = true)
	void updateQuotationToActiveByQid(Integer qid,String transId);

	@Query(value= "select * from nimai_m_quotation where (quotation_status!='Rejected' or quotation_status is null) and transaction_id=(:transId)", nativeQuery = true)
	List<QuotationMaster> findValidityDateAndQidByTransId(String transId);

	@Modifying
	@Query(value= "update nimai_m_quotation set quotation_status=null where transaction_id=(:transId) and quotation_id=(:qid)", nativeQuery = true)
	void updateQuotationForNewRequestByQid(Integer qid, String transId);

	@Modifying
	@Query(value= "update nimai_m_quotation set quotation_status='Expired' where transaction_id=(:transId) and quotation_id!=(:quotationId)", nativeQuery = true)
	void updateQuotationStatusToExpiredExceptRejectedStatus(String transId, Integer quotationId);

}
