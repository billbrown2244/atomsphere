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
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorful.atom.beans;


import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * This class is used by the FeedDoc to read an xml file into a Feed bean.
 * @author Bill Brown
 *
 */
public class FeedReader{
    
    private static SimpleDateFormat simplDateFmt = null;
    
    static{
        String timeZoneOffset = null;
        TimeZone timeZone = TimeZone.getDefault();
        int hours = (((timeZone.getRawOffset()/1000)/60)/60);
        if(hours >= 0){
            timeZoneOffset = TimeZone.getTimeZone("GMT"+"+"+hours).getID().substring(3);
        }else{
            timeZoneOffset = TimeZone.getTimeZone("GMT"+"-"+Math.abs(hours)).getID().substring(3);
        }
        simplDateFmt = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SS\'"+timeZoneOffset+"\'");
    }
    
    /**
     * This method transforms an xml stream into a Feed bean
     * @param reader the object containing the atom data
     * @return the atom Feed bean
     * @throws Exception if the stream cannot be parsed.
     */
    public Feed readFeed(XMLStreamReader reader) throws Exception{

        Feed feed = new Feed();
        while(reader.hasNext()){
            switch (reader.next()){
            
            case XMLStreamConstants.START_DOCUMENT:
                FeedDoc.encoding = reader.getEncoding();
                FeedDoc.xml_version = reader.getVersion();
                break;
     
            case XMLStreamConstants.START_ELEMENT:
                //call each feed elements read method depending on the name
                if(reader.getLocalName().equals("feed")){
                    feed.setAttributes(getAttributes(reader));
                }else if(reader.getLocalName().equals("author")){
                    feed.addAuthor(readAuthor(reader));
                }else if(reader.getLocalName().equals("category")){
                    feed.addCategory(readCategory(reader));
                }else if(reader.getLocalName().equals("contributor")){
                    feed.addContributor(readContributor(reader));
                }else if(reader.getLocalName().equals("generator")){
                    feed.setGenerator(readGenerator(reader));
                }else if(reader.getLocalName().equals("icon")){
                    feed.setIcon(readIcon(reader));
                }else if(reader.getLocalName().equals("id")){
                    feed.setId(readId(reader));
                }else if(reader.getLocalName().equals("link")){
                    feed.addLink(readLink(reader));
                }else if(reader.getLocalName().equals("logo")){
                    feed.setLogo(readLogo(reader));
                }else if(reader.getLocalName().equals("rights")){
                    feed.setRights(readRights(reader));
                }else if(reader.getLocalName().equals("subtitle")){
                    feed.setSubtitle(readSubtitle(reader));
                }else if(reader.getLocalName().equals("title")){
                    feed.setTitle(readTitle(reader));
                }else if(reader.getLocalName().equals("updated")){
                    feed.setUpdated(readUpdated(reader));
                }else if(reader.getLocalName().equals("entry")){
                    feed.addEntry(readEntry(reader));
                }else {//extension
                    feed.addExtension(readExtension(reader));
                }                
                break;
                
            case XMLStreamConstants.END_ELEMENT:
                reader.next();
                break;
            case XMLStreamConstants.ATTRIBUTE:
            case XMLStreamConstants.CDATA:
            case XMLStreamConstants.CHARACTERS:
            case XMLStreamConstants.COMMENT:
            case XMLStreamConstants.DTD:
            case XMLStreamConstants.END_DOCUMENT:
            case XMLStreamConstants.ENTITY_DECLARATION:
            case XMLStreamConstants.ENTITY_REFERENCE:
            case XMLStreamConstants.NAMESPACE:
            case XMLStreamConstants.NOTATION_DECLARATION:
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
            case XMLStreamConstants.SPACE:
                break;
            default:
                throw new Exception("unknown event in the xml file = "+reader.getEventType());
            }
        }
        
        //because the sort extension does not enforce placement of the element
        //do a check after the feed is built to determine if it needs to be sorted.
        feed.checkForAndApplyExtension(new Attribute("xmlns:sort","http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0"));
        
        return feed;
    }
    
    private List getAttributes(XMLStreamReader reader) throws Exception{
        List attributes = null;
        int eventSkip = 0;
        for(int i=0; i < reader.getNamespaceCount(); i++){
            eventSkip++;
            String attrName = "xmlns";
            if(reader.getNamespacePrefix(i) != null){
                attrName += ":"+reader.getNamespacePrefix(i);
            }
            if(attributes == null){
                attributes = new LinkedList();
            }
            attributes.add(new Attribute(attrName,reader.getNamespaceURI(i)));
        }
        for(int i=0; i < reader.getAttributeCount(); i++){
            eventSkip++;
            String attrName = null;
            if (reader.getAttributeName(i).getPrefix() != null && !reader.getAttributeName(i).getPrefix().equals("")){
                attrName = reader.getAttributeName(i).getPrefix()+":"+reader.getAttributeName(i).getLocalPart();
            }else{
                attrName = reader.getAttributeName(i).getLocalPart();
            }
            if(attributes == null){
                attributes = new LinkedList();
            }
            attributes.add(new Attribute(attrName,reader.getAttributeValue(i)));
        }

        return attributes;
    }
    
