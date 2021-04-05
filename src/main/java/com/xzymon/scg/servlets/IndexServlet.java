package com.xzymon.scg.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class IndexServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("<html><body>");
		out.println("<h1>Simple Card Game</h1>");
		out.println("<p>The time is : " + new Date() + "</p>");
		out.println("</body></html>");
		out.close();
	}

}