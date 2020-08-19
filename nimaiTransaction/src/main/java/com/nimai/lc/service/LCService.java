package com.nimai.lc.service;

import java.util.List;

import com.nimai.lc.bean.NimaiLCBean;
import com.nimai.lc.bean.NimaiLCMasterBean;
import com.nimai.lc.entity.Countrycurrency;
import com.nimai.lc.entity.NimaiLC;
import com.nimai.lc.entity.NimaiLCMaster;

public interface LCService {

	public void saveLCdetails(NimaiLCBean nimailcbean, String tid);

	public List<Countrycurrency> getCountry();

	public List<NimaiLCMaster> getAllTransactionDetails();

	public List<NimaiLCMaster> getAllTransactionForBank(String userid);

	public NimaiLCMaster getSpecificTransactionDetail(String transactionId);

	public List<NimaiLCMaster> getAllTransactionDetailsByStatus(String status);

	public List<NimaiLCMaster> getTransactionDetailByUserId(String userId, String branchEmailId);

	public List<NimaiLCMaster> getTransactionDetailByUserIdAndStatus(String userId, String status,
			String branchEmailId);

	public String generateSerialNo();

	public String generateYear();

	public String generateCountryCode(String userid);

	public String generateSubscriberType(String userid);

	public String generateTransactionType(String userid);

	public String confirmLCDet(String transId, String userId);

	public void cloneLCDetail(String oldTransId, String newTransId);

	public NimaiLCMaster checkTransaction(String transId);

	public NimaiLC findByTransactionIdToConfirm(String transId);

	public NimaiLC findByTransactionUserIdToConfirm(String transId, String userId);

	public void moveToHistory(String transId, String userId);

	public void saveLCMasterdetails(NimaiLCMasterBean nimailcbean, String tid);

	public List<NimaiLC> getAllDraftTransactionDetails(String userId, String branchEmailId);

	public void updateDraftLCdetails(NimaiLCBean nimailcbean);

	public NimaiLC getSpecificDraftTransactionDetail(String transactionId);

	public Integer getLcCount(String userId);

	public Integer getUtilizedLcCount(String userId);

	public NimaiLCMaster getTransactionForAcceptCheck(String transId);

	public void updateTransactionStatusToActive(String transactionId, String userId);

}
