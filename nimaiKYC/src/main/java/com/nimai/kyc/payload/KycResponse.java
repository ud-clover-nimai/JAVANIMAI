package com.nimai.kyc.payload;

import java.time.LocalDateTime;

import com.nimai.kyc.model.NimaiMCustomer;

public class KycResponse {

		private String kycid;
	    private String fileName;    	  
	    private String fileType;	   	    	      
	    private String userid; 
	    private String userName;
	    private String systemGeneratedName;
	  
	    
		public String getKycid() {
			return kycid;
		}
		public void setKycid(String kycid) {
			this.kycid = kycid;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getSystemGeneratedName() {
			return systemGeneratedName;
		}
		public void setSystemGeneratedName(String systemGeneratedName) {
			this.systemGeneratedName = systemGeneratedName;
		}
	    
	    

}
