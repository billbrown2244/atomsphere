package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamWriter;


public class FeedWriter{
    
    public void writeFeed(XMLStreamWriter writer, Feed feed) throws Exception{
        //open the element
        writer.writeStartElement("feed");
        Iterator feedAttrs = feed.getAttributes().iterator();
        //write the attributes
        while(feedAttrs.hasNext()){
            Attribute feedAttr = (Attribute)feedAttrs.next();
            writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
        }
        //write the generator
        writeGenerator(writer,feed.getGenerator());
        //write the id
        writeID(writer,feed.getId());
        //write the updated date
        writeUpdated(writer,feed.getUpdated());
        //write the title
        writeTitle(writer,feed.getTitle());
        //write the author
        if(feed.getAuthors() != null){
            writeAuthors(writer,feed.getAuthors());
        }
        //write the contributors
        if(feed.getContributors() != null){
            writeContributors(writer,feed.getContributors());
        }
        //write the links
        if(feed.getLinks() != null){
            writeLinks(writer,feed.getLinks());
        }
        //write the categories
        if(feed.getCategories() != null){
            writeCategories(writer,feed.getCategories());
        }
        //write the icon
        if(feed.getIcon() != null){
            writeIcon(writer,feed.getIcon());
        }
        //write the logo
        if(feed.getLogo() != null){
            writeLogo(writer,feed.getLogo());
        }
        //write the rights
        if(feed.getRights() != null){
            writeRights(writer,feed.getRights());
        }
        //write the extensions
        if(feed.getExtensions() != null){
            writeExtensions(writer,feed.getExtensions());
        }
        //write the entries
        if(feed.getEntries() != null){
            writeEntries(writer,feed.getEntries());
        }
        
        writer.writeEndElement();
    }
    
    private void writeGenerator(XMLStreamWriter writer,Generator generator) throws Exception{
        writer.writeStartElement("generator");
        if(generator.getAttributes() != null){
            Iterator feedAttrs = generator.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
            writer.writeCharacters(generator.getText());
        }
        writer.writeEndElement();
    }
    
