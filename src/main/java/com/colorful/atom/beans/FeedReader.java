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
 *  2008-03-11 wbrown - fix bug for atomXHTMLTextConstruct to wrap contents in xhtml:div element.
 */
package com.colorful.atom.beans;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This class is used by the FeedDoc to read an xml file into a Feed bean.
 * @author Bill Brown
 *
 */
class FeedReader{

	/**
	 * This method transforms an xml stream into a Feed bean
	 * @param reader the object containing the atom data
	 * @return the atom Feed bean
	 * @throws Exception if the stream cannot be parsed.
	 */
	Feed readFeed(XMLStreamReader reader) throws Exception{

		List<Attribute> attributes = null;
		List<Author> authors = null;
		List<Category> categories = null;
		List<Contributor> contributors = null;
		List<Link> links = null;
		List<Extension> extensions = null;
		Generator generator = null;
		Icon icon = null;
		Id id = null;
		Logo logo = null;
		Rights rights = null;
		Subtitle subtitle = null;
		Title title = null;
		Updated updated = null;
		SortedMap<String,Entry> entries = null;
		
		while(reader.hasNext()){
			switch (reader.next()){

			case XMLStreamConstants.START_DOCUMENT:
				FeedDoc.encoding = reader.getEncoding();
				FeedDoc.xml_version = reader.getVersion();
				break;

			case XMLStreamConstants.START_ELEMENT:
				//call each feed elements read method depending on the name
				if(reader.getLocalName().equals("feed")){
					attributes = getAttributes(reader,attributes);
				}else if(reader.getLocalName().equals("author")){
					authors = readAuthor(reader,authors);
				}else if(reader.getLocalName().equals("category")){
					categories = readCategory(reader,categories);
				}else if(reader.getLocalName().equals("contributor")){
					contributors = readContributor(reader,contributors);
				}else if(reader.getLocalName().equals("generator")){
					generator = readGenerator(reader);
				}else if(reader.getLocalName().equals("icon")){
					icon = readIcon(reader);
				}else if(reader.getLocalName().equals("id")){
					id = readId(reader);
				}else if(reader.getLocalName().equals("link")){
					links = readLink(reader,links);
				}else if(reader.getLocalName().equals("logo")){
					logo = readLogo(reader);
				}else if(reader.getLocalName().equals("rights")){
					rights = readRights(reader);
				}else if(reader.getLocalName().equals("subtitle")){
					subtitle = readSubtitle(reader);
				}else if(reader.getLocalName().equals("title")){
					title = readTitle(reader);
				}else if(reader.getLocalName().equals("updated")){
					updated = readUpdated(reader);
				}else if(reader.getLocalName().equals("entry")){
					entries = readEntry(reader,entries);
				}else {//extension
					extensions = readExtension(reader,extensions);
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
		
		Feed feed = FeedDoc.buildFeed(id,title,updated,rights,authors
				,categories,contributors,links,attributes,extensions
				,generator,subtitle,icon,logo,entries);

		//because the sort extension does not enforce placement of the element
		//do a check after the feed is built to determine if it needs to be sorted.
		return FeedDoc.checkForAndApplyExtension(feed,FeedDoc.sort);
	}

	List<Attribute> getAttributes(XMLStreamReader reader,List<Attribute> attributes) throws Exception{
		if(attributes == null){
			attributes = new LinkedList<Attribute>();
		}
		int eventSkip = 0;
		for(int i=0; i < reader.getNamespaceCount(); i++){
			eventSkip++;
			String attrName = "xmlns";
			if(reader.getNamespacePrefix(i) != null){
				attrName += ":"+reader.getNamespacePrefix(i);
			}
			if(attributes == null){
				attributes = new LinkedList<Attribute>();
			}
			attributes.add(FeedDoc.buildAttribute(attrName,reader.getNamespaceURI(i)));
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
				attributes = new LinkedList<Attribute>();
			}
			attributes.add(FeedDoc.buildAttribute(attrName,reader.getAttributeValue(i)));
		}

		return attributes;
	}

	List<Extension> readExtension(XMLStreamReader reader
			,List<Extension> extensions) throws Exception{
		
		if(extensions == null){
			extensions = new LinkedList<Extension>();
		}
		
		String elementName = null;
		String prefix = reader.getPrefix();
		if(prefix != null && !prefix.equals("")){
			elementName = prefix+":"+reader.getLocalName();
		}else{
			elementName = reader.getLocalName();
		}
		
		extensions.add(FeedDoc.buildExtension(elementName
				,getAttributes(reader,null),reader.getElementText()));
		return extensions;
	}

	SortedMap<String,Entry> readEntry(XMLStreamReader reader, SortedMap<String,Entry> entries) throws Exception{
		System.out.println("in read entry");
		if(entries == null){
			entries = new TreeMap<String,Entry>();
		}
		boolean breakOut = false;
		
		Id id = null;
		Title title = null;
		Updated updated = null;
		Rights rights = null;
		Content content = null;
		List<Attribute> attributes = null;
		List<Author> authors = null;
		List<Category> categories = null;
		List<Contributor> contributors = null;
		List<Link> links = null;
		List<Extension> extensions = null;
		Published published = null;
		Summary summary = null;
		Source source = null;
		
		attributes = getAttributes(reader,attributes);
		
		while(reader.hasNext()){
			switch (reader.next()){
			case XMLStreamConstants.START_ELEMENT:
				//call each feed elements read method depending on the name
				if(reader.getLocalName().equals("id")){
					id = readId(reader);
				}else if(reader.getLocalName().equals("author")){
					authors = readAuthor(reader,authors);
				}else if(reader.getLocalName().equals("category")){
					categories = readCategory(reader,categories);
				}else if(reader.getLocalName().equals("contributor")){
					contributors = readContributor(reader,contributors);
				}else if(reader.getLocalName().equals("content")){
					content = readContent(reader);
				}else if(reader.getLocalName().equals("link")){
					links = readLink(reader,links);
				}else if(reader.getLocalName().equals("published")){
					published = readPublished(reader);
				}else if(reader.getLocalName().equals("rights")){
					rights = readRights(reader);
				}else if(reader.getLocalName().equals("source")){
					source = readSource(reader);
				}else if(reader.getLocalName().equals("summary")){
					summary = readSummary(reader);
				}else if(reader.getLocalName().equals("title")){
					title = readTitle(reader);
				}else if(reader.getLocalName().equals("updated")){
					updated = readUpdated(reader);
				}else {//extension
					extensions = readExtension(reader,extensions);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				System.out.println("reached end entry.");
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
		
		entries.put(updated.getText(),FeedDoc.buildEntry(id,title,updated,rights,content
				,authors,categories,contributors,links,attributes
				,extensions,published,summary,source));
		
		return entries;
	}

	Summary readSummary(XMLStreamReader reader) throws Exception{
		List<Attribute> attributes = getAttributes(reader,null);
		//if the content is XHTML, we need to read in the contents of the div.
		String summary = null;
		if(containsXHTML(attributes)){
			summary = readXHTML(reader);
		}else{
			summary = reader.getElementText();
		}
		return FeedDoc.buildSummary(summary, attributes);
	}

	boolean containsXHTML(List<Attribute> attributes) {
		if(attributes != null){
			Iterator<Attribute> attrsItr = attributes.iterator();
			//look for the xhtml type.
			while(attrsItr.hasNext()){
				Attribute attribute = (Attribute)attrsItr.next();
				if(attribute.getName().equals("type") && attribute.getValue().equals("xhtml")){
					return true;
				}
			}
		}
		return false;
	}

	Source readSource(XMLStreamReader reader) throws Exception{
		boolean breakOut = false;
		
		List<Attribute> attributes = null;
		List<Author> authors = null;
		List<Category> categories = null;
		List<Contributor> contributors = null;
		List<Link> links = null;
		List<Extension> extensions = null;
		Generator generator = null;
		Icon icon = null;
		Id id = null;
		Logo logo = null;
		Rights rights = null;
		Subtitle subtitle = null;
		Title title = null;
		Updated updated = null;
		
		attributes = getAttributes(reader,attributes);

		while(reader.hasNext()){
			switch (reader.next()){

			case XMLStreamConstants.START_ELEMENT:
				//call each feed elements read method depending on the name
				if(reader.getLocalName().equals("author")){
					authors = readAuthor(reader,authors);
				}else if(reader.getLocalName().equals("category")){
					categories = readCategory(reader,categories);
				}else if(reader.getLocalName().equals("contributor")){
					contributors = readContributor(reader,contributors);
				}else if(reader.getLocalName().equals("generator")){
					generator = readGenerator(reader);
				}else if(reader.getLocalName().equals("icon")){
					icon = readIcon(reader);
				}else if(reader.getLocalName().equals("id")){
					id = readId(reader);
				}else if(reader.getLocalName().equals("link")){
					links = readLink(reader,links);
				}else if(reader.getLocalName().equals("logo")){
					logo = readLogo(reader);
				}else if(reader.getLocalName().equals("rights")){
					rights = readRights(reader);
				}else if(reader.getLocalName().equals("subtitle")){
					subtitle = readSubtitle(reader);
				}else if(reader.getLocalName().equals("title")){
					title = readTitle(reader);
				}else if(reader.getLocalName().equals("updated")){
					updated = readUpdated(reader);
				}else {//extension
					extensions = readExtension(reader,extensions);
				}                
				break;

			case XMLStreamConstants.END_ELEMENT:
				if(reader.getLocalName().equals("source")){
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
		return FeedDoc.buildSource(id,title,updated,rights
				,authors,categories,contributors,links,attributes
				,extensions,generator,subtitle,icon,logo);
	}

	SimpleDateFormat getSimpleDateFormat(){
		String timeZoneOffset = null;
		TimeZone timeZone = TimeZone.getDefault();
		//System.out.println("calnedar timezone = "+Calendar.getInstance().getTimeZone().toString());
		//System.out.println("timezone = "+timeZone.toString());
		int hours = (((timeZone.getRawOffset()/1000)/60)/60);
		System.out.println("hours = "+hours);
		if(hours >= 0){
			timeZoneOffset = TimeZone.getTimeZone("GMT"+"+"+hours).getID().substring(3);
		}else{
			timeZoneOffset = TimeZone.getTimeZone("GMT"+"-"+Math.abs(hours)).getID().substring(3);
		}
		System.out.println("simple date format = "+"yyyy-MM-dd\'T\'HH:mm:ss.SS\'"+timeZoneOffset+"\'");
		return new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SS\'"+timeZoneOffset+"\'");
	}
	
	Published readPublished(XMLStreamReader reader) throws Exception{
		String dateText = reader.getElementText();
		try{
			return FeedDoc.buildPublished(getSimpleDateFormat().parse(dateText));        
		}catch(Exception e){
			SimpleDateFormat simpleDateFmt2 = new SimpleDateFormat(getSimpleDateFormat().toPattern().substring(0,19));
			return FeedDoc.buildPublished(simpleDateFmt2.parse(dateText.substring(0,19)));
		}
	}
	

	
	Content readContent(XMLStreamReader reader) throws Exception{
		List<Attribute> attributes = getAttributes(reader,null);
		//if the content is XHTML, we need to skip the contents of the div.
		String content = null;
		if(containsXHTML(attributes)){
			content = readXHTML(reader);
		}else{			
			content = reader.getElementText();
		}
		return FeedDoc.buildContent(content, attributes);
	}
	

	Updated readUpdated(XMLStreamReader reader) throws Exception{
		String dateText = reader.getElementText();
		Updated updated = null;
		System.out.println("date Text before = "+dateText);
		try{
			//return FeedDoc.buildUpdated(getSimpleDateFormat().parse(dateText));    
			updated = FeedDoc.buildUpdated(getSimpleDateFormat().parse(dateText));
		}catch(Exception e){
			SimpleDateFormat simpleDateFmt2 = new SimpleDateFormat(getSimpleDateFormat().toPattern().substring(0,19));
			//return FeedDoc.buildUpdated(simpleDateFmt2.parse(dateText.substring(0,19)));
			updated = FeedDoc.buildUpdated(simpleDateFmt2.parse(dateText.substring(0,19)));
		}
		System.out.println("date Text after = "+updated.getText());
		return updated;
	}

	Title readTitle(XMLStreamReader reader) throws Exception{
		List<Attribute> attributes = getAttributes(reader,null);
		//if the content is XHTML, we need to read in the contents of the div.
		String title = null;
		if(containsXHTML(attributes)){
			title = readXHTML(reader);
		}else{
			title = reader.getElementText();
		}
		return FeedDoc.buildTitle(title, attributes);
	}

	String readXHTML(XMLStreamReader reader)
			throws XMLStreamException, Exception {
		StringBuffer xhtml = new StringBuffer();
		while(reader.hasNext()){
			boolean breakOut = false;
			switch (reader.next()){                 
				case XMLStreamConstants.START_ELEMENT:
					if(reader.getLocalName().equals("div")){
						//for now just ignore the attributes 
						getAttributes(reader,null);
					}else{
						if(reader.getPrefix() != null && !reader.getPrefix().equals("")){
							xhtml.append("<"+reader.getPrefix()+":"+reader.getLocalName());
						}else{
							xhtml.append("<"+reader.getLocalName());
						}
						List<Attribute> attributes = getAttributes(reader,null);
						//add the attributes
						if(attributes != null){
							Iterator<Attribute> attrItr = attributes.iterator();
							while(attrItr.hasNext()){
								Attribute attr = (Attribute)attrItr.next();
								xhtml.append(" "+attr.getName()+"="+attr.getValue());
							}
							xhtml.append(" ");
						}
						xhtml.append(">");
						
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(reader.getLocalName().equals("div")){
						breakOut = true;
					}else{
						if(reader.getPrefix() != null && !reader.getPrefix().equals("")){
							xhtml.append("</"+reader.getPrefix()+":"+reader.getLocalName()+">");
						}else{
							xhtml.append("</"+reader.getLocalName()+">");
						}
					}
					break;
				default:
					xhtml.append(reader.getText());					
			}
			if(breakOut){
				break;				
			}
		}
		return xhtml.toString().replaceAll("<br></br>","<br />").replaceAll("<hr></hr>","<hr />");
	}

	Subtitle readSubtitle(XMLStreamReader reader) throws Exception{
		List<Attribute> attributes = getAttributes(reader,null);
		//if the content is XHTML, we need to read in the contents of the div.
		String subtitle = null;
		if(containsXHTML(attributes)){
			subtitle = readXHTML(reader);
		}else{
			subtitle = reader.getElementText();
		}
		return FeedDoc.buildSubtitle(subtitle, attributes);
	}

	Rights readRights(XMLStreamReader reader) throws Exception{
		List<Attribute> attributes = getAttributes(reader,null);
		//if the content is XHTML, we need to read in the contents of the div.
		String rights = null;
		if(containsXHTML(attributes)){
			rights = readXHTML(reader);
		}else{
			rights = reader.getElementText();
		}
		return FeedDoc.buildRights(rights, attributes);
	}

	Logo readLogo(XMLStreamReader reader) throws Exception{
		return FeedDoc.buildLogo(getAttributes(reader,null),reader.getElementText());
	}

	List<Link> readLink(XMLStreamReader reader, List<Link> links) throws Exception{
		if(links == null){
			links = new LinkedList<Link>();
		}
		links.add(FeedDoc.buildLink(getAttributes(reader,null),reader.getElementText()));
		return links;
	}

	Id readId(XMLStreamReader reader) throws Exception{
		return FeedDoc.buildId(getAttributes(reader,null),reader.getElementText());
	}

	Icon readIcon(XMLStreamReader reader) throws Exception{
		return FeedDoc.buildIcon(getAttributes(reader,null),reader.getElementText());
	}

	Generator readGenerator(XMLStreamReader reader) throws Exception{
		return FeedDoc.buildGenerator(getAttributes(reader,null),reader.getElementText());
	}

	List<Contributor> readContributor(XMLStreamReader reader
			, List<Contributor> contributors) throws Exception{
		
		if(contributors == null){
			contributors = new LinkedList<Contributor>();
		}
		
		AtomPersonConstruct person = readAtomPersonConstruct(reader,"contributor");
		contributors.add(FeedDoc.buildContributor(person.getName(),person.getUri()
				,person.getEmail(),person.getAttributes(),person.getExtensions()));

		return contributors;
	}

	List<Category> readCategory(XMLStreamReader reader, List<Category> categories) throws Exception{
		if(categories == null){
			categories = new LinkedList<Category>();
		}
		categories.add(FeedDoc.buildCategory(getAttributes(reader,null),reader.getElementText()));
		return categories;
	}

	List<Author> readAuthor(XMLStreamReader reader
			, List<Author> authors) throws Exception{
		if(authors == null){
			authors = new LinkedList<Author>();
		}
		AtomPersonConstruct person = readAtomPersonConstruct(reader,"author");
		authors.add(FeedDoc.buildAuthor(person.getName(),person.getUri()
				,person.getEmail(),person.getAttributes(),person.getExtensions()));
		return authors;
	}

	AtomPersonConstruct readAtomPersonConstruct
	(XMLStreamReader reader,String personType) throws Exception{
		boolean breakOut = false;
		final List<Attribute> attributes = getAttributes(reader,null);
		Name name = null;
		URI uri = null;
		Email email = null;
		List<Extension> extensions = null;
		while(reader.hasNext()){
			switch (reader.next()){
			case XMLStreamConstants.START_ELEMENT:

				if(reader.getLocalName().equals("name")){
					name = FeedDoc.buildName(reader.getElementText());
				}else if(reader.getLocalName().equals("uri")){
					uri = FeedDoc.buildURI(reader.getElementText());
				}else if(reader.getLocalName().equals("email")){
					email = FeedDoc.buildEmail(reader.getElementText());
				}else{
					if(extensions == null){
						extensions = new LinkedList<Extension>();
					}
					extensions = readExtension(reader,extensions);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				if(reader.getLocalName().equals(personType)){
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
		return FeedDoc.buildAtomPersonConstruct(name,uri,email,attributes,extensions);
	}
}