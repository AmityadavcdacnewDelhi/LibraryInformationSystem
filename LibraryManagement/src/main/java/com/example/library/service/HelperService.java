package com.example.library.service;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.library.entities.Admin;
import com.example.library.entities.Constants;
import com.example.library.entities.Message;
import com.example.library.entities.User;

@Service
public class HelperService {

	@Autowired
	TransactionService service;

	public HelperService() {

	}

	public String verifyAdmin(Admin admin, Model model, HttpSession session) {
		if (admin.getUsername().equals(Constants.USERNAME) && admin.getPassword().equals(Constants.PASSWORD)) {

			model.addAttribute("title", "Dashboard");
			service.updateFineContinuously();
			session.setAttribute(Constants.SESSION_ADMIN, Constants.USERNAME);
			
			return "Basic/dashboard";
		} else {
			model.addAttribute("title", "Login");
			model.addAttribute("admin", admin);
			session.setAttribute("message", new Message("Wrong Username or Password", "alert-danger"));
		//	return "Basic/login";
			return "Basic/logincapta";
		}
	}

	public String verifyUser(User user, Model model, HttpSession session) {
		if (user.getUsername().equals(user.getUsername()) && user.getPassword().equals(user.getPassword())) {

			model.addAttribute("title", "Dashboard");
			service.updateFineContinuously();
			session.setAttribute(Constants.SESSION_ADMIN, Constants.USERNAME);
			
			return "Basic/dashboard";
		} else {
		model.addAttribute("title", "Login");
			model.addAttribute("user", user);
		session.setAttribute("message", new Message("Wrong Username or Password", "alert-danger"));
			return "Basic/login";
		}
	}
	
//	public String verifyAdmin(User admin, Model model, HttpSession session) {
//		if (admin.getUsername().equals(admin.username) && admin.getPassword().equals(admin.password)) {
//
//			model.addAttribute("title", "Dashboard");
//			service.updateFineContinuously();
//		//	session.setAttribute(Constants.SESSION_ADMIN, Constants.USERNAME);
//			session.setAttribute(admin.firstname, admin.lastname);
//			
//			
//			return "Basic/dashboard";
//		} else {
//			model.addAttribute("title", "Login");
//			model.addAttribute("admin", admin);
//			session.setAttribute("message", new Message("Wrong Username or Password", "alert-danger"));
//		//	return "Basic/login";
//			return "Basic/logincapta";
//		}
//	}
//	
//	public String verifyUser(User admin, Model model, HttpSession session) {
//		if (admin.getUsername().equals(admin.username) && admin.getPassword().equals(admin.password)) {
//
//			model.addAttribute("title", "Dashboard");
//			service.updateFineContinuously();
//		//	session.setAttribute(Constants.SESSION_ADMIN, Constants.USERNAME);
//			session.setAttribute(admin.getUsername(), admin.getPassword());
//			
//			return "Basic/dashboard";
//		} else {
//			model.addAttribute("title", "Login");
//			model.addAttribute("user", admin);
//			session.setAttribute("message", new Message("Wrong Username or Password", "alert-danger"));
//			return "Basic/login";
//		}
//	}
}
