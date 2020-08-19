package com.nimai.lc.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.lc.bean.NimaiLCBean;
import com.nimai.lc.bean.NimaiLCMasterBean;
import com.nimai.lc.entity.Countrycurrency;
import com.nimai.lc.entity.NimaiLC;
import com.nimai.lc.entity.NimaiLCMaster;
import com.nimai.lc.repository.CountryRepository;
import com.nimai.lc.repository.CountrycurrencyRepository;
import com.nimai.lc.repository.LCCountryRepository;
import com.nimai.lc.repository.LCGoodsRepository;
import com.nimai.lc.repository.LCMasterRepository;
import com.nimai.lc.repository.LCRepository;

@Service
public class LCServiceImpl implements LCService {

	@Autowired
	LCRepository lcrepo;

	@Autowired
	LCMasterRepository lcmasterrepo;

	@Autowired
	LCCountryRepository lccountryrepo;

	@Autowired
	LCGoodsRepository lcgoodsrepo;

	@Autowired
	CountrycurrencyRepository countryrepo;

	@Autowired
	CountryRepository countryRepo;

	@Autowired
	EntityManagerFactory em;

	@Override
	public void saveLCdetails(NimaiLCBean nimailcbean, String tid) {

		NimaiLC nimailc = new NimaiLC();
		System.out.println("transaction id= " + tid);
		nimailc.setTransactionId(tid);
		nimailc.setUserId(nimailcbean.getUserId());
		nimailc.setRequirementType(nimailcbean.getRequirementType());
		nimailc.setlCIssuanceBank(nimailcbean.getlCIssuanceBank());
		nimailc.setlCIssuanceBranch(nimailcbean.getlCIssuanceBranch());
		nimailc.setSwiftCode(nimailcbean.getSwiftCode());
		nimailc.setlCIssuanceCountry(nimailcbean.getlCIssuanceCountry());
		nimailc.setlCIssuingDate(nimailcbean.getlCIssuingDate());
		nimailc.setlCExpiryDate(nimailcbean.getlCExpiryDate());
		nimailc.setlCValue(nimailcbean.getlCValue());
		nimailc.setlCCurrency(nimailcbean.getlCCurrency());
		nimailc.setLastShipmentDate(nimailcbean.getLastShipmentDate());
		nimailc.setNegotiationDate(nimailcbean.getNegotiationDate());
		nimailc.setPaymentPeriod(nimailcbean.getPaymentPeriod());
		nimailc.setPaymentTerms(nimailcbean.getPaymentTerms());
		nimailc.setTenorEndDate(nimailcbean.getTenorEndDate());
		nimailc.setUserType(nimailcbean.getUserType());
		nimailc.setApplicantName(nimailcbean.getApplicantName());
		nimailc.setApplicantCountry(nimailcbean.getApplicantCountry());
		nimailc.setApplicantContactPerson(nimailcbean.getApplicantContactPerson());
		nimailc.setApplicantContactPersonEmail(nimailcbean.getApplicantContactPersonEmail());
		nimailc.setBeneName(nimailcbean.getBeneName());
		nimailc.setBeneBankCountry(nimailcbean.getBeneBankCountry());
		nimailc.setBeneContactPerson(nimailcbean.getBeneContactPerson());
		nimailc.setBeneContactPersonEmail(nimailcbean.getBeneContactPersonEmail());
		nimailc.setBeneBankName(nimailcbean.getBeneBankName());
		nimailc.setBeneSwiftCode(nimailcbean.getBeneSwiftCode());
		nimailc.setBeneCountry(nimailcbean.getBeneCountry());
		nimailc.setLoadingCountry(nimailcbean.getLoadingCountry());
		nimailc.setLoadingPort(nimailcbean.getLoadingPort());
		nimailc.setDischargeCountry(nimailcbean.getDischargeCountry());
		nimailc.setDischargePort(nimailcbean.getDischargePort());
		nimailc.setChargesType(nimailcbean.getChargesType());
		nimailc.setValidity(nimailcbean.getValidity());
		nimailc.setInsertedDate(nimailcbean.getInsertedDate());
		nimailc.setInsertedBy(nimailcbean.getInsertedBy());
		nimailc.setModifiedDate(nimailcbean.getModifiedDate());
		nimailc.setModifiedBy(nimailcbean.getModifiedBy());
		nimailc.setTransactionflag(nimailcbean.getTransactionFlag());
		nimailc.setTransactionStatus(nimailcbean.getTransactionStatus());
		nimailc.setBranchUserId(nimailcbean.getBranchUserId());
		nimailc.setBranchUserEmail(nimailcbean.getBranchUserEmail());
		nimailc.setGoodsType(nimailcbean.getGoodsType());
		nimailc.setUsanceDays(nimailcbean.getUsanceDays());
		nimailc.setStartDate(nimailcbean.getStartDate());
		nimailc.setEndDate(nimailcbean.getEndDate());
		nimailc.setOriginalTenorDays(nimailcbean.getOriginalTenorDays());
		nimailc.setRefinancingPeriod(nimailcbean.getRefinancingPeriod());
		nimailc.setLcMaturityDate(nimailcbean.getLcMaturityDate());
		nimailc.setLcNumber(nimailcbean.getLcNumber());
		nimailc.setLastBeneBank(nimailcbean.getLastBeneBank());
		nimailc.setLastBeneSwiftCode(nimailcbean.getLastBeneSwiftCode());
		nimailc.setLastBankCountry(nimailcbean.getLastBankCountry());
		nimailc.setRemarks(nimailcbean.getRemarks());
		nimailc.setDiscountingPeriod(nimailcbean.getDiscountingPeriod());
		nimailc.setConfirmationPeriod(nimailcbean.getConfirmationPeriod());
		nimailc.setFinancingPeriod(nimailcbean.getFinancingPeriod());
		nimailc.setLcProForma(nimailcbean.getLcProForma());
		nimailc.setTenorFile(nimailcbean.getTenorFile());

		lcrepo.save(nimailc);

	}

