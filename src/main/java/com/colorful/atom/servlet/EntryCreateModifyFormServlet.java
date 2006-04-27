package com.colorful.atom.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EntryCreateModifyFormServlet extends HttpServlet {
    

    
    
    private static final long serialVersionUID = -9222153218454275735L;

    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String feedName = request.getParameter("feedName");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();        
        out.println("<html><head><title>atom feed</title></head><body>");
        out.println("<form method=\"post\" action=\"feed/build\" >");
        out.println("<table>");
        out.println("<tr><td>* = Required</td></tr>");
        
        //out.println("<tr><td>Document File Name:*</td><td><input type=\"text\" name=\"feedFileName\" value=\""+feedName+"\" /></td><td><a href=\""+specPath+"#element.feed\" >help</a></td></tr>");
        
        
        out.println("<tr><td>Title:*</td><td><input type=\"text\" name=\"feedTitle\" value=\"\" /></td></tr>");
        out.println("<tr><td>Sub Title:</td><td><input type=\"text\" name=\"feedSubTitle\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Author:*</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Name:*</td><td><input type=\"text\" name=\"feedAuthorName\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Email:</td><td><input type=\"text\" name=\"feedAuthorEmail\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author URI:</td><td><input type=\"text\" name=\"feedAuthorURI\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Contributor:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Name:*</td><td><input type=\"text\" name=\"feedContributorName\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Email:</td><td><input type=\"text\" name=\"feedContributorEmail\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor URI:</td><td><input type=\"text\" name=\"feedContributorURI\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Link:*</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path:*<br />(leave default for web root)</td><td><input type=\"text\" name=\"feedLinkPath\" value=\"/\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Rel:*</td><td><input type=\"text\" name=\"feedLinkRel\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Media Type:<br />(ex.  application/atom+xml or text/html)</td><td><input type=\"text\" name=\"feedLinkMediaType\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Language:<br />(ex. en-US)</td><td><input type=\"text\" name=\"feedLinkLanguage\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Title:</td><td><input type=\"text\" name=\"feedLinkTitle\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Length:</td><td><input type=\"text\" name=\"feedLinkLength\" value=\"\" /></td></tr>");
       
        out.println("<tr><td>Category:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Term:*</td><td><input type=\"text\" name=\"feedCategoryTerm\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Scheme:</td><td><input type=\"text\" name=\"feedCategoryScheme\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Label</td><td><input type=\"text\" name=\"feedCategoryLabel\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Generator:</td><td><input type=\"text\" name=\"feedGenerator\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Generator URI:</td><td><input type=\"text\" name=\"feedGeneratorURI\" value=\"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Generator Version:</td><td><input type=\"text\" name=\"feedGeneratorVersion\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Icon URI Path:</td><td><input type=\"text\" name=\"feedIcon\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Logo URI Path:(2hz to 1vt)</td><td><input type=\"text\" name=\"feedLogo\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Rights:</td><td><input type=\"text\" name=\"feedRights\" value=\"\" /></td></tr>");
        
        out.println("<tr><td><input type=\"submit\" value=\"Build Atom Feed\" /></tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body></html>");

        out.flush();
        out.close();

    }
    
    
    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
}