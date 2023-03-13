package com.example.library.controller;

import java.util.List;

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
import com.example.library.entities.Constants;
import com.example.library.entities.Message;
import com.example.library.entities.Student;
import com.example.library.service.BookService;
import com.example.library.service.StudentService;
import com.example.library.service.TransactionService;

@Controller
@RequestMapping("/student/**")
public class StudentController {

	@Autowired
	BookService bookService;

	@Autowired
	StudentService studentService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	Admin admin;

	/*
	 * This is Controller to redirect to Add Student Page
	 */

	@GetMapping("/student/addStudent")
	public String addStudent(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("branches", admin.getBranches());
		
		model.addAttribute("divisions", admin.getDivision());
		model.addAttribute("years", admin.getYears());
		model.addAttribute("salutation", admin.getsalutation());
		model.addAttribute("student", new Student());
		model.addAttribute("title", "Add Student");
		return "Menus/addStudent";
	}

	/*
	 * This is Controller to save/Add Student
	 */
	@PostMapping("/student/saveStudent")
	public String saveStudent(@ModelAttribute("Student") Student student, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		String string = studentService.addStudent(student);

		if (string.equals("OK")) {
			String text = "Student successfully registered";
			session.setAttribute("message", new Message(text, "alert-success"));
		} else {
			String text = "Student with Same Data is Already added to Records";
			session.setAttribute("message", new Message(text, "alert-danger"));
		}

		model.addAttribute("branches", admin.getBranches());
		model.addAttribute("divisions", admin.getDivision());
		model.addAttribute("years", admin.getYears());
		model.addAttribute("student", new Student());

		model.addAttribute("title", "Add Student");
		return "Menus/addStudent";
	}

	/*
	 * Handling of Request to view Students list Information
	 */

	@GetMapping("/student/studentInfo")
	public String studentInfo(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		session.setAttribute("selectback", "viewlist");
		model.addAttribute("students", studentService.getAllStudents());
		model.addAttribute("title", "Students Info");
		return "Menus/studentInfo";
	}

	/*
	 * select student functionality of back page
	 */
	@GetMapping("/student/SelectStudent/{regNumber}")
	public ModelAndView studentSelect(@PathVariable("regNumber") Long regNumber, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return new ModelAndView("Basic/login");
		}

		return new ModelAndView("redirect:/student/OneStudent/" + regNumber);

	}

	/*
	 * Handling of Request to See Student Details
	 */
	@GetMapping("/student/OneStudent/{regNumber}")
	public String studentDetails(@PathVariable("regNumber") Long regNumber, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		transactionService.updateFineForStudent(regNumber);
		model.addAttribute("OneStudent", studentService.getOneStudent(regNumber));

		if (session.getAttribute("selectback").equals("search")) {
			model.addAttribute("backLink", "/book/searchPage");
		} else {
			model.addAttribute("backLink", "/student/studentInfo");
		}
		model.addAttribute("title", "Student Detail");
		return "Specific/OneStudent";
	}

	/*
	 * Handling of Request to see Transactions of One Student
	 */
	@GetMapping("/student/transaction/{regNumber}")
	public String studentTransactions(@PathVariable("regNumber") Long regNumber, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		transactionService.updateFineContinuously();
		model.addAttribute("transactions", studentService.getTransactionsByStudentRegNumber(regNumber));
		model.addAttribute("stuRegNr", regNumber);
		model.addAttribute("title", "Student Transactions");
		return "Specific/StudentTransaction";
	}

	/*
	 * Handling of Request to search Student By Registration Number
	 */
	@PostMapping("/student/searchByStudentRegN")
	public String searchByBookId(Model model, @RequestParam(value = "regNr") Long regNr, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		if (studentService.validateRegNr(regNr)) {
			Student student = studentService.searchByRegNr(regNr);
			model.addAttribute("OneStudent", student);
			model.addAttribute("title", "Student Information");
			model.addAttribute("backLink", "/book/searchPage");
			return "Specific/OneStudent";
		} else {
			model.addAttribute("title", "Search");
			model.addAttribute("students", studentService.getAllStudentsName());
			model.addAttribute("bookTitle", bookService.getUniqueBooksTitle());
			String text = "No Student available with such Registration Number";
			session.setAttribute("message", new Message(text, "alert-danger"));
			return "Specific/SearchBy";
		}

	}

	/*
	 * Handling of Request to search Student By Name
	 */
	@PostMapping("/student/searchByStudentName")
	public String searchByBookId(Model model, @RequestParam(value = "studentName") String fullName,
			HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		List<Student> students = studentService.searchBystudentName(fullName);

		if (students.size() == 1) {
			model.addAttribute("OneStudent", students.get(0));
			model.addAttribute("title", "Student Information");
			model.addAttribute("backLink", "/book/searchPage");
			return "Specific/OneStudent";
		} else {
			model.addAttribute("students", students);
			model.addAttribute("title", "Select Student");
			return "Specific/SelectStudent";
		}
	}

	/*
	 * Handling of Request to redirect to Pay fine Page
	 */
	@GetMapping("/student/finePaymentPage")
   //     @GetMapping("/student/checkout") 
	public String finePaymentPage(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		session.setAttribute("selectback", "viewlist");
		model.addAttribute("title", "Payment");
		model.addAttribute("student", new Student());

		return "Specific/PayFine";
	}

	/*
	 * Handling of Request to Pay fine of Student
	 */
	@PostMapping("/student/payFine")
	public ModelAndView payFine(Model model, @ModelAttribute("student") Student student, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return new ModelAndView("Basic/login");
		}

		if (studentService.validateRegNr(student.getRegistrationNumber())) {
			studentService.payFine(student);
			return new ModelAndView("redirect:/student/OneStudent/" + student.getRegistrationNumber());

		} else {
			model.addAttribute("title", "Payment");
			model.addAttribute("student", new Student());
			String text = "No student present with such Registration Number";
			session.setAttribute("message", new Message(text, "alert-danger"));

			return new ModelAndView("Specific/PayFine");
		}
	}

	/*
	 * Handling of Request to redirect update Student Information Page
	 */
	@GetMapping("/student/updateInfo/{regNumber}")
	public String updateInfo(@PathVariable("regNumber") Long regNumber, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("Student", studentService.getOneStudent(regNumber));

		model.addAttribute("branches", admin.getBranches());
		model.addAttribute("divisions", admin.getDivision());
		model.addAttribute("years", admin.getYears());

		model.addAttribute("title", "Update Information");
		return "Specific/UpdateStudent";
	}

	/*
	 * Handling of Request to save updated Student Information
	 */
	@PostMapping("/student/saveUpdate/{regNumber}")
	public ModelAndView updateStudent(@ModelAttribute("Student") Student student,
			@PathVariable("regNumber") Long regNumber, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return new ModelAndView("Basic/login");
		}

		studentService.updateStudentDetails(student, regNumber);

		return new ModelAndView("redirect:/student/OneStudent/" + student.getRegistrationNumber());

	}

	/*
	 * Handling of Request to delete Student
	 */
	@GetMapping("/student/deleteStudent/{regNumber}")
	public ModelAndView deleteStudent(@PathVariable("regNumber") Long regNumber, Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return new ModelAndView("Basic/login");
		}

		boolean res = studentService.deleteStudent(regNumber);
		if (!res) {
			String text = "This student can not be deleted as fine is not paid or book is not returned.";
			session.setAttribute("message", new Message(text, "alert-danger"));
		} else {
			String text = "Student deleted from Records.";
			session.setAttribute("message", new Message(text, "alert-success"));
		}

		return new ModelAndView("redirect:/student/studentInfo");

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
