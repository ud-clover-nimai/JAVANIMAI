package com.nimai.email.service;

import java.util.ArrayList;
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
import org.springframework.scheduling.annotation.Scheduled;
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

	@Scheduled(fixedDelay = 50000)
	public void setTransactionEmailInSchTable() {
		logger.info("============Inside scheduler method of setTransactionEmailInSchTable==============");
		/* query to fetch the list of data from nimaiEmailAlertsTobankSchedulerTablw */
		List<NimaiEmailSchedulerAlertToBanks> emailDetailsScheduled = userDao.getTransactionDetailByTrEmailStatus();
		Iterator itr = emailDetailsScheduled.iterator();
		while (itr.hasNext()) {
			NimaiEmailSchedulerAlertToBanks schdulerData = (NimaiEmailSchedulerAlertToBanks) itr.next();
			if (schdulerData.getTransactionEmailStatusToBanks().equalsIgnoreCase("pending")
					&& schdulerData.getCustomerid().substring(0, 2).equalsIgnoreCase("CU")
					&& schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPLOAD(DATA)")) {
				try {
					StoredProcedureQuery getBAnksEmail = em.createEntityManager()
							.createStoredProcedureQuery("get_eligible_banks", NimaiClient.class);
					getBAnksEmail.registerStoredProcedureParameter("inp_customer_userID", String.class,
							ParameterMode.IN);
					getBAnksEmail.registerStoredProcedureParameter("inp_transaction_ID", String.class,
							ParameterMode.IN);

					getBAnksEmail.setParameter("inp_customer_userID", schdulerData.getCustomerid());
					getBAnksEmail.setParameter("inp_transaction_ID", schdulerData.getTransactionid());
					getBAnksEmail.execute();
					ModelMapper modelMapper = new ModelMapper();
					List<NimaiClient> nimaiCust = getBAnksEmail.getResultList();
					EligibleEmailBeanResponse responseBean = new EligibleEmailBeanResponse();
					List<EligibleEmailList> emailIdList = new ArrayList<EligibleEmailList>();

					List<EligibleEmailList> emailId = nimaiCust.stream().map(obj -> {
						EligibleEmailList data = new EligibleEmailList();
						NimaiEmailSchedulerAlertToBanks schedulerEntity = new NimaiEmailSchedulerAlertToBanks();
						Calendar cal = Calendar.getInstance();
						Date insertedDate = cal.getTime();
						schedulerEntity.setInsertedDate(insertedDate);
						schedulerEntity.setCustomerid(schdulerData.getCustomerid());
						schedulerEntity.setTransactionid(schdulerData.getTransactionid());
						schedulerEntity.setEmailEvent("LC_UPLOAD_ALERT_ToBanks");
						schedulerEntity.setBanksEmailID(obj.getEmailAddress());
						schedulerEntity.setBankUserid(obj.getUserid());
						schedulerEntity.setBankUserName(obj.getFirstName());
						int i = schdulerData.getScedulerid();
						String trScheduledId = Integer.toString(i);
						schedulerEntity.setTrScheduledId(trScheduledId);
						schedulerEntity.setEmailFlag("pending");
						// Bank user id transactionEmaiStatus is pending
						schedulerEntity.setTransactionEmailStatusToBanks("pending");
						userDao.saveSchdulerData(schedulerEntity);
						data.setEmailList(obj.getEmailAddress());
						return data;
					}).collect(Collectors.toList());

					// customer id transactioStatus is set as "In-process" to avoid duplicate
					// entry of matching banks while iterating data
					int scedulerid = schdulerData.getScedulerid();
					userDao.updateTREmailStatus(scedulerid);

					logger.info(
							"Customer critria(minLc,blGoods,country) matching banks are not available to send the transaction upload alert");

				} catch (Exception e) {
					logger.info(
							"Customer critria(minLc,blGoods,country) matching banks are not available to send the customer transaction upload alert");

				}
			}
		}
	}

	@Override
 @Scheduled(fixedDelay = 50000)
	public ResponseEntity<?> sendTransactionStatusToBanksByScheduled() {

		// TODO Auto-generated method stub
		logger.info("=====InsidesendTransactionStatusToBanksByScheduled method========= ");
		GenericResponse response = new GenericResponse<>();

		/* query to fetch the list of data from nimaiEmailAlertsTobankSchedulerTablw */
		List<NimaiEmailSchedulerAlertToBanks> emailDetailsScheduled = userDao.getTransactionDetail();
//		Iterator itr = emailDetailsScheduled.iterator();
//		while (itr.hasNext()) {
		for (NimaiEmailSchedulerAlertToBanks schdulerData : emailDetailsScheduled) {
//			NimaiEmailSchedulerAlertToBanks schdulerData = (NimaiEmailSchedulerAlertToBanks) itr.next();
//			String errorString = this.resetUserValidator.bankEmailValidation(schdulerData,
//					schdulerData.getBanksEmailID());
//			if (errorString.equalsIgnoreCase("Success")) {
//			}

			if (schdulerData.getEmailEvent().equalsIgnoreCase("QUOTE_ACCEPT")
					|| schdulerData.getEmailEvent().equalsIgnoreCase("QUOTE_REJECTION")) {
				logger.info("============Inside QUOTE_ACCEPT & QUOTE_REJECTION condition==========");
				try {
					/* method for email sending to banks */
					emailInsert.sendQuotationStatusEmail(schdulerData.getEmailEvent(), schdulerData,
							schdulerData.getBanksEmailID());
					try {
						/* method for sending the email to customer */
						emailInsert.sendQuotationStatusEmailToCust(schdulerData.getEmailEvent(), schdulerData,
								schdulerData.getCustomerEmail());
					} catch (Exception e) {
						if (e instanceof NullPointerException) {
							response.setMessage("Email Sending failed");
							EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
							response.setData(emailError);
							return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
						}
					}
//					int scedulerid = schdulerData.getScedulerid();
					userDao.updateEmailFlag(schdulerData.getScedulerid());
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

			}
			if (schdulerData.getEmailEvent().equalsIgnoreCase("Bank_Details_tocustomer")) {
				logger.info("============Inside Bank_Details_tocustomer condition==========");
				QuotationMaster bnakQuotationDetails = userDao.getDetailsByQuoteId(schdulerData.getQuotationId());
				if (bnakQuotationDetails != null) {
					try {

						emailInsert.sendBankDetailstoCustomer(schdulerData.getEmailEvent(), schdulerData,
								schdulerData.getCustomerEmail(), bnakQuotationDetails);

//						int scedulerid = schdulerData.getScedulerid();
						userDao.updateEmailFlag(schdulerData.getScedulerid());
						response.setMessage(ErrorDescription.getDescription("ASA002"));
						return new ResponseEntity<Object>(response, HttpStatus.OK);

					}

					catch (Exception e) {
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
			}
			if (schdulerData.getEmailEvent().equalsIgnoreCase("QUOTE_PLACE_ALERT_ToBanks")) {
				logger.info("============Inside QUOTE_PLACE_ALERT_ToBanks condition==========");
				QuotationMaster bnakQuotationDetails = userDao.getDetailsByQuoteId(schdulerData.getQuotationId());
				if (bnakQuotationDetails != null) {
					try {
						emailInsert.sendQuotePlaceEmailToBanks(schdulerData.getEmailEvent(), schdulerData,
								schdulerData.getBanksEmailID(), bnakQuotationDetails);

//						int scedulerid = schdulerData.getScedulerid();
						userDao.updateEmailFlag(schdulerData.getScedulerid());
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
					logger.info("Inside sendTransactionStatusToBanksByScheduled quotation id not found");
					response.setMessage("Details not found");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}
			} else if (schdulerData.getEmailEvent().equalsIgnoreCase("BId_ALERT_ToCustomer")) {
				try {
					/*
					 * method for sending the email to customer tht he received one bid after bank
					 * place a quote against any transactionId
					 */
					String event = "BId_ALERT_ToCustomer";
					emailInsert.sendBidRecivedEmailToCust(event, schdulerData, schdulerData.getCustomerEmail());
					// int scedulerid = schdulerData.getScedulerid();
					userDao.updateEmailFlag(schdulerData.getScedulerid());
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
			}
			if (schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPLOAD(DATA)")
					|| schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPDATE(DATA)")) {

				logger.info("============Inside LC_UPLOAD_ALERT_ToBanks & LC_UPDATE_ALERT_ToBankscondition==========");

				NimaiLC custTransactionDetails = userDao.getTransactioDetailsByTransId(schdulerData.getTransactionid());
				NimaiClient custDetails = userDao.getCustDetailsByUserId(schdulerData.getCustomerid());
				try {
					if (custTransactionDetails != null && custDetails != null) {

						emailInsert.sendLcStatusEmailData(schdulerData, custTransactionDetails, custDetails);
						// int scedulerid = schdulerData.getScedulerid();
						userDao.updateEmailFlag(schdulerData.getScedulerid());
						response.setMessage(ErrorDescription.getDescription("ASA002"));
						return new ResponseEntity<Object>(response, HttpStatus.OK);

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
			if (schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPLOAD_ALERT_ToBanks")
					|| schdulerData.getEmailEvent().equalsIgnoreCase("LC_UPDATE_ALERT_ToBanks")) {

				logger.info("============Inside LC_UPLOAD_ALERT_ToBanks & LC_UPDATE_ALERT_ToBankscondition==========");

				NimaiLC custTransactionDetails = userDao.getTransactioDetailsByTransId(schdulerData.getTransactionid());
				NimaiClient custDetails = userDao.getCustDetailsByUserId(schdulerData.getCustomerid());
				if (custTransactionDetails != null && custDetails != null) {
					if (schdulerData.getTransactionEmailStatusToBanks() == null) {
						try {
							emailInsert.sendTransactionStatusToBanks(schdulerData, custTransactionDetails, custDetails);

							int scedulerid = schdulerData.getScedulerid();
							userDao.updateEmailFlag(scedulerid);
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
						try {

							emailInsert.sendTransactionStatusToBanks(schdulerData, custTransactionDetails, custDetails);
							// this scheduler id updating emailTRStatus flag from pending to sent
//							int schedulerId = Integer.parseInt(schdulerData.getTrScheduledId());
							userDao.updateTrStatusEmailFlag(Integer.parseInt(schdulerData.getTrScheduledId()));

							// this scheduler id updating bank email status flag from pending to sent
							int scedulerid = schdulerData.getScedulerid();
							userDao.updateBankEmailFlag(schdulerData.getScedulerid());

						} catch (Exception e) {
							if (e instanceof NullPointerException) {
								response.setMessage("Email Sending failed");
								EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
								response.setData(emailError);
								return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
							}
						}
					}

				} else {
					logger.info("Inside sendTransactionStatusToBanksByScheduled transaction id not found");
					response.setMessage("Details not found");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}
			}
			response.setMessage("Email send successFully for event");
		}
		return null;
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
