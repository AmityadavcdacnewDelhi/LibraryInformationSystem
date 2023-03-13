package com.example.library.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.example.library.entities.Admin;
import com.example.library.entities.Constants;
import com.example.library.service.HelperService;
import com.wf.captcha.base.Captcha;


import com.example.library.entities.Message;
import com.example.library.entities.User;
import com.example.library.repo.UserRepository;


@Controller
@RequestMapping("/app/**")
public class loginController {

	@Autowired
	HelperService helperService;
	
	@Autowired
	UserRepository userRepository;
	
	

	public loginController() {
	}

	/*
	 * Handling of Request to Open HOME PAGE
	 */
	@GetMapping("/app/home")
	public String getBookById(Model model, HttpSession session) {

		model.addAttribute("title", "Home");
		return "Basic/home";
	}
	

	/*
	 * Handling of Request to Open LOGIN PAGE
	 */
	@GetMapping("/app/login")
	public String login(Model model, HttpSession session) {

		System.out.println("DEBUG...1\n");

		model.addAttribute("title", "Login");
		model.addAttribute("admin", new Admin());
		

		return "Basic/login";
	//	return "Basic/logincapta";
	}

	@GetMapping("/app/signup")
	public String signup(Model model, HttpSession session) {

		System.out.println("DEBUG...1\n");

		model.addAttribute("title", "Login");
		model.addAttribute("admin", new Admin());
		return "Basic/signup";
	}
	

	@PostMapping("/app/saveuser")
	public String createAccount( User user, HttpSession session){
	    userRepository.save(user);
	   
	    return "Basic/home";
	} 

	///	 * Handling of LOGOUT Request
	 
	@PostMapping("/app/logout")
	public String logout(Model model, HttpSession session) throws IOException, ServletException {
		session.removeAttribute(Constants.SESSION_ADMIN);

		model.addAttribute("title", "Login");
		model.addAttribute("admin", new Admin());
		return "Basic/login";
	//	return "Basic/logincapta";
	}

	/*
	 * Handling of Request to verify Login Credentials
	 */
	@PostMapping("/app/verify")
	public String dashboard(@ModelAttribute("admin") Admin admin, Model model, HttpSession session) {

	//	return helperService.verifyAdmin(admin, model, session);
		return helperService.verifyAdmin(admin, model, session);

	}
	
//	@PostMapping("/app/verify")
//	public String dashboard(@ModelAttribute("admin") User admin, Model model, HttpSession session) {
//
//		return helperService.verifyUser(admin, model, session);
//
//	}

	/*
	 * Handling of Request to Open DASHBOARD
	 */
	@GetMapping("/app/dashboard")
	public String backToDashboard(Model model, HttpSession session) {

		if (checkSession(session).equals(Constants.SESSION_NOTOK)) {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", new Admin());
			return "Basic/login";
		}

		model.addAttribute("title", "Dashboard");
		return "Basic/dashboard";
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
