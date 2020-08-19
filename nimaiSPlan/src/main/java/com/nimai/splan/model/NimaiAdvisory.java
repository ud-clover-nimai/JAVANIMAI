package com.nimai.splan.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nimai_m_vas")
public class NimaiAdvisory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="VAS_ID")
	private Integer vas_id;
	
	@Column(name="COUNTRY_NAME")
	private String country_name;
	
	@Column(name="PLAN_NAME")
	private String plan_name;
	
	@Column(name="DESCRIPTION_1")
	private String description_1;
	
	@Column(name="DESCRIPTION_2")
	private String description_2;
	
	@Column(name="DESCRIPTION_3")
	private String description_3;
	
	@Column(name="DESCRIPTION_4")
	private String description_4;
	
	@Column(name="DESCRIPTION_5")
	private String description_5;
	
	@Column(name="PRICING")
	private Float pricing;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATED_BY")
	private String created_by;
	
	@Column(name="CREATED_DATE")
	private Date created_date;
	
	@Column(name="MODIFIED_BY")
	private String modified_by;
	
    @Column(name="MODIFIED_DATE")
	private Date modified_date;

	public Integer getVas_id() {
		return vas_id;
	}

	public void setVas_id(Integer vas_id) {
		this.vas_id = vas_id;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getPlan_name() {
		return plan_name;
	}

	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public String getDescription_1() {
		return description_1;
	}

	public void setDescription_1(String description_1) {
		this.description_1 = description_1;
	}

	public String getDescription_2() {
		return description_2;
	}

	public void setDescription_2(String description_2) {
		this.description_2 = description_2;
	}

	public String getDescription_3() {
		return description_3;
	}

	public void setDescription_3(String description_3) {
		this.description_3 = description_3;
	}

	public String getDescription_4() {
		return description_4;
	}

	public void setDescription_4(String description_4) {
		this.description_4 = description_4;
	}

	public String getDescription_5() {
		return description_5;
	}

	public void setDescription_5(String description_5) {
		this.description_5 = description_5;
	}

	public Float getPricing() {
		return pricing;
	}

	public void setPricing(Float pricing) {
		this.pricing = pricing;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getModified_by() {
		return modified_by;
	}

	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
  
   
}
