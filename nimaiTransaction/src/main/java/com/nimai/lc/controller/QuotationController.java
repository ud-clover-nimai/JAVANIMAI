package com.nimai.lc.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.lc.NimaiTransactionApplication;
import com.nimai.lc.bean.BankDetailsBean;
import com.nimai.lc.bean.NimaiLCMasterBean;
import com.nimai.lc.bean.QuotationBean;
import com.nimai.lc.bean.QuotationMasterBean;
import com.nimai.lc.bean.TransactionQuotationBean;
import com.nimai.lc.entity.NimaiLCMaster;
import com.nimai.lc.entity.Quotation;
import com.nimai.lc.entity.QuotationMaster;
import com.nimai.lc.payload.GenericResponse;
import com.nimai.lc.service.LCService;
import com.nimai.lc.service.QuotationService;
import com.nimai.lc.utility.ErrorDescription;

@RestController
public class QuotationController 
{
	@Autowired
	QuotationService quotationService;
	
	@Autowired
	LCService lcservice;
	
	private static final Logger logger = LoggerFactory.getLogger(NimaiTransactionApplication.class);

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/saveQuotationToDraft", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> placeQuotation(@RequestBody QuotationBean quotationBean) {
		logger.info("=========== Save Quotation To Draft ===========");
		GenericResponse response = new GenericResponse<>();
		NimaiLCMaster nmlc=lcservice.getTransactionForAcceptCheck(quotationBean.getTransactionId());
		if(nmlc==null)
		{
			try 
			{	
				String transId = quotationBean.getTransactionId();
				NimaiLCMaster transDetails=lcservice.checkTransaction(transId);
				System.out.println("transId:"+transId);
				if(transDetails==null)
				{
					response.setStatus("Failure");
					response.setErrCode("ASA002");
					response.setErrMessage(ErrorDescription.getDescription("ASA002"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}
				else
				{
					Quotation quoteDetails=quotationService.findDraftQuotation(transId,quotationBean.getUserId(),quotationBean.getBankUserId());
					if(quoteDetails==null)
					{
						Integer qid=quotationService.saveQuotationdetails(quotationBean);
						System.out.println("Quotation Id "+qid+" Saved");
						HashMap<String, Integer> getData=quotationService.calculateQuote(qid,quotationBean.getTransactionId(),"Draft");
						//quotationService.quotePlace(transId);
						getData.put("quotationId", qid);
						response.setData(getData);
						response.setStatus("Success");
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}
					else
					{
						//ModelMapper modelMapper = new ModelMapper();
						//QuotationBean quoteBean = modelMapper.map(quoteDetails, QuotationBean.class);
						Integer qidForUpdate=quotationService.findQuotationId(transId, quotationBean.getUserId(), quotationBean.getBankUserId());
						System.out.println("Qid For Update= "+qidForUpdate);
						quotationBean.setQuotationId(qidForUpdate);
						quotationService.updateDraftQuotationDet(quotationBean);
						HashMap<String, Integer> getData=quotationService.calculateQuote(quotationBean.getQuotationId(),quotationBean.getTransactionId(),"Draft");
						getData.put("quotationId", qidForUpdate);
						response.setData(getData);
						response.setStatus("Success");
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}
				}
				// System.out.println("nimaibean: "+nimailcBean.toString());
				// System.out.println("nimaiEntity: "+nimailcDetails.toString());
				
			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000")+" "+e);
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			response.setStatus("Failure");
			System.out.println("Quotation has already Accepted by the Customer for the transaction:"+quotationBean.getTransactionId());
			response.setErrMessage("Quotation has already Accepted by the Customer for the transaction:"+quotationBean.getTransactionId());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}
	
	

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/confirmQuotation", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> confirmQuotation(@RequestBody QuotationBean quotationBean) {
		logger.info("=========== Confirm Quotation ===========");
		GenericResponse response = new GenericResponse<>();
		try {
			Integer quotationId=quotationBean.getQuotationId();
			String transId = quotationBean.getTransactionId();
		
			System.out.println("Confirming Quotation Id:"+quotationId+" for transId:"+transId);
			
			quotationService.confirmQuotationdetails(quotationBean);
			//quotationService.quotePlace(transId);
			response.setStatus("Success");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		
			// System.out.println("nimaibean: "+nimailcBean.toString());
			// System.out.println("nimaiEntity: "+nimailcDetails.toString());
			
		catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000")+" "+e);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/calculateQuote", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> calculateQuote(@RequestBody QuotationMasterBean quotebean) 
	{
		logger.info("=========== Calculating Quote ===========");
		GenericResponse response = new GenericResponse<>();
		
		try
		{
			HashMap<String, Integer> outputFields=quotationService.calculateQuote(quotebean.getQuotationId(),quotebean.getTransactionId());
			response.setStatus("Success");
			response.setData(outputFields);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		catch (Exception e)
		{
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage("Exception: "+e);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	/*@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getQuotationCount", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getQuotationCountAgainstTransId(@RequestBody NimaiLCMasterBean nimailc) {
		GenericResponse response = new GenericResponse<>();
		String transId=nimailc.getTransactionId();
		//String userId = nimailc.getUserId();
		//String status=nimailc.getTransactionStatus();
		int count=0;
		try
		{
		 count = quotationService.getQuotationdetailsToCount(transId);
		
			response.setData(count);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		catch(Exception e)
		{
			response.setData(count);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}*/
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getDraftQuotationByUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getDraftQuotation(@RequestBody QuotationBean quotationBean) {
		logger.info("=========== Get Draft Quotation By UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String userId = quotationBean.getUserId();
		List<Quotation> draftQuotations = quotationService.getAllDraftQuotationDetails(userId);
		
		if (draftQuotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			// lcservice.setDataToList(transactions);
			response.setData(draftQuotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllQuotationByUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getQuotation(@RequestBody QuotationMasterBean quotationBean) {
		logger.info("=========== Get all Quotation By UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String userId = quotationBean.getUserId();
		List<QuotationMaster> quotations = quotationService.getAllQuotationDetails(userId);
		
		if (quotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			// lcservice.setDataToList(transactions);
			response.setData(quotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getQuotationDetailByUserIdAndStatus", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getQuotationByUserIdAndStatus(@RequestBody QuotationMasterBean quotationBean) {
		logger.info("=========== Get Quotation By UserId and Status ===========");
		GenericResponse response = new GenericResponse<>();
		String userId = quotationBean.getUserId();
		String status=quotationBean.getQuotationStatus();
		List<QuotationMaster> quotations = quotationService.getQuotationDetailByUserIdAndStatus(userId,status);
		if (quotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
 
	}
	
	@CrossOrigin( value = "*",allowedHeaders = "*")
	@PostMapping(value = "/updateDraftQuotation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateDraftQuotation(@RequestBody QuotationBean quotationbean) {
		logger.info("=========== Update Draft Quotation ==========="); 
		GenericResponse response = new GenericResponse<>();
		 try 
			 {
				 
				 quotationService.updateDraftQuotationDet(quotationbean);
				 //HashMap<String, Integer> getData=generateQuote(quotationbean.getTransactionId(),"Draft");
				 HashMap<String, Integer> getData=quotationService.calculateQuote(quotationbean.getQuotationId(),quotationbean.getTransactionId(),"Draft");
					
				 response.setData(getData);
				 response.setStatus("Success");
				 return new ResponseEntity<Object>(response, HttpStatus.OK);
			 } 
			 catch (Exception e) 
			 {
				 response.setStatus("Failure");
				 response.setErrCode("EXE000");
				 response.setErrMessage(ErrorDescription.getDescription("EXE000")+" "+e);
				 return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			 }
		
	}
	
	@CrossOrigin( value = "*",allowedHeaders = "*")
	@PostMapping(value = "/updateMasterQuotation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateQuotationDetails(@RequestBody QuotationMasterBean quotationbean) {
		logger.info("=========== Update Master Quotation ===========");
		 GenericResponse response = new GenericResponse<>();
		 Integer quotationId=quotationbean.getQuotationId();
		 String transId=quotationbean.getTransactionId();
		 
		 String userId=quotationbean.getUserId();
		 try 
			 {
				 
				 quotationService.moveQuoteToHistory(quotationId,transId, userId);
				 quotationService.updateQuotationMasterDetails(quotationbean);
				 //HashMap<String, Integer> getData=generateQuote(quotationbean.getTransactionId(),"Master");
				 HashMap<String, Integer> getData=quotationService.calculateQuote(quotationId,transId,"Master");
					
				 response.setData(getData);
				 response.setStatus("Success");
				 return new ResponseEntity<Object>(response, HttpStatus.OK);
			 } 
			 catch (Exception e) 
			 {
				 response.setStatus("Failure");
				 response.setErrCode("EXE000");
				 response.setErrMessage(ErrorDescription.getDescription("EXE000")+" "+e);
				 return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			 }
		
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getSpecificDraftQuotationDetailByQuotationId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecificDraftQuotation(@RequestBody QuotationBean quotationbean) {
		logger.info("=========== Get Specific Draft Quotation By QuotationId ===========");
		GenericResponse response = new GenericResponse<>();
		Integer quotationId = quotationbean.getQuotationId();
		System.out.println(""+quotationId);
		Quotation quote = quotationService.getSpecificDraftQuotationDetail(quotationId);
		if (quote == null) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quote);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllQRByUserIdTxnId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getAllDetailByUserIdAndTransactionId(@RequestBody QuotationBean quotationbean) {
		logger.info("=========== Get all Quotation Received By UserId and TransactionId ===========");
		GenericResponse response = new GenericResponse<>();
		String transactionId = quotationbean.getTransactionId();
		String userId=quotationbean.getUserId();
		
		System.out.println(""+transactionId);
		System.out.println(""+userId);
		List<QuotationMaster> quotations = quotationService.getQuotationDetailByUserIdAndTransactionId(userId,transactionId);
		
		if (quotations == null || quotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getQuotationDtlByQId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getAllQuotationByQuotationId(@RequestBody QuotationBean quotationbean) {
		logger.info("=========== Get Quotation By QuotationId ===========");
		GenericResponse response = new GenericResponse<>();
		Integer quotationId = quotationbean.getQuotationId();
		
		System.out.println(""+quotationId);
		
		List<QuotationMaster> quotations = quotationService.getQuotationDetailByQuotationId(quotationId);
		
		if (quotations == null || quotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/rejectQuote/{id}", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> rejectQuotation(@PathVariable("id") Integer quotationId,@RequestBody NimaiLCMasterBean nimailcmasterbean) {
		logger.info("=========== Reject Quotation ===========");
		GenericResponse response = new GenericResponse<>();
		
		String statusReason= nimailcmasterbean.getStatusReason();
		System.out.println("Quotation Id: "+quotationId);
		try
		{
			quotationService.updateQuotationForReject(quotationId,statusReason);
			
			response.setStatus("Quote Rejected Successfully");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
			
		}
		catch(Exception e)
		{
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000")+" "+e);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/acceptQuote", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> acceptQuotation(@RequestBody QuotationBean quotationbean) {
		logger.info("=========== Accept Quotation ===========");
		GenericResponse response = new GenericResponse<>();
		Integer quotationId = quotationbean.getQuotationId();
		String transId = quotationbean.getTransactionId();
		String userId= quotationbean.getUserId();
		System.out.println("Quotation Id: "+quotationId);
		QuotationMaster qm=quotationService.getDetailsOfAcceptedTrans(transId,userId);
		if(qm==null)
		{
			try
			{
				quotationService.updateQuotationForAccept(quotationId,transId);
				String bankUserId=quotationService.findBankUserIdByQuotationId(quotationId);
				List<BankDetailsBean> bdb=quotationService.getBankDet(bankUserId);
				response.setData(bdb);
				response.setStatus("Quote Accepted Successfully of Bank: "+bankUserId);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
				
			}
			catch(Exception e)
			{
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000")+" "+e);
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			response.setStatus("Failure");
			System.out.println("You cant accept the quote of bank for the transaction:"+quotationbean.getTransactionId());
			response.setErrMessage("You cant accept the quote of bank for the transaction:"+quotationbean.getTransactionId());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getQuotationDtlByBankUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getAllQuotationByBankUserId(@RequestBody QuotationBean quotationbean) {
		logger.info("=========== Get Quotation By Bank UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String bankUserId = quotationbean.getBankUserId();
		
		System.out.println(""+bankUserId);
		
		List<QuotationMaster> quotations = quotationService.getQuotationDetailByBankUserId(bankUserId);
		
		if (quotations == null || quotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getDraftQuotationByBankUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getDraftQuotationByBankUserId(@RequestBody QuotationBean quotationBean) throws NumberFormatException, ParseException {
		logger.info("=========== Get Draft Quotation By Bank UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String bankUserId = quotationBean.getBankUserId();
		//List<Quotation> draftQuotations = quotationService.getAllDraftQuotationDetailsByBankUserId(bankUserId);
		List<TransactionQuotationBean> draftQuotations = quotationService.getAllDraftTransQuotationDetailsByBankUserId(bankUserId);
		
		if (draftQuotations.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			// lcservice.setDataToList(transactions);
			response.setData(draftQuotations);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}
	
	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getTransQuotationDtlByBankUserIdAndStatus", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getAllTransQuotationByBankUserIdAndStatus(@RequestBody TransactionQuotationBean trquotationbean) throws ParseException {
		logger.info("=========== Get Transaction Quotation Details By Bank UserId and Status ===========");
		GenericResponse response = new GenericResponse<>();
		
		String bankUserId = trquotationbean.getBankUserId();
		//String quotationPlaced = trquotationbean.getQuotationPlaced();
		//String transactionStatus = trquotationbean.getTransactionStatus();
		String quotationStatus = trquotationbean.getQuotationStatus();
		System.out.println(""+bankUserId);
		//List<TransactionQuotationBean> quotations = quotationService.getTransactionQuotationDetailByBankUserIdAndStatus(bankUserId,quotationPlaced,transactionStatus);
		List<TransactionQuotationBean> quotations = quotationService.getTransactionQuotationDetailByBankUserIdAndStatus(bankUserId,quotationStatus);
		if (quotations == null) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quotations);
			
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}
	
	/*@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getConfirmationCharges", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getConfirmationCharges(@RequestBody QuotationMasterBean quotebean) throws ParseException {
		logger.info("=========== Get Transaction Quotation Details By Bank UserId and Status ===========");
		GenericResponse response = new GenericResponse<>();
		
		String bankUserId = trquotationbean.getBankUserId();
		String quotationPlaced = trquotationbean.getQuotationPlaced();
		String transactionStatus = trquotationbean.getTransactionStatus();
		System.out.println(""+bankUserId);
		
		List<TransactionQuotationBean> quotations = quotationService.getTransactionQuotationDetailByBankUserIdAndStatus(bankUserId,quotationPlaced,transactionStatus);
		
		if (quotations == null) {
			response.setStatus("Failure");
			response.setErrCode("ASA004");
			response.setErrMessage(ErrorDescription.getDescription("ASA004"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(quotations);
			
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}*/

}
