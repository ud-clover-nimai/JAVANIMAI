package com.nimai.splan.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimai.splan.model.NimaiMCustomer;
import com.nimai.splan.model.NimaiMSubscription;
import com.nimai.splan.model.NimaiSubscriptionDetails;
import com.nimai.splan.model.NimaiTempSubscription;
import com.nimai.splan.payload.GenericResponse;
import com.nimai.splan.payload.SPlanApprovalBean;
import com.nimai.splan.payload.SPlanCustResponseBean;
import com.nimai.splan.payload.SubscriptionBean;
import com.nimai.splan.payload.SubscriptionPlanResponse;
import com.nimai.splan.repository.NimaiMCustomerRepository;
import com.nimai.splan.repository.NimaiMSPlanRepository;
import com.nimai.splan.repository.NimaiMSubscriptionRepository;
import com.nimai.splan.repository.SubscriptionPlanRepository;
import com.nimai.splan.utility.ErrorDescription;
import com.nimai.splan.utility.SPlanUniqueNumber;
import com.nimai.splan.utility.SubscriptionPlanValidation;

@Service
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionPlanServiceImpl.class);

	@Autowired
	SubscriptionPlanValidation validationDao;

	@Autowired
	NimaiMSubscriptionRepository sPlanRepository;

	@Autowired
	NimaiMCustomerRepository userRepository;

	@Autowired
	SubscriptionPlanRepository subscriptionDetailsRepository;

	@Autowired
	EntityManagerFactory em;

	@Autowired
	NimaiMSPlanRepository masterSPlanRepo;

	@Override
	public ResponseEntity<Object> saveOrUpdateSubscriptionPlanDetails(SubscriptionBean subscriptionRequest) {
		GenericResponse response = new GenericResponse();

		LOGGER.info("saveOrUpdateSubscriptionPlanDetails is been called");
		LOGGER.info("User Id" + subscriptionRequest.getUserId() + " ,Subscription Id "
				+ subscriptionRequest.getSubscriptionId() + " ,Subscription Name "
				+ subscriptionRequest.getSubscriptionName() + ",Subscription Amount "
				+ subscriptionRequest.getSubscriptionAmount() + " ,Subscription Validity "
				+ subscriptionRequest.getSubscriptionValidity() + " ,LC count" + subscriptionRequest.getLcCount()
				+ " ,Status " + subscriptionRequest.getStatus() + " ,Subsidiaries "
				+ subscriptionRequest.getSubsidiaries() + " ,RelationshipManager "
				+ subscriptionRequest.getRelationshipManager() + " ,CustomerSupport "
				+ subscriptionRequest.getCustomerSupport()

		);
		// NimaiMSubscription subScription=null;
		String errorString = this.validationDao.subscriptionPlanValidation(subscriptionRequest);
		if (errorString.equalsIgnoreCase("success")) {
			try {
				NimaiTempSubscription subScriptionDetails = null;
				if (subscriptionRequest.getSubscriptionId() != null) {
					Calendar cal = Calendar.getInstance();
					Date modifiedDate = cal.getTime();
					subScriptionDetails = sPlanRepository.findBySubscriptionID(subscriptionRequest.getSubscriptionId());
					if (subScriptionDetails != null) {
						subScriptionDetails.setModifiedDate(modifiedDate);
						subScriptionDetails.setModifiedBy("Admin");
						response.setErrCode("ASA007");
						response.setErrMessage(ErrorDescription.getDescription("ASA007"));
						LOGGER.info(ErrorDescription.getDescription("ASA007"));

					} else {
						response.setErrCode("ASA010");
						response.setErrMessage(ErrorDescription.getDescription("ASA010"));
						LOGGER.error("An Exception has occured " + ErrorDescription.getDescription("ASA010"));
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}
				} else {
					subScriptionDetails = new NimaiTempSubscription();
					SPlanUniqueNumber subscriptionId = new SPlanUniqueNumber();
					subScriptionDetails.setSubscriptionId(subscriptionId.uniqueNumber());
					Calendar cal = Calendar.getInstance();
					Date insertedDate = cal.getTime();
					subScriptionDetails.setInsertedDate(insertedDate);
					subScriptionDetails.setInsertedBy("Admin");
					response.setErrCode("ASA001");
					LOGGER.info(ErrorDescription.getDescription("ASA001") + " " + subscriptionRequest);
					response.setErrMessage(ErrorDescription.getDescription("ASA001"));
				}
				subScriptionDetails.setSubscriptionName(subscriptionRequest.getSubscriptionName());
				subScriptionDetails.setSubscriptionValidity(subscriptionRequest.getSubscriptionValidity());
				subScriptionDetails.setRemark(subscriptionRequest.getRemark());
				subScriptionDetails.setCustomerSupport(subscriptionRequest.getCustomerSupport());
				subScriptionDetails.setRelationshipManager(subscriptionRequest.getRelationshipManager());
				subScriptionDetails.setSubsidiaries(subscriptionRequest.getSubsidiaries());
				subScriptionDetails.setSubscriptionAmount(subscriptionRequest.getSubscriptionAmount());
				subScriptionDetails.setlCount(subscriptionRequest.getLcCount());
				subScriptionDetails.setStatus("INACTIVE");
				NimaiTempSubscription subScription = sPlanRepository.save(subScriptionDetails);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				LOGGER.error("An Exception has Occured " + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {

			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(errorString);
			LOGGER.error("An Exception has Occured " + errorString);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> findAllSPlanDetails() {
		LOGGER.info("findAllSPlanDetails is been called");

		GenericResponse response = new GenericResponse<>();
		try {
			List<NimaiTempSubscription> subscriptionEntity = sPlanRepository.findAll();
			if (subscriptionEntity.isEmpty()) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			} else {
				List<SPlanCustResponseBean> subscriptionBean = new ArrayList<SPlanCustResponseBean>();
				for (NimaiTempSubscription temp : subscriptionEntity) {
					if (temp.getStatus().equalsIgnoreCase("Active")) {
						response.setErrMessage("No subscription Plan for Approval");
					} else {
						SPlanCustResponseBean responseBean = new SPlanCustResponseBean();
						responseBean.setSubscriptionAmount(temp.getSubscriptionAmount());
						responseBean.setSubscriptionName(temp.getSubscriptionName());
						responseBean.setSubscriptionId(temp.getSubscriptionId());
						responseBean.setSubscriptionValidity(temp.getSubscriptionValidity());
						responseBean.setCustomerSupport(temp.getCustomerSupport());
						responseBean.setRelationshipManager(temp.getRelationshipManager());
						responseBean.setLcCount(temp.getlCount());
						responseBean.setSubsidiaries(temp.getSubsidiaries());
						subscriptionBean.add(responseBean);
						response.setData(subscriptionBean);
					}

				}
				LOGGER.info("SPlan Found " + subscriptionBean);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			LOGGER.error("An Exception has occured " + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> saveUserSubscriptionPlan(SubscriptionBean subscriptionRequest, String userId) {

		LOGGER.info("saveUserSubscriptionPlan is been Called");
		LOGGER.info(" User Id " + userId + " ,Subscription Id " + subscriptionRequest.getSubscriptionId()
				+ " ,Subscription Name " + subscriptionRequest.getSubscriptionName() + " ,Subscription Validity "
				+ subscriptionRequest.getSubscriptionValidity() + " ,Remark " + subscriptionRequest.getRemark()
				+ " ,Subscription Amount " + subscriptionRequest.getSubscriptionAmount() + " ,Lc Count "
				+ subscriptionRequest.getLcCount() + " ,Subsidiaries " + subscriptionRequest.getSubsidiaries()
				+ " ,Relationship Manager " + subscriptionRequest.getRelationshipManager() + " ,Inserted By "
				+ subscriptionRequest.getInsertedBy()

		);

		GenericResponse response = new GenericResponse<>();
		try {
			if (subscriptionRequest.getSubscriptionId() != null) {
				Optional<NimaiMCustomer> mCustomer = userRepository.findByUserId(userId);
				NimaiMCustomer nc = userRepository.getOne(userId);
				System.out.println(nc);
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
					SPlanUniqueNumber endDate = new SPlanUniqueNumber();
					int year = endDate.getNoOfyears(subScriptionDetails.getSubscriptionValidity());
					int month = endDate.getNoOfMonths(subScriptionDetails.getSubscriptionValidity());
					System.out.println(year);
					System.out.println(month);
					subScriptionDetails.setStatus("ACTIVE");
					Calendar cal = Calendar.getInstance();
					Date today = cal.getTime();
					cal.add(Calendar.YEAR, year);
					cal.add(Calendar.MONTH, month);
					Date sPlanEndDate = cal.getTime();
					subScriptionDetails.setSubscriptionStartDate(today);
					subScriptionDetails.setSubscriptionEndDate(sPlanEndDate);

					// Chnages by Shubham Patil
					nc.setIsSPlanPurchased(true);
					nc.setPaymentStatus("PENDING");
					nc.setPaymentDate(null);
					nc.setModeOfPayment(null);
					// Changes ends

					NimaiSubscriptionDetails subScription = subscriptionDetailsRepository.save(subScriptionDetails);
					response.setErrCode("ASA001");
					response.setErrMessage(ErrorDescription.getDescription("ASA001"));
					LOGGER.info(ErrorDescription.getDescription("ASA001"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} else {
					response.setStatus("Failure");
					response.setErrCode("ASA003");
					response.setErrMessage(ErrorDescription.getDescription("ASA003"));
					LOGGER.error("An Exception has been Occured " + ErrorDescription.getDescription("ASA003"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setStatus("Failure");
				response.setErrCode("ASA009");
				response.setErrMessage(ErrorDescription.getDescription("ASA009"));
				LOGGER.error("An Exception has been Occured " + ErrorDescription.getDescription("ASA009"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			LOGGER.error("An Exception has been Occured " + ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> findSPlanDetailsByUserId(String userId) {
		LOGGER.info("findSPlanDetailsByUserId is been called");
		LOGGER.info("User Id " + userId);
		GenericResponse response = new GenericResponse<>();
		try {
			List<NimaiSubscriptionDetails> subscriptionEntity = subscriptionDetailsRepository.findAllByUserId(userId);

			if (subscriptionEntity.isEmpty()) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				LOGGER.error("An exception has occured " + ErrorDescription.getDescription("ASA002"));
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
				LOGGER.info("");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			LOGGER.error("An Exception has Occured " + ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> deleteBySubscriptionId(String subscriptionId) {

		LOGGER.info("deleteBySubscriptionId is been callled");

		GenericResponse response = new GenericResponse<>();
		try {
			Optional<NimaiTempSubscription> subscriptionPlan = sPlanRepository.findById(subscriptionId);
			if (subscriptionPlan.isPresent()) {
				sPlanRepository.delete(subscriptionPlan.get());
				response.setErrCode("ASA005");
				response.setErrMessage(ErrorDescription.getDescription("ASA005"));
				LOGGER.info("SuccessFully Deleted " + ErrorDescription.getDescription("ASA005"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setErrCode("ASA006");
				response.setErrMessage(ErrorDescription.getDescription("ASA006"));
				LOGGER.error("An Exception has occured " + ErrorDescription.getDescription("ASA006"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			LOGGER.error("An Exception has occured " + ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	private Date getDate(String pattern) throws ParseException {
		LOGGER.info("getDate is been called");
		return new SimpleDateFormat(pattern).parse(new SimpleDateFormat(pattern).format(new Date()));
	}

	@Override
	public ResponseEntity<?> getSPlanByUserId(String userId) {
		LOGGER.info("getSPlanByUserId is been called");
		GenericResponse response = new GenericResponse<>();
		try {
			List<NimaiSubscriptionDetails> subscriptionEntity = subscriptionDetailsRepository.findAllByUserId(userId);
			if (subscriptionEntity.isEmpty()) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				LOGGER.error("An Exception has Occured " + ErrorDescription.getDescription("ASA002"));
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

						LOGGER.info("SPlan found for user " + response);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {
						response.setStatus("Failure");
						response.setErrCode("ASA008");
						response.setErrMessage(ErrorDescription.getDescription("ASA008"));
						LOGGER.warn(ErrorDescription.getDescription("ASA008"));
						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}
				}
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			LOGGER.error("An Exception has Occured " + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> saveSplanDetails(SPlanApprovalBean sPlanBean) {
		LOGGER.info("saveSplanDetails is been called");
		GenericResponse response = new GenericResponse<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(sPlanBean);
			System.out.println(jsonString);
			JSONParser parser = new JSONParser(jsonString);
			JSONObject json = (JSONObject) parser.parse();
			String approvedBy = (String) json.get("approvedBy");
			JSONArray array = (JSONArray) json.get("approvalDetails");
			StringBuffer subscriptionIdList = new StringBuffer();
			StringBuffer statusList = new StringBuffer();
			for (Object o : array) {
				JSONObject innerJson = (JSONObject) o;
				String subscriptionId = (String) innerJson.get("subscriptionId");
				String status = (String) innerJson.get("status");
				if (status.equalsIgnoreCase("Inactive")) {
					System.out.println("update inactive cases");
					NimaiTempSubscription temp = sPlanRepository.getOne(subscriptionId);
					temp.setStatus("Rejected");
					sPlanRepository.save(temp);

				} else {
					subscriptionIdList.append(subscriptionId).append(",");

				}

			}
			subscriptionIdList.replace(subscriptionIdList.length() - 1, subscriptionIdList.length(), "");

			System.out.println("procedure inut subscriptionId" + subscriptionIdList);
			System.out.println("procedure input status" + statusList);

			String sId = subscriptionIdList.toString();
			String status = "Active";
			System.out.println("Input parameter for the procedure++++++++++++==========+++++++" + sId);

			StoredProcedureQuery cnfrmSplanDetails = em.createEntityManager()
					.createStoredProcedureQuery("insert_into_m_subscriptionplan", NimaiTempSubscription.class);
			cnfrmSplanDetails.registerStoredProcedureParameter("inp_subscription_id_list", String.class,
					ParameterMode.IN);

			cnfrmSplanDetails.registerStoredProcedureParameter("inp_approval_By", String.class, ParameterMode.IN);
			cnfrmSplanDetails.registerStoredProcedureParameter("inp_status", String.class, ParameterMode.IN);
			cnfrmSplanDetails.setParameter("inp_subscription_id_list", sId);
			cnfrmSplanDetails.setParameter("inp_approval_By", approvedBy);
			cnfrmSplanDetails.setParameter("inp_status", status);

			cnfrmSplanDetails.execute();
			LOGGER.info("SPlan Details Saved Successfully");
			response.setErrMessage("success");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrMessage("SubscriptionId is not proper");
			LOGGER.warn("Please Provide Proper Subscription Id ");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> findMSPlanDetails(String userId) {
		LOGGER.info("findMSPlanDetails is been called");
		LOGGER.info("User Id " + userId);
		GenericResponse response = new GenericResponse<>();
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
							LOGGER.warn("SPlan is not active for user with id " + userId);
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
				LOGGER.error("Invalid User Id Details");
				response.setErrMessage("Invalid UserId");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			LOGGER.error(ErrorDescription.getDescription("EXE000") + e.getMessage());
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> getAllMasterSPlanDetails() {
		LOGGER.info("getAllMasterSPlanDetails is been called");
		GenericResponse response = new GenericResponse<>();
		try {
			List<NimaiMSubscription> subscriptionEntity = masterSPlanRepo.findAll();
			if (subscriptionEntity.isEmpty()) {
				response.setStatus("Failure");
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				LOGGER.warn(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			} else {
				List<SPlanCustResponseBean> subscriptionBean = new ArrayList<SPlanCustResponseBean>();
				for (NimaiMSubscription temp : subscriptionEntity) {
						SPlanCustResponseBean responseBean = new SPlanCustResponseBean();
						responseBean.setSubscriptionAmount(temp.getSubscriptionAmount());
						responseBean.setSubscriptionName(temp.getSubscriptionName());
						responseBean.setSubscriptionId(temp.getSubscriptionId());
						responseBean.setSubscriptionValidity(temp.getSubscriptionValidity());
						responseBean.setCustomerSupport(temp.getCustomerSupport());
						responseBean.setRelationshipManager(temp.getRelationshipManager());
						responseBean.setLcCount(temp.getlCount());
						responseBean.setSubsidiaries(temp.getSubsidiaries());
						subscriptionBean.add(responseBean);
						response.setData(subscriptionBean);
					

				}
				LOGGER.info("SPlan details found" + subscriptionBean);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			LOGGER.error("An Exception has Occured " + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public ResponseEntity<?> findMSPlanDetails() {
		// TODO Auto-generated method stub
		return null;
	}
}
