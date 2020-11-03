package com.nimai.email.dao;

import java.util.Date;
import java.util.List;

import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailScheduler;
import com.nimai.email.entity.NimaiEmailSchedulerAlertToBanks;
import com.nimai.email.entity.NimaiFSubsidiaries;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.NimaiMBranch;
import com.nimai.email.entity.NimaiMLogin;
import com.nimai.email.entity.NimaiMRefer;

public interface UserServiceDao {

NimaiMLogin getCustomerDetailsByUserID(String userId) throws Exception;
NimaiMLogin update(NimaiMLogin nimaiLogin);
NimaiClient getClientDetailsbyUserId(String userId);
boolean checkUserTokenKey(String token);
NimaiMLogin getUserDetailsByTokenKey(String token);
NimaiClient getcliDetailsByEmailId(String emailId);
NimaiFSubsidiaries saveSubsidiaryDetails(NimaiFSubsidiaries subsidiaryDetails);
NimaiFSubsidiaries getSubsidiaryDetailsByToken(String token);
boolean isUserIdExisist(String userId);
NimaiMBranch getBranchUserDetails(String emailId);
NimaiMBranch updateBranchUser(NimaiMBranch branchUserDetails);
NimaiMBranch updateBranchUserDetails(NimaiMBranch branchUserDetails);
NimaiMRefer saveReferTokenDetails(NimaiMRefer referDetails);
NimaiMRefer getReferDetailsByToken(String token);
NimaiMBranch getbranchDetailsByToken(String token);
NimaiMBranch updateBranchUserDetails(String emailID, Date dnow, String passcodeValue);
NimaiMBranch updateBranchUser(String passcode, String tokenKey, Date insertedDate, String emailId, int id, Date tokenExpiry);
boolean isEntryPresent(int id);
NimaiLC getTransactioDetailsByTransIs(String transactionid);
List<NimaiEmailScheduler> getSchedulerDetails();
void updateEmailStatus(int scedulerid);
void updateReferTokenDetails(Date tokenExpiry, String refertokenKey, NimaiClient clientUseId, Date insertedDate,
		String emailId,int referenceId);
NimaiMRefer getreferDetails(int referenceId);
NimaiMBranch getBranchUserbyUserId(String userid);
void updateEmailStatus(String userid);
void updateLoginEmailStatus(String userid);
List<NimaiLC> getCustTransactionList(Date todaysDate);


}
