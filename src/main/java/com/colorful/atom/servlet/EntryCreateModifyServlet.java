package com.colorful.atom.servlet;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlString;
import org.w3.x2005.atom.AtomPersonConstruct;
import org.w3.x2005.atom.FeedDocument;
import org.w3.x2005.atom.EntryDocument.Entry;
import org.w3.x2005.atom.FeedDocument.Feed;
import org.w3.x2005.atom.LinkDocument.Link;

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
        String entryAuthorName = request.getParameter("entryAuthorName");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorURI = request.getParameter("entryAuthorURI");
        String entryContributorName = request.getParameter("entryContributorName");
        String entryContributorEmail = request.getParameter("entryContributorEmail");
        String entryContributorURI = request.getParameter("entryContributorURI");
       /*
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        //String atomUpdated = Calendar.getInstance().toString(); //required
        */
   
        try{
            
        FeedDocument feedDoc = FeedDocument.Factory.newInstance();
        //Feed feed = FeedDocument.Feed.Factory.newInstance();
        //feedDoc.setFeed(feed);
        Feed feed = feedDoc.addNewFeed();
        
        //add id
        feed.addNewId().set(XmlString.Factory.newValue(atomId));
       
        //add link
        Link selfLink = feed.addNewLink();
        selfLink.addNewHref().set(XmlString.Factory.newValue(docLocation));
        selfLink.addNewRel().set(XmlString.Factory.newValue("self"));
        
        //add title
        //AtomTextConstruct title = feed.addNewTitle();
        //title.set(XmlString.Factory.newValue(atomTitle));
        //or
        feed.addNewTitle().set(XmlString.Factory.newValue(atomTitle));

        feed.addNewSubtitle().set(XmlString.Factory.newValue("this is the subtitle."));
        
        //add author
        AtomPersonConstruct author = feed.addNewAuthor();
        author.addName(atomAuthor);
        author.addEmail(atomAuthorEmail);
        
        
        //add published
        feed.addNewUpdated().setCalendarValue(Calendar.getInstance());
        
        Entry entry = feed.addNewEntry();
        
        //feed.addNewLink().setHref(XmlString.Factory.newValue("www.testcompany.org"));
        //Entry entry = feed.addNewEntry();
        
        
        System.out.println(feedDoc.toString());
    } catch (Exception e){
        e.printStackTrace();
    }
        
        
    }
}
