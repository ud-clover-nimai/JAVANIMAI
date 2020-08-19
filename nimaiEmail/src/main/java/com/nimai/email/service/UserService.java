package com.nimai.email.service;

import org.springframework.http.ResponseEntity;

import com.nimai.email.bean.AdminBean;
import com.nimai.email.bean.BranchUserPassCodeBean;
import com.nimai.email.bean.BranchUserRequest;
import com.nimai.email.bean.KycEmailRequest;
import com.nimai.email.bean.LcUploadBean;
import com.nimai.email.bean.SubsidiaryBean;
import com.nimai.email.bean.UserRegistrationBean;

public interface UserService {
	boolean checkUserId(String userId);

	boolean checkEmailId(String emailAddress);

	ResponseEntity<Object> sendEmail(UserRegistrationBean userRegistrationBean) throws Exception;

	ResponseEntity<?> validateResetPasswordLink(String token);

	ResponseEntity<?> sendSubsidiaryEmail(SubsidiaryBean subsidiaryBean);

	ResponseEntity<?> validateSubsidiaryLink(String token);

	ResponseEntity<?> sendbranchUserLink(BranchUserRequest branchUserLink);

	ResponseEntity<?> sendTransactionStatus(LcUploadBean lcUploadBean);

	ResponseEntity<?> sendKYCStatus(KycEmailRequest kycReq);

	ResponseEntity<?> validatePassCodeValue(BranchUserPassCodeBean passCodeBean);

	ResponseEntity<?> sendLcUploadStatus(LcUploadBean lcUploadBean);

	ResponseEntity<Object> sendAdminEmail(AdminBean userRegistratinBean) throws Exception;


}
