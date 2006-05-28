package com.colorful.atom.beans;


import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;


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
        simplDateFmt = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'"+timeZoneOffset+"\'");
	}

    public Feed readFeed(XMLStreamReader reader) throws Exception{
        System.out.println("****DEBUG HELP FOR CONSTANTS*****");
        System.out.println("START_DOCUMENT = "+XMLStreamConstants.START_DOCUMENT);
        System.out.println("START_ELEMENT = "+XMLStreamConstants.START_ELEMENT);
        System.out.println("ATTRIBUTE = "+XMLStreamConstants.ATTRIBUTE);
        System.out.println("CDATA = "+XMLStreamConstants.CDATA);
        System.out.println("CHARACTERS = "+XMLStreamConstants.CHARACTERS);
        System.out.println("COMMENT = "+XMLStreamConstants.COMMENT);
        System.out.println("DTD = "+XMLStreamConstants.DTD);
        System.out.println("END_DOCUMENT = "+XMLStreamConstants.END_DOCUMENT);
        System.out.println("END_ELEMENT = "+XMLStreamConstants.END_ELEMENT);
        System.out.println("ENTITY_DECLARATION = "+XMLStreamConstants.ENTITY_DECLARATION);
        System.out.println("ENTITY_REFERENCE = "+XMLStreamConstants.ENTITY_REFERENCE);
        System.out.println("NAMESPACE = "+XMLStreamConstants.NAMESPACE);
        System.out.println("NOTATION_DECLARATION = "+XMLStreamConstants.NOTATION_DECLARATION);
        System.out.println("PROCESSING_INSTRUCTION = "+XMLStreamConstants.PROCESSING_INSTRUCTION);
        System.out.println("SPACE = "+XMLStreamConstants.SPACE);
       
        Feed feed = null;
        while(reader.hasNext()){
            switch (reader.next()){
            
            case XMLStreamConstants.START_DOCUMENT:
                System.out.println("in new start document.");
                FeedDoc.encoding = reader.getEncoding();
                FeedDoc.xml_version = reader.getVersion();
                break;
            
            case XMLStreamConstants.START_ELEMENT:
                System.out.println("found start element. "+reader.getName());
                //call each feed elements read method depending on the name
                if(reader.getLocalName().equals("feed")){
                    feed = new Feed();
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
                    System.out.println("characters = "+reader.getText());
                case XMLStreamConstants.COMMENT:
                case XMLStreamConstants.DTD:
                case XMLStreamConstants.END_DOCUMENT:
                case XMLStreamConstants.ENTITY_DECLARATION:
                case XMLStreamConstants.ENTITY_REFERENCE:
                case XMLStreamConstants.NAMESPACE:
                case XMLStreamConstants.NOTATION_DECLARATION:
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                case XMLStreamConstants.SPACE:
                    System.out.println("event type = "+reader.getEventType());
                    break;
                default:
                    throw new Exception("unknown event in the xml file = "+reader.getEventType());
            }
        }

        
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
            if (reader.getAttributeName(i).getPrefix() != null){
                attrName = reader.getAttributeName(i).getPrefix()+":"+reader.getAttributeName(i).getLocalPart();
            }else{
                attrName = reader.getAttributeName(i).getLocalPart();
            }
            if(attributes == null){
                attributes = new LinkedList();
            }
            attributes.add(new Attribute(attrName,reader.getAttributeValue(i)));
        }
        System.out.println("number of attributes = "+eventSkip);
        return attributes;
    }

    private Extension readExtension(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Entry readEntry(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Updated readUpdated(XMLStreamReader reader) throws Exception{
        Updated updated = new Updated();
        updated.setUpdated(simplDateFmt.parse(reader.getElementText()));        
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

    private Rights readRights(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Logo readLogo(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Link readLink(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Id readId(XMLStreamReader reader) throws Exception{
        Id id = new Id();
        id.setAttributes(getAttributes(reader));
        id.setUri(new URI(reader.getElementText()));        
        return id;
    }

    private Icon readIcon(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Generator readGenerator(XMLStreamReader reader) throws Exception{
        Generator generator = new Generator();
        generator.setAttributes(getAttributes(reader));
        generator.setText(reader.getElementText());
        return generator;
    }

    private Contributor readContributor(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Category readCategory(XMLStreamReader reader) {
        // TODO Auto-generated method stub
        return null;
    }

    private Author readAuthor(XMLStreamReader reader) throws Exception{
        Author author = new Author();
        author.setAttributes(getAttributes(reader));
        //skip to the end.
        return author;
    }
    
    
}