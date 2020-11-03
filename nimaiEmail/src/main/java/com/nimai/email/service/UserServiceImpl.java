package com.nimai.email.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nimai.email.api.GenericResponse;
import com.nimai.email.bean.AdminBean;
import com.nimai.email.bean.BranchUserPassCodeBean;
import com.nimai.email.bean.BranchUserRequest;
import com.nimai.email.bean.EodReport;
import com.nimai.email.bean.KycEmailRequest;
import com.nimai.email.bean.LcUploadBean;
import com.nimai.email.bean.SubsidiaryBean;
import com.nimai.email.bean.UserRegistrationBean;
import com.nimai.email.dao.EmailConfigurationdaoImpl;
import com.nimai.email.dao.EmailProcessImpl;
import com.nimai.email.dao.UserServiceDao;
import com.nimai.email.entity.EmailComponentMaster;
import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailScheduler;
import com.nimai.email.entity.NimaiFSubsidiaries;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.NimaiMBranch;
import com.nimai.email.entity.NimaiMLogin;
import com.nimai.email.entity.NimaiMRefer;
import com.nimai.email.utility.EmaiInsert;
import com.nimai.email.utility.EmailErrorCode;
import com.nimai.email.utility.EmailSend;
import com.nimai.email.utility.ErrorDescription;
import com.nimai.email.utility.ResetUserValidation;
import com.nimai.email.utility.Utils;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	EmailConfigurationdaoImpl emailConfigurationDAO;

	@Autowired
	UserServiceDao userDao;

	@Autowired
	EntityManagerFactory em;

	@Autowired
	private EmaiInsert emailInsert;

	@Autowired
	EmailProcessImpl emailProcessorImpl;

	@Autowired
	EmailSend emailSend;

	@Autowired
	private Utils utility;

	@Autowired
	EmailConfigurationdaoImpl emailConfigurationDAOImpl;

	@Autowired
	ResetUserValidation resetUserValidator;

	@Value("${accountActivation.url}")
	private String accountActivationLink;

	@Value("${forgotPassword.url}")
	private String forgorPasswordLink;

	@Value("${subsidiaryLink.url}")
	private String subAccountActivationLink;

	@Value("${branchUserLink.url}")
	private String branchUserActivationLink;

	@Value("${referUserLink.url}")
	private String referAccountActivationLink;

	@Override
	public boolean checkUserId(String userId) {
		return false;
	}

	@Override
	public boolean checkEmailId(String emailAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResponseEntity<Object> sendEmail(UserRegistrationBean userRegistratinBean) throws Exception {
		GenericResponse response = new GenericResponse();
		NimaiMLogin nimaiLogin = null;
		logger.info("=======Inside sendEmail method====" + userRegistratinBean.toString());
		String errorString = this.resetUserValidator.Validation(userRegistratinBean);
		if (errorString.equalsIgnoreCase("success")) {
			try {
				NimaiClient clientUseId = userDao.getcliDetailsByEmailId(userRegistratinBean.getEmail());
				nimaiLogin = userDao.getCustomerDetailsByUserID(clientUseId.getUserid());

				if (nimaiLogin != null && clientUseId != null) {

					NimaiClient userId = nimaiLogin.getUserid();
					logger.info(" ========Inside UserServiceImpl==============" + userId);
					String tokenKey = utility.generatePasswordResetToken();
					Date tokenExpiry = utility.getLinkExpiryDate();
					nimaiLogin.setTokenExpiryDate(tokenExpiry);
					nimaiLogin.setToken(tokenKey);

					userDao.update(nimaiLogin);

					try {
						String aCLlink = accountActivationLink + tokenKey;
						String fPlink = forgorPasswordLink + tokenKey;
						NimaiClient nimaiClientdetails = userDao.getClientDetailsbyUserId(userId.getUserid());
						if (userRegistratinBean.getEvent().equalsIgnoreCase("ACCOUNT_ACTIVATE")) {

							emailInsert.resetPasswordEmail(aCLlink, userRegistratinBean, nimaiLogin,
									nimaiClientdetails);

						} else if (userRegistratinBean.getEvent().equalsIgnoreCase("FORGOT_PASSWORD")) {
							emailInsert.resetForgorPasswordEmail(fPlink, userRegistratinBean,
									userRegistratinBean.getEmail(), clientUseId);
						}
						response.setErrCode("ASA002");
						response.setMessage(ErrorDescription.getDescription("ASA002"));
						return new ResponseEntity<Object>(response, HttpStatus.OK);

					} catch (Exception e) {
						e.printStackTrace();
						if (e instanceof NullPointerException) {
							response.setMessage("No email address provided for User");
							EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
							response.setData(emailError);
							return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		response.setErrCode("ASA005");
		response.setMessage(ErrorDescription.getDescription("ASA005"));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	@Scheduled(fixedDelay = 30000)
	public void sendAccountEmail() throws Exception {
		GenericResponse response = new GenericResponse();

		List<NimaiEmailScheduler> emailDetailsScheduled = userDao.getSchedulerDetails();
		for (NimaiEmailScheduler schdulerData : emailDetailsScheduled) {
			if (schdulerData.getEvent().equalsIgnoreCase("Cust_Splan_email")) {
				try {
					NimaiClient clientUseId = userDao.getClientDetailsbyUserId(schdulerData.getUserid());

					emailInsert.sendCustSPlanEmail(schdulerData, clientUseId);
					// int scedulerid = schdulerData.getAccountSchedulerId();
					userDao.updateEmailStatus(schdulerData.getAccountSchedulerId());
					response.setErrCode("ASA002");
					response.setMessage(ErrorDescription.getDescription("ASA002"));

				} catch (Exception e) {
					if (e instanceof NullPointerException) {
						response.setMessage("Email Sending failed");
						EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
						response.setData(emailError);

					}

				}

			} else if (schdulerData.getEvent().equalsIgnoreCase("KYC_UPLOAD")
					|| schdulerData.getEvent().equalsIgnoreCase("KYC_APPROVED")
					|| schdulerData.getEvent().equalsIgnoreCase("KYC_REJECT")) {
				NimaiClient clientUserId = userDao.getClientDetailsbyUserId(schdulerData.getUserid());
				if (clientUserId != null) {
					if (schdulerData.getrMemailId() != null) {
						try {
							/* method for email sending to customer kyc */
							emailInsert.sendKycEmail(schdulerData.getEvent(), clientUserId, schdulerData);
							try {
								/* method for sending the email to rm kyc */
								emailInsert.sendEmailToRm(schdulerData.getEvent(), clientUserId, schdulerData);
							} catch (Exception e) {
								if (e instanceof NullPointerException) {
									response.setMessage("Email Sending failed");
									EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
									response.setData(emailError);

								}
							}
							// int scedulerid = schdulerData.getAccountSchedulerId();
							userDao.updateEmailStatus(schdulerData.getAccountSchedulerId());
							response.setMessage(ErrorDescription.getDescription("ASA002"));

						} catch (Exception e) {
							if (e instanceof NullPointerException) {
								response.setMessage("Email Sending failed");
								EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
								response.setData(emailError);

							}
						}
					} else {
						try {
							/* method for email sending to customer kyc */
							emailInsert.sendKycEmail(schdulerData.getEvent(), clientUserId, schdulerData);
							int scedulerid = schdulerData.getAccountSchedulerId();
							userDao.updateEmailStatus(scedulerid);
							response.setMessage(ErrorDescription.getDescription("ASA002"));
						} catch (Exception e) {
							if (e instanceof NullPointerException) {
								response.setMessage("Email Sending failed");
								EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
								response.setData(emailError);

							}
						}
					}
				} else {
					response.setErrCode("ASA001");
					response.setMessage(ErrorDescription.getDescription("ASA001"));

				}

			}
		}
	}

	public ResponseEntity<Object> validateResetPasswordLink(String tokenKey) {
		logger.info("======validateResetPasswordLink method invoked=====:" + tokenKey);
		GenericResponse response = new GenericResponse();
		try {
			NimaiMLogin nimaiLogin = userDao.getUserDetailsByTokenKey(tokenKey);
			Date dnow = new Date();
			Date expirydate = nimaiLogin.getTokenExpiryDate();
			if (nimaiLogin != null && nimaiLogin.getToken().equals(tokenKey)) {
				if (dnow.before(expirydate)) {
					response.setMessage(tokenKey);
					response.setFlag(1);
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} else {
					response.setFlag(0);
					response.setErrCode("ASA004");
					response.setMessage(ErrorDescription.getDescription("ASA004"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		response.setMessage("Invalid Token");
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> sendSubsidiaryEmail(SubsidiaryBean subsidiaryBean) {
		logger.info("=========sendSubsidiaryEmail method invoked======:" + subsidiaryBean.toString());
		GenericResponse response = new GenericResponse();
		String errorString = this.resetUserValidator.subsidiaryValidation(subsidiaryBean);
		if (errorString.equalsIgnoreCase("success")) {
			try {

				NimaiClient clientUseId = userDao.getClientDetailsbyUserId(subsidiaryBean.getUserId());
				if (clientUseId != null) {
					String userId = clientUseId.getUserid();

					try {
						String tokenKey = userId.concat("_").concat(utility.generatePasswordResetToken());
						String link = "";
						if (subsidiaryBean.getEvent().equalsIgnoreCase("ADD_SUBSIDIARY")) {
							String clientDomainName = utility.getEmailDomain(clientUseId.getEmailAddress());
							String subsidiaryDomainName = utility.getEmailDomain(subsidiaryBean.getEmailId());
							if (clientDomainName.equalsIgnoreCase(subsidiaryDomainName)) {
								NimaiFSubsidiaries subsidiary = new NimaiFSubsidiaries();
								String token = tokenKey.substring(tokenKey.indexOf("_") + 1);
								Calendar cal = Calendar.getInstance();
								Date insertedDate = cal.getTime();
								Date tokenExpiry = utility.getLinkExpiryDate();
								subsidiary.setTokenExpiryDate(tokenExpiry);
								subsidiary.setSubsidiaryToken(tokenKey);
								subsidiary.setUserid(clientUseId);
								subsidiary.setInsertedDate(insertedDate);
								subsidiary.setSubsidiaryEmail(subsidiaryBean.getEmailId());
								userDao.saveSubsidiaryDetails(subsidiary);
								response.setFlag(0);
								link = subAccountActivationLink + tokenKey;
							} else {
								response.setErrCode("ASA006");
								response.setMessage(ErrorDescription.getDescription("ASA006"));
								return new ResponseEntity<Object>(response, HttpStatus.OK);
							}

						} else if (subsidiaryBean.getEvent().equalsIgnoreCase("ADD_REFER")) {

							NimaiMRefer referDetails = userDao.getreferDetails(subsidiaryBean.getReferenceId());
							if (referDetails != null) {
								String refertokenKey = ("RE")
										.concat(userId.concat("_").concat(utility.generatePasswordResetToken()));
								System.out.println(refertokenKey);
								NimaiMRefer referUser = new NimaiMRefer();
								String token = tokenKey.substring(tokenKey.indexOf("_") + 1);
								Calendar cal = Calendar.getInstance();
								Date insertedDate = cal.getTime();
								Date tokenExpiry = utility.getLinkExpiryDate();
								userDao.updateReferTokenDetails(tokenExpiry, refertokenKey, clientUseId, insertedDate,
										subsidiaryBean.getEmailId(), subsidiaryBean.getReferenceId());
								response.setFlag(1);
								link = referAccountActivationLink + refertokenKey;
							} else {
								response.setMessage("Refer Id Entry not Present");
								return new ResponseEntity<Object>(response, HttpStatus.OK);
							}

						}
						emailInsert.sendSubAccAcivationLink(link, subsidiaryBean);
						response.setErrCode("ASA002");
						response.setMessage(ErrorDescription.getDescription("ASA002"));
						return new ResponseEntity<Object>(response, HttpStatus.OK);

					} catch (Exception e) {
						e.printStackTrace();
						if (e instanceof NullPointerException) {
							response.setMessage("Email Sending failed");
							EmailErrorCode emailError = new EmailErrorCode("EmailNull", 409);
							response.setData(emailError);
							return new ResponseEntity<Object>(response, HttpStatus.CONFLICT);
						}
					}
				} else {
					response.setErrCode("ASA001");
					response.setMessage(ErrorDescription.getDescription("ASA001"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.setErrCode("ASA001");
				response.setMessage(ErrorDescription.getDescription("ASA001"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		response.setErrCode("ASA001");
		response.setMessage(ErrorDescription.getDescription("ASA001"));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity<?> validateSubsidiaryLink(String token) {
		logger.info("=======validateSubsidiaryLink method invoked======:" + token);
		GenericResponse response = new GenericResponse();
		String tokenInitial = token.substring(0, 2);
		System.out.println(tokenInitial);
		try {
			SubsidiaryBean beanResponse = new SubsidiaryBean();
			Date dnow = new Date();
			/* for refer token checking,token will start with RE */
			if (tokenInitial.equalsIgnoreCase("RE")) {
				NimaiMRefer nimaiRefer = userDao.getReferDetailsByToken(token);

				Date referExpirydate = nimaiRefer.getTokenExpiryTime();
				if (nimaiRefer != null && nimaiRefer.getToken().equals(token)) {
					if (dnow.before(referExpirydate)) {
						beanResponse.setEmailId(nimaiRefer.getEmailAddress());
						beanResponse.setUserId(nimaiRefer.getUserid().getUserid());
						response.setData(beanResponse);
						response.setMessage(token);
						response.setFlag(1);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {
						response.setFlag(0);
						response.setErrCode("ASA008");
						response.setMessage(ErrorDescription.getDescription("ASA008"));
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}

				}
			}
			/* for bank and user token initials will start with BA or Cu */
			else if ((tokenInitial.equalsIgnoreCase("CU") || tokenInitial.equalsIgnoreCase("BA"))) {
				NimaiFSubsidiaries nimaiFSub = userDao.getSubsidiaryDetailsByToken(token);
				Date subExpirydate = nimaiFSub.getTokenExpiryDate();
				if (nimaiFSub != null && nimaiFSub.getSubsidiaryToken().equals(token)) {
					if (dnow.before(subExpirydate)) {
						beanResponse.setEmailId(nimaiFSub.getSubsidiaryEmail());
						beanResponse.setUserId(nimaiFSub.getUserid().getUserid());
						response.setData(beanResponse);
						response.setMessage(token);
						response.setFlag(1);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {
						response.setFlag(0);
						response.setErrCode("ASA007");
						response.setMessage(ErrorDescription.getDescription("ASA007"));
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}

				}
			} else {
				/* while sending token from sendsubsidiary api token(token_passcode) */
				String passcode = token.substring(11, 16);
				String branchToken = token.substring(0, 10);
				NimaiMBranch nimaiMbranch = userDao.getbranchDetailsByToken(branchToken);
				Date subExpirydate = nimaiMbranch.getExpryTime();
				if (nimaiMbranch != null && nimaiMbranch.getToken().equals(branchToken)) {
					if (passcode.equals(nimaiMbranch.getPasscodeValue())) {
						if (dnow.before(subExpirydate)) {
							beanResponse.setEmailId(nimaiMbranch.getEmailId());
							beanResponse.setUserId(nimaiMbranch.getUserid());
							response.setData(beanResponse);
							response.setMessage(token);
							response.setFlag(1);
							return new ResponseEntity<Object>(response, HttpStatus.OK);
						} else {
							response.setFlag(0);
							response.setErrCode("ASA009");
							response.setMessage(ErrorDescription.getDescription("ASA009"));
							return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
						}
					} else {
						response.setMessage("Passcode does not match");
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setMessage("Invalid Token");
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	/* Resending the email within expiry time */
	@Override
	public ResponseEntity<?> sendbranchUserLink(BranchUserRequest branchUserLink) {
		logger.info("=====sendbranchUserLink=========:" + branchUserLink.toString());
		GenericResponse response = new GenericResponse<>();
		NimaiMBranch branchUserDetails = new NimaiMBranch();
		String errorString = this.resetUserValidator.branchUserValidation(branchUserLink);
		if (errorString.equalsIgnoreCase("success")) {
			int id = Integer.parseInt(branchUserLink.getBranchId());
			try {
				// NimaiClient clientUseId =
				// userDao.getClientDetailsbyUserId(branchUserLink.getUserId());
				NimaiMBranch branchUser = userDao.getBranchUserDetails(branchUserLink.getEmailId());
				String link = "";
				String passcode = "";
				String bUlink = "";
				String urltoken = "";
				if (branchUser != null && branchUser.getToken() != null) {
					Date dnow = new Date();
					System.out.println(dnow);
					Date expirydate = branchUser.getExpryTime();
					System.out.println(expirydate);
					if (dnow.before(expirydate)) {
						String passcodeValue = utility.passcodeValue();
						userDao.updateBranchUserDetails(branchUserLink.getEmailId(), dnow, passcodeValue);
						urltoken = branchUser.getToken().concat("_").concat(passcodeValue);
						bUlink = subAccountActivationLink + urltoken;
						try {
							emailInsert.sendBranchUserActivationLink(bUlink, branchUserLink, passcodeValue);
							response.setErrCode("ASA002");
							response.setData(urltoken);
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

						passcode = utility.passcodeValue();
						String userId = branchUserLink.getUserId();
						String tokenKey = utility.generatePasswordResetToken();
						Date tokenExpiry = utility.getLinkExpiryDate();
						Calendar cal = Calendar.getInstance();
						Date insertedDate = cal.getTime();
						userDao.updateBranchUser(passcode, tokenKey, insertedDate, branchUserLink.getEmailId(), id,
								tokenExpiry);
						userDao.updateBranchUser(branchUserDetails);
						urltoken = tokenKey.concat("_").concat(passcode);
						bUlink = subAccountActivationLink + urltoken;
						try {
							emailInsert.sendBranchUserActivationLink(bUlink, branchUserLink, passcode);
							response.setErrCode("ASA002");
							response.setData(urltoken);
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

				} else {

					passcode = utility.passcodeValue();
					String userId = branchUserLink.getUserId();
					String tokenKey = utility.generatePasswordResetToken();
					Date tokenExpiry = utility.getLinkExpiryDate();
					Calendar cal = Calendar.getInstance();
					Date insertedDate = cal.getTime();
					branchUserDetails.setEmailId(branchUserLink.getEmailId());
					userDao.updateBranchUser(passcode, tokenKey, insertedDate, branchUserLink.getEmailId(), id,
							tokenExpiry);
					urltoken = tokenKey.concat("_").concat(passcode);
					link = subAccountActivationLink + urltoken;
					try {

						emailInsert.sendBranchUserActivationLink(link, branchUserLink, passcode);
						response.setErrCode("ASA002");
						response.setData(urltoken);
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

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response.setErrCode("EX000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("Branch Id not found");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity<?> validatePassCodeValue(BranchUserPassCodeBean passCodeBean) {
		logger.info("=========validatePassCodeValue method invoked========");
		GenericResponse response = new GenericResponse();
		String errorString = this.resetUserValidator.passcodeValidation(passCodeBean);
		if (errorString.equalsIgnoreCase("success")) {
			NimaiMBranch nimaiMbranch = userDao.getbranchDetailsByToken(passCodeBean.getToken());
			Date dnow = new Date();
			Date subExpirydate = nimaiMbranch.getExpryTime();
			if (nimaiMbranch != null && nimaiMbranch.getToken().equals(passCodeBean.getToken())) {
				if (passCodeBean.getPasscodeValue().equals(nimaiMbranch.getPasscodeValue())) {
					if (dnow.before(subExpirydate)) {
						SubsidiaryBean beanResponse = new SubsidiaryBean();
						beanResponse.setEmailId(nimaiMbranch.getEmailId());
						beanResponse.setUserId(nimaiMbranch.getUserid());
						response.setData(beanResponse);
						response.setMessage(passCodeBean.getToken());
						response.setFlag(1);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {
						response.setFlag(0);
						response.setErrCode("ASA009");
						response.setMessage(ErrorDescription.getDescription("ASA009"));
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}
				} else {
					response.setMessage("Passcode does not match");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}

			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<Object> sendAdminEmail(AdminBean userRegistratinBean) throws Exception {
		GenericResponse response = new GenericResponse();

		logger.info("=======Inside sendAdminEmail method====" + userRegistratinBean.toString());
		try {

			if (userRegistratinBean.getEvent().equalsIgnoreCase("ACCOUNT_ACTIVATE")) {

				emailInsert.AdminEmail(userRegistratinBean);
			}
			response.setErrCode("ASA002");
			response.setMessage(ErrorDescription.getDescription("ASA002"));
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setErrCode("ASA005");
		response.setMessage(ErrorDescription.getDescription("ASA005"));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
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

	@Override
//@Scheduled(cron = "0 0 23 0 0 MON-SAT")
	public ByteArrayInputStream generateEodReport() {
		// TODO Auto-generated method stub

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		java.sql.Date todaysdate = new java.sql.Date(new java.util.Date().getTime());
		System.out.println(todaysdate);
		List<NimaiLC> customerList = userDao.getCustTransactionList(todaysdate);
		System.out.println(customerList.toString());
		Map<String, List<NimaiLC>> groupByUserId = customerList.stream()
				.collect(Collectors.groupingBy(NimaiLC::getUserId));
		System.out.println("New generated methods inside the generate Eod Report" + groupByUserId.toString());
		ArrayList<String> fileLocationArrayList = new ArrayList<>();
		for (Map.Entry<String, List<NimaiLC>> entry : groupByUserId.entrySet()) {
			try {
				String fileLocation = com.nimai.email.utility.ExcelUtility.generateReportToExcel(entry.getKey(),
						entry.getValue());
				System.out.println("filelocation name in the middle" + fileLocation);
				for (String filePresent : fileLocationArrayList)
					if (!filePresent.equalsIgnoreCase(fileLocation) && filePresent != null && !filePresent.isEmpty()) {
						try {
							boolean emailStatus = emailInsert.sendEodReport(fileLocation, entry.getKey());
							if (emailStatus != false) {
								System.out.println("false conditions");
								fileLocationArrayList.add(fileLocation);
							}

						} catch (Exception e) {

						}
					}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;

	}
}
