package com.nimai.email.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.email.api.GenericResponse;
import com.nimai.email.bean.AdminBean;
import com.nimai.email.bean.AlertToBanksBean;
import com.nimai.email.bean.BranchUserRequest;
import com.nimai.email.bean.KycEmailRequest;
import com.nimai.email.bean.LcUploadBean;
import com.nimai.email.bean.QuotationAlertRequest;
import com.nimai.email.bean.SubsidiaryBean;
import com.nimai.email.bean.UserRegistrationBean;
import com.nimai.email.dao.EmailConfigurationdaoImpl;
import com.nimai.email.dao.EmailProcessImpl;
import com.nimai.email.entity.EmailComponentMaster;
import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailSchedulerAlertToBanks;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.NimaiMLogin;
import com.nimai.email.entity.QuotationMaster;
import com.sun.mail.smtp.SMTPAddressFailedException;

@Component
public class EmaiInsert {

	private static Logger logger = LoggerFactory.getLogger(EmaiInsert.class);

	@Autowired
	EmailConfigurationdaoImpl emailConfigurationDAOImpl;

	@Autowired
	EmailProcessImpl emailProcessorImpl;

	@Autowired
	EmailSend emailSend;

	public EmailConfigurationdaoImpl getEmailConfigurationDAOImpl() {
		return emailConfigurationDAOImpl;
	}

	public void setEmailConfigurationDAOImpl(EmailConfigurationdaoImpl emailConfigurationDAOImpl) {
		this.emailConfigurationDAOImpl = emailConfigurationDAOImpl;
	}

