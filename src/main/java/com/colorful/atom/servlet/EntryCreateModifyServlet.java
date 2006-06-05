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
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.colorful.atom.beans.Attribute;
import com.colorful.atom.beans.Author;
import com.colorful.atom.beans.Category;
import com.colorful.atom.beans.Contributor;
import com.colorful.atom.beans.Email;
import com.colorful.atom.beans.Entry;
import com.colorful.atom.beans.Feed;
import com.colorful.atom.beans.FeedDoc;
import com.colorful.atom.beans.Id;
import com.colorful.atom.beans.Link;
import com.colorful.atom.beans.Published;
import com.colorful.atom.beans.Rights;
import com.colorful.atom.beans.Summary;
import com.colorful.atom.beans.Title;
import com.colorful.atom.beans.Content;
import com.colorful.atom.beans.URI;
import com.colorful.atom.beans.Updated;

public class EntryCreateModifyServlet extends HttpServlet {

    
    private static final long serialVersionUID = -16274221364816176L;

    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
    
    //Send an email to the user with their login and password 
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    { 
        String entryTitle = request.getParameter("entryTitle");
        String entrySummary = request.getParameter("entrySummary");
        String contentType = request.getParameter("contentType");
        String contentValue = request.getParameter("content");
        System.out.println("contentValue = "+contentValue);
        String entryAuthorName = request.getParameter("entryAuthorName");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorURI = request.getParameter("entryAuthorURI");
        String entryContributorName = request.getParameter("entryContributorName");
        String entryContributorEmail = request.getParameter("entryContributorEmail");
        String entryContributorURI = request.getParameter("entryContributorURI");
        String entryLinkPath = request.getParameter("entryLinkPath").trim();
        String entryLinkRel = request.getParameter("entryLinkRel").trim();
        String entryLinkMediaType = request.getParameter("entryLinkMediaType").trim();
        String entryLinkLanguage = request.getParameter("entryLinkLanguage").trim();
        String entryLinkTitle = request.getParameter("entryLinkTitle").trim();
        String entryLinkLength = request.getParameter("entryLinkLength").trim();
        String entryCategoryTerm = request.getParameter("entryCategoryTerm").trim();
        String entryCategoryScheme = request.getParameter("entryCategoryScheme").trim();
        String entryCategoryLabel = request.getParameter("entryCategoryLabel").trim();
        String entryRights = request.getParameter("entryRights").trim();
        String relativePath = request.getParameter("relativePath").trim();
        String formType = request.getParameter("formType").trim();
        boolean create = formType.equals("create")?true:false;

   
        try{
            //get the feed from the file.
            String fullPath = getServletContext().getRealPath(relativePath);
            Feed feed = FeedDoc.readFeedDoc(fullPath,true);
            
            //populate the entry
            Entry entry = new Entry();
            
            //add id (REQUIRED)
            String atomIDStr = AdminServlet.docRootURL+relativePath;
            entry.setId(new Id((atomIDStr+"#"+entryTitle).replaceAll("[' ']","%20")));
            System.out.println("entry id = "+entry.getId().getText());
            //add updated (REQUIRED)
            entry.setUpdated(new Updated(Calendar.getInstance().getTime()));
            
            //add title (REQUIRED)
            Title title = new Title();
            title.setText(entryTitle);
            entry.setTitle(title);
            
            //add summary
            if(entrySummary != null && !entrySummary.equals("")){
                Summary summary = new Summary();
                summary.setText(entrySummary);
                entry.setSummary(summary);
            }
            
            //add content
            if(!contentType.equals("*")){
                System.out.println("adding contnet");
                Content content = new Content(contentType);
                if(contentType.equals("link")){
                    content.addAttribute(new Attribute("src",contentValue));
                }else{
                    content.setContent(contentValue);
                }
                entry.setContent(content);
            }
            
            //add author
            if(entryAuthorName != null && !entryAuthorName.equals("")){
                Author author = new Author(entryAuthorName);
                if(entryAuthorEmail != null && !entryAuthorEmail.equals("")){
                    author.setEmail(new Email(entryAuthorEmail));
                }
                if(entryAuthorURI != null && !entryAuthorURI.equals("")){
                    author.setUri(new URI(entryAuthorURI));
                }
                entry.addAuthor(author);
            }
            
            //add contributor
            if(entryContributorName != null && !entryContributorName.equals("")){
                Contributor contributor = new Contributor(entryContributorName);
                if(entryContributorEmail != null && !entryContributorEmail.equals("")){
                    contributor.setEmail(new Email(entryContributorEmail));
                }
                if(entryContributorURI != null && !entryContributorURI.equals("")){
                    contributor.setUri(new URI(entryContributorURI));
                }
                entry.addContributor(contributor);
            }
            
            //add link
            if(entryLinkPath != null && !entryLinkPath.equals("")){
                Link link = new Link(AdminServlet.docRootURL+entryLinkPath);
                if(entryLinkRel != null && !entryLinkRel.equals("")){
                    link.setRel(new Attribute("rel",entryLinkRel));
                }
                if(entryLinkMediaType != null && !entryLinkMediaType.equals("")){
                    link.setType(new Attribute("type",entryLinkMediaType));
                }
                if(entryLinkLanguage != null && !entryLinkLanguage.equals("")){
                    link.setHreflang(new Attribute("hreflang",entryLinkLanguage));
                }
                if(entryLinkTitle != null && !entryLinkTitle.equals("")){
                    link.setTitle(new Attribute("title",entryLinkTitle));
                }
                if(entryLinkLength != null && !entryLinkLength.equals("")){
                    link.setLength(new Attribute("length",entryLinkLength));
                }
                entry.addLink(link);
            }
            
            //add category
            if(entryCategoryTerm != null && !entryCategoryTerm.equals("")){
                Category category = new Category(entryCategoryTerm);
                if(entryCategoryScheme != null && !entryCategoryScheme.equals("")){
                    category.setScheme(new Attribute("scheme",entryCategoryScheme));
                }
                if(entryCategoryLabel != null && !entryCategoryLabel.equals("")){
                    category.setLabel(new Attribute("label",entryCategoryLabel));
                }
                entry.addCategory(category);
            }
            
            //add rights
            if(entryRights != null && !entryRights.equals("")){
                Rights rights = new Rights();
                rights.setText(entryRights);
                entry.setRights(rights);
            }
            
            
            if(create){
                //create a new entry from the existing feed
                entry.setPublished(new Published(Calendar.getInstance().getTime()));
                feed.addEntry(entry);
            }else{
                //remove the old entry and replace it with the new. 
                
                Iterator entryItr = feed.getEntries().keySet().iterator();
                while(entryItr.hasNext()){
                    Entry oldEntry = (Entry)feed.getEntries().get(entryItr.next());
                    if(oldEntry.getTitle().getText().equals(entryTitle)){
                        Entry temp = (Entry)feed.getEntries().remove(oldEntry.getUpdated().getText());
                        entry.setPublished(temp.getPublished());
                        break;
                    }
                }
                feed.addEntry(entry);
            }
            
            //write the feed to the config and atom file
            FeedDoc.writeFeedDoc(getServletContext().getRealPath(relativePath),feed,FeedDoc.encoding,FeedDoc.xml_version);
            AdminServlet.writeFeedToConfigFile(relativePath,FeedDoc.readFeedString(getServletContext().getRealPath(relativePath)));
            
    
        } catch (Exception e){
            e.printStackTrace();
        }
        response.sendRedirect("../../modify?relativePath="+relativePath);
    }
}
