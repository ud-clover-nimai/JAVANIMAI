package com.nimai.ucm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NIMAI_M_CUSTOMER")
public class Customer implements Serializable {

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USERID")
	private String userid;

	@Column(name = "SUBSCRIBER_TYPE")
	private String subscriber_type;

	@Column(name = "BANK_TYPE")
	private String bank_type;

	@Column(name = "FIRST_NAME")
	private String first_name;

	@Column(name = "LAST_NAME")
	private String last_name;

	@Column(name = "EMAIL_ADDRESS")
	private String email_address;

	@Column(name = "MOBILE_NUMBER")
	private String mobile_number;

	@Column(name = "COUNTRY_NAME")
	private String country_name;

	@Column(name = "LANDLINE")
	private String land_line;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "COMPANY_NAME")
	private String company_name;

	@Column(name = "BUSINESS_TYPE")
	private String business_type;

	@Column(name = "BANK_NAME")
	private String bank_name;

	@Column(name = "SWIFT_CODE")
	private String swift_code;

	@Column(name = "TELEPHONE")
	private String telephone;

	@Column(name = "MIN_VALUEOF_LC")
	private String min_valueof_lc;

	@Column(name = "REGISTRATION_TYPE")
	private String registration_type;

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
	private String registered_country;

	@Column(name = "INSERTED_DATE")
	private Date inserted_date;

	@Column(name = "MODIFIED_DATE")
	private Date mdified_date;

	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getSubscriber_type() {
		return subscriber_type;
	}

	public void setSubscriber_type(String subscriber_type) {
		this.subscriber_type = subscriber_type;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getLand_line() {
		return land_line;
	}

	public void setLand_line(String land_line) {
		this.land_line = land_line;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getSwift_code() {
		return swift_code;
	}

	public void setSwift_code(String swift_code) {
		this.swift_code = swift_code;
	}
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMin_valueof_lc() {
		return min_valueof_lc;
	}

	public void setMin_valueof_lc(String min_valueof_lc) {
		this.min_valueof_lc = min_valueof_lc;
	}

	public String getRegistration_type() {
		return registration_type;
	}

	public void setRegistration_type(String registration_type) {
		this.registration_type = registration_type;
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

	public String getRegistered_country() {
		return registered_country;
	}

	public void setRegistered_country(String registered_country) {
		this.registered_country = registered_country;
	}

	public Date getInserted_date() {
		return inserted_date;
	}

	public void setInserted_date(Date inserted_date) {
		this.inserted_date = inserted_date;
	}

	public Date getMdified_date() {
		return mdified_date;
	}

	public void setMdified_date(Date mdified_date) {
		this.mdified_date = mdified_date;
	}

}
