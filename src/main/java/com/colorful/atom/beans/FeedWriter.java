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
 *  2006-11-14 wbrown - added javadoc documentation
 *  2007-02-19 wbrown - change looping through entries to be the same for all projects.
 *                      added support for writing empty extension elements.
 *  2007-06-20 wbrown - change the scope of writing entries to protected so that the FeedDoc.readEntryToString(Entry entry)  will work.
 *  2008-03-11 wbrown - fix bug for atomXHTMLTextConstruct to wrap contents in xhtml:div element.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import javax.xml.stream.XMLStreamWriter;

/**
 * This class is used by the FeedDoc to write a Feed bean to a xml file or java String.
 * @author Bill Brown
 *
 */
class FeedWriter{

	/**
	 * This method writes out the string representation of an atom feed to an xml file or java string.
	 * @param writer transforms the object to a string
	 * @param feed contains the data
	 * @param encoding file encoding
	 * @param version xml version
	 * @throws Exception if the file cannot be written to disk or if the string cannot be transformed.
	 */
	void writeFeed(XMLStreamWriter writer, Feed feed,String encoding,String version) throws Exception{

		//make sure the feed is sorted before it is written out to the file.
		//this prevents the client code from having to 
		//maintain the sorting during usage
		feed = FeedDoc.checkForAndApplyExtension(feed,FeedDoc.sort);

		//write the xml header.
		writer.writeStartDocument(encoding,version);

		//open the feed element
		writer.writeStartElement("feed");
		if(feed.getAttributes() != null){
			Iterator<Attribute> feedAttrs = feed.getAttributes().iterator();
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

		//write the subtitle
		if(feed.getSubtitle() != null){
			writeSubtitle(writer,feed.getSubtitle());
		}

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

	void writeSubtitle(XMLStreamWriter writer, Subtitle subtitle) throws Exception{
		boolean wrapInXhtmlDiv = false;
		writer.writeStartElement("subtitle");
		if(subtitle.getAttributes() != null){
			Iterator<Attribute> feedAttrs = subtitle.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());

				//check to see if we need to wrap the text in a an <xhtml:div> tag.
				if(feedAttr.getName().equals("type") && feedAttr.getValue().equals("xhtml")){
					wrapInXhtmlDiv = true;
				}
			}
		}

		if(wrapInXhtmlDiv){
			writer.writeStartElement("div");
			writer.writeAttribute("xmlns","http://www.w3.org/1999/xhtml");
			writeXHTML(writer,subtitle.getText());
			writer.writeEndElement();
		}else{
			writer.writeCharacters(subtitle.getText());
		}

		writer.writeEndElement();
	}

