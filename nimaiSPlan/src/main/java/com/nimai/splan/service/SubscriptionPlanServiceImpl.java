package com.nimai.splan.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.splan.model.NimaiMCustomer;
import com.nimai.splan.model.NimaiMSubscription;
import com.nimai.splan.model.NimaiSubscriptionDetails;
import com.nimai.splan.payload.GenericResponse;
import com.nimai.splan.payload.SPlanApprovalBean;
import com.nimai.splan.payload.SPlanResponseBean;
import com.nimai.splan.payload.SplanRequest;
import com.nimai.splan.payload.SubscriptionBean;
import com.nimai.splan.payload.SubscriptionPlanResponse;
import com.nimai.splan.payload.banksSplansReponse;
import com.nimai.splan.payload.customerSPlansResponse;
import com.nimai.splan.repository.NimaiMCustomerRepository;
import com.nimai.splan.repository.NimaiMSPlanRepository;
import com.nimai.splan.repository.SubscriptionPlanRepository;
import com.nimai.splan.utility.ErrorDescription;
import com.nimai.splan.utility.ModelMapper;
import com.nimai.splan.utility.SPlanUniqueNumber;
import com.nimai.splan.utility.SubscriptionPlanValidation;

@Service
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

	private static Logger logger = LoggerFactory.getLogger(SubscriptionPlanServiceImpl.class);

	@Autowired
	SubscriptionPlanValidation validationDao;

	@Autowired
	NimaiMCustomerRepository userRepository;

	@Autowired
	SubscriptionPlanRepository subscriptionDetailsRepository;

	@Autowired
	EntityManagerFactory em;

	@Autowired
	NimaiMSPlanRepository masterSPlanRepo;

	@Override
	public ResponseEntity<?> saveUserSubscriptionPlan(SubscriptionBean subscriptionRequest, String userId) {
		GenericResponse response = new GenericResponse<>();
		logger.info(" ================ Send saveUserSubscriptionPlan method Invoked ================");
		try {
			if (subscriptionRequest.getSubscriptionId() != null) {
				Optional<NimaiMCustomer> mCustomer = userRepository.findByUserId(userId);
				if (mCustomer.isPresent()) {
					NimaiSubscriptionDetails subScriptionDetails = new NimaiSubscriptionDetails();
					subScriptionDetails.setSubscriptionName(subscriptionRequest.getSubscriptionName());
					subScriptionDetails.setUserid(mCustomer.get());
					subScriptionDetails.setSubscriptionValidity(subscriptionRequest.getSubscriptionValidity());
					subScriptionDetails.setSubscriptionId(subscriptionRequest.getSubscriptionId());
					subScriptionDetails.setRemark(subscriptionRequest.getRemark());
					subScriptionDetails.setSubscriptionAmount(subscriptionRequest.getSubscriptionAmount());
					subScriptionDetails.setlCount(subscriptionRequest.getLcCount());
					subScriptionDetails.setSubsidiaries(subscriptionRequest.getSubsidiaries());
					subScriptionDetails.setRelationshipManager(subscriptionRequest.getRelationshipManager());
					subScriptionDetails.setCustomerSupport(subscriptionRequest.getCustomerSupport());
					subScriptionDetails.setInsertedBy(mCustomer.get().getFirstName());
					subScriptionDetails.setsPLanCountry(mCustomer.get().getAddress3());
					String customerType = subscriptionRequest.getSubscriptionId().substring(0, 2);
					if (customerType.equalsIgnoreCase("BA")) {
						subScriptionDetails.setCustomerType("Bank");
					} else {
						subScriptionDetails.setCustomerType("Customer");
					}
					SPlanUniqueNumber endDate = new SPlanUniqueNumber();
					int year = endDate.getNoOfyears(subScriptionDetails.getSubscriptionValidity());
					int month = endDate.getNoOfMonths(subScriptionDetails.getSubscriptionValidity());
					System.out.println(year);
					System.out.println(month);
					subScriptionDetails.setStatus("INACTIVE");
					Calendar cal = Calendar.getInstance();
					Date today = cal.getTime();
					cal.add(Calendar.YEAR, year);
					cal.add(Calendar.MONTH, month);
					Date sPlanEndDate = cal.getTime();
					subScriptionDetails.setSubscriptionStartDate(today);
					subScriptionDetails.setSubscriptionEndDate(sPlanEndDate);
					NimaiSubscriptionDetails subScription = subscriptionDetailsRepository.save(subScriptionDetails);
					response.setErrCode("ASA001");
					response.setErrMessage(ErrorDescription.getDescription("ASA001"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} else {
					response.setStatus("Failure");
					response.setErrCode("ASA003");
					response.setErrMessage(ErrorDescription.getDescription("ASA003"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setStatus("Failure");
				response.setErrCode("ASA009");
				response.setErrMessage(ErrorDescription.getDescription("ASA009"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> findSPlanDetailsByUserId(String userId) {
		logger.info(" ================ Send findSPlanDetailsByUserId method Invoked ================");
		GenericResponse response = new GenericResponse<>();
		try {
			List<NimaiSubscriptionDetails> subscriptionEntity = subscriptionDetailsRepository.findAllByUserId(userId);
			if (subscriptionEntity.isEmpty()) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			} else {
				List<SubscriptionPlanResponse> subscriptionBean = new ArrayList<SubscriptionPlanResponse>();
				for (NimaiSubscriptionDetails temp : subscriptionEntity) {
					SubscriptionPlanResponse responseBean = new SubscriptionPlanResponse();
					responseBean.setSubscriptionAmount(temp.getSubscriptionAmount());
					responseBean.setSubscriptionName(temp.getSubscriptionName());
					responseBean.setSubscriptionId(temp.getSubscriptionId());
					responseBean.setSubscriptionValidity(temp.getSubscriptionValidity());
					responseBean.setLcCount(temp.getlCount());
					responseBean.setRemark(temp.getRemark());
					responseBean.setUserId(temp.getUserid().getUserid());
					responseBean.setStatus(temp.getStatus());
					responseBean.setSubsidiaries(temp.getSubsidiaries());
					responseBean.setRelationshipManager(temp.getRelationshipManager());
					responseBean.setCustomerSupport(temp.getCustomerSupport());
					subscriptionBean.add(responseBean);
					response.setData(subscriptionBean);

				}
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	private Date getDate(String pattern) throws ParseException {
		return new SimpleDateFormat(pattern).parse(new SimpleDateFormat(pattern).format(new Date()));
	}

	@Override
	public ResponseEntity<?> getSPlanByUserId(String userId) {
		GenericResponse response = new GenericResponse<>();
		logger.info(" ================ getSPlanByUserId method Invoked ================");
		try {
			List<NimaiSubscriptionDetails> subscriptionEntity = subscriptionDetailsRepository.findAllByUserId(userId);
			if (subscriptionEntity.isEmpty()) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			} else {
				List<SubscriptionPlanResponse> subscriptionBean = new ArrayList<SubscriptionPlanResponse>();
				for (NimaiSubscriptionDetails temp : subscriptionEntity) {
					SubscriptionPlanResponse responseBean = new SubscriptionPlanResponse();
					if (temp.getStatus().equals("ACTIVE") && temp.getFlag() == 0) {

						responseBean.setSubscriptionAmount(temp.getSubscriptionAmount());
						responseBean.setSubscriptionName(temp.getSubscriptionName());
						responseBean.setSubscriptionId(temp.getSubscriptionId());
						responseBean.setSubscriptionValidity(temp.getSubscriptionValidity());
						responseBean.setLcCount(temp.getlCount());
						responseBean.setRemark(temp.getRemark());
						responseBean.setUserId(temp.getUserid().getUserid());
						responseBean.setStatus(temp.getStatus());
						subscriptionBean.add(responseBean);
						responseBean.setSubsidiaries(temp.getSubsidiaries());
						responseBean.setRelationshipManager(temp.getRelationshipManager());
						responseBean.setCustomerSupport(temp.getCustomerSupport());
						response.setData(subscriptionBean);

						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {
						response.setStatus("Failure");
						response.setErrCode("ASA008");
						response.setErrMessage(ErrorDescription.getDescription("ASA008"));
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}
				}
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> findMSPlanDetails(String userId) {
		GenericResponse response = new GenericResponse<>();
		logger.info("======findMSplanDetails method invoked===========");
		try {
			Optional<NimaiMCustomer> user = userRepository.findById(userId);
			if (user.isPresent()) {
				List<NimaiSubscriptionDetails> sPlanEntity = subscriptionDetailsRepository.findAllByUserId(userId);
				if (!sPlanEntity.isEmpty()) {
					for (NimaiSubscriptionDetails temp : sPlanEntity) {
						if (temp.getStatus().equalsIgnoreCase("Active")) {
							SubscriptionPlanResponse responseBean = new SubscriptionPlanResponse();
							responseBean.setSubscriptionAmount(temp.getSubscriptionAmount());
							responseBean.setSubscriptionName(temp.getSubscriptionName());
							responseBean.setSubscriptionId(temp.getSubscriptionId());
							responseBean.setSubscriptionValidity(temp.getSubscriptionValidity());
							responseBean.setLcCount(temp.getlCount());
							responseBean.setRemark(temp.getRemark());
							responseBean.setUserId(temp.getUserid().getUserid());
							responseBean.setStatus(temp.getStatus());
							responseBean.setSubsidiaries(temp.getSubsidiaries());
							responseBean.setRelationshipManager(temp.getRelationshipManager());
							responseBean.setCustomerSupport(temp.getCustomerSupport());
							response.setData(responseBean);
						} else {
							response.setErrMessage("SubscriptionPlan is not Activated on this userId");
						}
					}
				} else {
					List<SubscriptionPlanResponse> subscriptionBean = new ArrayList<SubscriptionPlanResponse>();
					List<NimaiMSubscription> subscriptionEntity = masterSPlanRepo.findAll();
					for (NimaiMSubscription mSPLan : subscriptionEntity) {
						SubscriptionPlanResponse responseBean = new SubscriptionPlanResponse();
						responseBean.setSubscriptionAmount(mSPLan.getSubscriptionAmount());
						responseBean.setSubscriptionName(mSPLan.getSubscriptionName());
						responseBean.setSubscriptionId(mSPLan.getSubscriptionId());
						responseBean.setSubscriptionValidity(mSPLan.getSubscriptionValidity());
						responseBean.setLcCount(mSPLan.getlCount());
						responseBean.setRemark(mSPLan.getRemark());
						responseBean.setStatus(mSPLan.getStatus());
						responseBean.setSubsidiaries(mSPLan.getSubsidiaries());
						responseBean.setRelationshipManager(mSPLan.getRelationshipManager());
						responseBean.setCustomerSupport(mSPLan.getCustomerSupport());
						subscriptionBean.add(responseBean);
						response.setData(subscriptionBean);

					}

				}

			} else {
				response.setErrMessage("Invalid UserId");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> findCustomerSPlanDetails(SplanRequest sPlanRequest) {
		GenericResponse response = new GenericResponse<>();
		logger.info("======findCustomerSPlanDetails method invoked===========");
		try {
			Optional<NimaiMCustomer> user = userRepository.findById(sPlanRequest.getUserId());
			SPlanResponseBean sPlanResponseBean = new SPlanResponseBean();

			if (user.isPresent()) {
				List<NimaiSubscriptionDetails> sPlanEntity = subscriptionDetailsRepository.findAllByUserId(sPlanRequest.getUserId());

				if (!sPlanEntity.isEmpty()) {
					for (NimaiSubscriptionDetails temp : sPlanEntity) {
						if (temp.getStatus().equalsIgnoreCase("Active")
								|| temp.getStatus().equalsIgnoreCase("Pending")) {

							SubscriptionPlanResponse detailResponse = ModelMapper.mapEntityToEntityResponse(temp);
							response.setData(detailResponse);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							sPlanResponseBean = sPlanMasterlist(user.get());
							if (sPlanResponseBean != null) {
								response.setData(sPlanResponseBean);
								return new ResponseEntity<>(response, HttpStatus.OK);
							} else {
								response.setErrCode("ASA012");
								response.setErrMessage(ErrorDescription.getDescription("ASA012"));
								return new ResponseEntity<>(response, HttpStatus.OK);
							}
						}

					}
				} else {
					sPlanResponseBean = sPlanMasterlist(user.get());
					if (sPlanResponseBean != null) {
						response.setData(sPlanResponseBean);
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						response.setErrCode("ASA012");
						response.setErrMessage(ErrorDescription.getDescription("ASA012"));
						return new ResponseEntity<>(response, HttpStatus.OK);
					}

				}
			} else {
				response.setErrCode("ASA003");
				response.setErrMessage(ErrorDescription.getDescription("ASA003"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

		}

		catch (Exception e) {
			response.setErrMessage("No entity Found");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		response.setErrMessage("No response");
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	public SPlanResponseBean sPlanMasterlist(NimaiMCustomer user) {
		logger.info("======SPlanResponseBean method invoked===========");
		SPlanResponseBean sPlanResponseBean = new SPlanResponseBean();

		String customerType = user.getUserid().substring(0, 2);
		logger.info("CountryName:" + user.getCountryName());
		if (customerType.equalsIgnoreCase("Cu")) {
			String custType = "Customer";
			List<NimaiMSubscription> custSPlanList = masterSPlanRepo.findByCountry(custType, user.getCountryName());
			System.out.println(custSPlanList.toString());
			if (!custSPlanList.isEmpty()) {
				List<customerSPlansResponse> custSubscriptionBean = ModelMapper
						.mapCustSplanListToSBeanRsponse(custSPlanList);
				sPlanResponseBean.setCustomerSplans(custSubscriptionBean);
				return sPlanResponseBean;

			} else {
				return null;
			}

		} else {
			String custType = "Bank";
			List<NimaiMSubscription> banksSPlanList = masterSPlanRepo.findByCountry(custType, user.getCountryName());

			if (!banksSPlanList.isEmpty()) {
				List<banksSplansReponse> banksubscriptionBean = ModelMapper
						.mapBankSplanListToSBeanRsponse(banksSPlanList);
				sPlanResponseBean.setBanksSplans(banksubscriptionBean);
				return sPlanResponseBean;

			} else {
				return null;
			}
		}

	}

}
