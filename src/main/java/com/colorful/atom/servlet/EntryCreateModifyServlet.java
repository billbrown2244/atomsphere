package com.colorful.atom.servlet;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.w3.x2005.atom.AtomPersonConstruct;
import org.w3.x2005.atom.EntryDocument;
import org.w3.x2005.atom.FeedDocument;
import org.w3.x2005.atom.EntryDocument.Entry;
import org.w3.x2005.atom.EntryDocument.Entry.TextContent;
import org.w3.x2005.atom.EntryDocument.Entry.TextContent.Type.Enum;
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
        String content = request.getParameter("content");
        String entryAuthorName = request.getParameter("entryAuthorName");
        String entryAuthorEmail = request.getParameter("entryAuthorEmail");
        String entryAuthorURI = request.getParameter("entryAuthorURI");
        String entryContributorName = request.getParameter("entryContributorName");
        String entryContributorEmail = request.getParameter("entryContributorEmail");
        String entryContributorURI = request.getParameter("entryContributorURI");
        String entryLinkPath = "/"+request.getParameter("entryLinkPath").trim();
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
        boolean create = false;
        if(formType.equals("create")){
            create = true;
        }
   
        try{

            XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(AdminServlet.atomConfigFile)));
            Map atomConfig = (Map)decode.readObject();
            XmlOptions options = new XmlOptions();
            options.setLoadStripWhitespace();
            options.setLoadTrimTextBuffer();
            FeedDocument feedDoc = FeedDocument.Factory.parse((String)atomConfig.get(relativePath),options);
            Feed feed = feedDoc.getFeed();
            Entry[] entries = feed.getEntryArray();
            Entry entry = null;
            
            
            if(create){
                //create a new entry from the existing feed
                entry = feed.addNewEntry();
                Entry[] temp = new Entry[entries.length+1];
                temp[0] = entry;
                for(int i=0; i < entries.length; i++){
                    temp[i+1] = entries[i];
                }
                entries = temp;
            }else{
                //create a new entry outside of the existing feed. 
                entry = EntryDocument.Factory.newInstance().addNewEntry();
            
                //remove the old entry and create a new one as the first on the list. 
                Entry[] temp = new Entry[entries.length];
                for(int i=0; i < temp.length; i++){
                    if(entries[i].getTitleArray()[0].getDomNode().getFirstChild().getNodeValue().equals(entryTitle)){
                       i--; 
                    }else{
                        temp[i] = entries[i];
                    }
                }
                entries[0] = entry;
                for(int i=0; i < temp.length -1; i++){
                    entries[i+1] = temp[i];
                }
                
            }
            
        
            //add id (REQUIRED)
            String atomIDStr = AdminServlet.docRootURL+relativePath;
            entry.addNewId().set(XmlString.Factory.newValue(atomIDStr+"#"+entryTitle));
            
            //add published (REQUIRED)
            entry.addNewUpdated().setCalendarValue(Calendar.getInstance());
            
            //add title (REQUIRED)
            entry.addNewTitle().set(XmlString.Factory.newValue(entryTitle));
        
            //add the content
            if(contentType.equals("text")||contentType.equals("html")){
                TextContent text = entry.addNewTextContent();
                text.setType(TextContent.Type.Enum.forString(contentType));
                text.set(XmlString.Factory.newValue(content));
            }
            
        feed.setEntryArray(entries);
        System.out.println(feedDoc.toString());
    
        } catch (Exception e){
            e.printStackTrace();
        }
        response.sendRedirect("../../modify?relativePath="+relativePath);
    }
}
