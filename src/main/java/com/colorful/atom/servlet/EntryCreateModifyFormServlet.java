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
        String entryTitle = request.getParameter("entryTitle");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();        
        out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+AdminServlet.cssURL+"\"/></head><body>");
        out.println("<form method=\"post\" action=\"create/entry\" >");
        out.println("<table>");
        out.println("<tr><td><span style=\"color: green;\">*</span> = Required</td><td><span style=\"color: green;\">(*)</span> = Required for parent</td></tr>");
        
        
        out.println("<tr><td>Title:*</td><td><input type=\"text\" name=\"feedTitle\" value=\"\" /></td></tr>");
        
        out.println("<tr><td>Title:<span style=\"color: green;\">*</span></td><td><input type=\"text\" name=\"feedTitle\" value=\""+title+"\" /></td></tr>");
        out.println("<tr><td>Sub Title:</td><td><input type=\"text\" name=\"feedSubTitle\" value=\""+subTitle+"\" /></td></tr>");
        
        
        out.println("<tr><td>Author:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Name:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"feedAuthorName\" value=\""+authorName[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Email:</td><td><input type=\"text\" name=\"feedAuthorEmail\" value=\""+authorEmail[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author URI:</td><td><input type=\"text\" name=\"feedAuthorURI\" value=\""+authorURI[0]+"\" /></td></tr>");
        
        
        out.println("<tr><td>Contributor:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Name:<span style=\"color: green;\">(*)</td><td><input type=\"text\" name=\"feedContributorName\" value=\""+contributorName[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Email:</td><td><input type=\"text\" name=\"feedContributorEmail\" value=\""+contributorEmail[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor URI:</td><td><input type=\"text\" name=\"feedContributorURI\" value=\""+contributorURI[0]+"\" /></td></tr>");
        
        out.println("<tr><td>Link:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path &amp; Name:<span style=\"color: green;\">(*)</span><br />(Leave blank for root)</td><td><input type=\"text\" name=\"feedLinkPath\" value=\""+href[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Rel:<span style=\"color: green;\">(*)</span><br />(ex. self)</td><td><input type=\"text\" name=\"feedLinkRel\" value=\""+rel[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Media Type:<br />(ex.  application/atom+xml or text/html)</td><td><input type=\"text\" name=\"feedLinkMediaType\" value=\""+type[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Language:<br />(ex. en-US)</td><td><input type=\"text\" name=\"feedLinkLanguage\" value=\""+hreflang[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Title:</td><td><input type=\"text\" name=\"feedLinkTitle\" value=\""+linkTitle[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Length:</td><td><input type=\"text\" name=\"feedLinkLength\" value=\""+linkLen[0]+"\" /></td></tr>");
       
        out.println("<tr><td>Category:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Term:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"feedCategoryTerm\" value=\""+categoryTerm[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Scheme URI:<br />&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedCategoryScheme\" value=\""+categorySchemeURI[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Label</td><td><input type=\"text\" name=\"feedCategoryLabel\" value=\""+categoryLabel[0]+"\" /></td></tr>");
        
        out.println("<tr><td>Generator:</td><td><input type=\"text\" name=\"feedGenerator\" value=\""+generatorStr+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Generator URI:</td><td><input type=\"text\" name=\"feedGeneratorURI\" value=\""+generatorURI+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Generator Version:</td><td><input type=\"text\" name=\"feedGeneratorVersion\" value=\""+generatorVersion+"\" /></td></tr>");
        
        out.println("<tr><td>Icon URI:(1hz to 1vt)</td><td><input type=\"text\" name=\"feedIcon\" value=\""+iconURL+"\" /></td></tr>");
        
        out.println("<tr><td>Logo URI:(2hz to 1vt)</td><td><input type=\"text\" name=\"feedLogo\" value=\""+logoURL+"\" /></td></tr>");
        
        out.println("<tr><td>Rights:</td><td><input type=\"text\" name=\"feedRights\" value=\""+rightsStr+"\" /></td></tr>");
        
        if(fileExists){
            out.println("<tr><td><input type=\"submit\" value=\"Modify Feed\" /></tr>");
        }else{
            out.println("<tr><td><input type=\"submit\" value=\"Create Feed\" /></tr>");
        }
        out.println("</table>");
        out.println("<input type=\"hidden\" name=\"formType\" value=\""+formType+"\" />");
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