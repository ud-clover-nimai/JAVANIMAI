package com.nimai.email.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="nimai_email_scheduler_alerts_tobanks")
public class NimaiEmailSchedulerAlertToBanks {

	@Id
	@Basic(optional = false)
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scheduler_Id")
	private int scedulerid;

	@Column(name = "customer_userId")
	private String customerid;

	@Column(name = "transaction_id")
	private String transactionid;

	@Column(name = "reason")
	private String reason;
	
	@Column(name="banks_email_id")
	private String banksEmailID;

	@Column(name = "inserted_date")
	private Date insertedDate;
	
	@Column(name="bank_userId")
private String bankUserid;
	
	@Column(name="bank_userName")
	private String bankUserName;
	
	@Column(name="email_event")
	private String emailEvent;
	
	@Column(name="quotation_id")
	private Integer quotationId;
	
	
	



	/**
	 * @return the quotationId
	 */
	public Integer getQuotationId() {
		return quotationId;
	}

	/**
	 * @param quotationId the quotationId to set
	 */
	public void setQuotationId(Integer quotationId) {
		this.quotationId = quotationId;
	}

	/**
	 * @return the emailEvent
	 */
	public String getEmailEvent() {
		return emailEvent;
	}

	/**
	 * @param emailEvent the emailEvent to set
	 */
	public void setEmailEvent(String emailEvent) {
		this.emailEvent = emailEvent;
	}

	/**
	 * @return the bankUserid
	 */
	public String getBankUserid() {
		return bankUserid;
	}

	/**
	 * @param bankUserid the bankUserid to set
	 */
	public void setBankUserid(String bankUserid) {
		this.bankUserid = bankUserid;
	}

	/**
	 * @return the bankUserName
	 */
	public String getBankUserName() {
		return bankUserName;
	}

	/**
	 * @param bankUserName the bankUserName to set
	 */
	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}

	/**
	 * @return the banksEmailID
	 */
	public String getBanksEmailID() {
		return banksEmailID;
	}

	/**
	 * @param banksEmailID the banksEmailID to set
	 */
	public void setBanksEmailID(String banksEmailID) {
		this.banksEmailID = banksEmailID;
	}

	/**
	 * @return the insertedDate
	 */
	public Date getInsertedDate() {
		return insertedDate;
	}

	/**
	 * @param insertedDate the insertedDate to set
	 */
	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}

	/**
	 * @return the scedulerid
	 */
	public int getScedulerid() {
		return scedulerid;
	}

	/**
	 * @param scedulerid the scedulerid to set
	 */
	public void setScedulerid(int scedulerid) {
		this.scedulerid = scedulerid;
	}

	/**
	 * @return the customerid
	 */
	public String getCustomerid() {
		return customerid;
	}

	/**
	 * @param customerid the customerid to set
	 */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	/**
	 * @return the transactionid
	 */
	public String getTransactionid() {
		return transactionid;
	}

	/**
	 * @param transactionid the transactionid to set
	 */
	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
