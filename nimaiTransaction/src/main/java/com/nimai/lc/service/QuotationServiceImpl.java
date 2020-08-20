package com.nimai.lc.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.lc.bean.BankDetailsBean;
import com.nimai.lc.bean.QuotationBean;
import com.nimai.lc.bean.QuotationMasterBean;
import com.nimai.lc.bean.TransactionQuotationBean;
import com.nimai.lc.entity.NimaiLCMaster;
import com.nimai.lc.entity.Quotation;
import com.nimai.lc.entity.QuotationMaster;
import com.nimai.lc.repository.LCMasterRepository;
import com.nimai.lc.repository.QuotationMasterRepository;
import com.nimai.lc.repository.QuotationRepository;

@Service
public class QuotationServiceImpl implements QuotationService {

	@Autowired
	QuotationRepository quotationRepo;
	
	@Autowired
	LCMasterRepository lcMasterRepo;

	@Autowired
	QuotationMasterRepository quotationMasterRepo;

	@Autowired
	EntityManagerFactory em;

	@Override
	public Integer saveQuotationdetails(QuotationBean quotationbean) {
		// TODO Auto-generated method stub
		Quotation quote = new Quotation();
		quote.setQuotationId(quotationbean.getQuotationId());
		quote.setTransactionId(quotationbean.getTransactionId());
		quote.setUserId(quotationbean.getUserId());
		quote.setBankUserId(quotationbean.getBankUserId());
		quote.setConfirmationCharges(quotationbean.getConfirmationCharges());
		quote.setConfChgsIssuanceToNegot(quotationbean.getConfChgsIssuanceToNegot());
		quote.setConfChgsIssuanceToexp(quotationbean.getConfChgsIssuanceToexp());
		quote.setConfChgsIssuanceToMatur(quotationbean.getConfChgsIssuanceToMatur());
		quote.setDiscountingCharges(quotationbean.getDiscountingCharges());
		quote.setBankAcceptCharges(quotationbean.getBankAcceptCharges());
		quote.setRefinancingCharges(quotationbean.getRefinancingCharges());
		quote.setApplicableBenchmark(quotationbean.getApplicableBenchmark());
		quote.setCommentsBenchmark(quotationbean.getCommentsBenchmark());
		quote.setNegotiationChargesFixed(quotationbean.getNegotiationChargesFixed());
		quote.setNegotiationChargesPerct(quotationbean.getNegotiationChargesPerct());
		quote.setDocHandlingCharges(quotationbean.getDocHandlingCharges());
		quote.setOtherCharges(quotationbean.getOtherCharges());
		quote.setChargesType(quotationbean.getChargesType());
		quote.setMinTransactionCharges(quotationbean.getMinTransactionCharges());
		quote.setInsertedBy(quotationbean.getInsertedBy());
		quote.setInsertedDate(quotationbean.getInsertedDate());
		quote.setModifiedBy(quotationbean.getModifiedBy());
		quote.setModifiedDate(quotationbean.getModifiedDate());
		quote.setValidityDate(quotationbean.getValidityDate());

		
		HashMap<String,String> bankDet=getBankDetailsByBankUserId(quotationbean.getBankUserId()); 
		quote.setBankName(bankDet.get("bankname"));
		quote.setBranchName(bankDet.get("branchname"));
		quote.setSwiftCode(bankDet.get("swiftcode"));
		quote.setCountryName(bankDet.get("countryname"));
		quote.setEmailAddress(bankDet.get("emailaddress"));
		quote.setTelephone(bankDet.get("telephone"));
		quote.setMobileNumber(bankDet.get("mobileno"));
		quote.setFirstName(bankDet.get("firstname"));
		quote.setLastName(bankDet.get("lastname"));
		
		quotationRepo.save(quote);
		
		return quote.getQuotationId();
	}

	@Override
	public HashMap<String, Integer> calculateQuote(Integer quotationId,String transactionId) 
	{
		
		System.out.println("Calculating Ouotation Id: "+quotationId+" for transaction id: "+transactionId);
		HashMap<String, Integer> getData=generateQuote(quotationId,transactionId,"Draft");
		//HashMap<String, Integer> getData=new HashMap<String, Integer>();
		//getData.put("Value", 1222);
		return getData;
	}
		
