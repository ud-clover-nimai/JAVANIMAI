package com.nimai.email.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nimai.email.api.GenericResponse;
import com.nimai.email.bean.AlertToBanksBean;
import com.nimai.email.bean.EligibleEmailBeanResponse;
import com.nimai.email.bean.EligibleEmailList;
import com.nimai.email.bean.EmailSendingDetails;
import com.nimai.email.bean.QuotationAlertRequest;
import com.nimai.email.controller.BanksAlertEmailController;
import com.nimai.email.dao.BanksAlertDao;
import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailSchedulerAlertToBanks;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.QuotationMaster;
import com.nimai.email.utility.EmaiInsert;
import com.nimai.email.utility.EmailErrorCode;
import com.nimai.email.utility.ErrorDescription;
import com.nimai.email.utility.ModelMapper;
import com.nimai.email.utility.ResetUserValidation;

@Service
@Transactional
public class BanksAlertEmailServiceImpl implements BanksALertEmailService {

	private static Logger logger = LoggerFactory.getLogger(BanksAlertEmailController.class);

	@Autowired
	BanksAlertDao userDao;

	@Autowired
	private EmaiInsert emailInsert;

	@Autowired
	EntityManagerFactory em;

	@Autowired
	ResetUserValidation resetUserValidator;

