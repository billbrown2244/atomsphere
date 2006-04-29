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
import org.w3.x2005.atom.EntryDocument.Entry;
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
            Entry[] entries = feed.getEntryArray();
            
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
            out.println("<tr><td>Title</td><td>"+titles[0].getDomNode().getFirstChild().getNodeValue()+"</td></tr>");
            if(subTitles.length > 0 && subTitles[0] != null){
                out.println("<tr><td>Sub Title</td><td>"+subTitles[0].getDomNode().getFirstChild().getNodeValue()+"</td></tr>");
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
            out.println("<tr><td>Link</td><td>"+links[0].getHref().getStringValue()+", REL="+links[0].getRel().getDomNode().getNodeValue()+"</td></tr>");
           
            //multiple
            if(categories.length > 0 && categories[0] != null){
                out.println("<tr><td>Category</td><td>"+categories[0].getTerm().getDomNode().getNodeValue()+"</td></tr>");
            }
            
            if(generators.length > 0 && generators[0] != null){
                out.println("<tr><td>Generator</td><td>"+generators[0].getDomNode().getFirstChild().getNodeValue()+"</td></tr>");
            }
            
            if(icons.length > 0 && icons[0] != null){
                out.println("<tr><td>Icon</td><td>"+icons[0].getDomNode().getFirstChild().getNodeValue()+"</td></tr>");
            }
            
            if(logos.length > 0 && logos[0] != null){
                out.println("<tr><td>Logo</td><td>"+logos[0].getDomNode().getFirstChild().getNodeValue()+"</td></tr>");
            }
            
            if(rights.length > 0 && rights[0] != null){
                out.println("<tr><td>Rights</td><td>"+rights[0].getDomNode().getFirstChild().getNodeValue()+"</td></tr>");
            }
 
            out.println("</table>");
            //create new entry;
            
            //list existing entries for that feed.
            for(int i=0; i < entries.length; i++){
                Entry entry = entries[i];
            }
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