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
import java.util.Calendar;
import java.util.Map;

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
import com.colorful.atom.beans.Generator;
import com.colorful.atom.beans.Icon;
import com.colorful.atom.beans.Id;
import com.colorful.atom.beans.Link;
import com.colorful.atom.beans.Logo;
import com.colorful.atom.beans.Rights;
import com.colorful.atom.beans.Subtitle;
import com.colorful.atom.beans.Title;
import com.colorful.atom.beans.URI;
import com.colorful.atom.beans.Updated;

public class FeedCreateModifyServlet extends HttpServlet {
    
    
    private static final long serialVersionUID = -16274212344816176L;
    
    
    
    /*
     * This servlet creates an atom 1.0 feed document without any entries.
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    { 
        //paramaters not included are the atomId and atomUpdated
        //atomId will be created with the path and title.
        //atomUpdated will be created with the Calendar.getInstance() method.
        String relativePath = request.getParameter("relativePath").trim();
        String feedAuthorName = request.getParameter("feedAuthorName").trim();
        String feedAuthorURI = request.getParameter("feedAuthorURI").trim();
        String feedAuthorEmail = request.getParameter("feedAuthorEmail").trim();
        String feedTitle = request.getParameter("feedTitle").trim();
        String feedSubTitle = request.getParameter("feedSubTitle").trim();
        String feedLinkPath = request.getParameter("feedLinkPath").trim();
        String feedLinkRel = request.getParameter("feedLinkRel").trim();
        String feedLinkMediaType = request.getParameter("feedLinkMediaType").trim();
        String feedLinkLanguage = request.getParameter("feedLinkLanguage").trim();
        String feedLinkTitle = request.getParameter("feedLinkTitle").trim();
        String feedLinkLength = request.getParameter("feedLinkLength").trim();
        String feedCategoryTerm = request.getParameter("feedCategoryTerm").trim();
        String feedCategoryScheme = request.getParameter("feedCategoryScheme").trim();
        String feedCategoryLabel = request.getParameter("feedCategoryLabel").trim();
        String feedContributorName = request.getParameter("feedContributorName").trim();
        String feedContributorURI = request.getParameter("feedContributorURI").trim();
        String feedContributorEmail = request.getParameter("feedContributorEmail").trim();
        String feedIcon = request.getParameter("feedIcon").trim();
        String feedLogo = request.getParameter("feedLogo").trim();
        String feedRights = request.getParameter("feedRights").trim();
        String formType = request.getParameter("formType").trim();
        boolean create = false;
        if(formType.equals("create")){
            create = true;
        }
        
        try{

            Feed feed = null;
            Map entries = null;
            if(create){
                //create a new feed bean.
                feed = new Feed(FeedDoc.atomBase,FeedDoc.lang_en);
            }else{
                //get the entries from document file if there are any.
                String fullPath = getServletContext().getRealPath(relativePath);
                feed = FeedDoc.readFeedDoc(fullPath,true);
                entries = feed.getEntries();
                feed = new Feed(FeedDoc.atomBase,FeedDoc.lang_en);
                //reset the entries if need be
                if(entries != null){
                    feed.setEntries(entries);
                }
            }
            
            
            //add id (REQUIRED)
            System.out.println("relativePath here = "+relativePath);
            if(!relativePath.startsWith("/")){
                relativePath = "/"+relativePath;
            }
            feed.setId(new Id(AdminServlet.docRootURL+relativePath));
            
            //add updated (REQUIRED)
            feed.setUpdated(new Updated(Calendar.getInstance().getTime()));
  
            //add title (REQUIRED)
            Title title = new Title();
            title.setText(feedTitle);
            feed.setTitle(title);
            
            //add the generator (Atomsphere)
            Generator generator = new Generator("http://www.colorfulsoftware.com/projects/atomsphere","1.0.0");
            generator.setText("Atomsphere");
            feed.setGenerator(generator);
            
            //add subtitle
            if(feedSubTitle != null && !feedSubTitle.equals("")){
                Subtitle subtitle = new Subtitle();
                subtitle.setText(feedSubTitle);
                feed.setSubtitle(subtitle);
            }
            
            
            //add author (REQUIRED)
            if(feedAuthorName != null && !feedAuthorName.equals("")){
                Author author = new Author(feedAuthorName);
                if(feedAuthorEmail != null && !feedAuthorEmail.equals("")){
                    author.setEmail(new Email(feedAuthorEmail));
                }
                if(feedAuthorURI != null && !feedAuthorURI.equals("")){
                    author.setUri(new URI(feedAuthorURI));
                }
                feed.addAuthor(author);
            }

            
            //add Contributor
            if(feedContributorName != null && !feedContributorName.equals("")){
                Contributor contributor = new Contributor(feedContributorName);
                if(feedContributorEmail != null && !feedContributorEmail.equals("")){
                    contributor.setEmail(new Email(feedContributorEmail));
                }
                if(feedContributorURI != null && !feedContributorURI.equals("")){
                    contributor.setUri(new URI(feedContributorURI));
                }
                feed.addContributor(contributor);
            }
            
            
            //add self ref link (RECOMMENDED)
            if(feedLinkPath != null && !feedLinkPath.equals("")){
                Link link = new Link(feedLinkPath);
                if(feedLinkRel != null && !feedLinkRel.equals("")){
                    link.setRel(new Attribute("rel",feedLinkRel));
                }
                if(feedLinkMediaType != null && !feedLinkMediaType.equals("")){
                    link.setType(new Attribute("type",feedLinkMediaType));
                }
                if(feedLinkLanguage != null && !feedLinkLanguage.equals("")){
                    link.setHreflang(new Attribute("hreflang",feedLinkLanguage));
                }
                if(feedLinkTitle != null && !feedLinkTitle.equals("")){
                    link.setTitle(new Attribute("title",feedLinkTitle));
                }
                if(feedLinkLength != null && !feedLinkLength.equals("")){
                    link.setLength(new Attribute("length",feedLinkLength));
                }
                feed.addLink(link);
            }
                        
            
            //add Category
            if(feedCategoryTerm != null && !feedCategoryTerm.equals("")){
                Category category = new Category(feedCategoryTerm);
                if(feedCategoryScheme != null && !feedCategoryScheme.equals("")){
                    category.setScheme(new Attribute("scheme",feedCategoryScheme));
                }
                if(feedCategoryLabel != null && !feedCategoryLabel.equals("")){
                    category.setLabel(new Attribute("label",feedCategoryLabel));
                }
                feed.addCategory(category);
            }
            
            //add Icon (1h X 1v)
            if(feedIcon != null && !feedIcon.equals("")){
                feed.setIcon(new Icon(new URI(feedIcon)));
            }
            
            //add Logo (2h X 1v)
            if(feedLogo != null && !feedLogo.equals("")){
                feed.setLogo(new Logo(new URI(feedLogo)));
            }
            
            //add Rights
            if(feedRights != null && !feedRights.equals("")){
                Rights rights = new Rights();
                rights.setText(feedRights);
                feed.setRights(rights);
            }
                    
            
            //write the feed to the config and atom file
            FeedDoc.writeFeedDoc(getServletContext().getRealPath(relativePath),feed,FeedDoc.encoding,FeedDoc.xml_version);
            AdminServlet.writeFeedToConfigFile(relativePath,FeedDoc.readFeedString(getServletContext().getRealPath(relativePath)));
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
        response.sendRedirect("../../atom");
    }
    
    
    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
}