	@Override
	public List<Countrycurrency> getCountry() {
		List<Countrycurrency> list = (List<Countrycurrency>) countryrepo.findAll();
		System.out.println(list);
		return list;
	}

	@Override
	public List<NimaiLC> getAllDraftTransactionDetails(String userId, String branchEmailId) {
		// TODO Auto-generated method stub
		if (userId.substring(0, 2).equalsIgnoreCase("BC") == true) {
			if (branchEmailId.equals(lcmasterrepo.getEmailAddress(userId))) {
				System.out.println("Bank as a customer - Master");
				return lcrepo.findAllDraftTransactionByUserId(userId);
			} else {
				return lcrepo.findAllDraftTransactionByUserIdAndBranchEmailId(userId, branchEmailId);
			}
		} else {
			return lcrepo.findAllDraftTransactionByUserId(userId);
		}
	}

	@Override
	public List<NimaiLCMaster> getAllTransactionDetails() {
		List<NimaiLCMaster> allTransactionList = lcmasterrepo.findAllTransaction();

		return allTransactionList;
	}

	@Override
	public NimaiLCMaster getSpecificTransactionDetail(String transactionId) {
		// TODO Auto-generated method stub
		return lcmasterrepo.findSpecificTransactionById(transactionId);
	}

	@Override
	public NimaiLC getSpecificDraftTransactionDetail(String transactionId) {
		// TODO Auto-generated method stub
		return lcrepo.findSpecificDraftTransaction(transactionId);
	}

	@Override
	public List<NimaiLCMaster> getAllTransactionDetailsByStatus(String status) {
		// TODO Auto-generated method stub
		return lcmasterrepo.findAllTransactionByStatus(status);
		// return lcmasterrepo.findAllActiveTransaction();
	}

	@Override
	public List<NimaiLCMaster> getTransactionDetailByUserId(String userId, String branchEmailId) {
		// TODO Auto-generated method stub
		if (userId.substring(0, 2).equalsIgnoreCase("BC") == true) {
			if (branchEmailId.equals(lcmasterrepo.getEmailAddress(userId))) {
				System.out.println("Bank as a customer");
				return lcmasterrepo.findByTransactionByUserId(userId);
			} else {
				return lcmasterrepo.findByTransactionByUserIdAndBranchEmail(userId, branchEmailId);
			}
		} else {
			return lcmasterrepo.findByTransactionByUserId(userId);
		}
	}

