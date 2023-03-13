package com.example.library.entities;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class Admin {

	public Admin() {

	}

	ArrayList<String> branches;
	ArrayList<String> years;
	ArrayList<String> divisions;
	ArrayList<String> categories;
	ArrayList<String> salutation;
	ArrayList<String> Bound;
	

	public ArrayList<String> getBranches() {

		return Constants.getAllBranches();
	}

	public ArrayList<String> getYears() {

		return Constants.getAllYears();
	}

	public ArrayList<String> getDivision() {

		return Constants.getAllDivision();
	}

	public ArrayList<String> getCategory() {

		return Constants.getAllCategories();
	}
	
	public ArrayList<String> getsalutation() {

		return Constants.getAllsalutation();
	}
	public ArrayList<String> getBound() {

		return Constants.getAllBound();
	}


	private String username;
	private String password;
	private String captcha;

	// Getter and Setter
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the captcha
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * @param captcha the captcha to set
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	

}
