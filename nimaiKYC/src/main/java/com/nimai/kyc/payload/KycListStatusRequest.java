package com.nimai.kyc.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class KycListStatusRequest {

	private String reason;
//	private String checkedBy;
//	@JsonFormat(pattern = "yyyy-MM-dd")
//	private LocalDateTime approvalDate;
	private String userId;
	private String status;
	private String kycId;

	

	
	public String getKycId() {
		return kycId;
	}

	public void setKycId(String kycId) {
		this.kycId = kycId;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}


}
