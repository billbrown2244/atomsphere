package com.colorful.atom.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3.x2005.atom.AtomPersonConstruct;
import org.w3.x2005.atom.AtomTextConstruct;
import org.w3.x2005.atom.FeedDocument;
import org.w3.x2005.atom.CategoryDocument.Category;
import org.w3.x2005.atom.FeedDocument.Feed;
import org.w3.x2005.atom.GeneratorDocument.Generator;
import org.w3.x2005.atom.IconDocument.Icon;
import org.w3.x2005.atom.LinkDocument.Link;
import org.w3.x2005.atom.LogoDocument.Logo;
import org.apache.xmlbeans.XmlOptions;

public class FeedCreateModifyFormServlet extends HttpServlet {
    

    
    private static final long serialVersionUID = 6072923458769031495L;

    
    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String relativePath = request.getParameter("relativePath");
        if(relativePath.startsWith("/")){
            relativePath = relativePath.substring(1);
        }
        boolean fileExists = false;
        if(new File(getServletContext().getRealPath(relativePath)).exists()){
            fileExists = true;
        }

        try{
        //get the document.
        FeedDocument feedDoc = null;
        Feed feed = null;
        
        //required
        AtomTextConstruct[] titles = null;
        String title = null;
        
        //optional
        AtomTextConstruct[] subTitles = null;
        String subTitle = null;
        
        //required (unless all entries have authors so make it required) 
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
        
        //optional - can have multiple. (should at least have a self referencing link.
        Link[] links = null;
        String[] href = null;
        String[] rel = null;
        String[] type = null;
        String[] hreflang = null;
        String[] linkTitle = null;
        String[] linkLen = null;
        
        //optional - can have multiple
        Category[] categories = null;
        String[] categoryTerm = null;
        String[] categorySchemeURI = null;
        String[] categoryLabel = null;
        
        //optional
        Generator[] generators = null;
        String generatorStr = null;
        String generatorURI = null;
        String generatorVersion = null;
   
        //optional
        Icon[] icons = null;
        String iconURL = null;
   
        //optional
        Logo[] logos = null;
        String logoURL = null;
        
        //optional
        AtomTextConstruct[] rights = null;
        String rightsStr = null;
        
        //determines the type of feed we are working with; new or existing
        String formType = null;
        
        if(fileExists){//this is an existing document
            formType = "modify";
            
            XmlOptions options = new XmlOptions();
            options.setLoadStripWhitespace();
            options.setLoadTrimTextBuffer();
            feedDoc = FeedDocument.Factory.parse(new File(getServletContext().getRealPath(relativePath)),options);
            feed = feedDoc.getFeed();
            //get the title
            titles = feed.getTitleArray();
            title = titles[0].getDomNode().getFirstChild().getNodeValue();
            
            //get the subTitle
            subTitles = feed.getSubtitleArray();
            if(subTitles.length > 0 && subTitles[0] != null){
                subTitle = subTitles[0].getDomNode().getFirstChild().getNodeValue();
            }else{
                subTitle = "";
            }
            
            
            //get the list of authors
            authors = feed.getAuthorArray();
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
            contributors = feed.getContributorArray();
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
            
            //get the list of links.
            links = feed.getLinkArray();
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
            
            //get the list of categories
            categories = feed.getCategoryArray();
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
            
            //get the generator
            generators = feed.getGeneratorArray();
            if(generators.length == 0){
                generatorStr = "";
            }else{
                generatorStr = generators[0].getDomNode().getFirstChild().getNodeValue();
            }
            if(generators.length == 0 || (generators[0].getUri() == null)){
                generatorURI = "";
            }else{
                generatorURI = generators[0].getUri().getDomNode().getFirstChild().getNodeValue();
            }
            if(generators.length == 0 || (generators[0].getVersion() == null)){
                generatorVersion = "";
            }else{
                generatorVersion = generators[0].getVersion().getDomNode().getFirstChild().getNodeValue();
            }
            
            //get the icon
            icons = feed.getIconArray();
            if(icons.length == 0){
                iconURL = "";
            }else{
                iconURL = icons[0].getDomNode().getFirstChild().getNodeValue();
                iconURL = iconURL.substring(AdminServlet.docRootURL.length());
                if(iconURL.startsWith("/")){
                    iconURL = iconURL.substring(1);
                }
            }
            
            //get the logo
            logos = feed.getLogoArray();
            if(logos.length == 0){
                logoURL = "";
            }else{
                logoURL = logos[0].getDomNode().getFirstChild().getNodeValue();
                logoURL = logoURL.substring(AdminServlet.docRootURL.length());
                if(logoURL.startsWith("/")){
                    logoURL = logoURL.substring(1);
                }
            }
            
            rights = feed.getRightsArray();
            if(rights.length == 0){
                rightsStr = "";
            }else{
                rightsStr = rights[0].getDomNode().getFirstChild().getNodeValue();
            }
            
        }else{//this is a create request so set everything to blank but the relative path. 
            formType = "create";
            
            title = "";
            subTitle = "";
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
            generatorStr = "";
            generatorURI = "";
            generatorVersion = "";
            iconURL = "";
            logoURL = "";
            rightsStr = "";
        }

        //output the form.
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();        
        out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+AdminServlet.cssURL+"\"/></head><body>");
        out.println("<form method=\"post\" action=\"create/feed\" >");

