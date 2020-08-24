package com.nimai.lc.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.nimai.lc.bean.BankDetailsBean;
import com.nimai.lc.bean.QuotationBean;
import com.nimai.lc.bean.QuotationMasterBean;
import com.nimai.lc.bean.TransactionQuotationBean;
import com.nimai.lc.entity.Quotation;
import com.nimai.lc.entity.QuotationMaster;

public interface QuotationService 
{
	public Integer saveQuotationdetails(QuotationBean quotationbean);
	public void confirmQuotationdetails(QuotationBean quotationbean);
	public HashMap<String, Integer> calculateQuote(Integer quotationId,String transId,String tableType);
	public void quotePlace(String transId);
	public int getQuotationdetailsToCount(String transId);
	public List<Quotation> getAllDraftQuotationDetails(String userId);
	public List<QuotationMaster> getQuotationDetailByUserIdAndStatus(String userId,String status);
	public List<QuotationMaster> getAllQuotationDetails(String userId);
	public void updateDraftQuotationDet(QuotationBean quotationbean);
	public void moveQuoteToHistory(Integer quotationId,String transId, String userId);
	public void updateQuotationMasterDetails(QuotationMasterBean quotationbean);
	public Quotation getSpecificDraftQuotationDetail(Integer quotationId);
	public List<QuotationMaster> getQuotationDetailByUserIdAndTransactionId(String userId, String transactionId);
	public List<QuotationMaster> getQuotationDetailByQuotationId(Integer quotationId);
	public void updateQuotationForReject(Integer quotationId,String statusReason);
	public List<QuotationMaster> getQuotationDetailByBankUserId(String bankUserId);
	public void updateQuotationForAccept(Integer quotationId,String transId);
	//public List<TransactionQuotationBean> getTransactionQuotationDetailByBankUserIdAndStatus(String bankUserId, String quotationPlaced, String transactionStatus) throws ParseException;
	public List<TransactionQuotationBean> getTransactionQuotationDetailByBankUserIdAndStatus(String bankUserId,String quotationStatus) throws ParseException;
		
	public List<Quotation> getAllDraftQuotationDetailsByBankUserId(String bankUserId);
	public HashMap<String, Integer> calculateQuote(Integer quotationId, String transactionId);
	public String findBankUserIdByQuotationId(Integer quotationId);
	public HashMap<String, String> getBankDetailsByBankUserId(String bankUserId);
	public List<BankDetailsBean> getBankDet(String bankUserId);
	public List<TransactionQuotationBean> getAllDraftTransQuotationDetailsByBankUserId(String bankUserId) throws NumberFormatException, ParseException;
	public Quotation findDraftQuotation(String transId, String userId, String bankUserId);
	public int findQuotationId(String transId, String userId, String bankUserId);
	public QuotationMaster getDetailsOfAcceptedTrans(String transId, String userId);
	public Integer getRejectedQuotationByTransactionId(String transactionId);
	public void updateQuotationStatusToActiveOrPlaced(Integer qid, String transactionId) throws ParseException;
	public List<QuotationMaster> getQuotationDetailByUserIdAndTransactionIdStatus(String userId, String transactionId,String status);
}
