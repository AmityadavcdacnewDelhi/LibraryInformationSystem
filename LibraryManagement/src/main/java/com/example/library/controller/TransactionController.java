package com.example.library.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.library.entities.Admin;
import com.example.library.entities.Constants;
import com.example.library.entities.Message;
import com.example.library.entities.Transaction;
import com.example.library.service.BookService;
import com.example.library.service.TransactionService;

@Controller
@RequestMapping("/transaction/**")
public class TransactionController {

	@Autowired
	BookService bookService;

	@Autowired
	TransactionService transactionService;

	/*
	 * Handling of Request to issue Book
	 */
	@PostMapping("/transaction/issueRequest")

	public String handleIssueBookRequest(@ModelAttribute("transaction") Transaction transaction, Model model,
			HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		String result = transactionService.handleIssueBookRequest(transaction);

		model.addAttribute("transaction", new Transaction());
		model.addAttribute("title", "Issue Book");
		if (result.equals("OK")) {
			String text = "Book is Issued to Student";
			session.setAttribute("message", new Message(text, "alert-success"));
		} else if ("BOOK LIMIT".equals(result)) {
			String text = "Student can not issue more than " + Constants.BOOK_LIMIT + " books";
			session.setAttribute("message", new Message(text, "alert-danger"));
		} else if ("ISSUEDBYSAME".equals(result)) {
			String text = "Same book was already issued by this Student";
			session.setAttribute("message", new Message(text, "alert-danger"));
		} else if ("ISSUEDBYOTHER".equals(result)) {
			String text = "This book is issued";
			session.setAttribute("message", new Message(text, "alert-danger"));
		}

		else if ("NOT_AVAILABLE".equals(result)) {
			String text = "No Book with Book ID " + transaction.getBookId() + " or No Student with Reg Number "
					+ transaction.getRegNumber() + " is available";
			session.setAttribute("message", new Message(text, "alert-danger"));
		}

		return "Menus/issueBook";
	}

	/*
	 * Handling of Request to find values by Book Id
	 */
	@PostMapping("/transaction/returnValues")

	public String handlereturnBookValues(@ModelAttribute("transaction") Transaction transaction, Model model,
			HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}
		int bookId = transaction.getBookId();
		Transaction localTransaction = transactionService.getDataByBookId(bookId);

		if (localTransaction == null) {
			String text = "This book was not issued";
			session.setAttribute("message", new Message(text, "alert-danger"));
			model.addAttribute("title", "Return Book");
			return "Menus/returnBook";

		} else {
			model.addAttribute("transaction", localTransaction);

			session.setAttribute("returnTransaction", localTransaction);
			model.addAttribute("title", "Return Book");
			return "Menus/returnBookTwo";
		}

	}

	/*
	 * Handling of Request to return Book
	 */
	@PostMapping("/transaction/returnBook")

	public String handlereturnBookRequest(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}
		Transaction transaction = (Transaction) session.getAttribute("returnTransaction");
		Long SrNr = transaction.getSrNr();
		int bookId = transaction.getBookId();
		transactionService.updateReturnDateandFine(SrNr);
		bookService.updateIsIssuedOnReturn(bookId);
		transactionService.updateFineContinuously();
		model.addAttribute("transac", transactionService.getAllTransactions());
		model.addAttribute("title", "Return Book");
		return "Specific/AllTransactions";
	}

	/*
	 * Handling of Request to see all Transactions
	 */
	@GetMapping("/transaction/allTransactions")
	public String getAllTransactions(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}
		model.addAttribute("transac", transactionService.getAllTransactions());

		model.addAttribute("title", "Transactions");
		transactionService.updateFineContinuously();
		return "Specific/AllTransactions";
	}

	/**
	 * This method checks session
	 * 
	 * @param session
	 * @return
	 */
	private String checkSession(HttpSession session) {
		String sessionInfo = (String) session.getAttribute(Constants.SESSION_ADMIN);

		if (sessionInfo == null) {
			session.setAttribute("message", new Message("SESSION NOT VALID PLEASE LOGIN AGAIN!!!", "alert-danger"));
			return Constants.SESSION_NOTOK;
		} else {
			return Constants.SESSION_OK;
		}
	}
}