	public void sendOtpEmail(String toMail, String otp) {

		System.out.println("sendOtpEmail getEmailotp");
		EmailComponentMaster emailconfigurationBean = null;
		ArrayList details1 = null;
		try {

			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration("PASSCODEMAIL");

			System.out.println("emailconfigurationBean output" + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body1 = new HashMap<String, String>();
			subject.put("Subject", "otp");

			ArrayList attachements = new ArrayList();
			body1.put("image", "<img height=\"100\" width=\"100\" src=\"cid:image1\">");
			body1.put("EmployeeName", "User");
			body1.put("OTP", otp);
			body1.put("timeout", "30");
			System.out.println("emailconfigurationBean.getEventid" + emailconfigurationBean.getEventId());

			/**
			 *
			 * input ArrayList 0 - eventId 1 - to email addresss 2 - Arraylist of parameters
			 * for subject 3 - Arraylist of parameters for body 4 - Arraylist of files for
			 * attachement
			 *
			 */

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body1);// 3
			details.add(attachements);// 4
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void resetPasswordEmail(String link, UserRegistrationBean resetpassword, NimaiMLogin nimaiLogin,
			NimaiClient clientDetails) throws Exception {

		EmailComponentMaster emailconfigurationBean = null;
		try {

			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(resetpassword.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			// ArrayList attachements = new ArrayList();
			// attachements.add(new File("D:\\BDO1.jpg"));
			body.put("link", link);
			body.put("username", clientDetails.getFirstName());
			body.put("userId", clientDetails.getUserid());
			body.put("password", nimaiLogin.getPassword());
			String toMail = resetpassword.getEmail();

			System.out.println(" Fetching Configuration for Reset Password Policy "
					+ emailconfigurationBean.getSubject() + " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			// details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void resetForgorPasswordEmail(String link, UserRegistrationBean forgotPassWord, String emailId,
			NimaiClient nimaiClientDetails) throws Exception {

		EmailComponentMaster emailconfigurationBean = null;
		ArrayList details1 = null;
//	        try {

		emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(forgotPassWord.getEvent());
		System.out.println("emailconfigurationBean output " + emailconfigurationBean);
		Map<String, String> subject = new HashMap<String, String>();
		Map<String, String> body1 = new HashMap<String, String>();
		subject.put("Subject", emailconfigurationBean.getSubject());
		// ArrayList attachements = new ArrayList();
		// attachements.add(new File("D:\\BDO1.jpg"));
		body1.put("link", link);
		body1.put("username", nimaiClientDetails.getFirstName());
		body1.put("userId", nimaiClientDetails.getUserid());
		// String toMail = emailconfigurationBean.getEmailTo();
		// System.out.println("TO mail.."+toMail);
		String toMail = emailId;

		System.out.println("emailconfigurationBean.getEventid" + emailconfigurationBean.getEventId());

		/**
		 *
		 * input ArrayList 0 - eventId 1 - to email addresss 2 - Arraylist of parameters
		 * for subject 3 - Arraylist of parameters for body 4 - Arraylist of files for
		 * attachement
		 *
		 */

		ArrayList details = new ArrayList();
		details.add(emailconfigurationBean.getEventId());// 0
		details.add(toMail);// 1
		details.add(subject);// 2
		details.add(body1);// 3
		// details.add(attachements);
		System.out.println("details" + details);
		emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

		emailSend.getDetailsEmail();

		/*
		 * } catch (Exception e) {
		 * 
		 * System.out.println("Exception" + e); e.printStackTrace(); }
		 */

	}

	public void sendSubAccAcivationLink(String Link, SubsidiaryBean subsidiaryBean) {
		EmailComponentMaster emailconfigurationBean = null;
		try {

			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(subsidiaryBean.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			// ArrayList attachements = new ArrayList();
			// attachements.add(new File("D:\\EmailSplan_changes_Checklist.xlsx"));
			body.put("link", Link);
			body.put("emailId", subsidiaryBean.getEmailId());
			if (subsidiaryBean.getEvent().equalsIgnoreCase("ADD_REFER")) {
				body.put("username", subsidiaryBean.getUserId());
			}
			String toMail = subsidiaryBean.getEmailId();

			System.out.println(" Fetching Configuration for Reset Password Policy "
					+ emailconfigurationBean.getSubject() + " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			// details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendBranchUserActivationLink(String bUlink, BranchUserRequest branchUserLink, String passcode) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(branchUserLink.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			// ArrayList attachements = new ArrayList();
			// attachements.add(new File("D:\\EmailSplan_changes_Checklist.xlsx"));
			body.put("link", bUlink);
			body.put("passcode", passcode);

			body.put("emailId", branchUserLink.getEmailId());

			String toMail = branchUserLink.getEmailId();

			System.out.println("Fetching Configuration for Reset Password Policy " + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			// details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendTransactionStatusEmail(String event, LcUploadBean lcUploadBean, String emailAddress) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(lcUploadBean.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			body.put("transactionId", lcUploadBean.getTransactionid());

			String toMail = emailAddress;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendLcStatusEmailWithData(String event, LcUploadBean lcUploadBean, String username, String emailAddress,
			NimaiLC transaction) {
		EmailComponentMaster emailconfigurationBean = null;
		GenericResponse response = new GenericResponse();
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(lcUploadBean.getEvent());
			System.out.println("========useraname=======:::" + username);
			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String lcValue = Integer.toString(transaction.getlCValue());
			String lcIssuingDate = convertStringToDate(transaction.getlCIssuingDate());
			String lcExpiryDate = convertStringToDate(transaction.getlCExpiryDate());
			subject.put("Subject", emailconfigurationBean.getSubject());

			body.put("transactionId", lcUploadBean.getTransactionid());
			body.put("username", username);
			body.put("userId", lcUploadBean.getUserId());
			body.put("lcIssuingValue", lcValue);
			body.put("lcIssuingDate", lcIssuingDate);
			body.put("lcExpiryDate", lcExpiryDate);

			String toMail = emailAddress;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3

			System.out.println("details" + details);

			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);
			response.setMessage("SMTP Address Failed Exception");

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("=======Exception============" + e);
			e.printStackTrace();
		}

	}

	public String convertStringToDate(Date indate) {
		String dateString = null;
		SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
		try {
			dateString = sdfr.format(indate);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return dateString;
	}

	public void sendTransactionStatusToBanks(String event, NimaiEmailSchedulerAlertToBanks alertBanksBean,
			String bankEmail, NimaiLC custTransactionDetails, NimaiClient custDetails) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(event);

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String lcValue = Integer.toString(custTransactionDetails.getlCValue());
			String lcIssuingDate = convertStringToDate(custTransactionDetails.getlCIssuingDate());
			String lcExpiryDate = convertStringToDate(custTransactionDetails.getlCExpiryDate());
			subject.put("Subject", emailconfigurationBean.getSubject());
			body.put("transactionId", alertBanksBean.getTransactionid());
			body.put("username", alertBanksBean.getBankUserName());
			body.put("userId", custTransactionDetails.getUserId());
			body.put("customerName", custDetails.getFirstName());
			body.put("lcIssuingValue", lcValue);
			body.put("lcIssuingDate", lcIssuingDate);
			body.put("lcExpiryDate", lcExpiryDate);
			if (event.equalsIgnoreCase("LC_REJECT_ALERT_ToBanks")) {
				body.put("reason", alertBanksBean.getReason());
			}

			String toMail = bankEmail;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendQuotationStatusEmail(String event, QuotationAlertRequest quotationReq, String bankEmailId) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(quotationReq.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			body.put("transactionId", quotationReq.getTransactionId());

			body.put("quotationId", quotationReq.getQuotationId());

			if (quotationReq.getEvent().equalsIgnoreCase("QUOTE_REJECTION")) {
				body.put("reason", quotationReq.getReason());
				body.put("quotationId", quotationReq.getQuotationId());
			}

			String toMail = bankEmailId;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendKycStatusEmail(String event, KycEmailRequest kycReq, String emailAddress) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(kycReq.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			body.put("userId", kycReq.getUserId());

			String toMail = emailAddress;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());
			emailSend.getDetailsEmail();
		} catch (Exception e) {
			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendTransactionStatusToBanks(String event, AlertToBanksBean alertBanksBean, String emailId) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(event);

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());
			body.put("transactionId", alertBanksBean.getTransactionId());

			if (event.equalsIgnoreCase("LC_REJECT_ALERT_ToBanks")) {
				body.put("reason", alertBanksBean.getReason());
			}

			String toMail = emailId;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendQuotePlaceEmailToBanks(String emailEvent, NimaiEmailSchedulerAlertToBanks schdulerData,
			String banksEmailID, QuotationMaster bankQuotationDetails) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(emailEvent);

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String validaityDate = convertStringToDate(bankQuotationDetails.getValidityDate());
			String totalQuoteValue = String.valueOf(bankQuotationDetails.getTotalQuoteValue());
			String quotationId = Float.toString(schdulerData.getQuotationId());
			subject.put("Subject", emailconfigurationBean.getSubject());
			body.put("transactionId", schdulerData.getTransactionid());
			body.put("username", schdulerData.getBankUserName());
			body.put("userId", schdulerData.getCustomerid());
			body.put("quotationId", quotationId);
			body.put("validatyDate", validaityDate);
			body.put("totalQuoteValue", totalQuoteValue);

			String toMail = schdulerData.getBanksEmailID();
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}

	public void AdminEmail(AdminBean resetpassword) throws Exception {

		EmailComponentMaster emailconfigurationBean = null;
		try {

			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(resetpassword.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			// ArrayList attachements = new ArrayList();
			// attachements.add(new File("D:\\BDO1.jpg"));
			body.put("link", resetpassword.getLink());
			body.put("username", resetpassword.getUserName());
			body.put("userId", resetpassword.getUserId());
			String toMail = resetpassword.getEmail();

			System.out.println(" Fetching Configuration for Reset Password Policy "
					+ emailconfigurationBean.getSubject() + " :: " + emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			// details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

	}
}
