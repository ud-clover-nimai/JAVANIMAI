package com.nimai.kyc.service;

import org.springframework.http.ResponseEntity;

import com.nimai.kyc.payload.kycBase64Request;

public interface KycStorageService {

	ResponseEntity<?> saveOrUpdatekycDetails(kycBase64Request kycRequest);

	

}
