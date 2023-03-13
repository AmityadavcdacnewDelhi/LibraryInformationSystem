package com.example.library.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.library.entities.User;
import com.example.library.repo.UserRepository;
import com.example.library.service.EmailService;

@Controller
public class ForgetpasswordController {
	
	Random random= new Random(1001);
    @Autowired
	private EmailService emailService;
    @Autowired
    UserRepository userRepository;
    String email;
	
	
@RequestMapping("/forget")	
public String openEmailForm()
{
	return "Basic/forget_email-form";
//	return "Basic/forgetpassword";
}

@PostMapping("/send-otp")
public String sendOtp(@RequestParam("email")String email, HttpSession session)
{
	System.out.println("Email");
	// generating otp of four digit
	
//	Random random= new Random(1000); //minimum four digit value
	
	int otp = random.nextInt(99999); //upper bound
	System.out.println( "otp is "+ otp);
	
	// write code to send otp to email...
	String subject ="otp from  Library Information system";
	String message =""
			+"<div style='border:1px solid green;padding:20px'>"
			+"<h2>"
			+"OTP is"
			+"</h2>"
			+ otp
			+"</div>";
			
	      
	String to= email;
	
	boolean flag= this.emailService.sendEmail(subject, message, to);
	
	if(flag)
	{
	//	return "Basic/verify_otp";
		session.setAttribute("myotp", otp);
		session.setAttribute("email",email);
		return "Basic/verify_otp";
	}
	else
	{
	//	session.setAttribute("message","check email is");
		return "Basic/forget_email-form";
	//	return "Basic/verify_otp";
		
	}
		
}

//verify otp
@PostMapping("/verify-otp")
public String verifyOtp(@RequestParam("otp") int otp, HttpSession session)
{
	int myOtp = (int) session.getAttribute("myotp");
	 email =(String)session.getAttribute("email");
	if(myOtp==otp)
	{
		
//	User user=	this.userRepository.getUserByUserName(email);
	//if(user==null)
	//{
		 // send error message
		session.setAttribute("message", "User does not exist");
		//return "Basic/forget_email-form";
		
	//}
	//else
	//{
		// send change password form
		
	//}
		
		return "Basic/change_email_form";	
	
	}
	else
	{
		session.setAttribute("message", "you have entered wrong otp");
		return "Basic/verify_otp";
	}
	
	
		
}

// implement interface
//public String  getEmail(@Param("email")String email)
//{
//	return email;	
//}

// change password 
//@PostMapping("/change_password")
//public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
//public String changePassword()
{
//	String email =(String)session.getAttribute("email");
//	User user = this.userRepository.getEmail(email);
//	user.setPassword(newpassword);
//    this.userRepository.save(user);
	
//	return "Basic/home";
	
}

}
