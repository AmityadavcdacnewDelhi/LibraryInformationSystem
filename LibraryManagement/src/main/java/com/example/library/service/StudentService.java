package com.example.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.entities.Student;
import com.example.library.entities.Transaction;
import com.example.library.repo.StudentRepository;
import com.example.library.repo.TransactionRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TransactionRepository transactionRepo;

	public StudentService() {

	}

	/*
	 * Service method to add Student
	 */
	public String addStudent(Student student) {

		List<Student> allStudent = studentRepository.findAll();
		for (Student s : allStudent) {
			if (s.getYear().equals(student.getYear()) && s.getBranch().equals(student.getBranch())
					&& s.getDivision().equals(student.getDivision())
					&& s.getRollNumber().equals(student.getRollNumber())) {

				return "NOT OK";
			}
		}

		studentRepository.save(student);

		return "OK";
	}

	/*
	 * Service method to get All Students Name
	 */
	public List<String> getAllStudentsName() {
		List<Student> allStudent = studentRepository.findAll();
		List<String> studentNames = new ArrayList<String>();
		for (Student student : allStudent) {

			studentNames.add(student.getName() + ' ' + student.getSurname());
		}
		return studentNames;
	}

	/*
	 * Service method to get One Student
	 */
	public Student getOneStudent(Long regNumber) {

		Optional<Student> optional = studentRepository.findById(regNumber);
		return optional.get();
	}

	/*
	 * Service method to get All Students
	 */
	public List<Student> getAllStudents() {
		return studentRepository.findAll();

	}

	/*
	 * Service method to get Transactions By Student Registration Number
	 */
	public List<Transaction> getTransactionsByStudentRegNumber(Long regNumber) {
		return transactionRepo.findByRegNumber(regNumber);
	}

	/*
	 * Service method to validate Registration Number
	 */
	public Boolean validateRegNr(Long regNr) {
		Optional<Student> optional = studentRepository.findById(regNr);
		if (optional.isEmpty()) {

			return false;

		} else {
			return true;
		}
	}

	/*
	 * Service method to search By Registration Number
	 */
	public Student searchByRegNr(Long regNr) {
		return studentRepository.getById(regNr);
	}

	/*
	 * Service method to search By student Name
	 */
	public List<Student> searchBystudentName(String fullName) {

		List<Student> student = new ArrayList<Student>();
		List<Student> allStudent = studentRepository.findAll();
		for (Student S : allStudent) {
			String fName = S.getName() + " " + S.getSurname();
			if (fName.equalsIgnoreCase(fullName)) {
				student.add(S);
			}
		}

		return student;
	}

	/*
	 * Service method to pay Fine
	 */
	public void payFine(Student student) {
		Student oneStudent = getOneStudent(student.getRegistrationNumber());
		int originalFinePaid = oneStudent.getFinePaid();
		originalFinePaid += student.getFinePaid();
		oneStudent.setFinePaid(originalFinePaid);
		studentRepository.save(oneStudent);

	}

	/*
	 * Service method to update Student Details
	 */
	public void updateStudentDetails(Student student, Long regNumber) {

		Student student2 = studentRepository.getById(regNumber);

		student2.setName(student.getName());
		student2.setSurname(student.getSurname());
		student2.setYear(student.getYear());
		student2.setBranch(student.getBranch());
		student2.setDivision(student.getDivision());
		student2.setRollNumber(student.getRollNumber());

		studentRepository.save(student2);

	}

	/*
	 * Service method to delete Student
	 */
	public boolean deleteStudent(Long regNumber) {

		Student student = studentRepository.getById(regNumber);
		if (student.getFine() > student.getFinePaid() || student.getBooks().size() > 0) {

			return false;
		} else {
			studentRepository.delete(student);
			return true;
		}

	}

}
