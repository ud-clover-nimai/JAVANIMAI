package com.nimai.email.service;

import org.springframework.http.ResponseEntity;

import com.nimai.email.bean.AlertToBanksBean;
import com.nimai.email.bean.QuotationAlertRequest;

public interface BanksALertEmailService {

	ResponseEntity<?> getAlleligibleBAnksEmail(AlertToBanksBean alertBanksBean);

	ResponseEntity<?> sendTransactionStatusToBanks(AlertToBanksBean alertBanksBean);

	ResponseEntity<?> sendQuotationStatusToBanks(QuotationAlertRequest qauotatioReq);

	ResponseEntity<?> sendTransactionStatusToBanksByScheduled();

}
