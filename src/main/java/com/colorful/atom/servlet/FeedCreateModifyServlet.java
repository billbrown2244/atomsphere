package com.colorful.atom.servlet;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlString;
import org.w3.x2005.atom.AtomPersonConstruct;
import org.w3.x2005.atom.FeedDocument;
import org.w3.x2005.atom.CategoryDocument.Category;
import org.w3.x2005.atom.FeedDocument.Feed;
import org.w3.x2005.atom.GeneratorDocument.Generator;
import org.w3.x2005.atom.LinkDocument.Link;

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
        String relativePath = "/"+request.getParameter("relativePath").trim();
        String feedAuthorName = request.getParameter("feedAuthorName").trim();
        String feedAuthorURI = request.getParameter("feedAuthorURI").trim();
        String feedAuthorEmail = request.getParameter("feedAuthorEmail").trim();
        String feedTitle = request.getParameter("feedTitle").trim();
        String feedSubTitle = request.getParameter("feedSubTitle").trim();
        String feedLinkPath = "/"+request.getParameter("feedLinkPath").trim();
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
        String feedGenerator = request.getParameter("feedGenerator").trim();
        String feedGeneratorURI = request.getParameter("feedGeneratorURI").trim();
        String feedGeneratorVersion = request.getParameter("feedGeneratorVersion").trim();
        String feedIcon = "/"+request.getParameter("feedIcon").trim();
        String feedLogo = "/"+request.getParameter("feedLogo").trim();
        String feedRights = request.getParameter("feedRights").trim();
        
        
        try{
            
            
            
            //create a blank document.
            FeedDocument feedDoc = FeedDocument.Factory.newInstance();
            
            
            //add the root element
            Feed feed = feedDoc.addNewFeed();
            //set the language
            feed.setLang("en-US");
            
            //add id (REQUIRED)
            String atomID = AdminServlet.docRootURL+relativePath;
            feed.addNewId().set(XmlString.Factory.newValue(atomID));
            
            //add published (REQUIRED)
            feed.addNewUpdated().setCalendarValue(Calendar.getInstance());
            
            //add title (REQUIRED)
            feed.addNewTitle().set(XmlString.Factory.newValue(feedTitle));
            
            //add subtitle
            if(feedSubTitle != null && !feedSubTitle.equals("")){
                feed.addNewSubtitle().set(XmlString.Factory.newValue(feedSubTitle));
            }
            
            //add author (REQUIRED)
            AtomPersonConstruct author = feed.addNewAuthor();
            author.addName(feedAuthorName);
            if(feedAuthorEmail != null && !feedAuthorEmail.equals("")){//(RECOMMENDED)
                author.addEmail(feedAuthorEmail);
            }
            if(feedAuthorURI != null && !feedAuthorURI.equals("")){//(RECOMMENDED)
                author.addUri(feedAuthorURI);
            }
            
            //add Contributor
            if(feedContributorName != null && !feedContributorName.equals("")){
                AtomPersonConstruct contributor = feed.addNewContributor();
                contributor.addName(feedContributorName);
                if(feedContributorEmail != null && !feedContributorEmail.equals("")){//(RECOMMENDED)
                    contributor.addEmail(feedContributorEmail);
                }
                if(feedContributorURI != null && !feedContributorURI.equals("")){//(RECOMMENDED)
                    contributor.addUri(feedContributorURI);
                }
            }
            
            //add self ref link (RECOMMENDED)
            Link link = feed.addNewLink();
            if(feedLinkPath.equals("/")){
                link.setHref(XmlString.Factory.newValue(AdminServlet.docRootURL+relativePath));
            }else{
                link.setHref(XmlString.Factory.newValue(AdminServlet.docRootURL+feedLinkPath));
            }
            if(feedLinkRel != null && !feedLinkRel.equals("")){
                link.setRel(XmlString.Factory.newValue(feedLinkRel));
            }
            if(feedLinkMediaType != null && !feedLinkMediaType.equals("")){
                link.setType(feedLinkMediaType);
            }
            if(feedLinkLanguage != null && !feedLinkLanguage.equals("")){
                link.setLang(feedLinkLanguage);
            }
            if(feedLinkTitle != null && !feedLinkTitle.equals("")){
                link.setTitle(XmlString.Factory.newValue(feedLinkTitle));
            }
            if(feedLinkLength != null && !feedLinkLength.equals("")){
                link.setLength(XmlString.Factory.newValue(feedLinkLength));
            }
            
            //add Category
            if(feedCategoryTerm != null && !feedCategoryTerm.equals("")){
                Category category = feed.addNewCategory();
                category.setTerm(XmlString.Factory.newValue(feedCategoryTerm));
                if(feedCategoryScheme != null && !feedCategoryScheme.equals("")){
                    category.setScheme(XmlString.Factory.newValue(feedCategoryScheme));
                }
                if(feedCategoryLabel != null && !feedCategoryLabel.equals("")){
                    category.setLabel(XmlString.Factory.newValue(feedCategoryLabel));
                }
            }
        
            //add Generator
            if(feedGenerator != null && !feedGenerator.equals("")){
                Generator generator = feed.addNewGenerator();
                generator.set(XmlString.Factory.newValue(feedGenerator));
                if(feedGeneratorURI != null && !feedGeneratorURI.equals("")){
                    generator.setUri(XmlString.Factory.newValue(feedGeneratorURI));
                }
                if(feedGeneratorVersion != null && !feedGeneratorVersion.equals("")){
                    generator.setVersion(XmlString.Factory.newValue(feedGeneratorVersion));
                }
            }
            
            //add Icon (1h X 1v)
            if(feedIcon != null && !feedIcon.equals("/")){
                feed.addNewIcon().set(XmlString.Factory.newValue(AdminServlet.docRootURL+feedIcon));
            }
            
            //add Logo (2h X 1v)
            if(feedLogo != null && !feedLogo.equals("/")){
                feed.addNewLogo().set(XmlString.Factory.newValue(AdminServlet.docRootURL+feedLogo));
            }
            
            //add Rights
            if(feedRights != null && !feedRights.equals("")){
                feed.addNewRights().set(XmlString.Factory.newValue(feedRights));
            }
            
           
            //create the file and write it to its destination             
            AdminServlet.writeFeedToConfigFile(relativePath,feedDoc.toString());
            FileWriter feedFile = new FileWriter(getServletContext().getRealPath(relativePath));
            feedFile.write(feedDoc.toString());
            feedFile.flush();
            feedFile.close();
            
            System.out.println(feedDoc.toString());
            
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