	@Override
	public ResponseEntity<?> getAlleligibleBAnksEmail(AlertToBanksBean alertBanksBean) {
		GenericResponse response = new GenericResponse();
		logger.info("=======getAlleligibleBAnksEmail method invoked===========");

		String errorString = this.resetUserValidator.banksEmailValidation(alertBanksBean,
				alertBanksBean.getBankEmail());
		if (errorString.equalsIgnoreCase("Success")) {
			/*
			 * while passing upload and update alert front end should complusary provide
			 * customerUserId and transactionId
			 */
			if (alertBanksBean.getEvent().equalsIgnoreCase("LC_UPLOAD_ALERT_ToBanks")
					|| alertBanksBean.getEvent().equalsIgnoreCase("LC_UPDATE_ALERT_ToBanks")) {
				try {
					StoredProcedureQuery getBAnksEmail = em.createEntityManager()
							.createStoredProcedureQuery("get_eligible_banks", NimaiClient.class);
					getBAnksEmail.registerStoredProcedureParameter("inp_customer_userID", String.class,
							ParameterMode.IN);
					getBAnksEmail.registerStoredProcedureParameter("inp_transaction_ID", String.class,
							ParameterMode.IN);

					getBAnksEmail.setParameter("inp_customer_userID", alertBanksBean.getCustomerUserId());
					getBAnksEmail.setParameter("inp_transaction_ID", alertBanksBean.getTransactionId());
					getBAnksEmail.execute();
					ModelMapper modelMapper = new ModelMapper();
					List<NimaiClient> nimaiCust = getBAnksEmail.getResultList();
					EligibleEmailBeanResponse responseBean = new EligibleEmailBeanResponse();

					List<EligibleEmailList> emailId = nimaiCust.stream().map(obj -> {
						EligibleEmailList data = new EligibleEmailList();
						NimaiEmailSchedulerAlertToBanks schedulerEntity = new NimaiEmailSchedulerAlertToBanks();
						Calendar cal = Calendar.getInstance();
						Date insertedDate = cal.getTime();
						schedulerEntity.setInsertedDate(insertedDate);
						schedulerEntity.setCustomerid(alertBanksBean.getCustomerUserId());
						schedulerEntity.setTransactionid(alertBanksBean.getTransactionId());
						schedulerEntity.setEmailEvent(alertBanksBean.getEvent());
						schedulerEntity.setBanksEmailID(obj.getEmailAddress());
						schedulerEntity.setBankUserid(obj.getUserid());
						schedulerEntity.setBankUserName(obj.getFirstName());
						userDao.saveSchdulerData(schedulerEntity);
						data.setEmailList(obj.getEmailAddress());
						return data;
					}).collect(Collectors.toList());

					response.setList(emailId);
					response.setErrCode("success");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} catch (Exception e) {
					logger.info("Error while fetching the details from email_get_baksEmail procedure");
					response.setMessage("Error While fetching Emails:" + e.getMessage());
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			}

			/*
			 * while sending this alert we front end data should be quotationId,bankEmailId
			 * should complusary provide
			 */
			else if (alertBanksBean.getEvent().equalsIgnoreCase("QUOTE_ACCEPT_ALERT_ToBanks")) {
				logger.info("===========Inside the else if condition QUOTE_ACCEPT_ALERT_ToBanks=============");
				NimaiEmailSchedulerAlertToBanks schedulerEntity = new NimaiEmailSchedulerAlertToBanks();
				int quotationId=Integer.parseInt(alertBanksBean.getQuotationId()); 
				QuotationMaster custTransactionDetails = userDao.getDetailsByQuoteId(quotationId);
				NimaiClient bankUserId = userDao.getCustDetailsByUserId(custTransactionDetails.getBankUserId());
				if (custTransactionDetails != null && bankUserId != null) {
					Calendar cal = Calendar.getInstance();
					Date insertedDate = cal.getTime();
					schedulerEntity.setInsertedDate(insertedDate);
					schedulerEntity.setQuotationId(quotationId);
					schedulerEntity.setTransactionid(custTransactionDetails.getTransactionId());
					schedulerEntity.setBankUserid(custTransactionDetails.getBankUserId());
					schedulerEntity.setBankUserName(bankUserId.getFirstName());
					schedulerEntity.setBanksEmailID(alertBanksBean.getBankEmail());
					schedulerEntity.setCustomerid(custTransactionDetails.getUserId());
					schedulerEntity.setEmailEvent(alertBanksBean.getEvent());
					userDao.saveSchdulerData(schedulerEntity);
				}

			} else {
				response.setErrCode("EX000");
				response.setMessage("Quotation Id Or Bank User Id not Found");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EX000");
			response.setMessage(ErrorDescription.getDescription("EX000") + errorString.toString());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("Email for Event"+alertBanksBean.getEvent()+"will be send soon");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> sendTransactionStatusToBanksByScheduled() {
		// TODO Auto-generated method stub
		logger.info("=====InsidesendTransactionStatusToBanksByScheduled method========= ");
		GenericResponse response = new GenericResponse<>();

		/* query to fetch the list of data from nimaiEmailAlertsTobankSchedulerTablw */
		List<NimaiEmailSchedulerAlertToBanks> emailDetailsScheduled = userDao.getTransactionDetail();
		Iterator itr = emailDetailsScheduled.iterator();
		while (itr.hasNext()) {
			NimaiEmailSchedulerAlertToBanks schdulerData = (NimaiEmailSchedulerAlertToBanks) itr.next();

			if (schdulerData.getEmailEvent().equalsIgnoreCase("QUOTE_ACCEPT_ALERT_ToBanks")) {
				 
				QuotationMaster bnakQuotationDetails = userDao.getDetailsByQuoteId(schdulerData.getQuotationId());
				if (bnakQuotationDetails != null) {
					try {
						emailInsert.sendQuotePlaceEmailToBanks(schdulerData.getEmailEvent(), schdulerData,
								schdulerData.getBanksEmailID(), bnakQuotationDetails);
						int scedulerid = schdulerData.getScedulerid();
						userDao.deleteEmailTransactionDetail(scedulerid);
					} catch (Exception e) {
						if (e instanceof NullPointerException) {
							response.setMessage("Email Sending failed");
							EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
							response.setData(emailError);
							return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
						}
					}

				} else {
					logger.info("Inside sendTransactionStatusToBanksByScheduled quotation id not found");
					response.setMessage("Details not found");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}
			} else if (schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPLOAD_ALERT_ToBanks")
					|| schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPDATE_ALERT_ToBanks")) {
				NimaiLC custTransactionDetails = userDao.getTransactioDetailsByTransIs(schdulerData.getTransactionid());
				NimaiClient custDetails = userDao.getCustDetailsByUserId(schdulerData.getCustomerid());
				try {
					if (custTransactionDetails != null && custDetails != null) {
						emailInsert.sendTransactionStatusToBanks(schdulerData.getEmailEvent(), schdulerData,
								schdulerData.getBanksEmailID(), custTransactionDetails, custDetails);
						
						int scedulerid = schdulerData.getScedulerid();
						userDao.deleteEmailTransactionDetail(scedulerid);

					} else {
						logger.info("Inside sendTransactionStatusToBanksByScheduled transaction id not found");
						response.setMessage("Details not found");
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}

				} catch (Exception e) {
					if (e instanceof NullPointerException) {
						response.setMessage("Email Sending failed");
						EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
						response.setData(emailError);
						return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
					}
				}
			}
		}
		response.setMessage("Email send successFully for event");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> sendTransactionStatusToBanks(AlertToBanksBean alertBanksBean) {
		logger.info("=======sendTransactionStatusToBanks method invoked=======");
		GenericResponse response = new GenericResponse<>();
		List<EmailSendingDetails> emailList = alertBanksBean.getBankEmails();
		for (EmailSendingDetails emailIds : emailList) {
			String errorString = this.resetUserValidator.banksEmailValidation(alertBanksBean, emailIds.getEmailId());
			if (errorString.equalsIgnoreCase("Success")) {
				try {
					emailInsert.sendTransactionStatusToBanks(alertBanksBean.getEvent(), alertBanksBean,
							emailIds.getEmailId());
				} catch (Exception e) {
					if (e instanceof NullPointerException) {
						response.setMessage("Email Sending failed");
						EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
						response.setData(emailError);
						return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
					}
				}
			} else {
				response.setErrCode("EX000");
				response.setMessage(ErrorDescription.getDescription("EX000") + errorString.toString());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}
		response.setMessage(ErrorDescription.getDescription("ASA002"));
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> sendQuotationStatusToBanks(QuotationAlertRequest quotationReq) {
		logger.info("==========sendQuotationStatusToBanks method invoked=========");
		GenericResponse response = new GenericResponse<>();
		String errorString = this.resetUserValidator.quotationAlertValidation(quotationReq);
		if (errorString.equalsIgnoreCase("Success")) {

			try {
				emailInsert.sendQuotationStatusEmail(quotationReq.getEvent(), quotationReq,
						quotationReq.getBankEmailId());
				response.setMessage(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					response.setMessage("Email Sending failed");
					EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
					response.setData(emailError);
					return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
				}
			}

		} else {
			response.setErrCode("EX000");
			response.setMessage(ErrorDescription.getDescription("EX000") + errorString.toString());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