	@Override
	public List<NimaiLCMaster> getTransactionDetailByUserIdAndStatus(String userId, String status,
			String branchEmailId) {
		// TODO Auto-generated method stub
		if (userId.substring(0, 2).equalsIgnoreCase("BC") == true) {
			if (branchEmailId.equals(lcmasterrepo.getEmailAddress(userId))) {
				System.out.println("Bank as a customer");
				return lcmasterrepo.findByTransactionByUserIdAndStatus(userId, status);
			} else {
				return lcmasterrepo.findByTransactionByUserIdStatusBranchEmail(userId, status, branchEmailId);
			}
		} else {
			return lcmasterrepo.findByTransactionByUserIdAndStatus(userId, status);
		}
	}

	@Override
	public List<NimaiLCMaster> getAllTransactionForBank(String userid) {
		// TODO Auto-generated method stub
		EntityManager entityManager = em.createEntityManager();
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("get_transaction_for_bank", NimaiLCMaster.class);
			storedProcedureQuery.registerStoredProcedureParameter("user_id", String.class, ParameterMode.IN);
			storedProcedureQuery.setParameter("user_id", userid);
			storedProcedureQuery.execute();
			List<NimaiLCMaster> list = storedProcedureQuery.getResultList();
			return list;
		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			entityManager.close();

		}
		return null;
	}

	@Override
	public String generateSerialNo() {
		// TODO Auto-generated method stub

		/*
		 * Random randomNo=new Random(System.currentTimeMillis()); StringBuilder
		 * builder=new StringBuilder(); int length=6; for(int i=1;i<=length;i++) { int
		 * digit=randomNo.nextInt(6); builder.append(digit); } String
		 * no=builder.toString(); return no;
		 */

		Random rand = new Random();
		int ranInt = rand.nextInt(100000) + 10000;

		return String.valueOf(ranInt);
	}

	@Override
	public String generateYear() {
		// TODO Auto-generated method stub
		DateFormat df = new SimpleDateFormat("YY");
		StringBuilder yearbuilder = new StringBuilder();
		yearbuilder.append(
				Calendar.getInstance().get(Calendar.DATE) < 10 ? ("0" + (Calendar.getInstance().get(Calendar.DATE)))
						: (Calendar.getInstance().get(Calendar.DATE)));
		yearbuilder.append((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10
				? ("0" + (Calendar.getInstance().get(Calendar.MONTH) + 1))
				: (Calendar.getInstance().get(Calendar.MONTH) + 1));
		yearbuilder.append(df.format(Calendar.getInstance().getTime()));
		String year = yearbuilder.toString();
		return year;
	}

	@Override
	public String generateCountryCode(String userid) {
		// TODO Auto-generated method stub
		return countryRepo.getCountryCode(userid);
	}

	@Override
	public String generateSubscriberType(String userid) {
		// TODO Auto-generated method stub
		return lcrepo.getSubscriberType(userid);
	}

	@Override
	public String generateTransactionType(String transType) {
		// TODO Auto-generated method stub
		String str = "";
		switch (transType) {
		case "Confirmation":
			str = "CONF";
			break;
		case "ConfirmAndDiscount":
			str = "CODI";
			break;
		case "Discounting":
			str = "DISC";
			break;
		case "Refinance":
			str = "REFI";
			break;
		case "Refinancing":
			str = "REFI";
			break;
		case "Banker":
			str = "BAAC";
			break;
		}
		return str;
	}

	@Override
	public String confirmLCDet(String transId, String userId) {
		// TODO Auto-generated method stub
		// lcrepo.insertIntoMaster(transId, userId);
		EntityManager entityManager = em.createEntityManager();
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("move_to_master",
					NimaiLC.class);

			storedProcedureQuery.registerStoredProcedureParameter("inp_transaction_id", String.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("inp_userid", String.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("validation_message", String.class,
					ParameterMode.OUT);
			storedProcedureQuery.setParameter("inp_transaction_id", transId);
			storedProcedureQuery.setParameter("inp_userid", userId);

			storedProcedureQuery.execute();
			String message = (String) storedProcedureQuery.getOutputParameterValue("validation_message");
			System.out.println("Status: " + message);
			return message;
		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			entityManager.close();

		}
		return null;
	}

	@Override
	public void cloneLCDetail(String oldTransId, String newTransId) {
		// TODO Auto-generated method stub
		EntityManager entityManager = em.createEntityManager();
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("clone_transaction",
					NimaiLC.class);

			storedProcedureQuery.registerStoredProcedureParameter("inp_transaction_id", String.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("updated_transaction_id", String.class,
					ParameterMode.IN);
			storedProcedureQuery.setParameter("inp_transaction_id", oldTransId);
			storedProcedureQuery.setParameter("updated_transaction_id", newTransId);

			storedProcedureQuery.execute();
		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			entityManager.close();

		}
	}

	@Override
	public NimaiLCMaster checkTransaction(String transId) {
		// TODO Auto-generated method stub
		return lcmasterrepo.findSpecificTransactionById(transId);
	}

	@Override
	public NimaiLC findByTransactionIdToConfirm(String transId) {
		// TODO Auto-generated method stub
		return lcrepo.findTransactionIdToConfirm(transId);
	}

	@Override
	public NimaiLC findByTransactionUserIdToConfirm(String transId, String userId) {
		// TODO Auto-generated method stub
		return lcrepo.findTransactionUserIdToConfirm(transId, userId);
	}

	@Override
	public void moveToHistory(String transId, String userId) {
		// TODO Auto-generated method stub
		// lcrepo.insertIntoMaster(transId, userId);
		EntityManager entityManager = em.createEntityManager();
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("move_to_historytbl",
					NimaiLCMaster.class);

			storedProcedureQuery.registerStoredProcedureParameter("inp_transaction_id", String.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("inp_userid", String.class, ParameterMode.IN);
			storedProcedureQuery.setParameter("inp_transaction_id", transId);
			storedProcedureQuery.setParameter("inp_userid", userId);

			storedProcedureQuery.execute();
		} catch (Exception e) {
			// TODO: handle exception

		} finally {
			entityManager.close();

		}
	}

	@Override
	public void saveLCMasterdetails(NimaiLCMasterBean nimailcbean, String tid) {

		NimaiLCMaster nimailc = new NimaiLCMaster();
		System.out.println("transaction id= " + tid);
		nimailc.setTransactionId(tid);
		nimailc.setUserId(nimailcbean.getUserId());
		nimailc.setRequirementType(nimailcbean.getRequirementType());
		nimailc.setlCIssuanceBank(nimailcbean.getlCIssuanceBank());
		nimailc.setlCIssuanceBranch(nimailcbean.getlCIssuanceBranch());
		nimailc.setSwiftCode(nimailcbean.getSwiftCode());
		nimailc.setlCIssuanceCountry(nimailcbean.getlCIssuanceCountry());
		nimailc.setlCIssuingDate(nimailcbean.getlCIssuingDate());
		nimailc.setlCExpiryDate(nimailcbean.getlCExpiryDate());
		nimailc.setlCValue(nimailcbean.getlCValue());
		nimailc.setlCCurrency(nimailcbean.getlCCurrency());
		nimailc.setLastShipmentDate(nimailcbean.getLastShipmentDate());
		nimailc.setNegotiationDate(nimailcbean.getNegotiationDate());
		nimailc.setPaymentPeriod(nimailcbean.getPaymentPeriod());
		nimailc.setPaymentTerms(nimailcbean.getPaymentTerms());
		nimailc.setTenorEndDate(nimailcbean.getTenorEndDate());
		nimailc.setUserType(nimailcbean.getUserType());
		nimailc.setApplicantName(nimailcbean.getApplicantName());
		nimailc.setApplicantCountry(nimailcbean.getApplicantCountry());
		nimailc.setApplicantContactPerson(nimailcbean.getApplicantContactPerson());
		nimailc.setApplicantContactPersonEmail(nimailcbean.getApplicantContactPersonEmail());
		nimailc.setBeneName(nimailcbean.getBeneName());
		nimailc.setBeneBankCountry(nimailcbean.getBeneBankCountry());
		nimailc.setBeneContactPerson(nimailcbean.getBeneContactPerson());
		nimailc.setBeneContactPersonEmail(nimailcbean.getBeneContactPersonEmail());
		nimailc.setBeneBankName(nimailcbean.getBeneBankName());
		nimailc.setBeneSwiftCode(nimailcbean.getBeneSwiftCode());
		nimailc.setBeneCountry(nimailcbean.getBeneCountry());
		nimailc.setLoadingCountry(nimailcbean.getLoadingCountry());
		nimailc.setLoadingPort(nimailcbean.getLoadingPort());
		nimailc.setDischargeCountry(nimailcbean.getDischargeCountry());
		nimailc.setDischargePort(nimailcbean.getDischargePort());
		nimailc.setChargesType(nimailcbean.getChargesType());
		nimailc.setValidity(nimailcbean.getValidity());
		Date now = new Date();
		nimailc.setInsertedDate(now);
		nimailc.setInsertedBy(nimailcbean.getInsertedBy());
		nimailc.setModifiedDate(nimailcbean.getModifiedDate());
		nimailc.setModifiedBy(nimailcbean.getModifiedBy());
		nimailc.setTransactionflag(nimailcbean.getTransactionFlag());
		nimailc.setTransactionStatus(nimailcbean.getTransactionStatus());
		nimailc.setBranchUserId(nimailcbean.getBranchUserId());
		nimailc.setBranchUserEmail(nimailcbean.getBranchUserEmail());
		nimailc.setGoodsType(nimailcbean.getGoodsType());
		nimailc.setUsanceDays(nimailcbean.getUsanceDays());
		nimailc.setStartDate(nimailcbean.getStartDate());
		nimailc.setEndDate(nimailcbean.getEndDate());
		nimailc.setOriginalTenorDays(nimailcbean.getOriginalTenorDays());
		nimailc.setRefinancingPeriod(nimailcbean.getRefinancingPeriod());
		nimailc.setLcMaturityDate(nimailcbean.getLcMaturityDate());
		nimailc.setLcNumber(nimailcbean.getLcNumber());
		nimailc.setLastBeneBank(nimailcbean.getLastBeneBank());
		nimailc.setLastBeneSwiftCode(nimailcbean.getLastBeneSwiftCode());
		nimailc.setLastBankCountry(nimailcbean.getLastBankCountry());
		nimailc.setRemarks(nimailcbean.getRemarks());
		nimailc.setDiscountingPeriod(nimailcbean.getDiscountingPeriod());
		nimailc.setConfirmationPeriod(nimailcbean.getConfirmationPeriod());
		nimailc.setFinancingPeriod(nimailcbean.getFinancingPeriod());
		nimailc.setLcProForma(nimailcbean.getLcProForma());
		nimailc.setTenorFile(nimailcbean.getTenorFile());
		nimailc.setQuotationReceived(nimailcbean.getQuotationReceived());

		lcmasterrepo.save(nimailc);

	}

	@Override
	public void updateDraftLCdetails(NimaiLCBean nimailcbean) {
		String tid = nimailcbean.getTransactionId();
		NimaiLC nimailc = lcrepo.getOne(tid);
		System.out.println("transaction id= " + tid);

		nimailc.setUserId(nimailcbean.getUserId());
		nimailc.setRequirementType(nimailcbean.getRequirementType());
		nimailc.setlCIssuanceBank(nimailcbean.getlCIssuanceBank());
		nimailc.setlCIssuanceBranch(nimailcbean.getlCIssuanceBranch());
		nimailc.setSwiftCode(nimailcbean.getSwiftCode());
		nimailc.setlCIssuanceCountry(nimailcbean.getlCIssuanceCountry());
		nimailc.setlCIssuingDate(nimailcbean.getlCIssuingDate());
		nimailc.setlCExpiryDate(nimailcbean.getlCExpiryDate());
		nimailc.setlCValue(nimailcbean.getlCValue());
		nimailc.setlCCurrency(nimailcbean.getlCCurrency());
		nimailc.setLastShipmentDate(nimailcbean.getLastShipmentDate());
		nimailc.setNegotiationDate(nimailcbean.getNegotiationDate());
		nimailc.setPaymentPeriod(nimailcbean.getPaymentPeriod());
		nimailc.setPaymentTerms(nimailcbean.getPaymentTerms());
		nimailc.setTenorEndDate(nimailcbean.getTenorEndDate());
		nimailc.setUserType(nimailcbean.getUserType());
		nimailc.setApplicantName(nimailcbean.getApplicantName());
		nimailc.setApplicantCountry(nimailcbean.getApplicantCountry());
		nimailc.setApplicantContactPerson(nimailcbean.getApplicantContactPerson());
		nimailc.setApplicantContactPersonEmail(nimailcbean.getApplicantContactPersonEmail());
		nimailc.setBeneName(nimailcbean.getBeneName());
		nimailc.setBeneBankCountry(nimailcbean.getBeneBankCountry());
		nimailc.setBeneContactPerson(nimailcbean.getBeneContactPerson());
		nimailc.setBeneContactPersonEmail(nimailcbean.getBeneContactPersonEmail());
		nimailc.setBeneBankName(nimailcbean.getBeneBankName());
		nimailc.setBeneSwiftCode(nimailcbean.getBeneSwiftCode());
		nimailc.setBeneCountry(nimailcbean.getBeneCountry());
		nimailc.setLoadingCountry(nimailcbean.getLoadingCountry());
		nimailc.setLoadingPort(nimailcbean.getLoadingPort());
		nimailc.setDischargeCountry(nimailcbean.getDischargeCountry());
		nimailc.setDischargePort(nimailcbean.getDischargePort());
		nimailc.setChargesType(nimailcbean.getChargesType());
		nimailc.setValidity(nimailcbean.getValidity());
		Date now = new Date();
		nimailc.setInsertedDate(nimailcbean.getInsertedDate());
		nimailc.setInsertedBy(nimailcbean.getInsertedBy());
		nimailc.setModifiedDate(now);
		nimailc.setModifiedBy(nimailcbean.getModifiedBy());
		nimailc.setTransactionflag(nimailcbean.getTransactionFlag());
		nimailc.setTransactionStatus(nimailcbean.getTransactionStatus());
		nimailc.setBranchUserId(nimailcbean.getBranchUserId());
		nimailc.setBranchUserEmail(nimailcbean.getBranchUserEmail());
		nimailc.setGoodsType(nimailcbean.getGoodsType());
		nimailc.setUsanceDays(nimailcbean.getUsanceDays());
		nimailc.setStartDate(nimailcbean.getStartDate());
		nimailc.setEndDate(nimailcbean.getEndDate());
		nimailc.setOriginalTenorDays(nimailcbean.getOriginalTenorDays());
		nimailc.setRefinancingPeriod(nimailcbean.getRefinancingPeriod());
		nimailc.setLcMaturityDate(nimailcbean.getLcMaturityDate());
		nimailc.setLcNumber(nimailcbean.getLcNumber());
		nimailc.setLastBeneBank(nimailcbean.getLastBeneBank());
		nimailc.setLastBeneSwiftCode(nimailcbean.getLastBeneSwiftCode());
		nimailc.setLastBankCountry(nimailcbean.getLastBankCountry());
		nimailc.setRemarks(nimailcbean.getRemarks());
		nimailc.setDiscountingPeriod(nimailcbean.getDiscountingPeriod());
		nimailc.setConfirmationPeriod(nimailcbean.getConfirmationPeriod());
		nimailc.setFinancingPeriod(nimailcbean.getFinancingPeriod());
		nimailc.setLcProForma(nimailcbean.getLcProForma());
		nimailc.setTenorFile(nimailcbean.getTenorFile());

		lcrepo.save(nimailc);

	}

	@Override
	public Integer getLcCount(String userId) {
		// TODO Auto-generated method stub
		Integer lccount;
		try {
			lccount = lcrepo.findLCCount(userId);
		} catch (NullPointerException ne) {
			lccount = 0;
		}
		return lccount;
	}

	@Override
	public Integer getUtilizedLcCount(String userId) {
		// TODO Auto-generated method stub
		Integer lcutilized;
		try {
			lcutilized = lcrepo.findUtilzedLCCount(userId);
		} catch (NullPointerException ne) {
			lcutilized = 0;
		}
		return lcutilized;
	}

	@Override
	public NimaiLCMaster getTransactionForAcceptCheck(String transId) {
		// TODO Auto-generated method stub
		return lcmasterrepo.getTransactionByTransIdTrStatusAndQuoteStatus(transId);
	}

	@Override
	public void updateTransactionStatusToActive(String transactionId, String userId) {
		// TODO Auto-generated method stub
		lcmasterrepo.updateTransactionStatusToActive(transactionId, userId);
	}

}
