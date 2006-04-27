package com.colorful.atom.servlet;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlOptions;
import org.w3.x2005.atom.AtomPersonConstruct;
import org.w3.x2005.atom.AtomTextConstruct;
import org.w3.x2005.atom.FeedDocument;
import org.w3.x2005.atom.CategoryDocument.Category;
import org.w3.x2005.atom.FeedDocument.Feed;
import org.w3.x2005.atom.GeneratorDocument.Generator;
import org.w3.x2005.atom.IconDocument.Icon;
import org.w3.x2005.atom.LinkDocument.Link;
import org.w3.x2005.atom.LogoDocument.Logo;


public class ModifyAllServlet extends HttpServlet {
    
    
    private static final long serialVersionUID = 3037012145860117978L;
    
    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String relativePath = request.getParameter("relativePath");
        try{
            //get the document.
            XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(AdminServlet.atomConfigFile)));
            Map atomConfig = (Map)decode.readObject();

            XmlOptions options = new XmlOptions();
            options.setLoadStripWhitespace();
            options.setLoadTrimTextBuffer();
            FeedDocument feedDoc = FeedDocument.Factory.parse((String)atomConfig.get(relativePath),options);
            Feed feed = feedDoc.getFeed();
            
            AtomTextConstruct[] titles = feed.getTitleArray();
            AtomTextConstruct[] subTitles = feed.getSubtitleArray();
            AtomPersonConstruct[] authors = feed.getAuthorArray();
            AtomPersonConstruct[] contributors = feed.getContributorArray();
            Link[] links = feed.getLinkArray();
            Category[] categories = feed.getCategoryArray();
            Generator[] generators = feed.getGeneratorArray();
            Icon[] icons = feed.getIconArray();
            Logo[] logos = feed.getLogoArray();
            AtomTextConstruct[] rights = feed.getRightsArray();
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter(); 
            out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+AdminServlet.cssURL+"\"/></head><body>");
            out.println("<h3><form method=\"post\" action=\"../atom/create\">Header Data <input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"submit\" value=\"Modify\" /></form></h3>");
            out.println("<table>");
            out.println("<tr><td>Title</td><td>"+titles[0].xmlText()+"</td></tr>");
            if(subTitles.length > 0 && subTitles[0] != null){
                out.println("<tr><td>Sub Title</td><td>"+subTitles[0].xmlText()+"</td></tr>");
            }
            //multiple
            String[] authorNames = authors[0].getNameArray();
            String[] authorEmails = authors[0].getEmailArray();
            String[] authorURIs = authors[0].getUriArray();
            String authorEmail = ((authorEmails.length > 0 && !authorEmails.equals(""))?", "+authorEmails[0]:"");
            String authorURI = ((authorURIs.length > 0 && !authorURIs[0].equals(""))?", "+authorURIs[0]:"");
            out.println("<tr><td>Author</td><td>"+authorNames[0]+authorEmail+authorURI+"</td></tr>");
            
            //multiple
            if(contributors.length > 0 && contributors[0] != null){
                String[] contributorsNames = contributors[0].getNameArray();
                out.println("<tr><td>Contributors</td><td>"+contributorsNames[0]+"</td></tr>");
            }
            
            //multiple
            out.println("<tr><td>Link</td><td>"+links[0].getHref().getStringValue()+", REL="+links[0].getRel().xmlText()+"</td></tr>");
           
            //multiple
            if(categories.length > 0 && categories[0] != null){
                out.println("<tr><td>Category</td><td>"+categories[0].getTerm().xmlText()+"</td></tr>");
            }
            
            if(generators.length > 0 && generators[0] != null){
                out.println("<tr><td>Generator</td><td>"+generators[0].xmlText()+"</td></tr>");
            }
            
            if(icons.length > 0 && icons[0] != null){
                out.println("<tr><td>Icon</td><td>"+icons[0].xmlText()+"</td></tr>");
            }
            
            if(logos.length > 0 && logos[0] != null){
                out.println("<tr><td>Logo</td><td>"+logos[0].xmlText()+"</td></tr>");
            }
            
            if(rights.length > 0 && rights[0] != null){
                out.println("<tr><td>Rights</td><td>"+rights[0].xmlText()+"</td></tr>");
            }
 
            out.println("</table>");
            
            out.println("<form method=\"post\" action=\"feed/build\" >");
            out.println("<table>");
            out.println("<tr><td>* = Required</td></tr>");
            
            //out.println("<tr><td>Document File Name:*</td><td><input type=\"text\" name=\"feedFileName\" value=\""+feedName+"\" /></td><td><a href=\"""+specPath+"#element.feed\" >help</a></td></tr>");
            
            
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
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path:*<br />(leave blank for web root)</td><td><input type=\"text\" name=\"feedLinkPath\" value=\"\" /></td></tr>");
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
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
}