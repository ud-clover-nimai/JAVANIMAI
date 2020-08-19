package com.nimai.uam.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author sahadeo.naik
 */
@Entity
@Table(name = "NIMAI_M_CUSTOMER")
@EntityListeners(AuditingEntityListener.class)
public class NimaiClient implements Serializable {

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
	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Basic(optional = false)
	@NotNull
	@Column(name = "MOBILE_NUMBER")
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

	@Column(name = "INSERTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertedDate;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Column(name = "BANK_NAME")
	private String bankNbfcName;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "SWIFT_CODE")
	private String swiftCode;

	@Column(name = "TELEPHONE")
	private String telephone;

	@Column(name = "MIN_VALUEOF_LC")
	private String minValueofLc;

//    @Size(max = 20)
//    @Column(name = "OWNER_NAME")
//    private String ownerName;

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
	private List<NimaiMLogin> nimaiMLoginList;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public NimaiClient() {
	}

	public NimaiClient(String userid) {
		this.userid = userid;
	}

	public NimaiClient(String userid, String subscriberType, String firstName, String lastName, String emailAddress,
			String mobileNumber) {
		this.userid = userid;

		this.subscriberType = subscriberType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.mobileNumber = mobileNumber;
	}

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

//    public String getRegistredOffice() {
//        return registredOffice;
//    }
//
//    public void setRegistredOffice(String registredOffice) {
//        this.registredOffice = registredOffice;
//    }

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

	public List<NimaiMLogin> getNimaiMLoginList() {
		return nimaiMLoginList;
	}

	public void setNimaiMLoginList(List<NimaiMLogin> nimaiMLoginList) {
		this.nimaiMLoginList = nimaiMLoginList;
	}

//    public List<NimaiFCountrygood> getNimaiFCountrygoodList() {
//        return nimaiFCountrygoodList;
//    }
//
//    public void setNimaiFCountrygoodList(List<NimaiFCountrygood> nimaiFCountrygoodList) {
//        this.nimaiFCountrygoodList = nimaiFCountrygoodList;
//    }
//
//    public List<NimaiFKyc> getNimaiFKycList() {
//        return nimaiFKycList;
//    }
//
//    public void setNimaiFKycList(List<NimaiFKyc> nimaiFKycList) {
//        this.nimaiFKycList = nimaiFKycList;
//    }
//
//    public List<NimaiMRefer> getNimaiMReferList() {
//        return nimaiMReferList;
//    }
//
//    public void setNimaiMReferList(List<NimaiMRefer> nimaiMReferList) {
//        this.nimaiMReferList = nimaiMReferList;
//    }

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userid != null ? userid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NimaiClient)) {
			return false;
		}
		NimaiClient other = (NimaiClient) object;
		if ((this.userid == null && other.userid != null)
				|| (this.userid != null && !this.userid.equals(other.userid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.nimai.uam.entity.NimaiClient [userid=" + userid + "]";
	}

}
