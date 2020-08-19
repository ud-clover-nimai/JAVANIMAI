package com.nimai.kyc.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 *
 * @author sahadeo.naik
 */
@Entity
@Table(name = "NIMAI_F_KYC")
public class NimaiFKyc implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "KYCID")
	private Integer kycid;

	@JoinColumn(name = "USERID", referencedColumnName = "USERID")
	@ManyToOne(optional = false)
	private NimaiMCustomer userid;

	@Basic(optional = false)
	@NotNull
	@Column(name = "FILE_NAME")
	private String fileName;

	@Basic(optional = false)
	@NotNull
	@Column(name = "FILE_TYPE")
	private String fileType;

	@NotNull
	@Column(name = "SYSTEM_GENERATED_NAME")
	private String systemGeneratedName;

	@Column(name = "APPROVER_NAME")
	private String checkedBy;

	@Column(name = "APPROVAL_DATE")
	private String approvalDate;

	@Column(name = "APPROVAL_REASON")
	private String reason;

	@Column(name = "KYC_STATUS")
	private String status;

	@Column(name = "INSERTED_BY")
	@CreatedBy
	private String insertedBy;

	@Column(name = "INSERTED_DATE")
	@CreatedDate
	private LocalDateTime insertedDate;

	@Column(name = "MODIFIED_DATE")
	@LastModifiedDate
	private LocalDateTime modifiedDate;

	@LastModifiedBy
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	public Integer getKycid() {
		return kycid;
	}

	public void setKycid(Integer kycid) {
		this.kycid = kycid;
	}

	public NimaiMCustomer getUserid() {
		return userid;
	}

	public void setUserid(NimaiMCustomer userid) {
		this.userid = userid;
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

	public String getSystemGeneratedName() {
		return systemGeneratedName;
	}

	public void setSystemGeneratedName(String systemGeneratedName) {
		this.systemGeneratedName = systemGeneratedName;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInsertedBy() {
		return insertedBy;
	}

	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}

	public LocalDateTime getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(LocalDateTime insertedDate) {
		this.insertedDate = insertedDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	
	

}