	@Override
	public void confirmQuotationdetails(QuotationBean quotationbean) {
		Integer quotationId=quotationbean.getQuotationId();
		String transId = quotationbean.getTransactionId();
		String userId = quotationbean.getUserId();
		EntityManager entityManager = em.createEntityManager();
		try {
		StoredProcedureQuery storedProcedureQuery = entityManager
				.createStoredProcedureQuery("move_to_quotation_master", Quotation.class);
		storedProcedureQuery.registerStoredProcedureParameter("inp_quotation_id", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_transaction_id", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_userid", String.class, ParameterMode.IN);
		storedProcedureQuery.setParameter("inp_quotation_id", quotationId);
		storedProcedureQuery.setParameter("inp_transaction_id", transId);
		storedProcedureQuery.setParameter("inp_userid", userId);

		storedProcedureQuery.execute();
		} catch (Exception e) {
			System.out.println(e);

		} finally {
			entityManager.close();

		}
		
	}

	@Override
	public HashMap<String, Integer> calculateQuote(Integer quotationId,String transId, String tableType) {
		// TODO Auto-generated method stub
		// String transactionId="4028870370f1880f0170f1899aec0001";
		EntityManager entityManager = em.createEntityManager();
		try {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("quote_calculation",
				NimaiLCMaster.class);
		// set parameters
		System.out.println("Calculating Quote for: "+quotationId);
		storedProcedure.registerStoredProcedureParameter("inp_quotation_id", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("inp_transaction_id", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("inp_table_type", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("negoDays", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("expDays", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("matDays", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("confChgsNegot", Float.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("confChgsMatur", Float.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("sumOfQuote", Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("totalQuote", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("inp_quotation_id", quotationId);
		storedProcedure.setParameter("inp_transaction_id", transId);
		storedProcedure.setParameter("inp_table_type", tableType);

		storedProcedure.execute();

		int negoDays = (int) storedProcedure.getOutputParameterValue("negoDays");
		int expDays = (int) storedProcedure.getOutputParameterValue("expDays");
		int matDays = (int) storedProcedure.getOutputParameterValue("matDays");
		float confChgsNegot = (float) storedProcedure.getOutputParameterValue("confChgsNegot");
		float confChgsMatur = (float) storedProcedure.getOutputParameterValue("confChgsMatur");
		int sumOfQuote = (int) storedProcedure.getOutputParameterValue("sumOfQuote");
		int totalQuote = (int) storedProcedure.getOutputParameterValue("totalQuote");

		System.out.println(negoDays + " " + expDays + " " + matDays + " " + sumOfQuote + " " + totalQuote);
		HashMap outputData = new HashMap();

		outputData.put("negotiationDays", negoDays);
		outputData.put("expiryDays", expDays);
		outputData.put("maturityDays", matDays);
		outputData.put("confChgsNegot", confChgsNegot);
		outputData.put("confChgsMatur", confChgsMatur);
		outputData.put("sumOfQuote", sumOfQuote);
		outputData.put("TotalQuote", totalQuote);
		

		return outputData;
	} catch (Exception e) {
		System.out.println(e);

	} finally {
		entityManager.close();

	}
	return null;

	}

	@Override
	public void quotePlace(String transId) {
		// TODO Auto-generated method stub
		quotationRepo.updateQuotationPlaced(transId);
	}

	@Override
	public int getQuotationdetailsToCount(String transId) {
		// TODO Auto-generated method stub
		return quotationRepo.getQuotationCount(transId);
	}

	@Override
	public List<Quotation> getAllDraftQuotationDetails(String userId) {
		// TODO Auto-generated method stub
		return quotationRepo.findAllDraftQuotation(userId);
	}
	
	

	@Override
	public List<QuotationMaster> getAllQuotationDetails(String userId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findAllQuotationByUserId(userId);
	}

	@Override
	public List<QuotationMaster> getQuotationDetailByUserIdAndStatus(String userId, String status) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findAllQuotation(userId, status);
	}

	@Override
	public void updateDraftQuotationDet(QuotationBean quotationbean) {
		Integer qid = quotationbean.getQuotationId();
		Quotation quote = quotationRepo.getOne(qid);
		System.out.println("Quotation id= " + qid);
		System.out.println("ConfirmationCharges= " + quote.getConfirmationCharges());
		quote.setUserId(quotationbean.getUserId());
		quote.setBankUserId(quotationbean.getBankUserId());
		quote.setTransactionId(quotationbean.getTransactionId());
		quote.setConfirmationCharges(quotationbean.getConfirmationCharges());
		quote.setConfChgsIssuanceToNegot(quotationbean.getConfChgsIssuanceToNegot());
		quote.setConfChgsIssuanceToexp(quotationbean.getConfChgsIssuanceToexp());
		quote.setConfChgsIssuanceToMatur(quotationbean.getConfChgsIssuanceToMatur());
		quote.setDiscountingCharges(quotationbean.getDiscountingCharges());
		quote.setBankAcceptCharges(quotationbean.getBankAcceptCharges());
		quote.setRefinancingCharges(quotationbean.getRefinancingCharges());
		quote.setApplicableBenchmark(quotationbean.getApplicableBenchmark());
		quote.setCommentsBenchmark(quotationbean.getCommentsBenchmark());
		quote.setNegotiationChargesFixed(quotationbean.getNegotiationChargesFixed());
		quote.setNegotiationChargesPerct(quotationbean.getNegotiationChargesPerct());
		Date now = new Date();
		quote.setDocHandlingCharges(quotationbean.getDocHandlingCharges());
		quote.setOtherCharges(quotationbean.getOtherCharges());
		quote.setChargesType(quotationbean.getChargesType());
		quote.setMinTransactionCharges(quotationbean.getMinTransactionCharges());
		quote.setInsertedBy(quotationbean.getInsertedBy());
		quote.setInsertedDate(quotationbean.getInsertedDate());
		quote.setModifiedBy(quotationbean.getModifiedBy());
		quote.setModifiedDate(now);
		
		HashMap<String,String> bankDet=getBankDetailsByBankUserId(quotationbean.getBankUserId()); 
		quote.setBankName(bankDet.get("bankname"));
		quote.setBranchName(bankDet.get("branchname"));
		quote.setSwiftCode(bankDet.get("swiftcode"));
		quote.setCountryName(bankDet.get("countryname"));
		quote.setEmailAddress(bankDet.get("emailaddress"));
		quote.setTelephone(bankDet.get("telephone"));
		quote.setMobileNumber(bankDet.get("mobileno"));
		quote.setFirstName(bankDet.get("firstname"));
		quote.setLastName(bankDet.get("lastname"));
		
		quotationRepo.save(quote);
	}

	@Override
	public void moveQuoteToHistory(Integer quotationId,String transId, String userId) {
		// TODO Auto-generated method stub
		// lcrepo.insertIntoMaster(transId, userId);
		EntityManager entityManager = em.createEntityManager();
		try {
		StoredProcedureQuery storedProcedureQuery = entityManager
				.createStoredProcedureQuery("move_quotation_to_historytbl", QuotationMaster.class);
		storedProcedureQuery.registerStoredProcedureParameter("inp_quotation_id", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_transaction_id", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("inp_userid", String.class, ParameterMode.IN);
		storedProcedureQuery.setParameter("inp_quotation_id", quotationId);
		storedProcedureQuery.setParameter("inp_transaction_id", transId);
		storedProcedureQuery.setParameter("inp_userid", userId);

		storedProcedureQuery.execute();
		} catch (Exception e) {
			System.out.println(e);

		} finally {
			entityManager.close();

		}
	}

	@Override
	public void updateQuotationMasterDetails(QuotationMasterBean quotationbean) {
		Integer qid = quotationbean.getQuotationId();
		QuotationMaster quote = quotationMasterRepo.getOne(qid);
		System.out.println("Quotation id= " + qid);

		quote.setUserId(quotationbean.getUserId());
		quote.setBankUserId(quotationbean.getBankUserId());
		quote.setTransactionId(quotationbean.getTransactionId());
		quote.setConfirmationCharges(quotationbean.getConfirmationCharges());
		quote.setConfChgsIssuanceToNegot(quotationbean.getConfChgsIssuanceToNegot());
		quote.setConfChgsIssuanceToexp(quotationbean.getConfChgsIssuanceToexp());
		quote.setConfChgsIssuanceToMatur(quotationbean.getConfChgsIssuanceToMatur());
		quote.setDiscountingCharges(quotationbean.getDiscountingCharges());
		quote.setBankAcceptCharges(quotationbean.getBankAcceptCharges());
		quote.setRefinancingCharges(quotationbean.getRefinancingCharges());
		quote.setApplicableBenchmark(quotationbean.getApplicableBenchmark());
		quote.setCommentsBenchmark(quotationbean.getCommentsBenchmark());
		quote.setNegotiationChargesFixed(quotationbean.getNegotiationChargesFixed());
		quote.setNegotiationChargesPerct(quotationbean.getNegotiationChargesPerct());
		Date now = new Date();
		quote.setDocHandlingCharges(quotationbean.getDocHandlingCharges());
		quote.setOtherCharges(quotationbean.getOtherCharges());
		quote.setChargesType(quotationbean.getChargesType());
		quote.setMinTransactionCharges(quotationbean.getMinTransactionCharges());
		quote.setTotalQuoteValue(quotationbean.getTotalQuoteValue());
		quote.setValidityDate(quotationbean.getValidityDate());
		quote.setInsertedBy(quotationbean.getInsertedBy());
		quote.setInsertedDate(quotationbean.getInsertedDate());
		quote.setModifiedBy(quotationbean.getModifiedBy());
		quote.setModifiedDate(now);
		
		HashMap<String,String> bankDet=getBankDetailsByBankUserId(quotationbean.getBankUserId()); 
		quote.setBankName(bankDet.get("bankname"));
		quote.setBranchName(bankDet.get("branchname"));
		quote.setSwiftCode(bankDet.get("swiftcode"));
		quote.setCountryName(bankDet.get("countryname"));
		quote.setEmailAddress(bankDet.get("emailaddress"));
		quote.setTelephone(bankDet.get("telephone"));
		quote.setMobileNumber(bankDet.get("mobileno"));
		quote.setFirstName(bankDet.get("firstname"));
		quote.setLastName(bankDet.get("lastname"));
		
		quotationMasterRepo.save(quote);
	}

	@Override
	public Quotation getSpecificDraftQuotationDetail(Integer quotationId) {
		// TODO Auto-generated method stub
		return quotationRepo.findSpecificDraftQuotation(quotationId);
	}

	@Override
	public List<QuotationMaster> getQuotationDetailByUserIdAndTransactionId(String userId, String transactionId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findAllQuotationByUserIdAndTransactionId(userId, transactionId);
	}
	
	@Override
	public List<QuotationMaster> getQuotationDetailByUserIdAndTransactionIdStatus(String userId, String transactionId,String status) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findQuotationByUserIdAndTransactionIdStatus(userId, transactionId, status);
	}

	@Override
	public List<QuotationMaster> getQuotationDetailByQuotationId(Integer quotationId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findAllQuotationByQuotationId(quotationId);
	}

	@Override
	public void updateQuotationForReject(Integer quotationId,String statusReason) {
		// TODO Auto-generated method stub
		quotationMasterRepo.updateQuotationStatusToReject(quotationId);
		String tid=quotationMasterRepo.findTransactionIdByQid(quotationId);
		System.out.println("Quotation Rejected for trans: "+tid);
		lcMasterRepo.updateTransactionStatusToReject(tid,statusReason);
	}

	@Override
	public void updateQuotationForAccept(Integer quotationId, String transId) {
		// TODO Auto-generated method stub
		Integer qid=getRejectedQuotationByTransactionId(transId);
		System.out.println("Rejected QuotationId: "+qid);
		if(qid!=null)
		{
			quotationMasterRepo.updateQuotationStatusToExpiredExceptRejectedStatus(transId,qid);
			quotationMasterRepo.updateQuotationStatusToAccept(quotationId);
			
		}
		else
		{
		quotationMasterRepo.updateQuotationStatusToAccept(quotationId);
		quotationMasterRepo.updateQuotationStatusToExpired(transId,quotationId);
		}
		lcMasterRepo.updateTransactionStatusToAccept(transId);
	}

	@Override
	public List<QuotationMaster> getQuotationDetailByBankUserId(String bankUserId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findAllQuotationBybankUserId(bankUserId);
	}
	
	@Override
	public List<Quotation> getAllDraftQuotationDetailsByBankUserId(String bankUserId) {
		// TODO Auto-generated method stub
		return quotationRepo.findAllDraftQuotationByBankUserId(bankUserId);
	}

	@Override
	public List<TransactionQuotationBean> getTransactionQuotationDetailByBankUserIdAndStatus(String bankUserId,String quotationStatus) throws NumberFormatException,ParseException {
		
	//public List<TransactionQuotationBean> getTransactionQuotationDetailByBankUserIdAndStatus(String bankUserId,String quotationPlaced,String transactionStatus) throws NumberFormatException,ParseException {
		// TODO Auto-generated method stub
		
		//List<TransactionQuotationBean> details = quotationMasterRepo.findTransQuotationBybankUserIdAndStatus(bankUserId,quotationPlaced,transactionStatus);
		List<TransactionQuotationBean> details = quotationMasterRepo.findTransQuotationBybankUserIdAndStatus(bankUserId,quotationStatus);
		List<TransactionQuotationBean> finalList=mapListToResponseBean(details);
		
		return finalList;
	}	
	
	@Override
	public List<TransactionQuotationBean> getAllDraftTransQuotationDetailsByBankUserId(String bankUserId) throws NumberFormatException, ParseException {
		// TODO Auto-generated method stub
		List<TransactionQuotationBean> details = quotationRepo.findDraftTransQuotationBybankUserId(bankUserId);
		List<TransactionQuotationBean> finalList=mapListToResponseBean(details);
		
		return finalList;
	}
	
	
	private List<TransactionQuotationBean> mapListToResponseBean(List<TransactionQuotationBean> details) throws NumberFormatException,ParseException{
		// TODO Auto-generated method stub
		List<TransactionQuotationBean> list1 = new ArrayList<TransactionQuotationBean>();
		
		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Object objA:details) 
		{
			
			TransactionQuotationBean responseBean=new TransactionQuotationBean();
			responseBean.setTransactionId(((Object[])objA)[0]==null?"null":((Object[])objA)[0].toString());
			responseBean.setUserId(((Object[])objA)[1]==null?"null":((Object[])objA)[1].toString());
			responseBean.setRequirementType(((Object[])objA)[2]==null?"null":((Object[])objA)[2].toString());
			responseBean.setlCIssuanceBank(((Object[])objA)[3]==null?"null":((Object[])objA)[3].toString());
			
			responseBean.setlCIssuanceBranch(((Object[])objA)[4]==null?"null":((Object[])objA)[4].toString());
			responseBean.setlCSwiftCode(((Object[])objA)[5]==null?"null":((Object[])objA)[5].toString());
			responseBean.setlCIssuanceCountry(((Object[])objA)[6]==null?"null":((Object[])objA)[6].toString());
			responseBean.setlCValue(((Object[])objA)[7]==null?0:Integer.valueOf(((Object[])objA)[7].toString()));
			responseBean.setlCCurrency(((Object[])objA)[8]==null?"null":((Object[])objA)[8].toString());
			responseBean.setlCIssuingDate(((Object[])objA)[9]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[9].toString()));
			responseBean.setLastShipmentDate(((Object[])objA)[10]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[10].toString()));
			responseBean.setGoodsType(((Object[])objA)[11]==null?"null":((Object[])objA)[11].toString());
			responseBean.setNegotiationDate(((Object[])objA)[12]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[12].toString()));
			responseBean.setlCExpiryDate(((Object[])objA)[13]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[13].toString()));
			responseBean.setUsanceDays(((Object[])objA)[14]==null?0:Integer.valueOf(((Object[])objA)[14].toString()));
			responseBean.setPaymentTerms(((Object[])objA)[15]==null?"null":((Object[])objA)[15].toString());
			responseBean.setStartDate(((Object[])objA)[16]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[16].toString()));
			responseBean.setEndDate(((Object[])objA)[17]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[17].toString()));
			responseBean.setOriginalTenorDays(((Object[])objA)[18]==null?0:Integer.valueOf(((Object[])objA)[18].toString()));
			responseBean.setRefinancingPeriod(((Object[])objA)[19]==null?"null":((Object[])objA)[19].toString());
			responseBean.setlCMaturityDate(((Object[])objA)[20]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[20].toString()));
			responseBean.setlCNumber(((Object[])objA)[21]==null?"null":((Object[])objA)[21].toString());
			responseBean.setLastBeneBank(((Object[])objA)[22]==null?"null":((Object[])objA)[22].toString());
			responseBean.setLastBeneSwiftCode(((Object[])objA)[23]==null?"null":((Object[])objA)[23].toString());
			responseBean.setLastBankCountry(((Object[])objA)[24]==null?"null":((Object[])objA)[24].toString());
			responseBean.setRemarks(((Object[])objA)[25]==null?"null":((Object[])objA)[25].toString());
			responseBean.setDiscountingPeriod(((Object[])objA)[26]==null?"null":((Object[])objA)[26].toString());
			responseBean.setConfirmationPeriod(((Object[])objA)[27]==null?"null":((Object[])objA)[27].toString());
			responseBean.setFinancingPeriod(((Object[])objA)[28]==null?"null":((Object[])objA)[28].toString());
			responseBean.setUserType(((Object[])objA)[29]==null?"null":((Object[])objA)[29].toString());
			responseBean.setApplicantName(((Object[])objA)[30]==null?"null":((Object[])objA)[30].toString());
			responseBean.setApplicantCountry(((Object[])objA)[31]==null?"null":((Object[])objA)[31].toString());
			responseBean.setApplicantContactPerson(((Object[])objA)[32]==null?"null":((Object[])objA)[32].toString());
			responseBean.setApplicantContactPersonEmail(((Object[])objA)[33]==null?"null":((Object[])objA)[33].toString());
			responseBean.setBeneName(((Object[])objA)[34]==null?"null":((Object[])objA)[34].toString());
			responseBean.setBeneCountry(((Object[])objA)[35]==null?"null":((Object[])objA)[35].toString());
			responseBean.setBeneContactPerson(((Object[])objA)[36]==null?"null":((Object[])objA)[36].toString());
			responseBean.setBeneContactPersonEmail(((Object[])objA)[37]==null?"null":((Object[])objA)[37].toString());
			responseBean.setBeneBankName(((Object[])objA)[38]==null?"null":((Object[])objA)[38].toString());
			responseBean.setBeneSwiftCode(((Object[])objA)[39]==null?"null":((Object[])objA)[39].toString());
			responseBean.setBeneBankCountry(((Object[])objA)[40]==null?"null":((Object[])objA)[40].toString());
			responseBean.setLoadingCountry(((Object[])objA)[41]==null?"null":((Object[])objA)[41].toString());
			responseBean.setLoadingPort(((Object[])objA)[42]==null?"null":((Object[])objA)[42].toString());
			responseBean.setDischargeCountry(((Object[])objA)[43]==null?"null":((Object[])objA)[43].toString());
			responseBean.setDischargePort(((Object[])objA)[44]==null?"null":((Object[])objA)[44].toString());
			responseBean.setLcChargesType(((Object[])objA)[45]==null?"null":((Object[])objA)[45].toString());
			responseBean.setValidity(((Object[])objA)[46]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[46].toString()));
			responseBean.setLcProforma(((Object[])objA)[47]==null?"null":((Object[])objA)[47].toString());
			responseBean.setPaymentPeriod(((Object[])objA)[48]==null?"null":((Object[])objA)[48].toString());
			
			responseBean.setQuotationPlaced(((Object[])objA)[49]==null?"null":((Object[])objA)[49].toString());
			responseBean.setTransactionStatus(((Object[])objA)[50]==null?"null":((Object[])objA)[50].toString());
			responseBean.setAcceptedOn(((Object[])objA)[51]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[51].toString()));
			
			responseBean.setQuotationId(((Object[])objA)[52]==null?0:Integer.valueOf(((Object[])objA)[52].toString()));
			responseBean.setBankUserId(((Object[])objA)[53]==null?"null":((Object[])objA)[53].toString());
			responseBean.setConfirmationCharges(((Object[])objA)[54]==null?0:Float.valueOf(((Object[])objA)[54].toString()));
			responseBean.setConfChgsIssuanceToNegot(((Object[])objA)[55]==null?"null":((Object[])objA)[55].toString());
			responseBean.setConfChgsIssuanceToExp(((Object[])objA)[56]==null?"null":((Object[])objA)[56].toString());
			responseBean.setConfChgsIssuanceToMatur(((Object[])objA)[57]==null?"null":((Object[])objA)[57].toString());
			responseBean.setDiscountingCharges(((Object[])objA)[58]==null?0:Float.valueOf(((Object[])objA)[58].toString()));
			responseBean.setRefinancingCharges(((Object[])objA)[59]==null?0:Float.valueOf(((Object[])objA)[59].toString()));
			responseBean.setBankerAcceptCharges(((Object[])objA)[60]==null?0:Float.valueOf(((Object[])objA)[60].toString()));
			responseBean.setApplicableBenchmark(((Object[])objA)[61]==null?0:Float.valueOf(((Object[])objA)[61].toString()));
			responseBean.setCommentsBenchmark(((Object[])objA)[62]==null?"null":((Object[])objA)[62].toString());
			responseBean.setNegotiationChargesFixed(((Object[])objA)[63]==null?0:Float.valueOf(((Object[])objA)[63].toString()));
			responseBean.setNegotiationChargesPerct(((Object[])objA)[64]==null?0:Float.valueOf(((Object[])objA)[64].toString()));
			responseBean.setDocHandlingCharges(((Object[])objA)[65]==null?0:Float.valueOf(((Object[])objA)[65].toString()));
			responseBean.setOtherCharges(((Object[])objA)[66]==null?0:Float.valueOf(((Object[])objA)[66].toString()));
			responseBean.setChargesType(((Object[])objA)[67]==null?"null":((Object[])objA)[67].toString());
			responseBean.setMinTransactionCharges(((Object[])objA)[68]==null?0:Float.valueOf(((Object[])objA)[68].toString()));
			
			responseBean.setTotalQuoteValue(((Object[])objA)[69]==null?0:Float.valueOf(((Object[])objA)[69].toString()));
			responseBean.setValidityDate(((Object[])objA)[70]==null?new Date(0):(Date)simpleDateFormat.parse(((Object[])objA)[70].toString()));
			responseBean.setQuoteRank(((Object[])objA)[72]==null?0:Integer.valueOf(((Object[])objA)[72].toString()));
			list1.add(responseBean);
		}
		return list1;
	}

	
		
		
		/*
		 * return details;
		 * 
		 * 
		 * 
		 * int key=1; for(Object o: details) { JSONArray array = new JSONArray();
		 * for(int i = 0; i < 15; i++) { try {
		 * array.put(listKey[i]+":"+((Object[])o)[i].toString());
		 * //jsonObject.put(""+key++, ((Object[])o)[i].toString());
		 * list1.add(listKey[i]); list2.add(((Object[])o)[i].toString());
		 * 
		 * //System.out.println(""+((Object[])o)[i].toString()); }
		 * catch(NullPointerException npe) { array.put(listKey[i]+":"+"null");
		 * list1.add(listKey[i]); list2.add("null"); //System.out.println("null"); }
		 * 
		 * } jsonObject.put("transaction "+key++,array);
		 * 
		 * }
		 * 
		 * System.out.println(""+jsonObject.toString());
		 * 
		 * for(int i=0;i<list1.size();i++) { for } return jsonObject.toString();
		 */

		


	private HashMap<String, Integer> generateQuote(Integer quotationId,String transId, String tableType) 
	{
		// TODO Auto-generated method stub
		HashMap<String, Integer> outputFields=calculateQuote(quotationId,transId,tableType);
		return outputFields;
	}

	@Override
	public String findBankUserIdByQuotationId(Integer quotationId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findBankUserIdByQuotationId(quotationId);
		
	}

	@Override
	public HashMap<String,String> getBankDetailsByBankUserId(String bankUserId) 
	{
		// TODO Auto-generated method stub
		HashMap<String,String> bankDetMap=new HashMap<String, String>();
		List bankDetails=quotationMasterRepo.findBankDetailsBybankUserId(bankUserId);
		for(Object det:bankDetails) 
		{
			bankDetMap.put("firstname",((Object[])det)[0]==null?null:((Object[])det)[0].toString());
			bankDetMap.put("lastname",((Object[])det)[1]==null?null:((Object[])det)[1].toString());
			bankDetMap.put("emailaddress",((Object[])det)[2]==null?null:((Object[])det)[2].toString());
			bankDetMap.put("mobileno",((Object[])det)[3]==null?null:((Object[])det)[3].toString());
			bankDetMap.put("countryname",((Object[])det)[4]==null?null:((Object[])det)[4].toString());
			bankDetMap.put("bankname",((Object[])det)[5]==null?null:((Object[])det)[5].toString());
			bankDetMap.put("branchname",((Object[])det)[6]==null?null:((Object[])det)[6].toString());
			bankDetMap.put("swiftcode",((Object[])det)[7]==null?null:((Object[])det)[7].toString());
			bankDetMap.put("telephone",((Object[])det)[8]==null?null:((Object[])det)[8].toString());
		}
		
		return bankDetMap;
	}

	@Override
	public List<BankDetailsBean> getBankDet(String bankUserId) 
	{
		List<BankDetailsBean> list1 = new ArrayList<BankDetailsBean>();
		// TODO Auto-generated method stub
		List<BankDetailsBean> bankDetails=quotationMasterRepo.findBankDetailsBybankUserId(bankUserId);
		for(Object det:bankDetails) 
		{
			BankDetailsBean bDet=new BankDetailsBean();
			bDet.setBankUserId(bankUserId);
			bDet.setFirstName(((Object[])det)[0]==null?null:((Object[])det)[0].toString());
			bDet.setLastName(((Object[])det)[1]==null?null:((Object[])det)[1].toString());
			bDet.setEmailAddress(((Object[])det)[2]==null?null:((Object[])det)[2].toString());
			bDet.setMobileNumber(((Object[])det)[3]==null?null:((Object[])det)[3].toString());
			bDet.setCountryName(((Object[])det)[4]==null?null:((Object[])det)[4].toString());
			bDet.setBankName(((Object[])det)[5]==null?null:((Object[])det)[5].toString());
			bDet.setBranchName(((Object[])det)[6]==null?null:((Object[])det)[6].toString());
			bDet.setSwiftCode(((Object[])det)[7]==null?null:((Object[])det)[7].toString());
			bDet.setTelephone(((Object[])det)[8]==null?null:((Object[])det)[8].toString());
			
			list1.add(bDet);
		}
		
		return list1;
	}

	@Override
	public Quotation findDraftQuotation(String transId, String userId, String bankUserId) {
		// TODO Auto-generated method stub
		return quotationRepo.findQuotationDetails(transId,userId,bankUserId);
	}

	@Override
	public int findQuotationId(String transId, String userId, String bankUserId) {
		// TODO Auto-generated method stub
		return quotationRepo.getQuotationId(transId,userId,bankUserId);
	}

	@Override
	public QuotationMaster getDetailsOfAcceptedTrans(String transId, String userId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findAcceptedTransByTransIdUserId(transId,userId);
	}

	@Override
	public Integer getRejectedQuotationByTransactionId(String transactionId) {
		// TODO Auto-generated method stub
		return quotationMasterRepo.findRejectedQuotationByTransId(transactionId);
	}

	@Override
	public void updateQuotationStatusToActiveOrPlaced(Integer qid,String transId) throws ParseException {
		// TODO Auto-generated method stub
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		Date todaydate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		List<QuotationMaster> listOfQuotation=quotationMasterRepo.findValidityDateAndQidByTransId(transId);
		//System.out.println("List: "+listOfQuotation);
		//System.out.println("Current Date: "+todaydate);
		for(QuotationMaster qmb:listOfQuotation)
		{
			if(todaydate.compareTo(qmb.getValidityDate())>0)
				//NewRequest
				quotationMasterRepo.updateQuotationForNewRequestByQid(qmb.getQuotationId(),transId);
			if(todaydate.compareTo(qmb.getValidityDate())<=0)
				
				quotationMasterRepo.updateQuotationToActiveByQid(qmb.getQuotationId(), transId);
				
			//System.out.println("Quotation Id: "+qmb.getQuotationId()+" Validity Date: "+qmb.getValidityDate());
		}
		//NewRequest
		//quotationMasterRepo.updateQuotationToActiveByQid(qid,transId);
	}

	
}
