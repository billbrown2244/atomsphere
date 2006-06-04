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
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.colorful.atom.beans.Attribute;
import com.colorful.atom.beans.Author;
import com.colorful.atom.beans.Category;
import com.colorful.atom.beans.Content;
import com.colorful.atom.beans.Contributor;
import com.colorful.atom.beans.Email;
import com.colorful.atom.beans.Entry;
import com.colorful.atom.beans.Feed;
import com.colorful.atom.beans.FeedDoc;
import com.colorful.atom.beans.Link;
import com.colorful.atom.beans.Name;
import com.colorful.atom.beans.Rights;
import com.colorful.atom.beans.Summary;
import com.colorful.atom.beans.Title;
import com.colorful.atom.beans.URI;


public class EntryCreateModifyFormServlet extends HttpServlet {
    
    
    
    
    private static final long serialVersionUID = -9222153218454275735L;
    
    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String entryTitle = request.getParameter("entryTitle");
        String relativePath = request.getParameter("relativePath");
        String updated = request.getParameter("updated");
        boolean entryExists = false; 
        String formType = "create";
        
        try{
            
            //open the file to get the entrys.            
            Feed feed = FeedDoc.readFeedDoc(getServletContext().getRealPath(relativePath),true);
            Map entries = feed.getEntries();
            Entry entry = null;
            
            //check to see if this entry exists 
            //if not, we are creating a new one.
            System.out.println("updated = "+updated);
            if(entries != null){ 
                entry = (Entry)entries.get(updated);
                if( entry != null){                        
                    entryExists = true;
                }
            }

            
            if(entryExists){
                formType = "modify";
                if(entry.getSummary() == null){
                    entry.setSummary(new Summary());
                }
                if(entry.getContent() == null){
                    entry.setContent(new Content());
                }
                if(entry.getAuthors() == null){
                    entry.addAuthor(new Author());
                }
                if(entry.getContributors() == null){
                    entry.addContributor(new Contributor());
                }
                if(entry.getLinks() == null){
                    entry.addLink(new Link());
                }
                if(entry.getCategories() == null){
                    entry.addCategory(new Category()); 
                }
                if(entry.getRights() == null){
                    entry.setRights(new Rights());
                }
            }else{
                entry = new Entry();
                entry.setTitle(new Title());
                entry.setSummary(new Summary());
                entry.setContent(new Content());
                entry.addAuthor(new Author());
                entry.addContributor(new Contributor());
                entry.addLink(new Link());
                entry.addCategory(new Category());
                entry.setRights(new Rights());
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
            
            //print the title
            if(entry.getTitle().getText().equals("")){
                entry.getTitle().setText(entryTitle);
            }
            out.println("<tr><td>Title:<span style=\"color: green;\">*</span></td><td><input type=\"text\" name=\"entryTitle\" value=\""+entry.getTitle().getText()+"\" /></td></tr>");
            
            //print the summary
            out.println("<tr><td>Summary:</td><td><input type=\"text\" name=\"entrySummary\" value=\""+entry.getSummary().getText()+"\" /></td></tr>");
            
            //print the content
            Content content = entry.getContent();
            if(content.getContent() == null){
                content.setContent("");
            }
            String type = "*";
            boolean isLink = false;
            if(content.getAttributes() != null){
                //look for the type attribute
                Iterator attrItr = content.getAttributes().iterator();
                while(attrItr.hasNext()){
                    Attribute attr = (Attribute)attrItr.next();
                    if(attr.getName().equals("type")){
                        type = attr.getValue();
                        break;
                    }
                }
                //look for the src attribute
                attrItr = content.getAttributes().iterator();
                while(attrItr.hasNext()){
                    Attribute attr = (Attribute)attrItr.next();
                    if(attr.getName().equals("src")){
                        isLink = true;
                        break;
                    }
                }
            }
            out.println("<tr><td>Content:<span style=\"color: green;\">*</span></td></tr>");
            out.println("<tr><td><select id=\"contentType\" name=\"contentType\" onchange=\"showInput()\">");
            out.println("<option value=\"*\">:::Select:::</option>");
            if(type.equals("text")){
                out.println("<option selected=\"true\">text</option>");
            }else{
                out.println("<option>text</option>");
            }
            if(type.equals("html")){
                out.println("<option selected=\"true\">html</option>");
            }else{
                out.println("<option>html</option>");
            }
            if(type.equals("xhtml")){
                out.println("<option selected=\"true\">xhtml</option>");
            }else{
                out.println("<option>xhtml</option>");
            }
            if(isLink){
                out.println("<option selected=\"true\">link</option>");
            }else{
                out.println("<option>link</option>");
            }
            if(!type.equals("*") && !type.equals("text") && !type.equals("html") && !type.equals("xhtml") && !isLink){
                out.println("<option selected=\"true\">other</option>");
            }else{
                out.println("<option>other</option>");
            }
            out.println("</select></td></tr>");
            if(isLink){//this is a link
                out.println("<tr><td><div id=\"inputArea\">http://<input type=\"text\" name =\"content\" value=\""+content.getContent()+"\" /></div></td></tr>"); 
            }else{
                if(!type.equals("*")){
                    out.println("<tr><td><div id=\"inputArea\"><textarea name=\"content\" rows=\"6\" cols=\"40\">"+content.getContent()+"</textarea></div></td></tr>");
                }else{
                    out.println("<tr><td><div id=\"inputArea\"></div></td></tr>");
                }
            }
            
            
            //print the first author
            Author author = (Author)entry.getAuthors().get(0);
            if(author.getName() == null){
                author.setName(new Name());
            }
            if(author.getUri() == null){
                author.setUri(new URI());
            }
            if(author.getEmail() == null){
                author.setEmail(new Email());
            }
            out.println("<tr><td>Author:<span style=\"color: green;\">*</span></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Name:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"entryAuthorName\" value=\""+author.getName().getText()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Email:</td><td><input type=\"text\" name=\"entryAuthorEmail\" value=\""+author.getEmail().getText()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author URI:</td><td><input type=\"text\" name=\"entryAuthorURI\" value=\""+author.getUri().getText()+"\" /></td></tr>");
            
            
            //print the first contributor
            Contributor contributor = (Contributor)entry.getContributors().get(0);
            if(contributor.getName() == null){
                contributor.setName(new Name());
            }
            if(contributor.getUri() == null){
                contributor.setUri(new URI());
            }
            if(contributor.getEmail() == null){
                contributor.setEmail(new Email());
            }
            out.println("<tr><td>Contributor:<span style=\"color: green;\">*</span></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Name:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"entryAuthorName\" value=\""+contributor.getName().getText()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Email:</td><td><input type=\"text\" name=\"entryAuthorEmail\" value=\""+contributor.getEmail().getText()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor URI:</td><td><input type=\"text\" name=\"entryAuthorURI\" value=\""+contributor.getUri().getText()+"\" /></td></tr>");
            
            //print the first link
            Link link = (Link)entry.getLinks().get(0);            
            if(link.getHref() == null){
                link.setHref(new Attribute("href"));
            }
            String path = "";
            if(!link.getHref().getValue().equals("")){
                path = link.getHref().getValue().substring(link.getHref().getValue().lastIndexOf(File.separator));
            }
            if(link.getRel() == null){
                link.setRel(new Attribute("rel"));
            }
            if(link.getType() == null){
                link.setType(new Attribute("type"));
            }
            if(link.getHreflang() == null){
                link.setHreflang(new Attribute("hreflang"));
            }
            if(link.getTitle() == null){
                link.setTitle(new Attribute("title"));
            }
            if(link.getLength() == null){
                link.setLength(new Attribute("length"));
            }
            out.println("<tr><td>Link:<span style=\"color: green;\">*</span></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path &amp; Name:<span style=\"color: green;\">(*)</span><br />(Leave blank for root)</td><td><input type=\"text\" name=\"entryLinkPath\" value=\""+path+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Rel:<span style=\"color: green;\">(*)</span><br />(ex. self)</td><td><input type=\"text\" name=\"entryLinkRel\" value=\""+link.getRel().getValue()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Media Type:<br />(ex.  application/atom+xml or text/html)</td><td><input type=\"text\" name=\"entryLinkMediaType\" value=\""+link.getType().getValue()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Language:<br />(ex. en-US)</td><td><input type=\"text\" name=\"entryLinkLanguage\" value=\""+link.getHreflang().getValue()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Title:</td><td><input type=\"text\" name=\"entryLinkTitle\" value=\""+link.getTitle().getValue()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Length:</td><td><input type=\"text\" name=\"entryLinkLength\" value=\""+link.getLength().getValue()+"\" /></td></tr>");
            
            
            //print the category
            Category category = (Category)entry.getCategories().get(0);
            if(category.getTerm() == null){
                category.setTerm(new Attribute("term"));
            }
            
            if(category.getScheme() == null){
                category.setScheme(new Attribute("scheme"));
            }
            if(category.getLabel() == null){
                category.setLabel(new Attribute("label"));
            }
            out.println("<tr><td>Category:</td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Term:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"entryCategoryTerm\" value=\""+category.getTerm().getValue()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Scheme URI:<br />&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"entryCategoryScheme\" value=\""+category.getScheme().getValue()+"\" /></td></tr>");
            out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Label</td><td><input type=\"text\" name=\"entryCategoryLabel\" value=\""+category.getLabel().getValue()+"\" /></td></tr>");
            
            
            //print the rights
            out.println("<tr><td>Rights:</td><td><input type=\"text\" name=\"entryRights\" value=\""+entry.getRights().getText()+"\" /></td></tr>");
            
            
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