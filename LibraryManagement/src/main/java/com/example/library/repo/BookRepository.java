package com.example.library.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.library.entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

	@Query("SELECT DISTINCT b.title FROM Book b")
	List<String> getDistinctBook();

	List<Book> findByTitleAndAuthor(String title, String author);

	int countByTitleAndAuthor(String title, String author);

	int countByTitleAndAuthorAndIsIssued(String title, String author, Boolean b);

	List<Book> findByTitle(String bookTitle);

}