    private void writeID(XMLStreamWriter writer,Id id) throws Exception{
        writer.writeStartElement("id");
        if(id.getAttributes() != null){
            Iterator feedAttrs = id.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        writer.writeCharacters(id.getUri().getText());
        writer.writeEndElement();
    }
    
    private void writeUpdated(XMLStreamWriter writer,Updated updated) throws Exception{
        writer.writeStartElement("updated");
        writer.writeCharacters(updated.getText());
        writer.writeEndElement();
    }
    
    private void writeTitle(XMLStreamWriter writer,Title title) throws Exception{
        writer.writeStartElement("title");
        if(title.getAttributes() != null){
            Iterator feedAttrs = title.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        writer.writeCharacters(title.getText());
        writer.writeEndElement();
    }
    
    private void writeAuthors(XMLStreamWriter writer,List authors) throws Exception{
        //loop through and print out each author.
        Iterator authorList = authors.iterator();
        while(authorList.hasNext()){
            Author author = (Author)authorList.next();
            writer.writeStartElement("author");
            if(author.getAttributes() != null){
                Iterator feedAttrs = author.getAttributes().iterator();
                //write the attributes
                while(feedAttrs.hasNext()){
                    Attribute feedAttr = (Attribute)feedAttrs.next();
                    writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
                }
            }
            writeName(writer,author.getName());
            if(author.getUri() != null){
                writeUri(writer,author.getUri());
            }
            if(author.getEmail() != null){
                writeEmail(writer,author.getEmail());
            }
            if(author.getExtensions() != null){
                writeExtensions(writer,author.getExtensions());
            }
            writer.writeEndElement();
        }
        
    }
    
    private void writeName(XMLStreamWriter writer,Name name) throws Exception{
        writer.writeStartElement("name");
        writer.writeCharacters(name.getText());
        writer.writeEndElement();
    }
    
    private void writeUri(XMLStreamWriter writer,URI uri) throws Exception{
        writer.writeStartElement("uri");
        writer.writeCharacters(uri.getText());
        writer.writeEndElement();
    }
    
    private void writeEmail(XMLStreamWriter writer,Email email) throws Exception{
        writer.writeStartElement("email");
        writer.writeCharacters(email.getText());
        writer.writeEndElement();
    }
    
    private void writeContributors(XMLStreamWriter writer,List contributors) throws Exception{
        //loop through and print out each contributor.
        Iterator contributorList = contributors.iterator();
        while(contributorList.hasNext()){
            Contributor contributor = (Contributor)contributorList.next();
            writer.writeStartElement("contributor");
            if(contributor.getAttributes() != null){
                Iterator feedAttrs = contributor.getAttributes().iterator();
                //write the attributes
                while(feedAttrs.hasNext()){
                    Attribute feedAttr = (Attribute)feedAttrs.next();
                    writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
                }
            }
            writeName(writer,contributor.getName());
            if(contributor.getUri() != null){
                writeUri(writer,contributor.getUri());
            }
            if(contributor.getEmail() != null){
                writeEmail(writer,contributor.getEmail());
            }
            if(contributor.getExtensions() != null){
                writeExtensions(writer,contributor.getExtensions());
            }
            writer.writeEndElement();
        }
    }
    
    private void writeRights(XMLStreamWriter writer, Rights rights) throws Exception{
        writer.writeStartElement("rights");
        if(rights.getAttributes() != null){
            Iterator feedAttrs = rights.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        writer.writeCharacters(rights.getText());
        writer.writeEndElement();
        
    }
    
    private void writeLogo(XMLStreamWriter writer, Logo logo) throws Exception{
        writer.writeStartElement("logo");
        if(logo.getAttributes() != null){
            Iterator feedAttrs = logo.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        writer.writeCharacters(logo.getUri());
        writer.writeEndElement();        
    }
    
    private void writeIcon(XMLStreamWriter writer, Icon icon) throws Exception{
        writer.writeStartElement("icon");
        if(icon.getAttributes() != null){
            Iterator feedAttrs = icon.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        writer.writeCharacters(icon.getUri());
        writer.writeEndElement(); 
    }
    
    private void writeCategories(XMLStreamWriter writer, List categories) throws Exception{
        
        Iterator categoryList = categories.iterator();
        while(categoryList.hasNext()){
            Category category = (Category)categoryList.next();
            writer.writeEmptyElement("category");                        
            if(category.getAttributes() != null){
                Iterator feedAttrs = category.getAttributes().iterator();
                //write the attributes
                while(feedAttrs.hasNext()){
                    Attribute feedAttr = (Attribute)feedAttrs.next();
                    writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
                }
            }
        }
    }
    
    private void writeLinks(XMLStreamWriter writer, List links) throws Exception{
        
        Iterator linksList = links.iterator();
        while(linksList.hasNext()){
            Link link = (Link)linksList.next();
            writer.writeEmptyElement("link");
            if(link.getAttributes() != null){
                Iterator feedAttrs = link.getAttributes().iterator();
                //write the attributes
                while(feedAttrs.hasNext()){
                    Attribute feedAttr = (Attribute)feedAttrs.next();
                    writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
                }
            }
        }
    }
    
    private void writeExtensions(XMLStreamWriter writer,List extensions) throws Exception{
        Iterator extensionsList = extensions.iterator();
        while(extensionsList.hasNext()){
            Extension extension = (Extension)extensionsList.next();
            writer.writeStartElement(extension.getElementName());
            if(extension.getAttributes() != null){
                Iterator feedAttrs = extension.getAttributes().iterator();
                //write the attributes
                while(feedAttrs.hasNext()){
                    Attribute feedAttr = (Attribute)feedAttrs.next();
                    writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
                }
            }
            if(extension.getContent() != null){
                writer.writeCharacters(extension.getContent());
            }
            writer.writeEndElement();
        }
    }
    
    private void writeEntries(XMLStreamWriter writer, Map entries) throws Exception{
        // TODO Auto-generated method stub
        
    }
}