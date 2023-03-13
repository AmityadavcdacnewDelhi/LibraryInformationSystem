package com.example.library.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

	public Transaction() {	
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long srNr;
	private int bookId;
	private String bookName;
	private Long regNumber;
	private String studentFullName;
	private Date issueDate;
	private Date expectedReturnDate;
	private Date actualReturnDate;
	private int fine;
	
	public Long getSrNr() {
		return srNr;
	}
	public void setSrNr(Long srNr) {
		this.srNr = srNr;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public Long getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(Long regNumber) {
		this.regNumber = regNumber;
	}
	public String getStudentFullName() {
		return studentFullName;
	}
	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getExpectedReturnDate() {
		return expectedReturnDate;
	}
	public void setExpectedReturnDate(Date expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}
	public Date getActualReturnDate() {
		return actualReturnDate;
	}
	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	public int getFine() {
		return fine;
	}
	public void setFine(int fine) {
		this.fine = fine;
	}
	
	
}
