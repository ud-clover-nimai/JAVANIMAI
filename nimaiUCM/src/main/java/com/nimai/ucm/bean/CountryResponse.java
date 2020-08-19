package com.nimai.ucm.bean;

import java.util.List;

public class CountryResponse {

	private String countryName;
	private String currency;
	private Integer countryId;
	private List<StateResponce> stateResponce;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public List<StateResponce> getStateResponce() {
		return stateResponce;
	}

	public void setStateResponce(List<StateResponce> stateResponce) {
		this.stateResponce = stateResponce;
	}

}
