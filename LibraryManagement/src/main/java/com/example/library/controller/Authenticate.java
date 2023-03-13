package com.example.library.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PreparedStatement ps;
	Connection connection;
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 connection = DriverManager.getConnection("jdbc:mysql://localhost/employee","root","cdac");
			 ps= connection.prepareStatement("select * from User where username=? and password=?");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		
		try {
			if(ps!=null)
				ps.close();
			if(connection!=null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String username= request.getParameter("username");
		String password= request.getParameter("password");
		
		     try {
				ps.clearParameters();
				 ps.setString(1, username);
				 ps.setString(2,password);
				 try(ResultSet result = ps.executeQuery())
				 {
					 if(result.next())
					 {
						 HttpSession session = request.getSession();
						 session.setAttribute("username", username);
						 response.sendRedirect("Basic/dashboard.html");
						 System.out.println("Successful");
					 }
					 else
					
					out.println("Authentication Failed");
					
				 }
			} catch (SQLException e) {
			//	TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			}
		   
	    }
	}