	void writeGenerator(XMLStreamWriter writer,Generator generator) throws Exception{
		writer.writeStartElement("generator");
		if(generator.getAttributes() != null){
			Iterator<Attribute> feedAttrs = generator.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
			}
			writer.writeCharacters(generator.getText());
		}
		writer.writeEndElement();
	}

	void writeID(XMLStreamWriter writer,Id id) throws Exception{
		writer.writeStartElement("id");
		if(id.getAttributes() != null){
			Iterator<Attribute> feedAttrs = id.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
			}
		}
		writer.writeCharacters(id.getAtomUri());
		writer.writeEndElement();
	}

	void writeUpdated(XMLStreamWriter writer,Updated updated) throws Exception{
		writer.writeStartElement("updated");
		writer.writeCharacters(updated.getText());
		writer.writeEndElement();
	}

	void writeTitle(XMLStreamWriter writer,Title title) throws Exception{
		boolean wrapInXhtmlDiv = false;
		writer.writeStartElement("title");
		if(title.getAttributes() != null){
			Iterator<Attribute> feedAttrs = title.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());

				//check to see if we need to wrap the text in a an <xhtml:div> tag.
				if(feedAttr.getName().equals("type") && feedAttr.getValue().equals("xhtml")){
					wrapInXhtmlDiv = true;
				}
			}
		}

		if(wrapInXhtmlDiv){
			writer.writeStartElement("div");
			writer.writeAttribute("xmlns","http://www.w3.org/1999/xhtml");
			writeXHTML(writer,title.getText());
			writer.writeEndElement();
		}else{
			writer.writeCharacters(title.getText());
		}

		writer.writeEndElement();
	}

	void writeXHTML(XMLStreamWriter writer, String text) throws Exception{
		try{

			if(text.indexOf('<') == -1){
				writer.writeCharacters(text);
			}else{
				//write up until the first element
				writer.writeCharacters(text.substring(0,text.indexOf('<')));
				text = text.substring(text.indexOf('<')+1);

				//get the opening element content 
				String startElement = text.substring(0,text.indexOf('>'));
				//check for empty element
				if(startElement.indexOf('/') == startElement.length()-1){
					//check for attributes
					String[] attributes = startElement.split(" ");
					if(attributes.length > 1){
						//if the name has a prefix, just write it as part of the local name.
						writer.writeEmptyElement(attributes[0]);
						for(int i=1; i < attributes.length; i++){
							if(attributes[i].indexOf("=") != -1){
								writer.writeAttribute(attributes[i].split("\\=")[0], attributes[i].split("\\=")[1]);
							}
						}
					}else{
						//if the name has a prefix, just write it as part of the local name.
						writer.writeEmptyElement(startElement.trim());
					}
					//search for the next element 
					writeXHTML(writer,text.substring(text.indexOf('>')+1));
				}else{//this is regular start element
					//check for attributes
					String[] attributes = startElement.split(" ");
					if(attributes.length > 1){
						//if the name has a prefix, just write it as part of the local name.
						writer.writeStartElement(attributes[0]);
						for(int i=1; i < attributes.length; i++){
							if(attributes[i].indexOf("=") != -1){
								writer.writeAttribute(attributes[i].split("\\=")[0], attributes[i].split("\\=")[1]);
							}
						}
					}else{
						//if the name has a prefix, just write it as part of the local name.
						writer.writeStartElement(startElement.trim());
					}
					//write the characters up until the begining of the end element.
					text = text.substring(startElement.length()+1);
					if(attributes.length > 1){
						writer.writeCharacters(text.substring(0,text.indexOf("</"+attributes[0])));
						text = text.substring(text.indexOf("</"+attributes[0])+("</"+attributes[0]+">").length());
					}else{
						writer.writeCharacters(text.substring(0,text.indexOf("</"+startElement)));
						text = text.substring(text.indexOf("</"+startElement)+("</"+startElement+">").length());
					}

					//write the end element
					writer.writeEndElement();

					writeXHTML(writer,text);
				}
			}
		}catch(Exception e){
			throw new Exception("Content is not valid XHTML",e);
		}
	}

	void writeAuthors(XMLStreamWriter writer,List<Author> authors) throws Exception{
		//loop through and print out each author.
		Iterator<Author> authorList = authors.iterator();
		while(authorList.hasNext()){
			Author author = authorList.next();
			writer.writeStartElement("author");
			if(author.getAttributes() != null){
				Iterator<Attribute> feedAttrs = author.getAttributes().iterator();
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

	void writeName(XMLStreamWriter writer,Name name) throws Exception{
		writer.writeStartElement("name");
		writer.writeCharacters(name.getText());
		writer.writeEndElement();
	}

	void writeUri(XMLStreamWriter writer,URI uri) throws Exception{
		writer.writeStartElement("uri");
		writer.writeCharacters(uri.getText());
		writer.writeEndElement();
	}

	void writeEmail(XMLStreamWriter writer,Email email) throws Exception{
		writer.writeStartElement("email");
		writer.writeCharacters(email.getText());
		writer.writeEndElement();
	}

	void writeContributors(XMLStreamWriter writer,List<Contributor> contributors) throws Exception{
		//loop through and print out each contributor.
		Iterator<Contributor> contributorList = contributors.iterator();
		while(contributorList.hasNext()){
			Contributor contributor = (Contributor)contributorList.next();
			writer.writeStartElement("contributor");
			if(contributor.getAttributes() != null){
				Iterator<Attribute> feedAttrs = contributor.getAttributes().iterator();
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

	void writeRights(XMLStreamWriter writer, Rights rights) throws Exception{
		boolean wrapInXhtmlDiv = false;
		writer.writeStartElement("rights");
		if(rights.getAttributes() != null){
			Iterator<Attribute> feedAttrs = rights.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());

				//check to see if we need to wrap the text in a an <xhtml:div> tag.
				if(feedAttr.getName().equals("type") && feedAttr.getValue().equals("xhtml")){
					wrapInXhtmlDiv = true;
				}
			}
		}
		if(wrapInXhtmlDiv){
			writer.writeStartElement("div");
			writer.writeAttribute("xmlns","http://www.w3.org/1999/xhtml");
			writeXHTML(writer,rights.getText());
			writer.writeEndElement();
		}else{
			writer.writeCharacters(rights.getText());
		}
		writer.writeEndElement();

	}

	void writeLogo(XMLStreamWriter writer, Logo logo) throws Exception{
		writer.writeStartElement("logo");
		if(logo.getAttributes() != null){
			Iterator<Attribute> feedAttrs = logo.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
			}
		}
		writer.writeCharacters(logo.getAtomUri());
		writer.writeEndElement();        
	}

	void writeIcon(XMLStreamWriter writer, Icon icon) throws Exception{
		writer.writeStartElement("icon");
		if(icon.getAttributes() != null){
			Iterator<Attribute> feedAttrs = icon.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
			}
		}
		writer.writeCharacters(icon.getAtomUri());
		writer.writeEndElement(); 
	}

	void writeCategories(XMLStreamWriter writer, List<Category> categories) throws Exception{

		Iterator<Category> categoryList = categories.iterator();
		while(categoryList.hasNext()){
			Category category = (Category)categoryList.next();
			writer.writeEmptyElement("category");                        
			if(category.getAttributes() != null){
				Iterator<Attribute> feedAttrs = category.getAttributes().iterator();
				//write the attributes
				while(feedAttrs.hasNext()){
					Attribute feedAttr = (Attribute)feedAttrs.next();
					writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
				}
			}
		}
	}

	void writeLinks(XMLStreamWriter writer, List<Link> links) throws Exception{

		Iterator<Link> linksList = links.iterator();
		while(linksList.hasNext()){
			Link link = (Link)linksList.next();
			writer.writeEmptyElement("link");
			if(link.getAttributes() != null){
				Iterator<Attribute> feedAttrs = link.getAttributes().iterator();
				//write the attributes
				while(feedAttrs.hasNext()){
					Attribute feedAttr = (Attribute)feedAttrs.next();
					writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
				}
			}
		}
	}

	void writeExtensions(XMLStreamWriter writer,List<Extension> extensions) throws Exception{
		Iterator<Extension> extensionsList = extensions.iterator();
		while(extensionsList.hasNext()){
			Extension extension = (Extension)extensionsList.next();

			//if there is no content, then write an empty extension element.
			if(extension.getContent() == null){
				String elementName = extension.getElementName();
				if(elementName.indexOf(":") == -1){
					writer.writeEmptyElement(elementName);
				}else{
					String prefix = elementName.substring(0,elementName.indexOf(":"));
					String localName = elementName.substring(elementName.indexOf(":")+1);
					writer.writeEmptyElement(prefix,localName,"");
				}
				if(extension.getAttributes() != null){
					Iterator<Attribute> feedAttrs = extension.getAttributes().iterator();
					//write the attributes
					while(feedAttrs.hasNext()){
						Attribute feedAttr = (Attribute)feedAttrs.next();
						writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
					}
				}
			}else{
				String elementName = extension.getElementName();
				if(elementName.indexOf(":") == -1){
					writer.writeStartElement(elementName);
				}else{
					String prefix = elementName.substring(0,elementName.indexOf(":"));
					String localName = elementName.substring(elementName.indexOf(":")+1);
					writer.writeStartElement(prefix,localName,"");
				}
				if(extension.getAttributes() != null){
					Iterator<Attribute> feedAttrs = extension.getAttributes().iterator();
					//write the attributes
					while(feedAttrs.hasNext()){
						Attribute feedAttr = (Attribute)feedAttrs.next();
						writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());
					}
				}
				//add the content.
				writer.writeCharacters(extension.getContent());

				//close the element.
				writer.writeEndElement();
			}
		}
	}

	void writeEntries(XMLStreamWriter writer, SortedMap<String,Entry> entries) throws Exception{

		//print out the entries.
		Iterator<Entry> entryItr = entries.values().iterator();
		while(entryItr.hasNext()){
			Entry entry = (Entry)entryItr.next();
			writer.writeStartElement("entry");
			if(entry.getAttributes() != null){
				Iterator<Attribute> entryAttrs = entry.getAttributes().iterator();
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
			if(entry.getContent() != null){
				writeContent(writer,entry.getContent());
			}

			writer.writeEndElement();
		}
	}

	void writeSummary(XMLStreamWriter writer, Summary summary) throws Exception{
		boolean wrapInXhtmlDiv = false;
		writer.writeStartElement("summary");
		if(summary.getAttributes() != null){
			Iterator<Attribute> feedAttrs = summary.getAttributes().iterator();
			//write the attributes
			while(feedAttrs.hasNext()){
				Attribute feedAttr = (Attribute)feedAttrs.next();
				writer.writeAttribute(feedAttr.getName(),feedAttr.getValue());

				//check to see if we need to wrap the text in a an <xhtml:div> tag.
				if(feedAttr.getName().equals("type") && feedAttr.getValue().equals("xhtml")){
					wrapInXhtmlDiv = true;
				}
			}
		}

		if(wrapInXhtmlDiv){
			writer.writeStartElement("div");
			writer.writeAttribute("xmlns","http://www.w3.org/1999/xhtml");
			writeXHTML(writer,summary.getText());
			writer.writeEndElement();
		}else{
			writer.writeCharacters(summary.getText());
		}

		writer.writeEndElement();
	}

	void writePublished(XMLStreamWriter writer, Published published) throws Exception{
		writer.writeStartElement("published");
		writer.writeCharacters(published.getText());
		writer.writeEndElement();
	}

	void writeContent(XMLStreamWriter writer, Content content) throws Exception{
		//look for the src attribute to see if we need to build
		//an empty tag.
		boolean externalLink = false;
		boolean wrapInXhtmlDiv = false;
		if(content.getAttributes() != null){
			Iterator<Attribute> contentAttrs = content.getAttributes().iterator();


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

				//check to see if we need to wrap the text in a an <xhtml:div> tag.
				if(contentAttr.getName().equals("type") && contentAttr.getValue().equals("xhtml")){
					wrapInXhtmlDiv = true;
				}
			}

		}else{//there are not attributes so assume default 'text';
			writer.writeStartElement("content");
		}
		if(content.getContent() != null){
			if(wrapInXhtmlDiv){
				writer.writeStartElement("div");
				writer.writeAttribute("xmlns","http://www.w3.org/1999/xhtml");
				writeXHTML(writer,content.getContent());
				writer.writeEndElement();
			}else{
				writer.writeCharacters(content.getContent());
			} 
		}
		if(!externalLink){
			writer.writeEndElement();
		}
	}

	void writeSource(XMLStreamWriter writer, Source source) throws Exception{

		//open the source element
		writer.writeStartElement("source");
		if(source.getAttributes() != null){
			Iterator<Attribute> sourceAttrs = source.getAttributes().iterator();
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