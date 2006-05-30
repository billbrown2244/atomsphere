/*
Atomsphere - an atom feed library.
Copyright (C) 2006 William R. Brown.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.colorful.atom.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

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
import org.w3.x2005.atom.LinkDocument.Link;


public class EntryCreateModifyFormServlet extends HttpServlet {
    

    
    
    private static final long serialVersionUID = -9222153218454275735L;

    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String entryTitle = request.getParameter("entryTitle");
        String relativePath = request.getParameter("relativePath");
        boolean entryExists = false; 
        
        try{
            
            //required 
            //can have multiple
            AtomPersonConstruct[] authors = null;
            String[] authorName = null;
            String[] authorEmail = null;
            String[] authorURI = null;
            
            //optional - can have multiple
            AtomPersonConstruct[] contributors = null;
            String[] contributorName = null;
            String[] contributorEmail = null;
            String[] contributorURI = null;
        
            //optional - can have multiple
            Category[] categories = null;
            String[] categoryTerm = null;
            String[] categorySchemeURI = null;
            String[] categoryLabel = null;
            
            //optional - can have multiple. (cannot have more than one alternate).
            Link[] links = null;
            String[] href = null;
            String[] rel = null;
            String[] type = null;
            String[] hreflang = null;
            String[] linkTitle = null;
            String[] linkLen = null;
            
            //optional
            AtomTextConstruct[] rights = null;
            String rightsStr = null;
            
            //optional
            AtomTextConstruct[] summary = null;
            String summaryStr = null;
            
            
            //determines the type of entry we are working with; new or existing
            String formType = null;
            
            //open the file to get the entrys.
            XmlOptions options = new XmlOptions();
            options.setLoadStripWhitespace();
            options.setLoadTrimTextBuffer();
            FeedDocument entryDoc = FeedDocument.Factory.parse(new File(getServletContext().getRealPath(relativePath)),options);
            Feed feed = entryDoc.getFeed();
            Entry[] entries = feed.getEntryArray();
            Entry entry = null;
            int entryIndex = 0;
            for(int i=0; i < entries.length; i++){
                AtomTextConstruct title = entries[i].getTitleArray(0);
                String titleVal = title.getDomNode().getFirstChild().getNodeValue();
                if(titleVal.equals(entryTitle)){
                    entryIndex = i;
                    entryExists = true;
                    entry = entries[i];
                }
            }
            if(entryExists){
                formType = "modify";
                //get the list of authors
                authors = entry.getAuthorArray();
                authorName = new String[authors.length];
                authorEmail = new String[authors.length];
                authorURI = new String[authors.length];
                for(int i=0; i < authors.length; i++){
                    authorName[i] = authors[i].getNameArray()[0];
                    if(authors[i].getEmailArray().length == 0){
                        authorEmail[i] = "";
                    }else{
                        authorEmail[i] = authors[i].getEmailArray()[0];
                    }
                    if(authors[i].getUriArray().length == 0){
                        authorURI[i] = "";
                    }else{
                        authorURI[i] = authors[i].getUriArray()[0];
                    }
                }
                
                //get the list of contributors
                contributors = entry.getContributorArray();
                if(contributors.length == 0){
                    contributorName = new String[1];
                    contributorEmail = new String[1];
                    contributorURI = new String[1];
                    contributorName[0] = "";
                    contributorEmail[0] = "";
                    contributorURI[0] = "";
                }else{
                contributorName = new String[contributors.length];
                contributorEmail = new String[contributors.length];
                contributorURI = new String[contributors.length];
                }
                for(int i=0; i < contributors.length; i++){
                    if(contributors[i].getNameArray().length == 0){
                        contributorName[i] = "";
                    }else{
                        contributorName[i] = contributors[i].getNameArray()[0];
                    }
                    if(contributors[i].getEmailArray().length == 0){
                        contributorEmail[i] = "";
                    }else{
                        contributorEmail[i] = contributors[i].getEmailArray()[0];
                    }
                    if(contributors[i].getUriArray().length == 0){
                        contributorURI[i] = "";
                    }else{
                        contributorURI[i] = contributors[i].getUriArray()[0];
                    }
                }
                
                
                //get the list of categories
                categories = entry.getCategoryArray();
                categoryTerm = new String[categories.length];
                categorySchemeURI = new String[categories.length];
                categoryLabel = new String[categories.length];
                for(int i=0; i < categories.length; i++){
                    if(categories[i].getTerm() == null){
                        categoryTerm[i] = "";
                    }else{
                        categoryTerm[i] = categories[i].getTerm().getDomNode().getFirstChild().getNodeValue();
                    }
                    if(categories[i].getScheme() == null){
                        categorySchemeURI[i] = "";
                    }else{
                        categorySchemeURI[i] = categories[i].getScheme().getDomNode().getFirstChild().getNodeValue();
                    }
                    if(categories[i].getLabel() == null){
                        categoryLabel[i] = "";
                    }else{
                        categoryLabel[i] = categories[i].getLabel().getDomNode().getFirstChild().getNodeValue();
                    }
                }
                
                //get the list of links.
                links = entry.getLinkArray();
                href = new String[links.length];
                rel = new String[links.length];
                type = new String[links.length];
                hreflang = new String[links.length];
                linkTitle = new String[links.length];
                linkLen = new String[links.length];
                for(int i=0; i < links.length; i++){
                    if(links[i].getHref() == null){
                        href[i] = "";
                    }else{
                        href[i] = links[i].getHref().getDomNode().getFirstChild().getNodeValue();
                        href[i] = href[i].substring(AdminServlet.docRootURL.length());
                        if(href[i].startsWith("/")){
                            href[i] = href[i].substring(1);
                        }
                    }
                    if(links[i].getRel() == null){
                        rel[i] = "";
                    }else{
                        rel[i] = links[i].getRel().getDomNode().getFirstChild().getNodeValue();
                    }
                    if(links[i].getType() == null){
                        type[i] = "";
                    }else{
                        type[i] = links[i].getType();
                    }
                    if(links[i].getHreflang() == null){
                        hreflang[i] = "";
                    }else{
                        hreflang[i] = links[i].getHreflang();
                    }
                    if(links[i].getTitle() == null){
                        linkTitle[i] = "";
                    }else{
                        linkTitle[i] = links[i].getTitle().getDomNode().getFirstChild().getNodeValue();
                    }
                    if(links[i].getLength() == null){
                        linkLen[i] = "";
                    }else{
                        linkLen[i] = links[i].getLength().getDomNode().getFirstChild().getNodeValue();
                    }
                }
                
                rights = entry.getRightsArray();
                if(rights.length == 0){
                    rightsStr = "";
                }else{
                    rightsStr = rights[0].getDomNode().getFirstChild().getNodeValue();
                }
                
                summary = entry.getSummaryArray();
                if(summary.length == 0){
                    summaryStr = "";
                }else{
                    summaryStr = summary[0].getDomNode().getFirstChild().getNodeValue();
                }
                
            }else{
                formType = "create";
            entry = Entry.Factory.newInstance();
            
           // title = "";
            summaryStr = "";
            authorName = new String[]{""};
            authorEmail = new String[]{""};
            authorURI = new String[]{""};
            contributorName = new String[]{""};
            contributorEmail = new String[]{""};
            contributorURI = new String[]{""};
            href = new String[]{""};
            rel = new String[]{""};
            type = new String[]{""};
            hreflang = new String[]{""};
            linkTitle = new String[]{""};
            linkLen = new String[]{""};
            categoryTerm = new String[]{""};
            categorySchemeURI = new String[]{""};
            categoryLabel = new String[]{""};
            rightsStr = "";
            }
        

        
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();        
        out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+AdminServlet.cssURL+"\"/>");
        ResourceBundle bundle = ResourceBundle.getBundle("atomsphere",Locale.getDefault(),this.getClass().getClassLoader());
        out.println(bundle.getString("javascript"));
        
        out.println("</head><body>");
        out.println("<form method=\"post\" action=\"create/entry\" >");
        out.println("<table>");
        out.println("<tr><td><span style=\"color: green;\">*</span> = Required</td><td><span style=\"color: green;\">(*)</span> = Required for parent</td></tr>");
        
        
        out.println("<tr><td>Title:<span style=\"color: green;\">*</span></td><td><input type=\"text\" name=\"entryTitle\" value=\""+entryTitle+"\" /></td></tr>");
        
        out.println("<tr><td>Summary:</td><td><input type=\"text\" name=\"entrySummary\" value=\""+summaryStr+"\" /></td></tr>");
        
        out.println("<tr><td>Content:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td><select id=\"contentType\" name=\"contentType\" onchange=\"showInput()\"><option value=\"*\">:::Select:::</option><option>text</option><option>html</option><option>xhtml</option><option>link</option><option>other</option></select></td></tr>");
        out.println("<tr><td><div id=\"inputArea\"></div>");
        
        out.println("<tr><td>Author:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Name:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"entryAuthorName\" value=\""+authorName[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Email:</td><td><input type=\"text\" name=\"entryAuthorEmail\" value=\""+authorEmail[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author URI:</td><td><input type=\"text\" name=\"entryAuthorURI\" value=\""+authorURI[0]+"\" /></td></tr>");

        out.println("<tr><td>Contributor:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Name:<span style=\"color: green;\">(*)</td><td><input type=\"text\" name=\"entryContributorName\" value=\""+contributorName[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Email:</td><td><input type=\"text\" name=\"entryContributorEmail\" value=\""+contributorEmail[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor URI:</td><td><input type=\"text\" name=\"entryContributorURI\" value=\""+contributorURI[0]+"\" /></td></tr>");
 
        out.println("<tr><td>Link:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path &amp; Name:<span style=\"color: green;\">(*)</span><br />(Leave blank for root)</td><td><input type=\"text\" name=\"entryLinkPath\" value=\""+href[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Rel:<span style=\"color: green;\">(*)</span><br />(ex. self)</td><td><input type=\"text\" name=\"entryLinkRel\" value=\""+rel[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Media Type:<br />(ex.  application/atom+xml or text/html)</td><td><input type=\"text\" name=\"entryLinkMediaType\" value=\""+type[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Language:<br />(ex. en-US)</td><td><input type=\"text\" name=\"entryLinkLanguage\" value=\""+hreflang[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Title:</td><td><input type=\"text\" name=\"entryLinkTitle\" value=\""+linkTitle[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Length:</td><td><input type=\"text\" name=\"entryLinkLength\" value=\""+linkLen[0]+"\" /></td></tr>");
      
        out.println("<tr><td>Category:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Term:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"entryCategoryTerm\" value=\""+categoryTerm[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Scheme URI:<br />&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"entryCategoryScheme\" value=\""+categorySchemeURI[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Label</td><td><input type=\"text\" name=\"entryCategoryLabel\" value=\""+categoryLabel[0]+"\" /></td></tr>");
         
        out.println("<tr><td>Rights:</td><td><input type=\"text\" name=\"entryRights\" value=\""+rightsStr+"\" /></td></tr>");
        
        
        if(entryExists){
            out.println("<tr><td><input type=\"submit\" value=\"Modify Entry\" /></tr>");
        }else{
            out.println("<tr><td><input type=\"submit\" value=\"Create Entry\" /></tr>");
        }
        out.println("</table>");
        out.println("<input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" />");
        out.println("<input type=\"hidden\" name=\"formType\" value=\""+formType+"\" />");
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