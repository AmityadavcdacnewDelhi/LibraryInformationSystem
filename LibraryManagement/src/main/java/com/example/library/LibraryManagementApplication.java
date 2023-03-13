package com.example.library;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import library.captcha.CaptchaGenerator;


@SpringBootApplication
public class LibraryManagementApplication {

	// Starting of the Project
	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}

	@Bean
	public  CaptchaGenerator getCaptchaGenerator()
	{
		return new CaptchaGenerator();
	}
	
}