    private Extension readExtension(XMLStreamReader reader) throws Exception{
        Extension extension = new Extension();
        String prefix = reader.getPrefix();
        if(prefix != null && !prefix.equals("")){
            extension.setElementName(prefix+":"+reader.getLocalName());
        }else{
            extension.setElementName(reader.getLocalName());
        }
        extension.setAttributes(getAttributes(reader));
        extension.setContent(reader.getElementText());
        return extension;
    }
    
    private Entry readEntry(XMLStreamReader reader) throws Exception{
        boolean breakOut = false;
        Entry entry = new Entry();
        entry.setAttributes(getAttributes(reader));
        while(reader.hasNext()){
            switch (reader.next()){
            case XMLStreamConstants.START_ELEMENT:
                //call each feed elements read method depending on the name
                if(reader.getLocalName().equals("id")){
                    entry.setId(readId(reader));
                }else if(reader.getLocalName().equals("author")){
                    entry.addAuthor(readAuthor(reader));
                }else if(reader.getLocalName().equals("category")){
                    entry.addCategory(readCategory(reader));
                }else if(reader.getLocalName().equals("contributor")){
                    entry.addContributor(readContributor(reader));
                }else if(reader.getLocalName().equals("content")){
                    entry.setContent(readContent(reader));
                }else if(reader.getLocalName().equals("link")){
                    entry.addLink(readLink(reader));
                }else if(reader.getLocalName().equals("published")){
                    entry.setPublished(readPublished(reader));
                }else if(reader.getLocalName().equals("link")){
                    entry.addLink(readLink(reader));
                }else if(reader.getLocalName().equals("rights")){
                    entry.setRights(readRights(reader));
                }else if(reader.getLocalName().equals("source")){
                    entry.setSource(readSource(reader));
                }else if(reader.getLocalName().equals("summary")){
                    entry.setSummary(readSummary(reader));
                }else if(reader.getLocalName().equals("title")){
                    entry.setTitle(readTitle(reader));
                }else if(reader.getLocalName().equals("updated")){
                    entry.setUpdated(readUpdated(reader));
                }else {//extension
                    entry.addExtension(readExtension(reader));
                }
                break;
                
            case XMLStreamConstants.END_ELEMENT:
                if(reader.getLocalName().equals("entry")){
                    breakOut = true;
                }else{
                    reader.next();
                }
                break;            
            }
            if(breakOut){
                break;
            }
        }
        return entry;
    }
    
    private Summary readSummary(XMLStreamReader reader) throws Exception{
        Summary summary = new Summary();
        summary.setAttributes(getAttributes(reader));
        summary.setText(reader.getElementText());
        return summary;
    }

    private Source readSource(XMLStreamReader reader) throws Exception{
        boolean breakOut = false;
        Source source = new Source();
        source.setAttributes(getAttributes(reader));

        while(reader.hasNext()){
            switch (reader.next()){
            
            case XMLStreamConstants.START_ELEMENT:
                //call each feed elements read method depending on the name
                if(reader.getLocalName().equals("author")){
                    source.addAuthor(readAuthor(reader));
                }else if(reader.getLocalName().equals("category")){
                    source.addCategory(readCategory(reader));
                }else if(reader.getLocalName().equals("contributor")){
                    source.addContributor(readContributor(reader));
                }else if(reader.getLocalName().equals("generator")){
                    source.setGenerator(readGenerator(reader));
                }else if(reader.getLocalName().equals("icon")){
                    source.setIcon(readIcon(reader));
                }else if(reader.getLocalName().equals("id")){
                    source.setId(readId(reader));
                }else if(reader.getLocalName().equals("link")){
                    source.addLink(readLink(reader));
                }else if(reader.getLocalName().equals("logo")){
                    source.setLogo(readLogo(reader));
                }else if(reader.getLocalName().equals("rights")){
                    source.setRights(readRights(reader));
                }else if(reader.getLocalName().equals("subtitle")){
                    source.setSubtitle(readSubtitle(reader));
                }else if(reader.getLocalName().equals("title")){
                    source.setTitle(readTitle(reader));
                }else if(reader.getLocalName().equals("updated")){
                    source.setUpdated(readUpdated(reader));
                }else {//extension
                    source.addExtension(readExtension(reader));
                }                
                break;
                
            case XMLStreamConstants.END_ELEMENT:
                if(reader.getLocalName().equals("contributor")){
                    breakOut = true;
                }else{
                    reader.next();
                }
                break;
            }
            if(breakOut){
                break;
            }
        }
        return source;
    }

