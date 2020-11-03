package com.nimai.kyc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sahadeo.naik
 */
@Entity
@Table(name = "NIMAI_M_CUSTOMER")
public class NimaiMCustomer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "SUBSCRIBER_TYPE")
	private String subscriberType;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "USERID")
	private String userid;

	@Basic(optional = false)
	@NotNull
	@Column(name = "FIRST_NAME")
	private String firstName;

	@Basic(optional = false)
	@NotNull
	@Column(name = "LAST_NAME")
	private String lastName;

	@Basic(optional = false)
	@NotNull
	private String emailAddress;

	@Basic(optional = false)
	@NotNull
	private String mobileNumber;

	@Column(name = "COUNTRY_NAME")
	private String countryName;

	@Column(name = "LANDLINE")
	private String landline;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "BUSINESS_TYPE")
	private String businessType;

	@Column(name = "BANK_NAME")
	private String bankNbfcName;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "SWIFT_CODE")
	private String swiftCode;

	@Column(name = "MIN_VALUEOF_LC")
	private String minValueofLc;

	@Column(name = "TELEPHONE")
	private String telephone;

	@Column(name = "REGISTRATION_TYPE")
	private String registrationType;

	@Column(name = "PROVINCENAME")
	private String provincename;

	@Column(name = "ADDRESS1")
	private String address1;

	@Column(name = "ADDRESS2")
	private String address2;

	@Column(name = "ADDRESS3")
	private String address3;

	@Column(name = "CITY")
	private String city;

	@Column(name = "PINCODE")
	private String pincode;

	@Column(name = "REGISTERED_COUNTRY")
	private String rgistredCountry;

	@Column(name = "IS_REGISTER")
	private Integer isRegister;

	@Column(name = "IS_RMASSIGNED")
	private Integer isRmassigned;

	@Column(name = "RM_ID")
	private String rmId;

	@Column(name = "IS_BDETAILSFILLED")
	private Integer isBdetailsfilled;

	@Column(name = "IS_SPLANPURCHASED")
	private Integer isSplanPurchased;

	@Column(name = "MODE_OF_PAYMENT")
	private String modeOfPayment;

	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;

	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	@Column(name = "KYC_STATUS")
	private String kycStatus;

	@Column(name = "KYC_APPROVALDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date kycApprovedDate;

	@Column(name = "INSERTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertedDate;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Column(name = "ACCOUNT_TYPE")
	private String accountType;

	@Column(name = "ACCOUNT_SOURCE")
	private String accountSource;

	@Column(name = "ACCOUNT_CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountCreatedDate;

	@Column(name = "ACCOUNT_STATUS")
	private String accountStatus;

	@Column(name = "CURRENCY_CODE")
	private String currencyCode;

	@Column(name = "EMAIL_ADDRESS1")
	private String emailAddress1;

	@Column(name = "EMAIL_ADDRESS2")
	private String emailAddress2;

	@Column(name = "EMAIL_ADDRESS3")
	private String emailAddress3;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
//    private List<NimaiMLogin> nimaiMLoginList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
//    private List<NimaiFCountrygood> nimaiFCountrygoodList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "custUserId")
	private List<NimaiKyc> nimaKycList;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
//    private List<NimaiMRefer> nimaiMReferList;

	public NimaiMCustomer() {
	}

	public NimaiMCustomer(String userid) {
		this.userid = userid;
	}

	public NimaiMCustomer(String userid, String subscriptionId, String subscriberType, String firstName,
			String lastName, String emailAddress, String mobileNumber) {
		this.userid = userid;

		this.subscriberType = subscriberType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the rgistredCountry
	 */
	public String getRgistredCountry() {
		return rgistredCountry;
	}

	/**
	 * @param rgistredCountry the rgistredCountry to set
	 */
	public void setRgistredCountry(String rgistredCountry) {
		this.rgistredCountry = rgistredCountry;
	}

	/**
	 * @return the isRegister
	 */
	public int getIsRegister() {
		return isRegister;
	}

	/**
	 * @param isRegister the isRegister to set
	 */
	public void setIsRegister(int isRegister) {
		this.isRegister = isRegister;
	}

	/**
	 * @return the isRmassigned
	 */
	public int getIsRmassigned() {
		return isRmassigned;
	}

	/**
	 * @param isRmassigned the isRmassigned to set
	 */
	public void setIsRmassigned(int isRmassigned) {
		this.isRmassigned = isRmassigned;
	}

	/**
	 * @return the rmId
	 */
	public String getRmId() {
		return rmId;
	}

	/**
	 * @param rmId the rmId to set
	 */
	public void setRmId(String rmId) {
		this.rmId = rmId;
	}

	/**
	 * @return the isBdetailsfilled
	 */
	public int getIsBdetailsfilled() {
		return isBdetailsfilled;
	}

	/**
	 * @param isBdetailsfilled the isBdetailsfilled to set
	 */
	public void setIsBdetailsfilled(int isBdetailsfilled) {
		this.isBdetailsfilled = isBdetailsfilled;
	}

	/**
	 * @return the isSplanPurchased
	 */
	public int getIsSplanPurchased() {
		return isSplanPurchased;
	}

	/**
	 * @param isSplanPurchased the isSplanPurchased to set
	 */
	public void setIsSplanPurchased(int isSplanPurchased) {
		this.isSplanPurchased = isSplanPurchased;
	}

	/**
	 * @return the modeOfPayment
	 */
	public String getModeOfPayment() {
		return modeOfPayment;
	}

	/**
	 * @param modeOfPayment the modeOfPayment to set
	 */
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	/**
	 * @return the paymentStatus
	 */
	public String getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * @param paymentStatus the paymentStatus to set
	 */
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the kycStatus
	 */
	public String getKycStatus() {
		return kycStatus;
	}

	/**
	 * @param kycStatus the kycStatus to set
	 */
	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	/**
	 * @return the kycApprovedDate
	 */
	public Date getKycApprovedDate() {
		return kycApprovedDate;
	}

	/**
	 * @param kycApprovedDate the kycApprovedDate to set
	 */
	public void setKycApprovedDate(Date kycApprovedDate) {
		this.kycApprovedDate = kycApprovedDate;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the accountSource
	 */
	public String getAccountSource() {
		return accountSource;
	}

	/**
	 * @param accountSource the accountSource to set
	 */
	public void setAccountSource(String accountSource) {
		this.accountSource = accountSource;
	}

	/**
	 * @return the accountCreatedDate
	 */
	public Date getAccountCreatedDate() {
		return accountCreatedDate;
	}

	/**
	 * @param accountCreatedDate the accountCreatedDate to set
	 */
	public void setAccountCreatedDate(Date accountCreatedDate) {
		this.accountCreatedDate = accountCreatedDate;
	}

	/**
	 * @return the accountStatus
	 */
	public String getAccountStatus() {
		return accountStatus;
	}

	/**
	 * @param accountStatus the accountStatus to set
	 */
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the emailAddress1
	 */
	public String getEmailAddress1() {
		return emailAddress1;
	}

	/**
	 * @param emailAddress1 the emailAddress1 to set
	 */
	public void setEmailAddress1(String emailAddress1) {
		this.emailAddress1 = emailAddress1;
	}

	/**
	 * @return the emailAddress2
	 */
	public String getEmailAddress2() {
		return emailAddress2;
	}

	/**
	 * @param emailAddress2 the emailAddress2 to set
	 */
	public void setEmailAddress2(String emailAddress2) {
		this.emailAddress2 = emailAddress2;
	}

	/**
	 * @return the emailAddress3
	 */
	public String getEmailAddress3() {
		return emailAddress3;
	}

	/**
	 * @param emailAddress3 the emailAddress3 to set
	 */
	public void setEmailAddress3(String emailAddress3) {
		this.emailAddress3 = emailAddress3;
	}

//	/**
//	 * @return the nimaKycList
//	 */
//	public List<NimaiKyc> getNimaKycList() {
//		return nimaKycList;
//	}
//
//	/**
//	 * @param nimaKycList the nimaKycList to set
//	 */
//	public void setNimaKycList(List<NimaiKyc> nimaKycList) {
//		this.nimaKycList = nimaKycList;
//	}

	public String getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Date getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getBankNbfcName() {
		return bankNbfcName;
	}

	public void setBankNbfcName(String bankNbfcName) {
		this.bankNbfcName = bankNbfcName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMinValueofLc() {
		return minValueofLc;
	}

	public void setMinValueofLc(String minValueofLc) {
		this.minValueofLc = minValueofLc;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userid != null ? userid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NimaiMCustomer)) {
			return false;
		}
		NimaiMCustomer other = (NimaiMCustomer) object;
		if ((this.userid == null && other.userid != null)
				|| (this.userid != null && !this.userid.equals(other.userid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "in.nimai.NimaiMCustomer[ userid=" + userid + " ]";
	}

}
