package com.example.library.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.library.entities.Admin;
import com.example.library.entities.Book;
import com.example.library.entities.Constants;
import com.example.library.entities.Message;
import com.example.library.entities.Transaction;
import com.example.library.service.BookService;
import com.example.library.service.StudentService;
import com.example.library.service.TransactionService;

@Controller
@RequestMapping("/book/**")
public class BookController {

	@Autowired
	BookService bookService;

	@Autowired
	Admin admin;

	@Autowired
	StudentService studentService;

	@Autowired
	TransactionService transactionService;

	/*
	 * Handling of request to go to Add Book Page
	 */
	@GetMapping("/book/addBook")
	public String addBook(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("book", new Book());
		model.addAttribute("categories", admin.getCategory());
		model.addAttribute("Bound", admin.getBound());
		model.addAttribute("salutation", admin.getsalutation());
		model.addAttribute("title", "Add Book");

		return "Menus/addBook";
	}

	/*
	 * Handling of request to save book to repository
	 */
	@PostMapping("/book/saveBook")
	public String saveBook(@ModelAttribute("book") Book book, @RequestParam(value = "copies") int copies, Model model,
			HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		bookService.addBookService(book, copies);
		String text = Integer.toString(copies) + " " + "books successfully added to library.";
		session.setAttribute("message", new Message(text, "alert-success"));

		model.addAttribute("book", new Book());
		model.addAttribute("categories", admin.getCategory());
		model.addAttribute("title", "Add Book");

		return "Menus/addBook";
	}

	/*
	 * Handling of request to see book details
	 */
	@GetMapping("/book/OneBook/{bookId}")
	public String bookDetails(@PathVariable("bookId") int bookId, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("OneBook", bookService.getOneBook(bookId));
		model.addAttribute("title", "Book Details");

		return "Specific/OneBook";
	}

	/*
	 * Handling of request to Issue Book
	 */
	@GetMapping("/book/issueBook")
	public String issueBook(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("transaction", new Transaction());
		model.addAttribute("title", "Issue Book");

		return "Menus/issueBook";
	}

	/*
	 * Handling of request to return book
	 */
	@GetMapping("/book/returnBook")
	public String returnBook(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("transaction", new Transaction());
		model.addAttribute("title", "Return Book");

		return "Menus/returnBook";
	}

	/*
	 * Handling of request to view list of all Books
	 */
	@GetMapping("/book/bookInfoUnique")
	public String bookInfoUnique(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("distinctBooks", bookService.getListOfAllDistinctBooks());
		model.addAttribute("title", "Books Info");

		return "Menus/bookInfoUnique";
	}

	/*
	 * Handling of request to view list of all Books
	 */
	@GetMapping("/book/OneBook/{title}/{author}")
	public String bookInfoUnique(@PathVariable("title") String title, @PathVariable("author") String author,
			Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		List<Integer> availabilityInfo = bookService.getAvailabilityInfo(title, author);

		model.addAttribute("total", availabilityInfo.get(0));
		model.addAttribute("issued", availabilityInfo.get(1));
		model.addAttribute("available", availabilityInfo.get(2));
		model.addAttribute("OneBook", bookService.findByTitleAndAuthor(title, author));
		model.addAttribute("title", "Book Information");

		return "Specific/Availability";

	}

