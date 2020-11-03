package com.nimai.email.utility;

import java.io.File;
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
import com.nimai.email.entity.NimaiEmailScheduler;
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

	public void resetPasswordEmail(String link, UserRegistrationBean resetpassword, NimaiMLogin nimaiLogin,
			NimaiClient clientDetails) throws Exception {

		EmailComponentMaster emailconfigurationBean = null;
		try {

			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(resetpassword.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			ArrayList attachements = new ArrayList();
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
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("resetPasswordEmail Exception" + e);
			e.printStackTrace();
		}

	}

	public void resetForgorPasswordEmail(String link, UserRegistrationBean forgotPassWord, String emailId,
			NimaiClient nimaiClientDetails) throws Exception {

		EmailComponentMaster emailconfigurationBean = null;
		ArrayList details1 = null;
		try {

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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body1);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();
		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("Exception" + e);
			e.printStackTrace();
		}

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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendSubAccAcivationLink Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendBranchUserActivationLink Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendTransactionStatusEmail Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);

			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);
			response.setMessage("SMTP Address Failed Exception");

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("=======sendLcStatusEmailWithData Exception============" + e);
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

	public void sendTransactionStatusToBanks(NimaiEmailSchedulerAlertToBanks alertBanksBean,
			NimaiLC custTransactionDetails, NimaiClient custDetails) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(alertBanksBean.getEmailEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String lcValue = Integer.toString(custTransactionDetails.getlCValue());
			String lcIssuingDate = convertStringToDate(custTransactionDetails.getlCIssuingDate());
			String lcExpiryDate = convertStringToDate(custTransactionDetails.getValidity());
			subject.put("Subject", emailconfigurationBean.getSubject());
			body.put("transactionId", alertBanksBean.getTransactionid());
			body.put("username", alertBanksBean.getBankUserName());
			System.out.println("=============" + alertBanksBean.getBankUserName());
			body.put("userId", custTransactionDetails.getUserId());
			body.put("customerName", custDetails.getFirstName());
			body.put("lcIssuingValue", lcValue);
			body.put("lcIssuingDate", lcIssuingDate);
			body.put("lcExpiryDate", lcExpiryDate);
			if (alertBanksBean.getEmailEvent().equalsIgnoreCase("LC_REJECT_ALERT_ToBanks")) {
				body.put("reason", alertBanksBean.getReason());
			}

			String toMail = alertBanksBean.getBanksEmailID();
			String emailId = presentEmailID(custDetails);
			System.out.println("EmailID frm presented" + emailId);
			if (emailId != null && !emailId.trim().isEmpty()) {
				System.out.println("CC email Details inside sendLcStatusEmailData details" + emailId);
				emailConfigurationDAOImpl.saveCCEmails(emailId, emailconfigurationBean.getEventId());
			}
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======SMTP ADDRESS Failed Exception============" + e);

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendTransactionStatusToBanks Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendQuotationStatusEmail SMTP ADDRESS Failed Exception============" + e);

			System.out.println("sendQuotationStatusEmail Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendQuotationStatusEmail Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendTransactionStatusToBanks SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendTransactionStatusToBanks Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendTransactionStatusToBanks Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendQuotePlaceEmailToBanks SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendQuotePlaceEmailToBanks Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendQuotePlaceEmailToBanks Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendQuotationStatusEmail(String emailEvent, NimaiEmailSchedulerAlertToBanks schdulerData,
			String banksEmailID) {
		logger.info("=========Inside sendQuotationStatusEmail method with schedulerData from database==========");
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(schdulerData.getEmailEvent());

			System.out.println("Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String quoteId = String.valueOf(schdulerData.getQuotationId());
			String subjectSValue = "Quotation accepted by customer";
			subject.put("Subject", subjectSValue);
			String bankUserName = schdulerData.getBankUserName();
			body.put("htmlBody", "<br>Dear" + " " + bankUserName + ",<br><br>Quotation id" + " " + quoteId
					+ "accepted by customer,<br>whose Transaction id is" + schdulerData.getTransactionid()
					+ "<br><br>\r\n"
					+ "   <br><br> If you already get the quotation status<br>Please ignore and delete this message<br>\r\n"
					+ "<br>");

			if (schdulerData.getEmailEvent().equalsIgnoreCase("QUOTE_REJECTION")) {
				String subjectRejection = "Quotation rejected by customer";
				subject.put("Subject", subjectRejection);
				body.put("reason", schdulerData.getReason());
				body.put("quotationId", quoteId);
				body.put("htmlBody", "<br>Dear " + " " + bankUserName + ", <br><br>Quotation id" + quoteId
						+ "rejected by customer,<br>whose transaction id is" + " " + schdulerData.getTransactionid()
						+ ".\r\n" + "<br>Reason for rejecting quote is" + schdulerData.getReason() + ".<br>\r\n"
						+ "   <br><br> If you already get the quotation status<br>Please ignore and delete this message<br>");
			}

			String toMail = schdulerData.getBanksEmailID();
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendQuotationStatusEmail SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendQuotationStatusEmail Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendQuotationStatusEmail Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendQuotationStatusEmailToCust(String emailEvent, NimaiEmailSchedulerAlertToBanks schdulerData,
			String custEmailID) {
		logger.info("=========Inside sendQuotationStatusEmailToCust method with schedulerData from database==========");
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(schdulerData.getEmailEvent());

			System.out.println("Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String quoteId = String.valueOf(schdulerData.getQuotationId());
			String Subject = "Quotation confirmed successfully";
			String custName = schdulerData.getCustomerUserName();
			subject.put("Subject", Subject);

			body.put("htmlBody", "<br>Dear" + " " + custName + ", <br><br>Quotation id" + " " + quoteId + " "
					+ "confirmed successfully,<br>for transaction id" + " " + schdulerData.getTransactionid()
					+ "<br><br>\r\n"
					+ "   <br><br> If you already get the quotation status<br>Please ignore and delete this message<br>\r\n"
					+ "<br>");

			if (schdulerData.getEmailEvent().equalsIgnoreCase("QUOTE_REJECTION")) {
				String subjectRejection = "Quotation reject successfully";
				subject.put("Subject", subjectRejection);
				body.put("reason", schdulerData.getReason());

				body.put("htmlBody", "<br>Dear" + " " + custName + " <br><br>Quotation id" + " " + quoteId + " "
						+ "rejected successfully,<br>whose transaction id is" + " " + schdulerData.getTransactionid()
						+ ".\r\n" + "<br>Reason for rejecting quote is" + " " + schdulerData.getReason() + ".<br>\r\n"
						+ "   <br><br> If you already get the quotation status<br>Please ignore and delete this message<br>");
			}

			String toMail = custEmailID;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendQuotationStatusEmailToCust SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendQuotationStatusEmailToCust Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendQuotationStatusEmailToCust Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendBidRecivedEmailToCust(String emailEvent, NimaiEmailSchedulerAlertToBanks schdulerData,
			String customerEmail) {
		// TODO Auto-generated method stub
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(emailEvent);

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			subject.put("Subject", emailconfigurationBean.getSubject());
			body.put("transactionId", schdulerData.getTransactionid());
			body.put("dmkgjor", schdulerData.getCustomerUserName());
			String toMail = customerEmail;
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendBidRecivedEmailToCust SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendBidRecivedEmailToCust Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendBidRecivedEmailToCust Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendBankDetailstoCustomer(String emailEvent, NimaiEmailSchedulerAlertToBanks schdulerData,
			String customerEmail, QuotationMaster bnakQuotationDetails) {
		logger.info("Inside EmailInsert sendBankDetailstoCustomer method");
		// TODO Auto-generated method stub
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(emailEvent);

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String validaityDate = convertStringToDate(bnakQuotationDetails.getValidityDate());
			String totalQuoteValue = String.valueOf(bnakQuotationDetails.getTotalQuoteValue());
			String quotationId = Float.toString(schdulerData.getQuotationId());
			subject.put("Subject", emailconfigurationBean.getSubject());
			body.put("transactionId", schdulerData.getTransactionid());
			body.put("customerName", schdulerData.getCustomerUserName());
			body.put("bankName", bnakQuotationDetails.getBankName());
			body.put("branchName", bnakQuotationDetails.getBranchName());
			body.put("userId", schdulerData.getCustomerid());
			body.put("quotationId", quotationId);
			body.put("validatyDate", validaityDate);
			body.put("totalQuoteValue", totalQuoteValue);

			String toMail = schdulerData.getCustomerEmail();
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);

			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendBankDetailstoCustomer SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendBankDetailstoCustomer Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("sendBankDetailstoCustomer Exception" + e);
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
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======AdminEmail SMTP ADDRESS Failed Exception============" + e);
			System.out.println("AdminEmail Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("AdminEmail Exception" + e);
			e.printStackTrace();
		}

	}

	public void sendLcStatusEmailData(NimaiEmailSchedulerAlertToBanks schdulerData, NimaiLC transactionDetails,
			NimaiClient clientUserId) {
		// TODO Auto-generated method stub
		EmailComponentMaster emailconfigurationBean = null;
		GenericResponse response = new GenericResponse();
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(schdulerData.getEmailEvent());
			System.out.println("========useraname=======:::" + schdulerData.getCustomerUserName());
			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			String ccEmail = "";
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String lcValue = Integer.toString(transactionDetails.getlCValue());
			String lcIssuingDate = convertStringToDate(transactionDetails.getlCIssuingDate());
			String lcExpiryDate = convertStringToDate(transactionDetails.getValidity());
			subject.put("Subject", emailconfigurationBean.getSubject());

			body.put("transactionId", schdulerData.getTransactionid());
			body.put("username", schdulerData.getCustomerUserName());
			body.put("userId", schdulerData.getCustomerid());
			body.put("lcIssuingValue", lcValue);
			body.put("lcIssuingDate", lcIssuingDate);
			body.put("lcExpiryDate", lcExpiryDate);

			String toMail = schdulerData.getCustomerEmail();
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			String emailId = presentEmailID(clientUserId);
			System.out.println("EmailID frm presented" + emailId);
			if (emailId != null && !emailId.trim().isEmpty()) {
				System.out.println("CC email Details inside sendLcStatusEmailData details" + emailId);
				emailConfigurationDAOImpl.saveCCEmails(emailId, emailconfigurationBean.getEventId());
			}
			ArrayList attachements = new ArrayList();
			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);

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

	public void sendCustSPlanEmail(NimaiEmailScheduler schdulerData, NimaiClient clientUseId) {
		// TODO Auto-generated method stub
		EmailComponentMaster emailconfigurationBean = null;
		logger.info("=======Inside sendCustSPlanEmail============" + schdulerData.toString());
		GenericResponse response = new GenericResponse();
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(schdulerData.getEvent());
			System.out.println("========useraname=======:::" + schdulerData.getUserName());
			System.out.println(" Fetching Configuration for sendCustSPlanEmail " + emailconfigurationBean);
			String ccEmail = "";
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			String sPlanStartDate = convertStringToDate(schdulerData.getSubscriptionEndDate());
			String sPLanEndDate = convertStringToDate(schdulerData.getSubscriptionStartDate());
			subject.put("Subject", emailconfigurationBean.getSubject());

			ArrayList attachements = new ArrayList();
			// attachements.add(new File("D:\\EmailSplan_changes_Checklist.xlsx"));

			body.put("username", schdulerData.getUserName());
			body.put("suscriptionId", schdulerData.getSubscriptionId());
			body.put("subscriptionName", schdulerData.getSubscriptionName());
			body.put("relationshipManager", schdulerData.getRelationshipManager());
			body.put("customerSupport", schdulerData.getCustomerSupport());
			body.put("splanStartDate", sPlanStartDate);
			body.put("splanEndDate", sPLanEndDate);
			body.put("splanValidity", schdulerData.getSubscriptionValidity());
			body.put("splanAmount", schdulerData.getSubscriptionAmount());

			String toMail = schdulerData.getEmailId();
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			String emailId = presentEmailID(clientUseId);
			System.out.println("EmailID frm presented" + emailId);
			if (emailId != null && !emailId.trim().isEmpty()) {
				System.out.println("CC email Details inside sendCustSPlanEmail details" + emailId);
				emailConfigurationDAOImpl.saveCCEmails(emailId, emailconfigurationBean.getEventId());
			}

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);

			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendCustSPlanEmail SMTP ADDRESS Failed Exception============" + e);
			response.setMessage("sendCustSPlanEmail SMTP Address Failed Exception");

			System.out.println("Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("=======sendCustSPlanEmail Exception============" + e);
			e.printStackTrace();
		}

	}

	public String presentEmailID(NimaiClient client) {
		String ccEmails = "";
		if (client.getEmailAddress1() == null && client.getEmailAddress2() != null
				&& client.getEmailAddress3() != null) {
			ccEmails = client.getEmailAddress2() + "," + client.getEmailAddress3();
		} else if (client.getEmailAddress1() != null && client.getEmailAddress2() == null
				&& client.getEmailAddress3() == null) {
			ccEmails = client.getEmailAddress1();
		} else if (client.getEmailAddress2() == null && client.getEmailAddress1() != null
				&& client.getEmailAddress3() != null) {
			ccEmails = client.getEmailAddress1() + "," + client.getEmailAddress3();
		} else if (client.getEmailAddress2() != null && client.getEmailAddress1() == null
				&& client.getEmailAddress3() == null) {
			ccEmails = client.getEmailAddress2();
		} else if (client.getEmailAddress3() == null && client.getEmailAddress1() != null
				&& client.getEmailAddress2() != null) {
			ccEmails = client.getEmailAddress1() + "," + client.getEmailAddress2();
		} else if (client.getEmailAddress3() != null && client.getEmailAddress1() == null
				&& client.getEmailAddress2() == null) {
			ccEmails = client.getEmailAddress3();

		} else if (client.getEmailAddress1() != null && client.getEmailAddress2() != null
				&& client.getEmailAddress3() != null) {
			ccEmails = client.getEmailAddress1() + "," + client.getEmailAddress2() + "," + client.getEmailAddress3();
		} else if (client.getEmailAddress1() != null && client.getEmailAddress2() == null
				&& client.getEmailAddress3() == null) {
			ccEmails = client.getEmailAddress1();
		} else if (client.getEmailAddress2() != null && client.getEmailAddress1() == null
				&& client.getEmailAddress3() == null) {
			ccEmails = client.getEmailAddress2();
		} else if (client.getEmailAddress3() != null && client.getEmailAddress1() == null
				&& client.getEmailAddress2() == null) {
			ccEmails = client.getEmailAddress3();
		} else if (client.getEmailAddress3() == null && client.getEmailAddress1() == null
				&& client.getEmailAddress2() == null) {
			System.out.println("iside present emailId nll method");
			return null;
		}
		System.out.println("ccEmails from present emailId method:" + ccEmails);
		return ccEmails;

	}

	public void sendKycEmail(String event, NimaiClient clientUserId, NimaiEmailScheduler schdulerData) {
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(schdulerData.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();

			subject.put("Subject", emailconfigurationBean.getSubject());

			String userID = schdulerData.getUserid();
			String custName = schdulerData.getUserName();
			String rmname = schdulerData.getrMName();
			String toMail = schdulerData.getEmailId();
			String rmEmail = schdulerData.getrMemailId();
			ArrayList attachements = new ArrayList();

			body.put("htmlBody", "<br>Dear" + " " + custName + ", <br><br>"
					+ "Your kyc received successfully,<br>NIMAI Team will update the approval status of KYC within two working days"
					+ "<br><br>\r\n"
					+ "   <br><br> If you already get the kyc status<br>Please ignore and delete this message<br>\r\n"
					+ "<br>");

			if (schdulerData.getEvent().equalsIgnoreCase("KYC_REJECT")) {
				body.put("htmlBody", "<br>Dear" + " " + custName + ", <br><br>"
						+ "Your kyc has been rejected by NIMAI admin team,<br>Please contact to your relationship manager for more details"
						+ " and resubmit document to avoid delay." + "<br><br>\r\n"
						+ "   <br><br> If you already get the kyc status<br>Please ignore and delete this message<br>\r\n"
						+ "<br>");
			}
			if (schdulerData.getEvent().equalsIgnoreCase("KYC_APPROVED")) {
				body.put("htmlBody", "<br>Dear" + " " + custName + ", <br><br>"
						+ "Your kyc has been approved by NIMAI admin team,<br>You are ready to go for the transaction placement."
						+ "<br><br>\r\n"
						+ "   <br><br> If you already get the kyc status<br>Please ignore and delete this message<br>\r\n"
						+ "<br>");
			}

			String emailId = presentEmailID(clientUserId);
			System.out.println("EmailID frm presented" + emailId);
			if (emailId != null && !emailId.trim().isEmpty()) {
				System.out.println("CC email Details inside sendCustSPlanEmail details" + emailId);
				emailConfigurationDAOImpl.saveCCEmails(emailId, emailconfigurationBean.getEventId());
			}

			System.out.println("Fetching Configuration for sendKycEmail" + emailconfigurationBean.getSubject() + " :: "
					+ emailconfigurationBean.getEventId());

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());
			emailSend.getDetailsEmail();

		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendKycEmail SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendKycEmail Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("sendKycStatusEmail Exception" + e);
			e.printStackTrace();
		}
	}

	public void sendEmailToRm(String event, NimaiClient clientUserId, NimaiEmailScheduler schdulerData) {
		// TODO Auto-generated method stub
		EmailComponentMaster emailconfigurationBean = null;
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration(schdulerData.getEvent());

			System.out.println(" Fetching Configuration for Reset Password Policy " + emailconfigurationBean);

			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			ArrayList attachements = new ArrayList();
			subject.put("Subject", emailconfigurationBean.getSubject());
			String rmname = schdulerData.getrMName();
			String toMail = schdulerData.getrMemailId();
			String reason = schdulerData.getReason();
			body.put("htmlBody", "<br>Dear" + " " + rmname + ", <br><br>" + "Kyc has upload against userId is" + " "
					+ schdulerData.getUserid() + " " + ",<br>NIMAI Team please verify the customer kyc" + "<br><br>\r\n"
					+ "   <br><br> If you already get the quotation status<br>Please ignore and delete this message<br>\r\n"
					+ "<br>");

			if (schdulerData.getEvent().equalsIgnoreCase("KYC_REJECT")) {
				body.put("htmlBody", "<br>Dear" + " " + rmname + ", <br><br>" + "You have reject kyc whose userId is,"
						+ " " + schdulerData.getUserid() + "<br><br>Reason for rejecting is" + " " + reason + "<br>\r\n"
						+ "<br>Please notify customer to resubmitt of kyc document." + "<br><br>\r\n"
						+ "   <br><br> If you already get the kyc status<br>Please ignore and delete this message<br>\r\n"
						+ "<br>");
			}
			if (schdulerData.getEvent().equalsIgnoreCase("KYC_APPROVED")) {
				body.put("htmlBody", "<br>Dear" + " " + rmname + ", <br><br>"
						+ "You have approve the kyc whose userId is," + " " + schdulerData.getUserid()
						+ "   <br><br> If you already get the kyc status<br>Please ignore and delete this message<br>\r\n"
						+ "<br>");
			}

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);
			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());
			emailSend.getDetailsEmail();
		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendEmailToRm SMTP ADDRESS Failed Exception============" + e);
			System.out.println("sendEmailToRm Exception" + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("sendEmailToRm Exception" + e);
			e.printStackTrace();
		}

	}

	public boolean sendEodReport(String fileLocation, String key) {
		// TODO Auto-generated method stub
		EmailComponentMaster emailconfigurationBean = null;
		logger.info("=======Inside sendEodReport============");
		GenericResponse response = new GenericResponse();
		try {
			emailconfigurationBean = emailConfigurationDAOImpl.findEventConfiguration("EOD_REPORT");

			System.out.println(" Fetching Configuration for sendCustSPlanEmail " + emailconfigurationBean);
			String ccEmail = "";
			Map<String, String> subject = new HashMap<String, String>();
			Map<String, String> body = new HashMap<String, String>();
			subject.put("Subject", emailconfigurationBean.getSubject());

			ArrayList attachements = new ArrayList();
			attachements.add(fileLocation);

//			body.put("username", schdulerData.getUserName());
//			body.put("suscriptionId", schdulerData.getSubscriptionId());
//			body.put("subscriptionName", schdulerData.getSubscriptionName());
//			body.put("relationshipManager", schdulerData.getRelationshipManager());
//			body.put("customerSupport", schdulerData.getCustomerSupport());
//			body.put("splanStartDate", sPlanStartDate);
//			body.put("splanEndDate", sPLanEndDate);
//			body.put("splanValidity", schdulerData.getSubscriptionValidity());
//			body.put("splanAmount", schdulerData.getSubscriptionAmount());

			String toMail = "djjagtap123@gmail.com";
			System.out.println("Fetching Configuration for transaction status" + emailconfigurationBean.getSubject()
					+ " :: " + emailconfigurationBean.getEventId());
			// String emailId = presentEmailID(clientUseId);
			// System.out.println("EmailID frm presented" + emailId);
//			if (emailId != null && !emailId.trim().isEmpty()) {
//				System.out.println("CC email Details inside sendCustSPlanEmail details" + emailId);
//				emailConfigurationDAOImpl.saveCCEmails(emailId, emailconfigurationBean.getEventId());
//			}

			ArrayList details = new ArrayList();
			details.add(emailconfigurationBean.getEventId());// 0
			details.add(toMail);// 1
			details.add(subject);// 2
			details.add(body);// 3
			details.add(attachements);
			System.out.println("details" + details);

			emailProcessorImpl.saveEmail(details, emailconfigurationBean.getEventId());

			emailSend.getDetailsEmail();
			return true;
		} catch (SMTPAddressFailedException e) {
			logger.info("=======sendCustSPlanEmail SMTP ADDRESS Failed Exception============" + e);
			response.setMessage("sendCustSPlanEmail SMTP Address Failed Exception");

			System.out.println("Exception" + e);
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			logger.info("=======sendCustSPlanEmail Exception============" + e);
			e.printStackTrace();
			return false;
		}

	}
}
