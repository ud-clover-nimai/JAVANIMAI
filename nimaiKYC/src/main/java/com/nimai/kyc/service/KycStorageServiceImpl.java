package com.nimai.kyc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nimai.kyc.exception.ErrorDescription;
import com.nimai.kyc.model.GenericResponse;
import com.nimai.kyc.model.NimaiKyc;
import com.nimai.kyc.model.NimaiMCustomer;
import com.nimai.kyc.payload.kycBase64Request;
import com.nimai.kyc.repository.NimaiCustomerRepository;
import com.nimai.kyc.repository.NimaiKycBase64Repository;
import com.nimai.kyc.util.ModelMapper;
import com.nimai.kyc.util.kycValidator;

@Service
public class KycStorageServiceImpl implements KycStorageService {

	@Autowired
	NimaiCustomerRepository nimaiCustomerRepository;

	@Autowired
	kycValidator kycValidator;

	@Autowired
	NimaiKycBase64Repository kycRepository;

	/*
	 * while uploading the kyc the kyc status will be 0 in nimaiFKYC as well as it
	 * will update the status on the nimaiCustomer with 0(customer upload the kyc)
	 */
	@Override
	public ResponseEntity<?> saveOrUpdatekycDetails(kycBase64Request kycRequest) {
		GenericResponse response = new GenericResponse();
		String errorString = this.kycValidator.kycRequestValidator(kycRequest);
		if (errorString.equalsIgnoreCase("success")) {
			NimaiMCustomer userId = nimaiCustomerRepository.findByUId(kycRequest.getUserId());
			String customerUserId = userId.getUserid();
			List<NimaiKyc> kycDetailList = kycRepository.getKycDetailsByUserId(kycRequest.getUserId());
			if (userId != null) {

				if (kycRequest.getBusinessDocumentList().size() > 0) {
					List<NimaiKyc> kycList = kycRequest.getBusinessDocumentList().stream().map(mapper -> {
						NimaiKyc nimaiKycDoc = new NimaiKyc();
						nimaiKycDoc.setCustUserId(new NimaiMCustomer(customerUserId));
						nimaiKycDoc.setInsertedBy(userId.getFirstName());
						nimaiKycDoc = ModelMapper.convertEntityKycRequestToDbObj(mapper, nimaiKycDoc);
						return nimaiKycDoc;
					}).collect(Collectors.toList());

					kycRepository.saveAll(kycList);
					response.setMessage("Busines kyc save successfully");
				}
				if (kycRequest.getPersonalDocumentList().size() > 0) {
					List<NimaiKyc> kycList = kycRequest.getPersonalDocumentList().stream().map(mapper -> {
						NimaiKyc nimaiKycDoc = new NimaiKyc();
						nimaiKycDoc.setCustUserId(new NimaiMCustomer(customerUserId));
						nimaiKycDoc.setInsertedBy(userId.getFirstName());
						nimaiKycDoc = ModelMapper.convertEntityKycRequestToDbObj(mapper, nimaiKycDoc);
						return nimaiKycDoc;
					}).collect(Collectors.toList());

					kycRepository.saveAll(kycList);
					response.setErrMessage("Personal kyc save successfully");
				}

				nimaiCustomerRepository.updateKycStatus("Pending", userId.getUserid());
				userId.setKycStatus("Pending");
				nimaiCustomerRepository.save(userId);
				
				response.setErrCode("EXE000");				
				return new ResponseEntity<Object>(response, HttpStatus.OK);

			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		response.setErrCode("User not registred");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

}
