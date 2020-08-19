package com.nimai.lc.controller;

import java.text.ParseException;
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
import com.nimai.lc.bean.NimaiLCBean;
import com.nimai.lc.bean.NimaiLCMasterBean;
import com.nimai.lc.entity.Countrycurrency;
import com.nimai.lc.entity.NimaiLC;
import com.nimai.lc.entity.NimaiLCMaster;
import com.nimai.lc.payload.GenericResponse;
import com.nimai.lc.service.LCService;
import com.nimai.lc.service.QuotationService;
import com.nimai.lc.utility.ErrorDescription;
import com.nimai.lc.utility.NimaiLCValidation;

@RestController
public class LCController {

	private static final Logger logger = LoggerFactory.getLogger(NimaiTransactionApplication.class);

	@Autowired
	LCService lcservice;

	@Autowired
	QuotationService quotationService;

	@Autowired
	NimaiLCValidation lcValid;

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@PostMapping(value = "/saveLCToDraft", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveLCdetails(@RequestBody NimaiLCBean nimailcbean) {
		logger.info("=========== Save LC To Draft ===========");
		GenericResponse response = new GenericResponse<>();
		// GenericResponse<Object> response = new GenericResponse<Object>();

		String statusString = "success"; // this.lcValid.validateLCDetails(nimailcbean);
		if (statusString.equalsIgnoreCase("Success")) {
			try {
				String tid = generateTransactionId(nimailcbean.getUserId(), nimailcbean.getRequirementType());
				lcservice.saveLCdetails(nimailcbean, tid);
				response.setStatus("Success");
				response.setData(tid);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(statusString);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	private String generateTransactionId(String userid, String transType) {
		// TODO Auto-generated method stub

		StringBuffer newtransactionId = new StringBuffer();
		newtransactionId.append(userid.substring(0, 2));
		newtransactionId.append(lcservice.generateYear());
		newtransactionId.append(lcservice.generateCountryCode(userid));
		newtransactionId.append(lcservice.generateTransactionType(transType));
		newtransactionId.append(lcservice.generateSerialNo());

		System.out.println(" TRANSACTION ID :::::::::::: " + newtransactionId.toString());
		return newtransactionId.toString();

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getcountryData", produces = "application/json", method = RequestMethod.GET)
	public List<Countrycurrency> getCountry() {
		return lcservice.getCountry();
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllTransactionDetails", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<?> getTransactiondetails() {
		logger.info("=========== Get all transaction details ===========");
		GenericResponse response = new GenericResponse<>();
		List<NimaiLCMaster> transactions = lcservice.getAllTransactionDetails();
		if (transactions.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			// lcservice.setDataToList(transactions);
			response.setData(transactions);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}

	/*
	 * public List<NimaiLC> getAllTransaction() { return
	 * lcservice.getAllTransactionDetails(); }
	 */

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getSpecificTxnDetailByTxnId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecificTransaction(@RequestBody NimaiLCMasterBean nimailcbean) {
		logger.info("=========== Get Specific Transaction By TransactionId ===========");
		GenericResponse response = new GenericResponse<>();
		String transactionId = nimailcbean.getTransactionId();
		System.out.println("" + transactionId);
		NimaiLCMaster trans = lcservice.getSpecificTransactionDetail(transactionId);
		if (trans == null) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(trans);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getTransactionDetailByUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getTransactionByUserId(@RequestBody NimaiLCMasterBean nimailc) {
		logger.info("=========== Get Transaction By UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String userId = nimailc.getUserId();
		String branchEmailId = nimailc.getBranchUserEmail();
		List<NimaiLCMaster> transactions = lcservice.getTransactionDetailByUserId(userId, branchEmailId);
		if (transactions.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(transactions);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllTxnDetailsByStatus/{status}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTransactionByStatus(@PathVariable(value = "status") String status) {
		logger.info("=========== Get Transaction By Status ===========");
		System.out.println("Requested Status: " + status);
		GenericResponse response = new GenericResponse<>();
		List<NimaiLCMaster> transactions = lcservice.getAllTransactionDetailsByStatus(status);

		if (transactions.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(transactions);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllTxnByUserIdAndStatus", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getTransactionByUserIdAndStatus(@RequestBody NimaiLCMasterBean nimailc) {
		logger.info("=========== Get Transactions By UserId and Status ===========");
		GenericResponse response = new GenericResponse<>();
		String userId = nimailc.getUserId();
		String status = nimailc.getTransactionStatus();
		String branchEmailId = nimailc.getBranchUserEmail();
		List<NimaiLCMaster> transactions = lcservice.getTransactionDetailByUserIdAndStatus(userId, status,
				branchEmailId);
		if (transactions.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(transactions);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/cloneLC", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> cloneLCDetails(@RequestBody NimaiLCBean nimailcBean) {
		logger.info("=========== Clonning LC ===========");
		GenericResponse response = new GenericResponse<>();
		try {
			String transId = nimailcBean.getTransactionId();
			NimaiLCMaster nimailcDetails = lcservice.getSpecificTransactionDetail(transId);
			// String updatedtid = generateTransactionId(nimailcDetails.getUserId(),
			// nimailcDetails.getRequirementType());

			// lcservice.cloneLCDetail(transId, updatedtid);
			// System.out.println("nimaibean: "+nimailcBean.toString());
			// System.out.println("nimaiEntity: "+nimailcDetails.toString());
			if (nimailcDetails == null) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Success");
				response.setData(nimailcDetails);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/confirmLC", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> confirmLCDetails(@RequestBody NimaiLCBean nimailc) {
		logger.info("=========== Confirming LC ===========");
		GenericResponse response = new GenericResponse<>();
		String statusString = "Success"; // this.lcValid.validateUserTransaction(nimailc);
		try {
			Integer lcCount = lcservice.getLcCount(nimailc.getUserId());
			Integer utilizedLcCount = lcservice.getUtilizedLcCount(nimailc.getUserId());
			System.out.println("Counts for User: " + nimailc.getUserId());
			System.out.println("LC Count: " + lcCount);
			System.out.println("LC Utilzed Count: " + utilizedLcCount);
			if (lcCount == 0) {
				response.setStatus("Failure");
				response.setErrMessage("Please Select Subscription Plan");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
			if (lcCount > utilizedLcCount) {
				try {
					String transId = nimailc.getTransactionId();
					String userId = nimailc.getUserId();
					System.out.println(transId + " " + userId);
					String sts = lcservice.confirmLCDet(transId, userId);
					if (sts.equals("Validation Success")) {
						response.setStatus("Success");
						response.setData(sts);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {
						response.setStatus("Failure");
						response.setErrMessage(sts);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}
				} catch (Exception e) {
					response.setStatus("Failure");
					response.setErrCode("EXE000");
					response.setErrMessage(ErrorDescription.getDescription("EXE000") + e);
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setStatus("Failure");
				response.setErrMessage("You had reached maximum LC Count!");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (NullPointerException ne) {
			response.setStatus("Failure");
			System.out.println("You are not Subscribe to a Plan. Please Subscribe");
			response.setErrMessage("You are not Subscribe to a Plan. Please Subscribe");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		/*
		 * List<NimaiLC> transactions=lcservice.getTransactionDetailByUserId(userId);
		 * if(transactions.isEmpty()) { response.setStatus("Failure");
		 * response.setErrCode("ASA002");
		 * response.setErrMessage(ErrorDescription.getDescription("ASA002")); return new
		 * ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST); } else {
		 * response.setData(transactions); return new ResponseEntity<Object>(response,
		 * HttpStatus.OK); }
		 */

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/checkLCCount", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> checkLCCount(@RequestBody NimaiLCBean nimailc) {
		logger.info("=========== Checking LC Count ===========");
		GenericResponse response = new GenericResponse<>();
		String statusString = "Success"; // this.lcValid.validateUserTransaction(nimailc);
		try {
			Integer lcCount = lcservice.getLcCount(nimailc.getUserId());
			Integer utilizedLcCount = lcservice.getUtilizedLcCount(nimailc.getUserId());
			System.out.println("Counts for User: " + nimailc.getUserId());
			System.out.println("LC Count: " + lcCount);
			System.out.println("LC Utilzed Count: " + utilizedLcCount);
			if (lcCount == 0) {
				response.setStatus("Failure");
				response.setErrMessage("Please Subscribe for Subscription Plan To Place a New Transaction");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else if (lcCount > utilizedLcCount) {
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Failure");
				response.setErrMessage("You had reached maximum LC Count! Please Renew Your Subscribe Plan");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (NullPointerException ne) {
			response.setStatus("Failure");
			System.out.println("You are not Subscribe to a Plan. Please Subscribe");
			response.setErrMessage("You are not Subscribe to a Plan. Please Subscribe");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}

	/*
	 * @CrossOrigin(value = "*", allowedHeaders = "*")
	 * 
	 * @RequestMapping(value = "/calculateQuote", produces = "application/json",
	 * method = RequestMethod.POST) public ResponseEntity<?>
	 * calculateQuote(@RequestBody NimaiLCBean nimailc) { GenericResponse response =
	 * new GenericResponse<>();
	 * 
	 * try { HashMap<String, Integer>
	 * outputFields=lcservice.calculateQuote(nimailc.getTransactionId());
	 * response.setStatus("Success"); response.setData(outputFields); return new
	 * ResponseEntity<Object>(response, HttpStatus.OK); } catch (Exception e) {
	 * response.setStatus("Failure"); response.setErrCode("EXE000");
	 * response.setErrMessage("Exception: "+e); return new
	 * ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST); } }
	 */

	/*
	 * @CrossOrigin(value = "*", allowedHeaders = "*")
	 * 
	 * @RequestMapping(value = "/placeQuotation", produces = "application/json",
	 * method = RequestMethod.POST) public ResponseEntity<?>
	 * placeQuotation(@RequestBody QuotationBean quotationBean) { GenericResponse
	 * response = new GenericResponse<>(); try {
	 * 
	 * String transId = quotationBean.getTransactionId(); NimaiLC
	 * transDetails=lcservice.checkTransaction(transId); if(transDetails==null) {
	 * response.setStatus("Failure"); response.setErrCode("ASA002");
	 * response.setErrMessage(ErrorDescription.getDescription("ASA002")); return new
	 * ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST); } else {
	 * quotationService.saveQuotationdetails(quotationBean);
	 * lcservice.quotePlace(transId); response.setStatus("Success"); return new
	 * ResponseEntity<Object>(response, HttpStatus.OK); } //
	 * System.out.println("nimaibean: "+nimailcBean.toString()); //
	 * System.out.println("nimaiEntity: "+nimailcDetails.toString());
	 * 
	 * } catch (Exception e) { response.setStatus("Failure");
	 * response.setErrCode("EXE000");
	 * response.setErrMessage(ErrorDescription.getDescription("EXE000")); return new
	 * ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST); }
	 * 
	 * }
	 */

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAllNewRequestsForBank", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getTransactionForBank(@RequestBody NimaiLCBean nimailcbean) {
		logger.info("=========== Get new Request for Bank ===========");
		GenericResponse response = new GenericResponse<>();
		String userid = nimailcbean.getUserId();
		List<NimaiLCMaster> transactions = lcservice.getAllTransactionForBank(userid);
		if (transactions.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(transactions);
			response.setStatus("Success");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@PostMapping(value = "/updateMasterLC", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateLCdetails(@RequestBody NimaiLCMasterBean nimailcbean) {
		logger.info("=========== Updating Master LC ===========");
		GenericResponse response = new GenericResponse<>();
		String transId = nimailcbean.getTransactionId();
		String userId = nimailcbean.getUserId();
		// GenericResponse<Object> response = new GenericResponse<Object>();
		// String statusString = this.lcValid.validateLCDetails(nimailcbean);
		// if (statusString.equalsIgnoreCase("Success"))
		// {
		try {

			lcservice.moveToHistory(transId, userId);
			lcservice.saveLCMasterdetails(nimailcbean, transId);
			response.setStatus("Success");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + " " + e);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		/*
		 * } else { response.setStatus("Failure"); response.setErrCode("EXE000");
		 * response.setErrMessage(statusString); return new
		 * ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST); }
		 */
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getDraftTransactionByUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getDraftTransaction(@RequestBody NimaiLCBean nimailcbean) {
		logger.info("=========== Get Draft Transaction By UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String userId = nimailcbean.getUserId();
		String branchEmailId = nimailcbean.getBranchUserEmail();
		List<NimaiLC> draftTransactions = lcservice.getAllDraftTransactionDetails(userId, branchEmailId);
		if (draftTransactions.isEmpty()) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			// lcservice.setDataToList(transactions);
			response.setData(draftTransactions);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@PostMapping(value = "/updateLCDraft", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateDraftLCdetails(@RequestBody NimaiLCBean nimailcbean) {
		logger.info("=========== Updating draft LC ===========");
		GenericResponse response = new GenericResponse<>();

		String statusString = "Success";// this.lcValid.validateLCDetails(nimailcbean);
		if (statusString.equalsIgnoreCase("Success")) {
			try {

				lcservice.updateDraftLCdetails(nimailcbean);
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000") + " " + e);
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(statusString);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getSpecificDraftTxnDetailByTxnId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecificDraftTransaction(@RequestBody NimaiLCMasterBean nimailcbean) {
		logger.info("=========== Get Specific Draft Transaction By TransactionId ===========");
		GenericResponse response = new GenericResponse<>();
		String transactionId = nimailcbean.getTransactionId();
		System.out.println("" + transactionId);
		NimaiLC trans = lcservice.getSpecificDraftTransactionDetail(transactionId);
		if (trans == null) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			response.setData(trans);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/reopenTransactionByTxnIdUserId", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> reopenTransactionByTxnIdUserId(@RequestBody NimaiLCMasterBean nimailcbean)
			throws ParseException {
		logger.info("=========== Reopen Transaction By TransactionId and UserId ===========");
		GenericResponse response = new GenericResponse<>();
		String transactionId = nimailcbean.getTransactionId();
		String userId = nimailcbean.getUserId();
		System.out.println("Reopening Transaction: " + transactionId + " for user: " + userId);
		NimaiLCMaster trans = lcservice.checkTransaction(transactionId);
		if (trans == null) {
			response.setStatus("Failure");
			response.setErrCode("ASA002");
			response.setErrMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			Integer qid = quotationService.getRejectedQuotationByTransactionId(transactionId);
			System.out.println("Rejected QuotationId: " + qid);
			if (qid == null) {
				response.setStatus("Failure");
				response.setErrMessage("No Rejected Quotation Present");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				lcservice.updateTransactionStatusToActive(transactionId, userId);
				quotationService.updateQuotationStatusToActiveOrPlaced(qid, transactionId);
				response.setStatus("Success");// .setData(trans);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}

		}
	}
}
