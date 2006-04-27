package com.colorful.atom.servlet;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.w3.x2005.atom.AtomDateConstruct;
import org.w3.x2005.atom.AtomPersonConstruct;
import org.w3.x2005.atom.AtomTextConstruct;
import org.w3.x2005.atom.FeedDocument;
import org.w3.x2005.atom.CategoryDocument.Category;
import org.w3.x2005.atom.FeedDocument.Feed;
import org.w3.x2005.atom.GeneratorDocument.Generator;
import org.w3.x2005.atom.IconDocument.Icon;
import org.w3.x2005.atom.IdDocument.Id;
import org.w3.x2005.atom.LinkDocument.Link;
import org.w3.x2005.atom.LogoDocument.Logo;

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
        String formType = request.getParameter("formType").trim();
        boolean create = false;
        if(formType.equals("create")){
            create = true;
        }
        
        try{
            
            FeedDocument feedDoc = null;
            Feed feed = null;
            
            if(create){
                feedDoc = FeedDocument.Factory.newInstance();
                feed = feedDoc.addNewFeed();
            }else{
                XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(AdminServlet.atomConfigFile)));
                Map atomConfig = (Map)decode.readObject();
                XmlOptions options = new XmlOptions();
                options.setLoadStripWhitespace();
                options.setLoadTrimTextBuffer();
                feedDoc = FeedDocument.Factory.parse((String)atomConfig.get(relativePath),options);
                feed = feedDoc.getFeed();
            }
            
            //set the language
            feed.setLang("en-US");
            
            //add id (REQUIRED)
            String atomIDStr = AdminServlet.docRootURL+relativePath;
            Id atomID = null;
            if(create){
                atomID = feed.addNewId();
            }else{
                atomID = feed.getIdArray(0);
            }
            atomID.set(XmlString.Factory.newValue(atomIDStr));
            
            
            //add published (REQUIRED)
            AtomDateConstruct atomDate = null;
            if(create){
                atomDate = feed.addNewUpdated();
            }else{
                atomDate = feed.getUpdatedArray(0);
            }
            atomDate.setCalendarValue(Calendar.getInstance());
            
            
            //add title (REQUIRED)
            AtomTextConstruct atomTitle = null;
            if(create){
                atomTitle = feed.addNewTitle();
            }else{
                atomTitle = feed.getTitleArray(0);
            }
            atomTitle.set(XmlString.Factory.newValue(feedTitle));
            
            
            //add subtitle
            if(feedSubTitle != null && !feedSubTitle.equals("")){
                AtomTextConstruct atomSubTitle = null;
                if(create){
                    atomSubTitle = feed.addNewSubtitle();
                }else{
                    if(feed.getSubtitleArray() == null){
                        atomSubTitle = feed.addNewSubtitle();
                    }else{
                        atomSubTitle = feed.getSubtitleArray(0);
                    }
                }
                atomSubTitle.set(XmlString.Factory.newValue(feedSubTitle));
            }
            
            
            //add author (REQUIRED)            
            AtomPersonConstruct author = null;
            if(create){
                author = feed.addNewAuthor();
                author.addName(feedAuthorName);
            }else{
                if(feed.getAuthorArray() == null){
                    author = feed.addNewAuthor();
                    author.addName(feedAuthorName);
                }else{
                    author = feed.getAuthorArray(0);
                    author.setNameArray(0,feedAuthorName);
                }
            }            
            if(feedAuthorEmail != null && !feedAuthorEmail.equals("")){//(RECOMMENDED)
                if(create){
                    author.addEmail(feedAuthorEmail);
                }else{
                    if(author.getEmailArray() == null){
                        author.addEmail(feedAuthorEmail);
                    }else{
                        author.setEmailArray(0,feedAuthorEmail);
                    }
                }
            }
            if(feedAuthorURI != null && !feedAuthorURI.equals("")){//(RECOMMENDED)
                if(create){
                    author.addUri(feedAuthorURI);
                }else{
                    if(author.getUriArray() == null){
                        author.addUri(feedAuthorURI);
                    }else{
                        author.setUriArray(0,feedAuthorURI);
                    }
                }
            }
            
            
            //add Contributor
            if(feedContributorName != null && !feedContributorName.equals("")){
                AtomPersonConstruct contributor = null;
                if(create){
                    contributor = feed.addNewContributor();
                    contributor.addName(feedContributorName);
                }else{
                    if(feed.getContributorArray() == null){
                        contributor = feed.addNewContributor();
                        contributor.addName(feedContributorName);
                    }else{
                        contributor = feed.getContributorArray(0);
                        contributor.setNameArray(0,feedContributorName);
                    }
                }
                if(feedContributorEmail != null && !feedContributorEmail.equals("")){//(RECOMMENDED)
                    if(create){
                        contributor.addEmail(feedContributorEmail);
                    }else{
                        if(contributor.getEmailArray() == null){
                            contributor.addEmail(feedContributorEmail);
                        }else{
                            contributor.setEmailArray(0,feedContributorEmail);
                        }
                    }
                }
                if(feedContributorURI != null && !feedContributorURI.equals("")){//(RECOMMENDED)
                    if(create){
                        contributor.addUri(feedContributorURI);
                    }else{
                        if(contributor.getUriArray() == null){
                            contributor.addUri(feedContributorURI);
                        }else{
                            contributor.setUriArray(0,feedContributorURI);
                        }
                    }
                }
            }
            
            //add self ref link (RECOMMENDED)
            Link link = null;
            if(create){
                link = feed.addNewLink();
            }else{
                if(feed.getLinkArray() == null){
                    link = feed.addNewLink();
                }else{
                    link = feed.getLinkArray(0);
                }
            }
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
                Category category = null;
                if(create){
                    category = feed.addNewCategory();
                }else{
                    if(feed.getCategoryArray() == null){
                        category = feed.addNewCategory(); 
                    }else{
                        category = feed.getCategoryArray(0);
                    }
                }
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
                Generator generator = null;
                if(create){
                    generator = feed.addNewGenerator();
                }else{
                    if(feed.getGeneratorArray() == null){
                        generator = feed.addNewGenerator(); 
                    }else{
                        generator = feed.getGeneratorArray(0);
                    }
                }
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
                Icon atomIcon = null;
                if(create){
                    atomIcon = feed.addNewIcon();
                }else{
                    if(feed.getIconArray() == null){
                        atomIcon = feed.addNewIcon();
                    }else{
                        atomIcon = feed.getIconArray(0);
                    }
                }
                atomIcon.set(XmlString.Factory.newValue(AdminServlet.docRootURL+feedIcon));
            }
            
            //add Logo (2h X 1v)
            if(feedLogo != null && !feedLogo.equals("/")){
                Logo atomLogo = null;
                if(create){
                    atomLogo = feed.addNewLogo();
                }else{
                    if(feed.getLogoArray() == null){
                        atomLogo = feed.addNewLogo();
                    }else{
                        atomLogo = feed.getLogoArray(0);
                    }
                }
                atomLogo.set(XmlString.Factory.newValue(AdminServlet.docRootURL+feedLogo));
            }
            
            //add Rights
            if(feedRights != null && !feedRights.equals("")){
                AtomTextConstruct atomRights = null;
                if(create){
                    atomRights = feed.addNewRights();
                }else{
                    if(feed.getRightsArray() == null){
                        atomRights = feed.addNewRights();
                    }else{
                        atomRights = feed.getRightsArray(0);
                    }
                }
                atomRights.set(XmlString.Factory.newValue(feedRights));
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
