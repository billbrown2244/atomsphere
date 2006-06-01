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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.colorful.atom.beans.Author;
import com.colorful.atom.beans.Category;
import com.colorful.atom.beans.Contributor;
import com.colorful.atom.beans.Entry;
import com.colorful.atom.beans.FeedDoc;
import com.colorful.atom.beans.Feed;
import com.colorful.atom.beans.Generator;
import com.colorful.atom.beans.Icon;
import com.colorful.atom.beans.Logo;
import com.colorful.atom.beans.Rights;
import com.colorful.atom.beans.Subtitle;
import com.colorful.atom.beans.Title;
import com.colorful.atom.beans.Link;

public class ModifyAllServlet extends HttpServlet {
    
    
    private static final long serialVersionUID = 3037012145860117978L;
    
    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String relativePath = request.getParameter("relativePath");
        try{

            //get the document file
            String fullPath = getServletContext().getRealPath(relativePath);
            Feed feed = FeedDoc.readFeedDoc(fullPath,true);

            Title title = feed.getTitle();
            Subtitle subtitle = feed.getSubtitle();
            List authors = feed.getAuthors();
            List contributors = feed.getContributors();
            List links = feed.getLinks();
            List categories = feed.getCategories();
            Generator generator = feed.getGenerator();
            Icon icon = feed.getIcon();
            Logo logo = feed.getLogo();
            Rights rights = feed.getRights();
            Map entries = feed.getEntries();
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter(); 
            out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+AdminServlet.cssURL+"\"/></head><body>");
            out.println("<h3><form method=\"post\" action=\"../atom/create\">Header Data <input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"submit\" value=\"Modify\" /></form></h3>");
            out.println("<table>");
            
            //list title
            out.println("<tr><td>Title</td><td>"+title.getText()+"</td></tr>");
            
            //list subtitle
            if(subtitle != null){
                out.println("<tr><td>Sub Title</td><td>"+subtitle.getText()+"</td></tr>");
            }
            
            //list the authors
            if(authors != null){
                Iterator authorItr = authors.iterator();
                while(authorItr.hasNext()){
                    Author author = (Author)authorItr.next();
                    out.println("<tr><td>Author</td><td>"+author.getName().getText()+"</td></tr>");
                }
            }
            
            //list the contributors
            if(contributors != null){
                Iterator contributorItr = contributors.iterator();
                while(contributorItr.hasNext()){
                    Contributor contributor = (Contributor)contributorItr.next();
                    out.println("<tr><td>Contributor</td><td>"+contributor.getName().getText()+"</td></tr>");
                }
            }
            
            //list the links
            if(links != null){
                Iterator linkItr = links.iterator();
                while(linkItr.hasNext()){
                    Link link = (Link)linkItr.next();
                    out.println("<tr><td>Link</td><td>"+link.getHref().getValue()+", REL="+link.getRel().getValue()+"</td></tr>");
                }
            }
           
            //list the categories
            if(categories != null){
                Iterator categoryItr = categories.iterator();
                while(categoryItr.hasNext()){
                    Category category = (Category)categoryItr.next();
                    out.println("<tr><td>Category</td><td>"+category.getTerm().getValue()+"</td></tr>");
                }
            }

            //list the generator
            if(generator != null){
                out.println("<tr><td>Generator</td><td>"+generator.getText()+"</td></tr>");
            }
            
            //list the icon
            if(icon != null){
                out.println("<tr><td>Icon</td><td>"+icon.getText()+"</td></tr>");
            }
            
            //list the logo
            if(logo != null){
                out.println("<tr><td>Logo</td><td>"+logo.getText()+"</td></tr>");
            }
            
            //list the rights
            if(rights != null){
                out.println("<tr><td>Rights</td><td>"+rights.getText()+"</td></tr>");
            }
 
            out.println("</table>");
            
            //create new entry;
            out.println("<form method=\"post\" action=\"modify/create\" >");
            out.println("<table><tr><td>Entry Title:</td><td><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input name=\"entryTitle\" value=\"\" /></td><td><input type=\"submit\" value=\"New\" /></td></tr></table>");
            out.println("</form>");
            
            //list existing entries for that feed.
            if(entries != null){
            Iterator entryItr = entries.keySet().iterator();
            while(entryItr.hasNext()){
                Entry entry = (Entry)entries.get(entryItr.next());
                out.println("<table><tr>"); 
                out.println("<td>"+entry.getTitle().getText()+"</td>");
                out.println("<td><form method=\"post\" action=\"modify/create\"><input type=\"hidden\" name=\"entryTitle\" value=\""+entry.getTitle().getText()+"\" /><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"hidden\" name=\"updated\" value=\""+entry.getUpdated().getText()+"\" /><input type=\"submit\" value=\"Modify\" /></form></td>");
                out.println("<td><form method=\"post\" action=\"modify/delete\"><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"hidden\" name=\"updated\" value=\""+entry.getUpdated().getText()+"\" /><input type=\"submit\" value=\"Delete\" /></form></td>");
                out.println("</tr></table>");
            }
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