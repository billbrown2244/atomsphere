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
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamWriter;


public class FeedWriter{
    
    public void writeFeed(XMLStreamWriter writer, Feed feed,String encoding,String version) throws Exception{
        
        //write the xml header.
        writer.writeStartDocument(encoding,version);
        
        //open the feed element
        writer.writeStartElement("feed");
        if(feed.getAttributes() != null){
            Iterator feedAttrs = feed.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        //write the id (REQUIRED)
        writeID(writer,feed.getId());
        //write the updated date (REQUIRED)
        writeUpdated(writer,feed.getUpdated());
        //write the generator (should be required but isn't)
        if(feed.getGenerator() != null){
            writeGenerator(writer,feed.getGenerator());
        }
        //write the title (REQUIRED)
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
        writer.writeCharacters(logo.getUri().getText());
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
        writer.writeCharacters(icon.getUri().getText());
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
        
        System.out.println("entries size in feed writer = "+entries.size());
        //with this sorting, we want to print the last element first.
        Object[] entryArr = entries.values().toArray();
        for(int i=(entryArr.length -1); i >=0; i--){
            Entry entry = (Entry)entryArr[i];
            writer.writeStartElement("entry");
            if(entry.getAttributes() != null){
                Iterator entryAttrs = entry.getAttributes().iterator();
                //write the attributes
                while(entryAttrs.hasNext()){
                    Attribute entryAttr = (Attribute)entryAttrs.next();
                    writer.writeAttribute(entryAttr.getName(),entryAttr.getValue());
                }
            }
            //write the id
            writeID(writer,entry.getId());
            //write the updated date
            writeUpdated(writer,entry.getUpdated());
            //write the title
            writeTitle(writer,entry.getTitle());
            //write the source
            if(entry.getSource() != null){
                writeSource(writer,entry.getSource());
            }
            //write the author
            if(entry.getAuthors() != null){
                writeAuthors(writer,entry.getAuthors());
            }
            //write the contributors
            if(entry.getContributors() != null){
                writeContributors(writer,entry.getContributors());
            }
            //write the links
            if(entry.getLinks() != null){
                writeLinks(writer,entry.getLinks());
            }
            //write the categories
            if(entry.getCategories() != null){
                writeCategories(writer,entry.getCategories());
            }
            //write the published date
            if(entry.getPublished() != null){
                writePublished(writer,entry.getPublished());
            }
            //write the rights
            if(entry.getRights() != null){
                writeRights(writer,entry.getRights());
            }
            //write the extensions
            if(entry.getExtensions() != null){
                writeExtensions(writer,entry.getExtensions());
            }
            //write the summary
            if(entry.getSummary() != null){
                writeSummary(writer,entry.getSummary());
            }
            //write the content
            System.out.println("content in feed writer = "+entry.getContent());
            if(entry.getContent() != null){
                writeContent(writer,entry.getContent());
            }
            
            writer.writeEndElement();
        }
    }

    private void writeSummary(XMLStreamWriter writer, Summary summary) throws Exception{
        writer.writeStartElement("summary");
        if(summary.getAttributes() != null){
            Iterator feedAttrs = summary.getAttributes().iterator();
            //write the attributes
            while(feedAttrs.hasNext()){
                Attribute feedAttr = (Attribute)feedAttrs.next();
                writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
            }
        }
        writer.writeCharacters(summary.getText());
        writer.writeEndElement();
    }

    private void writePublished(XMLStreamWriter writer, Published published) throws Exception{
        writer.writeStartElement("published");
        writer.writeCharacters(published.getText());
        writer.writeEndElement();
    }

    private void writeContent(XMLStreamWriter writer, Content content) throws Exception{
        //look for the src attribute to see if we need to build
        //an empty tag.
        boolean externalLink = false;
        if(content.getAttributes() != null){
        	System.out.println("content has attributes.");
            Iterator contentAttrs = content.getAttributes().iterator();

            
            while(contentAttrs.hasNext()){
                Attribute contentAttr = (Attribute)contentAttrs.next();
                if(contentAttr.getName().equals("src")){
                    externalLink = true;
                    writer.writeEmptyElement("content");
                    break;
                }
                
            }
            if(!externalLink){
                writer.writeStartElement("content");
            }
            
            //write the attributes
            contentAttrs = content.getAttributes().iterator();
            while(contentAttrs.hasNext()){
                Attribute contentAttr = (Attribute)contentAttrs.next();
                writer.writeAttribute(contentAttr.getName(),contentAttr.getValue());
            }
            
        }else{//there are not attributes so assume default 'text';
        	System.out.println("content has no attributes");
            writer.writeStartElement("content");
        }
        System.out.println("content = "+content.getContent());
        if(content.getContent() != null){
            writer.writeCharacters(content.getContent());
        }
        if(!externalLink){
            writer.writeEndElement();
        }
    }
    
    private void writeSource(XMLStreamWriter writer, Source source) throws Exception{
        
        //open the source element
        writer.writeStartElement("source");
        if(source.getAttributes() != null){
            Iterator sourceAttrs = source.getAttributes().iterator();
            //write the attributes
            while(sourceAttrs.hasNext()){
                Attribute sourceAttr = (Attribute)sourceAttrs.next();
                writer.writeAttribute(sourceAttr.getName(),sourceAttr.getValue());
            }
        }
        //write the id
        if(source.getId() != null){
            writeID(writer,source.getId());
        }
        //write the updated date
        if(source.getUpdated() != null){
            writeUpdated(writer,source.getUpdated());
        }
        //write the generator
        if(source.getGenerator() != null){
            writeGenerator(writer,source.getGenerator());
        }
        //write the title
        if(source.getTitle() != null){
            writeTitle(writer,source.getTitle());
        }
        //write the author
        if(source.getAuthors() != null){
            writeAuthors(writer,source.getAuthors());
        }
        //write the contributors
        if(source.getContributors() != null){
            writeContributors(writer,source.getContributors());
        }
        //write the links
        if(source.getLinks() != null){
            writeLinks(writer,source.getLinks());
        }
        //write the categories
        if(source.getCategories() != null){
            writeCategories(writer,source.getCategories());
        }
        //write the icon
        if(source.getIcon() != null){
            writeIcon(writer,source.getIcon());
        }
        //write the logo
        if(source.getLogo() != null){
            writeLogo(writer,source.getLogo());
        }
        //write the rights
        if(source.getRights() != null){
            writeRights(writer,source.getRights());
        }
        //write the extensions
        if(source.getExtensions() != null){
            writeExtensions(writer,source.getExtensions());
        }
        
        writer.writeEndElement();
        
    }
}