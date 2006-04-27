package com.colorful.atom.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FeedDeleteServlet extends HttpServlet{
    
    
    private static final long serialVersionUID = 3210505776232984130L;

    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String relativePath = request.getParameter("relativePath");
        try{
            AdminServlet.removeFeed(relativePath, getServletContext());
        }catch(Exception e){
            e.printStackTrace();
        }
        response.sendRedirect("../atom");
    }
    
    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
}