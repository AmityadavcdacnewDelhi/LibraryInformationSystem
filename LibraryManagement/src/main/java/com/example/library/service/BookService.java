package com.example.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.entities.Book;
import com.example.library.entities.Transaction;
import com.example.library.repo.BookRepository;
import com.example.library.repo.TransactionRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	TransactionRepository transactionRepo;

	public BookService() {

	}

	/*
	 * Service Method to add Book
	 */
	public void addBookService(Book book, int copies) {
		for (int i = 0; i < copies; i++) {

			Book OneBook = new Book();
			OneBook.setBookId(book.getBookId());
			OneBook.setTitle(book.getTitle());
			OneBook.setAuthor(book.getAuthor());
			OneBook.setStudent(null);
			OneBook.setCategory(book.getCategory());
		//	OneBook.setCopy(book.getCopy());
			OneBook.setPrice(book.getPrice());
			OneBook.setIssued(false);
			bookRepository.save(OneBook);
		}

	}

	/*
	 * Service Method to get Unique book Title List
	 */
	public List<String> getUniqueBooksTitle() {
		return bookRepository.getDistinctBook();
	}

	/*
	 * Service Method to update IsIssued On Return
	 */
	public void updateIsIssuedOnReturn(int bookId) {

		Book book = bookRepository.getById(bookId);
		book.setIssued(false);
		bookRepository.save(book);

	}

	/*
	 * Service Method to get One Book
	 */
	public Book getOneBook(int bookId) {
		Optional<Book> optional = bookRepository.findById(bookId);
		return optional.get();
	}

	/*
	 * Service Method to get List Of All Distinct Books
	 */
	public List<Book> getListOfAllDistinctBooks() {
		List<Book> allBooks = bookRepository.findAll();
		List<Book> unicBooks = new ArrayList<>();
		String unicTitle = "";
		String unicAuthor = "";
		for (Book b : allBooks) {
			String title = b.getTitle();
			String author = b.getAuthor();
			if (!(unicTitle.equals(title) && unicAuthor.equals(author))) {
				unicBooks.add(b);
				unicTitle = title;
				unicAuthor = author;
			}
		}
		return unicBooks;
	}

	/*
	 * Service Method to get Availability Information
	 */
	public List<Integer> getAvailabilityInfo(String title, String author) {
		List<Integer> availList = new ArrayList<>();

		int total = bookRepository.countByTitleAndAuthor(title, author);
		int issued = bookRepository.countByTitleAndAuthorAndIsIssued(title, author, true);
		int available = bookRepository.countByTitleAndAuthorAndIsIssued(title, author, false);
		availList.add(total);
		availList.add(issued);
		availList.add(available);

		return availList;
	}

	/*
	 * Service Method to find Book By Title And Author
	 */
	public Book findByTitleAndAuthor(String title, String author) {

		List<Book> booksL = bookRepository.findByTitleAndAuthor(title, author);
		return booksL.get(0);
	}

	/*
	 * Service Method to validate BookId
	 */
	public Boolean validateBookId(int bookId) {
		Optional<Book> optional = bookRepository.findById(bookId);
		if (optional.isEmpty()) {
			return false;

		} else {
			return true;
		}
	}

	/*
	 * Service Method to search By BookId
	 */
	public Book searchByBookId(int bookId) {

		return bookRepository.getById(bookId);

	}

	/*
	 * Service Method to get Transactions By BookId
	 */
	public List<Transaction> getTransactionsByBookId(int bookId) {

		return transactionRepo.findByBookId(bookId);
	}

	/*
	 * Service Method to search Book By Title
	 */
	public List<Book> searchByTitle(String bookTitle) {

		return bookRepository.findByTitle(bookTitle);
	}

	/*
	 * Service Method to find List of Books By Title And Author
	 */
	public List<Book> findByTitleAndAuthorAllBooks(String title, String author) {

		List<Book> booksL = bookRepository.findByTitleAndAuthor(title, author);

		return booksL;
	}

	/*
	 * Service Method to update book Information and Save
	 */
	public void updatebookInfoandSave(int bookId, Book bookLocal) {

		Book oneBook = getOneBook(bookId);
		List<Book> bookList = findByTitleAndAuthorAllBooks(oneBook.getTitle(), oneBook.getAuthor());

		for (Book book : bookList) {
			book.setTitle(bookLocal.getTitle());
			book.setAuthor(bookLocal.getAuthor());
			book.setPrice(bookLocal.getPrice());
			book.setCategory(bookLocal.getCategory());

			bookRepository.save(book);

		}

	}

	/*
	 * Service Method to delete Book
	 */
	public void deleteBook(int bookId) {
		bookRepository.deleteById(bookId);

	}

}