    private Published readPublished(XMLStreamReader reader) throws Exception{
        Published published = new Published();
        String dateText = reader.getElementText();
        try{
            published.setPublished(simplDateFmt.parse(dateText));        
        }catch(Exception e){
            SimpleDateFormat simpleDateFmt2 = new SimpleDateFormat(simplDateFmt.toPattern().substring(0,19));
            published.setPublished(simpleDateFmt2.parse(dateText.substring(0,19)));
        }
        return published;
    }

    private Content readContent(XMLStreamReader reader) throws Exception{
        Content content = new Content();
        content.setAttributes(getAttributes(reader));
        content.setContent(reader.getElementText());
        return content;
    }

    private Updated readUpdated(XMLStreamReader reader) throws Exception{
        Updated updated = new Updated();
        String dateText = reader.getElementText();
        try{
        updated.setUpdated(simplDateFmt.parse(dateText));        
        }catch(Exception e){
            SimpleDateFormat simpleDateFmt2 = new SimpleDateFormat(simplDateFmt.toPattern().substring(0,19));
            updated.setUpdated(simpleDateFmt2.parse(dateText.substring(0,19)));
        }
        return updated;
    }
    
    private Title readTitle(XMLStreamReader reader) throws Exception{
        Title title = new Title();
        title.setAttributes(getAttributes(reader));
        title.setText(reader.getElementText());
        return title;
    }
    
    private Subtitle readSubtitle(XMLStreamReader reader) throws Exception{
        Subtitle subtitle = new Subtitle();
        subtitle.setAttributes(getAttributes(reader));
        subtitle.setText(reader.getElementText());
        return subtitle;
    }
    
    private Rights readRights(XMLStreamReader reader) throws Exception{
        Rights rights = new Rights();
        rights.setAttributes(getAttributes(reader));
        rights.setText(reader.getElementText());
        return rights;
    }
    
    private Logo readLogo(XMLStreamReader reader) throws Exception{
        Logo logo = new Logo();
        logo.setAttributes(getAttributes(reader));
        logo.setUri(new URI(reader.getElementText()));
        return logo;
    }
    
    private Link readLink(XMLStreamReader reader) throws Exception{
        Link link = new Link();
        link.setAttributes(getAttributes(reader));
        return link;
    }
    
    private Id readId(XMLStreamReader reader) throws Exception{
        Id id = new Id();
        id.setAttributes(getAttributes(reader));
        id.setUri(new URI(reader.getElementText()));        
        return id;
    }
    
    private Icon readIcon(XMLStreamReader reader) throws Exception{
        Icon icon = new Icon();
        icon.setAttributes(getAttributes(reader));
        icon.setUri(new URI(reader.getElementText()));
        return icon;
    }
    
    private Generator readGenerator(XMLStreamReader reader) throws Exception{
        Generator generator = new Generator();
        generator.setAttributes(getAttributes(reader));
        generator.setText(reader.getElementText());
        return generator;
    }
    
    private Contributor readContributor(XMLStreamReader reader) throws Exception{
        boolean breakOut = false;
        Contributor contributor = new Contributor();
        contributor.setAttributes(getAttributes(reader));
        while(reader.hasNext()){
            switch (reader.next()){
            case XMLStreamConstants.START_ELEMENT:
                
                if(reader.getLocalName().equals("name")){
                    contributor.setName(new Name(reader.getElementText()));
                }else if(reader.getLocalName().equals("uri")){
                    contributor.setUri(new URI(reader.getElementText()));
                }else if(reader.getLocalName().equals("email")){
                    contributor.setEmail(new Email(reader.getElementText()));
                }else{
                    contributor.addExtension(readExtension(reader));
                }
                break;
                
            case XMLStreamConstants.END_ELEMENT:
                if(reader.getLocalName().equals("contributor")){
                    breakOut = true;
                }else{
                    reader.next();
                }
                break;
            }
            if(breakOut){
                break;
            }
        } 
        
        return contributor;
    }
    
    private Category readCategory(XMLStreamReader reader) throws Exception{
        Category category = new Category();
        category.setAttributes(getAttributes(reader));
        return category;
    }
    
    private Author readAuthor(XMLStreamReader reader) throws Exception{
        boolean breakOut = false;
        Author author = new Author();
        author.setAttributes(getAttributes(reader));
        while(reader.hasNext()){
            switch (reader.next()){
            case XMLStreamConstants.START_ELEMENT:
                
                if(reader.getLocalName().equals("name")){
                    author.setName(new Name(reader.getElementText()));
                }else if(reader.getLocalName().equals("uri")){
                    author.setUri(new URI(reader.getElementText()));
                }else if(reader.getLocalName().equals("email")){
                    author.setEmail(new Email(reader.getElementText()));
                }else{
                    author.addExtension(readExtension(reader));
                }
                break;
                
            case XMLStreamConstants.END_ELEMENT:
                if(reader.getLocalName().equals("author")){
                    breakOut = true;
                }else{
                    reader.next();
                }
                break;
            }
            if(breakOut){
                break;
            }
        } 
        
        return author;
    }
    
    
}