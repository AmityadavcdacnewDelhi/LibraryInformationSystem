package com.example.library.entities;

import java.util.ArrayList;

public class Constants {

	public Constants() {

	}

	public static String USERNAME = "Amit";
	public static String PASSWORD = "Cdac";
	public static int FINE_PER_DAY = 5;
	public static int RETURN_DURATION = 15;
	public static int BOOK_LIMIT = 4;
	public static String SESSION_ADMIN = "ADMIN";
	
	public static String SESSION_OK = "SOK";
	
	public static String SESSION_NOTOK = "SNOTOK";

	// Categories
	public static ArrayList<String> getAllCategories() {
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("Technology");
		categories.add("Full Stack");
		categories.add("MBA");
		categories.add("java");
		categories.add("C#");
		categories.add("DSA");
		categories.add("Others");

		return categories;
	}

	// Branches
	public static ArrayList<String> getAllBranches() {
		ArrayList<String> branches = new ArrayList<String>();
		branches.add("CSE");
		branches.add("IT");
		branches.add("E&TC");
		branches.add("Compter Science");
		branches.add("Civil");
		branches.add("MBA");

		return branches;
	}

	// Years
	public static ArrayList<String> getAllYears() {
		ArrayList<String> years = new ArrayList<String>();
		years.add("First");
		years.add("Second");
		years.add("Third");
		years.add("Fourth");

		return years;
	}

	// Divisions
	public static ArrayList<String> getAllDivision() {
		ArrayList<String> divisions = new ArrayList<String>();
		divisions.add("A");
		divisions.add("B");
		divisions.add("C");

		
	   return divisions;
	}
	

	public static ArrayList<String> getAllsalutation() {
		// TODO Auto-generated method stub
		ArrayList<String> salutation = new ArrayList<String>();
		salutation.add("Mr");
		salutation.add("Mrs");
		salutation.add("Dr");
	//	salutation.add("Mrs");
		return salutation;
	}

	public static ArrayList<String> getAllBound() {
		// TODO Auto-generated method stub
		ArrayList<String> Bound = new ArrayList<String>();
		Bound.add("Soft_Copy");
		Bound.add("Hard_Copy");
		Bound.add("Spirial_Copy");
		return Bound;
		
	}
	

}
