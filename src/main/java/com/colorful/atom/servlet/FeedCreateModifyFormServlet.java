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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.colorful.atom.beans.Attribute;
import com.colorful.atom.beans.Author;
import com.colorful.atom.beans.Category;
import com.colorful.atom.beans.Contributor;
import com.colorful.atom.beans.Email;
import com.colorful.atom.beans.Feed;
import com.colorful.atom.beans.FeedDoc;
import com.colorful.atom.beans.Icon;
import com.colorful.atom.beans.Link;
import com.colorful.atom.beans.Logo;
import com.colorful.atom.beans.Name;
import com.colorful.atom.beans.Rights;
import com.colorful.atom.beans.Subtitle;
import com.colorful.atom.beans.Title;
import com.colorful.atom.beans.URI;

public class FeedCreateModifyFormServlet extends HttpServlet {
    

    
    private static final long serialVersionUID = 6072923458769031495L;

    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String relativePath = request.getParameter("relativePath");
        System.out.println("relativePath = "+relativePath);
        if(relativePath.startsWith(File.separator) || relativePath.startsWith("/")){
            relativePath = relativePath.substring(1);
        }
        boolean fileExists = false;
        if(new File(getServletContext().getRealPath(relativePath)).exists()){
            fileExists = true;
        }

        try{
            String formType = null;
            Feed feed = null;
        if(fileExists){//this is an existing document
            feed = FeedDoc.readFeedDoc(getServletContext().getRealPath(relativePath),true);
            formType = "modify";
            
            if(feed.getSubtitle() == null){
                feed.setSubtitle(new Subtitle());
            }
            
            if(feed.getAuthors() == null){
                feed.addAuthor(new Author(""));
            }
            if(feed.getContributors() == null){
                feed.addContributor(new Contributor(""));
            }
            
            if(feed.getLinks() == null){
                feed.addLink(new Link(""));
            }
            
            if(feed.getCategories() == null){
                feed.addCategory(new Category("")); 
            }
            
            if(feed.getIcon() == null){
                feed.setIcon(new Icon());
            }
            
            if(feed.getLogo() == null){
                feed.setLogo(new Logo());
            }
            
            if(feed.getRights() == null){
                feed.setRights(new Rights());
            }
             
        }else{//this is a create request so set everything to blank but the relative path. 
            feed = new Feed(FeedDoc.atomBase,FeedDoc.lang_en);
            formType = "create";
            
            feed.setTitle(new Title());
            feed.setSubtitle(new Subtitle());
            feed.addAuthor(new Author(""));
            feed.addContributor(new Contributor(""));
            feed.addLink(new Link(""));
            feed.addCategory(new Category(""));
            feed.setIcon(new Icon());
            feed.setLogo(new Logo());
            feed.setRights(new Rights());
        }

        //output the form.
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();        
        out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+AdminServlet.cssURL+"\"/></head><body>");
        out.println("<a href=\""+AdminServlet.docRootURL+"\" ><h3>Feed Admin Page</h3></a>");
        out.println("<form method=\"post\" action=\"create/feed\" >");

        out.println("<table>");
        out.println("<tr><td><span style=\"color: green;\">*</span> = Required</td><td><span style=\"color: green;\">(*)</span> = Required for parent</td></tr>");
        
        if(fileExists){//make the field read only
        out.println("<tr><td>Feed Path &amp; Name:*</td><td><input type=\"text\" name=\"relativePath\" value=\""+relativePath+"\" readonly=\"readonly\" /></td><td><a href=\""+AdminServlet.atomSpecURL+"#element.feed\" >help</a></td></tr>");
        }else{
        out.println("<tr><td>Feed Path &amp; Name:*</td><td><input type=\"text\" name=\"relativePath\" value=\""+relativePath+"\" /></td><td><a href=\""+AdminServlet.atomSpecURL+"#element.feed\" >help</a></td></tr>");
        }
        
        //print title
        out.println("<tr><td>Title:<span style=\"color: green;\">*</span></td><td><input type=\"text\" name=\"feedTitle\" value=\""+feed.getTitle().getText()+"\" /></td></tr>");
        
        //print sub title
        out.println("<tr><td>Sub Title:</td><td><input type=\"text\" name=\"feedSubTitle\" value=\""+feed.getSubtitle().getText()+"\" /></td></tr>");
        
        //print author.
        Author author = (Author)feed.getAuthors().get(0);
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
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Name:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"feedAuthorName\" value=\""+author.getName().getText()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Email:</td><td><input type=\"text\" name=\"feedAuthorEmail\" value=\""+author.getEmail().getText()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author URI:&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedAuthorURI\" value=\""+author.getUri().getText()+"\" /></td></tr>");
        
        //print contributor
        Contributor contributor = (Contributor)feed.getContributors().get(0);
        if(contributor.getName() == null){
            contributor.setName(new Name());
        }
        if(contributor.getUri() == null){
            contributor.setUri(new URI());
        }
        if(contributor.getEmail() == null){
            contributor.setEmail(new Email());
        }
        out.println("<tr><td>Contributor:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Name:<span style=\"color: green;\">(*)</td><td><input type=\"text\" name=\"feedContributorName\" value=\""+contributor.getName().getText()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Email:</td><td><input type=\"text\" name=\"feedContributorEmail\" value=\""+contributor.getEmail().getText()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor URI:&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedContributorURI\" value=\""+contributor.getUri().getText()+"\" /></td></tr>");
        
        //print link
        Link link = (Link)feed.getLinks().get(0);            
        if(link.getHref() == null){
            link.setHref(new Attribute("href"));
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
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path &amp; Name:<span style=\"color: green;\">(*)&nbsp;&nbsp;&nbsp;(full URI required)</span><br />(Leave blank for root)</td><td><input type=\"text\" name=\"feedLinkPath\" value=\""+link.getHref().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Rel:<span style=\"color: green;\">(*)</span><br />(ex. self)</td><td><input type=\"text\" name=\"feedLinkRel\" value=\""+link.getRel().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Media Type:<br />(ex.  application/atom+xml or text/html)</td><td><input type=\"text\" name=\"feedLinkMediaType\" value=\""+link.getType().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Language:<br />(ex. en-US)</td><td><input type=\"text\" name=\"feedLinkLanguage\" value=\""+link.getHreflang().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Title:</td><td><input type=\"text\" name=\"feedLinkTitle\" value=\""+link.getTitle().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Length:</td><td><input type=\"text\" name=\"feedLinkLength\" value=\""+link.getLength().getValue()+"\" /></td></tr>");
       
        //print the category 
        Category category = (Category)feed.getCategories().get(0);
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
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Term:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"feedCategoryTerm\" value=\""+category.getTerm().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Scheme URI:<br />&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedCategoryScheme\" value=\""+category.getScheme().getValue()+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Label</td><td><input type=\"text\" name=\"feedCategoryLabel\" value=\""+category.getLabel().getValue()+"\" /></td></tr>");
        
        //print the icon.
        out.println("<tr><td>Icon URI:(1hz to 1vt)&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedIcon\" value=\""+feed.getIcon().getText()+"\" /></td></tr>");
        
        //print the logo.
        out.println("<tr><td>Logo URI:(2hz to 1vt)&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedLogo\" value=\""+feed.getLogo().getText()+"\" /></td></tr>");
        
        //print the rights.
        out.println("<tr><td>Rights:</td><td><input type=\"text\" name=\"feedRights\" value=\""+feed.getRights().getText()+"\" /></td></tr>");
        
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
        
        }catch(Exception e){
            e.printStackTrace();
            //return to an error page.
        }

    }
    
    
    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
}