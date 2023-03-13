package com.example.library.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.entities.Book;
import com.example.library.entities.Constants;
import com.example.library.entities.Student;
import com.example.library.entities.Transaction;
import com.example.library.repo.BookRepository;
import com.example.library.repo.StudentRepository;
import com.example.library.repo.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	StudentRepository studentRepo;

	public TransactionService() {

	}

	/*
	 * Service Method to handle Issue Book Request
	 */
	public String handleIssueBookRequest(Transaction transaction) {
		Transaction trans = new Transaction();
		LocalDate today = LocalDate.now();
		Date issueDate = Date.valueOf(today);
		LocalDate expectedLocal = today.plusDays(Constants.RETURN_DURATION);
		Date expectedReturnDate = Date.valueOf(expectedLocal);

		Optional<Book> optionalB = bookRepository.findById(transaction.getBookId());

		Optional<Student> optionalS = studentRepo.findById(transaction.getRegNumber());

		if ((!optionalB.isEmpty()) && (!optionalS.isEmpty())) {
			Book book = optionalB.get();
			Student student = optionalS.get();
			List<Book> books = student.getBooks();
			if (books.size() > (Constants.BOOK_LIMIT - 1)) {
				return "BOOK LIMIT";
			}
			if (book.isIssued()) {
				for (Book b : books) {
					System.out.println(b);
					if (b.getAuthor().equals(book.getAuthor()) && b.getTitle().equals(book.getTitle())) {
						return "ISSUEDBYSAME";
					}
				}
				return "ISSUEDBYOTHER";
			} else {

				for (Book b : books) {
					if (b.getAuthor().equals(book.getAuthor()) && b.getTitle().equals(book.getTitle())) {
						return "ISSUEDBYSAME";
					}
				}
				book.setIssued(true);
				bookRepository.save(book);
				books.add(book);
				student.setBook(books);
				studentRepo.save(student);

				trans.setBookName(book.getTitle());
				trans.setBookId(transaction.getBookId());
				trans.setRegNumber(transaction.getRegNumber());
				trans.setStudentFullName(student.getName() + " " + student.getSurname());

				trans.setIssueDate(issueDate);
				trans.setExpectedReturnDate(expectedReturnDate);
				trans.setActualReturnDate(null);
				trans.setFine(0);
				transactionRepo.save(trans);
				return "OK";

			}
		}

		else {
			return "NOT_AVAILABLE";
		}
	}

	/*
	 * Service Method to get All Transactions
	 */
	public List<Transaction> getAllTransactions() {

		return transactionRepo.findByOrderBySrNrDesc();
	}

	/*
	 * Service Method to get Data By BookId
	 */
	public Transaction getDataByBookId(int bookId) {
		Transaction transaction = null;
		List<Transaction> transactionList = transactionRepo.findByBookId(bookId);

		for (Transaction t : transactionList) {
			if (t.getActualReturnDate() == null) {
				Long srNr = t.getSrNr();

				Optional<Transaction> optional = transactionRepo.findById(srNr);
				return optional.get();

			}
		}
		return transaction;
	}

	/*
	 * Service Method to update Return Date and Fine
	 */
	public void updateReturnDateandFine(Long srNr) {
		Transaction transaction = transactionRepo.getById(srNr);

		updateFineForStudent(transaction.getRegNumber());

		Date expectedReturnDate = transaction.getExpectedReturnDate();
		Date actualReturnDate = Date.valueOf(LocalDate.now());
		transaction.setActualReturnDate(actualReturnDate);

		if (expectedReturnDate.toLocalDate().isBefore(LocalDate.now())) {

			int fine = calculateFine(expectedReturnDate, actualReturnDate, transaction);
			transaction.setFine(fine);
		} else {
			transaction.setFine(0);
		}
		transactionRepo.save(transaction);

		Book book = bookRepository.getById(transaction.getBookId());

		Student student = studentRepo.getById(transaction.getRegNumber());
		List<Book> books = student.getBooks();
		books.remove(book);
		student.setBook(books);
		studentRepo.save(student);

	}

	/*
	 * Service Method to calculate Fine
	 */
	private int calculateFine(Date expectedReturnDate, Date actualReturnDate, Transaction transaction) {

		Period period = Period.between(expectedReturnDate.toLocalDate(), actualReturnDate.toLocalDate());

		int days = (period.getYears() * 365) + (period.getMonths() * 30) + (period.getDays());

		int fine = days * Constants.FINE_PER_DAY;

		int bookId = transaction.getBookId();

		int price = 0;
		Optional<Book> optional = bookRepository.findById(bookId);
		if (!optional.isEmpty()) {
			Book book = optional.get();
			price = book.getPrice();
		}

		if (fine > price) {
			fine = price;
		}

		return fine;
	}

	/*
	 * Service Method to update Fine Continuously
	 */
	public void updateFineContinuously() {

		List<Transaction> completeTransactions = transactionRepo.findAll();

		for (Transaction compTrans : completeTransactions) {
			Date expectedReturnDate = compTrans.getExpectedReturnDate();
			if (compTrans.getActualReturnDate() != null) {
				if (expectedReturnDate.toLocalDate().isBefore(compTrans.getActualReturnDate().toLocalDate())) {
					int fine = calculateFine(expectedReturnDate, compTrans.getActualReturnDate(), compTrans);
					compTrans.setFine(fine);
				} else {
					compTrans.setFine(0);
				}
				transactionRepo.save(compTrans);
			} else {
				if (expectedReturnDate.toLocalDate().isBefore(LocalDate.now())) {

					int fine = calculateFine(expectedReturnDate, Date.valueOf(LocalDate.now()), compTrans);
					compTrans.setFine(fine);
				} else {
					compTrans.setFine(0);
				}
			}
		}

	}

	/*
	 * Service Method to update Fine For Student
	 */
	public void updateFineForStudent(Long regNumber) {
		Student student = studentRepo.getById(regNumber);
		List<Transaction> transactionsForStudent = transactionRepo.findByRegNumber(regNumber);

		int fineStudent = 0;
		for (Transaction transac : transactionsForStudent) {
			fineStudent += transac.getFine();
		}
		student.setFine(fineStudent);
		studentRepo.save(student);
	}

	/*
	 * Service Method to Pay Fine
	 */
	public void payFine(Long regNum, int fineAmount) {
		Student student = studentRepo.getById(regNum);
		int finePaid = student.getFinePaid();
		finePaid += fineAmount;
		student.setFinePaid(finePaid);
		studentRepo.save(student);
	}
}