        out.println("<table>");
        out.println("<tr><td><span style=\"color: green;\">*</span> = Required</td><td><span style=\"color: green;\">(*)</span> = Required for parent</td></tr>");
        
        if(fileExists){//make the field read only
        out.println("<tr><td>Feed Path &amp; Name:*</td><td><input type=\"text\" name=\"relativePath\" value=\""+relativePath+"\" readonly=\"readonly\" /></td><td><a href=\""+AdminServlet.atomSpecURL+"#element.feed\" >help</a></td></tr>");
        }else{
        out.println("<tr><td>Feed Path &amp; Name:*</td><td><input type=\"text\" name=\"relativePath\" value=\""+relativePath+"\" /></td><td><a href=\""+AdminServlet.atomSpecURL+"#element.feed\" >help</a></td></tr>");
        }
        
        out.println("<tr><td>Title:<span style=\"color: green;\">*</span></td><td><input type=\"text\" name=\"feedTitle\" value=\""+title+"\" /></td></tr>");
        out.println("<tr><td>Sub Title:</td><td><input type=\"text\" name=\"feedSubTitle\" value=\""+subTitle+"\" /></td></tr>");
        
        
        out.println("<tr><td>Author:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Name:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"feedAuthorName\" value=\""+authorName[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author Email:</td><td><input type=\"text\" name=\"feedAuthorEmail\" value=\""+authorEmail[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Author URI:</td><td><input type=\"text\" name=\"feedAuthorURI\" value=\""+authorURI[0]+"\" /></td></tr>");
        
        
        out.println("<tr><td>Contributor:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Name:<span style=\"color: green;\">(*)</td><td><input type=\"text\" name=\"feedContributorName\" value=\""+contributorName[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor Email:</td><td><input type=\"text\" name=\"feedContributorEmail\" value=\""+contributorEmail[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Contributor URI:</td><td><input type=\"text\" name=\"feedContributorURI\" value=\""+contributorURI[0]+"\" /></td></tr>");
        
        out.println("<tr><td>Link:<span style=\"color: green;\">*</span></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Path &amp; Name:<span style=\"color: green;\">(*)</span><br />(Leave blank for root)</td><td><input type=\"text\" name=\"feedLinkPath\" value=\""+href[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Rel:<span style=\"color: green;\">(*)</span><br />(ex. self)</td><td><input type=\"text\" name=\"feedLinkRel\" value=\""+rel[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Media Type:<br />(ex.  application/atom+xml or text/html)</td><td><input type=\"text\" name=\"feedLinkMediaType\" value=\""+type[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Language:<br />(ex. en-US)</td><td><input type=\"text\" name=\"feedLinkLanguage\" value=\""+hreflang[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Title:</td><td><input type=\"text\" name=\"feedLinkTitle\" value=\""+linkTitle[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Link Length:</td><td><input type=\"text\" name=\"feedLinkLength\" value=\""+linkLen[0]+"\" /></td></tr>");
       
        out.println("<tr><td>Category:</td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Term:<span style=\"color: green;\">(*)</span></td><td><input type=\"text\" name=\"feedCategoryTerm\" value=\""+categoryTerm[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Scheme URI:<br />&nbsp;&nbsp;&nbsp;(full URI required)</td><td><input type=\"text\" name=\"feedCategoryScheme\" value=\""+categorySchemeURI[0]+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Category Label</td><td><input type=\"text\" name=\"feedCategoryLabel\" value=\""+categoryLabel[0]+"\" /></td></tr>");
        
        out.println("<tr><td>Generator:</td><td><input type=\"text\" name=\"feedGenerator\" value=\""+generatorStr+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Generator URI:</td><td><input type=\"text\" name=\"feedGeneratorURI\" value=\""+generatorURI+"\" /></td></tr>");
        out.println("<tr><td>&nbsp;&nbsp;&nbsp;Generator Version:</td><td><input type=\"text\" name=\"feedGeneratorVersion\" value=\""+generatorVersion+"\" /></td></tr>");
        
        out.println("<tr><td>Icon URI:(1hz to 1vt)</td><td><input type=\"text\" name=\"feedIcon\" value=\""+iconURL+"\" /></td></tr>");
        
        out.println("<tr><td>Logo URI:(2hz to 1vt)</td><td><input type=\"text\" name=\"feedLogo\" value=\""+logoURL+"\" /></td></tr>");
        
        out.println("<tr><td>Rights:</td><td><input type=\"text\" name=\"feedRights\" value=\""+rightsStr+"\" /></td></tr>");
        
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