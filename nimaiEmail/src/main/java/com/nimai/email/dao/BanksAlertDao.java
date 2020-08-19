package com.nimai.email.dao;

import java.util.List;

import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailSchedulerAlertToBanks;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.QuotationMaster;

public interface BanksAlertDao {

	NimaiEmailSchedulerAlertToBanks saveSchdulerData(NimaiEmailSchedulerAlertToBanks schedulerEntity);

	List<NimaiEmailSchedulerAlertToBanks> getTransactionDetail();

	void deleteEmailTransactionDetail(int scedulerid);

	NimaiLC getTransactioDetailsByTransIs(String transactionid);

	NimaiClient getCustDetailsByUserId(String transactionid);

	QuotationMaster getDetailsByQuoteId(int quotationId);
}
