
package com.nimai.email.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.email.api.GenericResponse;
import com.nimai.email.bean.AdminBean;
import com.nimai.email.bean.BranchUserPassCodeBean;
import com.nimai.email.bean.BranchUserRequest;
import com.nimai.email.bean.KycEmailRequest;
import com.nimai.email.bean.LcUploadBean;
import com.nimai.email.bean.SubsidiaryBean;
import com.nimai.email.bean.UserRegistrationBean;
import com.nimai.email.service.UserService;
import com.nimai.email.utility.EmaiInsert;

/**
 * @author ASUS
 *
 */
/**
 * @author ASUS
 *
 */
/**
 * @param branchUserLink
 * @return
 */
@RestController
public class EmailController {

	private static Logger logger = LoggerFactory.getLogger(EmailController.class);
	@Autowired
	EmaiInsert emailInsert;

	@Autowired
	UserService userEmailService;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping("/sendSetPasswordLink")
	public ResponseEntity<?> sendResetPasswordLink(@RequestBody UserRegistrationBean registerLink) throws Exception {
		logger.info(
				" ================ Send resetPassword Link API is  Invoked ================" + registerLink.toString());
		return userEmailService.sendEmail(registerLink);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/validatePasswordLink/{token}")
	public ResponseEntity<?> getEmployeeById(@PathVariable(value = "token") String token) {
		logger.info(" ================ Send getEmployeeById API is  Invoked ================:" + token);
		return userEmailService.validateResetPasswordLink(token);
	}

	/*
	 * email sending api for refer and subsidiary user
	 */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping("/sendSubsidiaryAcivationLink")
	public ResponseEntity<?> sendSubsidiaryActivarionLink(@RequestBody SubsidiaryBean registerLink) throws Exception {
		logger.info(" ================ Send sendSubsidiaryActivarionLink API is  Invoked ================:"
				+ registerLink.toString());
		GenericResponse response = new GenericResponse();
		if (!(registerLink.getEvent().equalsIgnoreCase("ADD_SUBSIDIARY")
				|| registerLink.getEvent().equalsIgnoreCase("ADD_REFER"))) {
			response.setMessage("Invalid Event");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return userEmailService.sendSubsidiaryEmail(registerLink);
	}

	/*
	 * Validating api for subsidiary, refer
	 */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/validateAccLink/{token}")
	public ResponseEntity<?> getSubsidiaryDetails(@PathVariable(value = "token") String token) {
		logger.info(" ================ Send getSubsidiaryDetails API is  Invoked ================:" + token);
		return userEmailService.validateSubsidiaryLink(token);
	}

	/*
	 * Validating branch user passcodeVlue
	 */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/validatePasscode")
	public ResponseEntity<?> checBranchUserkPassCode(@RequestBody BranchUserPassCodeBean passCodeBean) {
		logger.info(
				" ================ checBranchUserkPassCode API Invoked ================:" + passCodeBean.toString());
		return userEmailService.validatePassCodeValue(passCodeBean);
	}

	/* branch user email sending api */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping("/sendBranchUserLink")
	public ResponseEntity<?> sendBranchUserLink(@RequestBody BranchUserRequest branchUserLink) {
		logger.info(" ================ sendBranchUserLink API Invoked ================:" + branchUserLink.toString());
		GenericResponse response = new GenericResponse();
		if (!branchUserLink.getEvent().equalsIgnoreCase("ADD_BRANCH_USER")) {
			response.setMessage("Invalid Event");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return userEmailService.sendbranchUserLink(branchUserLink);

	}

	/* email alert to customer when uploading,rejecting,reopening the lc */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping("/sendTransactionStatus")
	public ResponseEntity<?> sendLcUploadStatusToCustomer(@RequestBody LcUploadBean lcUploadBean) {
		logger.info(" ================ sendLcUploadStatusToCustomer API Invoked ================:"
				+ lcUploadBean.toString());
		return userEmailService.sendTransactionStatus(lcUploadBean);

	}

	/*
	 * email alert to customer when lc uploading,rejecting,reopening the lc with lc
	 * details
	 */
	@RequestMapping("/sendLcStatus")
	public ResponseEntity<?> sendLcUploadStatusEmail(@RequestBody LcUploadBean lcUploadBean) {
		logger.info("==========inside sendLcUploadStatusEmail API invoked===========");
		return userEmailService.sendLcUploadStatus(lcUploadBean);
	}

	/* sending the email after customer accept or reject the quotation to banks */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping("/sendKYCStatusToCustomer")
	public ResponseEntity<?> sendKYCStatusToCustomer(@RequestBody KycEmailRequest kycReq) {
		logger.info(" ================ sendKYCStatusToCustomer API Invoked ================:" + kycReq.toString());
		return userEmailService.sendKYCStatus(kycReq);

	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping("/sendAdminSetPasswordLink")
	public ResponseEntity<?> sendAdminResetPasswordLink(@RequestBody AdminBean adminBean) throws Exception {
		logger.info(
				" ================ Send resetPaasword Link API is  Invoked ================" + adminBean.toString());
		return userEmailService.sendAdminEmail(adminBean);
	}
}