	/*
	 * Handling of request to go to Search Page
	 */
	@GetMapping("/book/searchPage")
	public String loadSearchPage(Model model, HttpSession selectSession) {

		if (checkSession(selectSession).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("title", "Search");
		model.addAttribute("students", studentService.getAllStudentsName());
		model.addAttribute("bookTitle", bookService.getUniqueBooksTitle());
		selectSession.setAttribute("selectback", "search");

		return "Specific/SearchBy";
	}

	/*
	 * Handling of request to search Book By BOOK ID
	 */
	@PostMapping("/book/searchByBookId")
	public String searchByBookId(Model model, @RequestParam(value = "bookId") int bookId, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		if (bookService.validateBookId(bookId)) {
			Book book = bookService.searchByBookId(bookId);
			model.addAttribute("OneBook", book);
			model.addAttribute("title", "Book Information");
			return "Specific/OneBook";
		} else {
			model.addAttribute("title", "Search");
			model.addAttribute("students", studentService.getAllStudentsName());
			model.addAttribute("bookTitle", bookService.getUniqueBooksTitle());
			String text = "No book present with such ID";
			session.setAttribute("message", new Message(text, "alert-danger"));
			return "Specific/SearchBy";
		}
	}

	/*
	 * Handling of request to view list of transactions of One Books
	 */
	@GetMapping("/book/transaction/{bookId}")
	public String studentTransactions(@PathVariable("bookId") int bookId, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		transactionService.updateFineContinuously();

		model.addAttribute("transactions", bookService.getTransactionsByBookId(bookId));
		model.addAttribute("bookId", bookId);
		model.addAttribute("title", "Book Transactions");

		return "Specific/BookTransaction";
	}

	/*
	 * Handling of request to search Book By BOOK TITLE
	 */
	@PostMapping("/book/searchByBookTitle")
	public String searchByBookTitle(Model model, @RequestParam(value = "bookTitle") String bookTitle,
			HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		List<Book> books = bookService.searchByTitle(bookTitle);
		model.addAttribute("books", books);
		model.addAttribute("title", "Books");
		return "Specific/BookIds";

	}

	/*
	 * Handling of request to go to update Book Page
	 */
	@GetMapping("/book/updateBook/{title}/{author}")
	public String updateBookInfo(@PathVariable("title") String title, @PathVariable("author") String author,
			Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("Book", bookService.findByTitleAndAuthor(title, author));
		model.addAttribute("categories", admin.getCategory());
		model.addAttribute("title", "Book Update");
		return "Specific/updateBook";
	}

	/*
	 * Handling of request to save updated Book
	 */
	@PostMapping("/book/saveUpdate/{bookId}")
	public ModelAndView saveUpdatedBook(@PathVariable("bookId") int bookId, @ModelAttribute("Book") Book bookLocal,
			Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return new ModelAndView("Basic/login");
		}

		bookService.updatebookInfoandSave(bookId, bookLocal);
		String text = "Book Updated Successfully";
		session.setAttribute("message", new Message(text, "alert-success"));

		return new ModelAndView("redirect:/book/OneBook/" + bookLocal.getTitle() + "/" + bookLocal.getAuthor());

	}

	/*
	 * Handling of request to go to delete Book Page
	 */
	@GetMapping("/book/deleteBooks/{title}/{author}")
	public String saveUpdatedBook(@PathVariable("title") String title, @PathVariable("author") String author,
			Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		List<Book> allBooks = bookService.findByTitleAndAuthorAllBooks(title, author);

		model.addAttribute("books", allBooks);
		model.addAttribute("title", "Delete Books");

		return "Specific/deleteBooks";

	}

	/*
	 * Handling of request to delete Books
	 */
	@PostMapping("/book/deleteBooks")
	public ModelAndView deleteBooks(HttpServletRequest request, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return new ModelAndView("Basic/login");
		}

		try {

			int flag = 0;
			Book locBook = null;

			if (request.getParameterValues("bookId") != null) {

				for (String bookId : request.getParameterValues("bookId")) {
					Book oneBook = bookService.getOneBook(Integer.parseInt(bookId));
					if (!oneBook.isIssued()) {
						bookService.deleteBook(Integer.parseInt(bookId));
					} else {
						locBook = bookService.getOneBook(Integer.parseInt(bookId));
						flag = 1;
					}
				}

			} else {
				flag = 2;
			}

			if (flag == 0) {
				String text = "Selected books Successfully deleted";
				session.setAttribute("message", new Message(text, "alert-success"));
				return new ModelAndView("redirect:/book/bookInfoUnique");
			} else if (flag == 1) {

				String text = "Issued Books Can not be Deleted";
				session.setAttribute("message", new Message(text, "alert-danger"));
				String locAuthor = locBook.getAuthor();
				String locTitle = locBook.getTitle();
				return new ModelAndView("redirect:/book/deleteBooks/" + locTitle + "/" + locAuthor);

			} else {
				String text = "No book was Selected";
				session.setAttribute("message", new Message(text, "alert-danger"));
				return new ModelAndView("redirect:/book/bookInfoUnique");

			}
		} catch (Exception e) {

			System.out.println("Something went Wrong " + e.getMessage());
			return new ModelAndView("redirect:/book/bookInfoUnique");
		}

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
