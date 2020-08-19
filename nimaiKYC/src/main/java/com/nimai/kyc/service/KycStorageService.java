package com.nimai.kyc.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nimai.kyc.model.NimaiFKyc;
import com.nimai.kyc.payload.KycListRequest;
import com.nimai.kyc.payload.KycListStatusRequest;
import com.nimai.kyc.payload.PagedResponse;
import com.nimai.kyc.payload.kycBase64Request;

public interface KycStorageService {

	String storeFile(MultipartFile file, String userid, String fileType);

	Resource loadFileAsResource(String fileName);

	PagedResponse<?> findAllKycData(KycListRequest request);

	ResponseEntity<Object> getKycByUserId(String userId);

	ResponseEntity<Object> getReasonByuserID(String userId);

	NimaiFKyc saveKycReasonStatus(KycListStatusRequest request);

	ResponseEntity<?> saveOrUpdatekycDetails(kycBase64Request kycRequest);

	
}
