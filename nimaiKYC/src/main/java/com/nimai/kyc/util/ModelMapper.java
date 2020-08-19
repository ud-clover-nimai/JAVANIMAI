package com.nimai.kyc.util;

import java.util.Calendar;
import java.util.Date;

import com.nimai.kyc.model.NimaiFKyc;
import com.nimai.kyc.model.NimaiKyc;
import com.nimai.kyc.payload.BusinessKycList;
import com.nimai.kyc.payload.KycResponse;
import com.nimai.kyc.payload.PersonalKycList;

public class ModelMapper {

	public static KycResponse mapKycDetailsToKycResponse(NimaiFKyc fKyc) {
		KycResponse kycResponse = new KycResponse();
		kycResponse.setFileName(fKyc.getFileName());
		kycResponse.setFileType(fKyc.getFileType());
		// kycResponse.setKycid(fKyc.getKycid());
		kycResponse.setUserid(fKyc.getUserid().getUserid());
		kycResponse.setUserName(fKyc.getUserid().getFirstName());

		return kycResponse;

	}

	public static Object mapKycDetailsToKycStatus(NimaiFKyc kycStatus) {
		NimaiFKyc response = new NimaiFKyc();
		response.setReason(kycStatus.getReason());
		response.setApprovalDate(kycStatus.getApprovalDate());
		response.setCheckedBy(kycStatus.getCheckedBy());
		return response;
	}

	public static NimaiKyc convertEntityKycRequestToDbObj(BusinessKycList mapper, NimaiKyc nimaiKycDoc) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		nimaiKycDoc.setCountry(mapper.getCountry());
		nimaiKycDoc.setTitle("Business Kyc");
		nimaiKycDoc.setEncodedFileContent(mapper.getEncodedFileContent());
		nimaiKycDoc.setInsertedDate(today);
		nimaiKycDoc.setDocumentName(mapper.getDocumentName());
		nimaiKycDoc.setDocumentType(mapper.getDocumentType());
		nimaiKycDoc.setKycStatus("0");

		return nimaiKycDoc;

	}

	public static NimaiKyc convertEntityKycRequestToDbObj(PersonalKycList mapper, NimaiKyc nimaiKycDoc) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		nimaiKycDoc.setCountry(mapper.getCountry());
		nimaiKycDoc.setTitle("Personal Kyc");
		nimaiKycDoc.setEncodedFileContent(mapper.getEncodedFileContent());
		nimaiKycDoc.setInsertedDate(today);
		nimaiKycDoc.setDocumentName(mapper.getDocumentName());
		nimaiKycDoc.setDocumentType(mapper.getDocumentType());
		nimaiKycDoc.setKycStatus("0");

		return nimaiKycDoc;
	}
}
