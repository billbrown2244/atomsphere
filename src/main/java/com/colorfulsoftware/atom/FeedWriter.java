/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation
 *  2007-02-19 wbrown - change looping through entries 
 *  					to be the same for all projects.
 *                      added support for writing empty extension elements.
 *  2007-06-20 wbrown - change the scope of writing entries to protected 
 *  					so that the FeedDoc.readEntryToString(Entry entry)  
 *  					will work.
 *  2008-03-11 wbrown - fix bug for atomXHTMLTextConstruct to 
 *  					wrap contents in xhtml:div element.
 *  2008-04-09 wbrown - add the atomsphere generator tag to the output. 
 *  2008-04-17 wbrown - move document encoding back to FeedDoc.
 *  2008-09-22 wbrown - fix issue with xhtml attribute values.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;

import javax.xml.stream.XMLStreamWriter;

/**
 * Used by the FeedDoc to write a Feed bean to a xml file or java String.
 * 
 * @author Bill Brown
 * 
 */
class FeedWriter implements Serializable {

	private static final long serialVersionUID = 1698105180717850473L;

	// used internally by FeedDoc to write feed to output streams.
	void writeFeed(XMLStreamWriter writer, Feed feed) throws Exception {

		// open the feed element
		writer.writeStartElement("feed");
		// write the attributes.
		writeAttributes(writer, feed.getAttributes());
		// write the id (REQUIRED)
		writeID(writer, feed.getId());
		// write the updated date (REQUIRED)
		writeUpdated(writer, feed.getUpdated());
		// write the generator (should be required but isn't)
		if (feed.getGenerator() != null) {
			writeGenerator(writer, feed.getGenerator());
		}
		// write the title (REQUIRED)
		writeTitle(writer, feed.getTitle());

		// write the subtitle
		if (feed.getSubtitle() != null) {
			writeSubtitle(writer, feed.getSubtitle());
		}

		// write the author
		if (feed.getAuthors() != null) {
			writeAuthors(writer, feed.getAuthors());
		}
		// write the contributors
		if (feed.getContributors() != null) {
			writeContributors(writer, feed.getContributors());
		}
		// write the links
		if (feed.getLinks() != null) {
			writeLinks(writer, feed.getLinks());
		}
		// write the categories
		if (feed.getCategories() != null) {
			writeCategories(writer, feed.getCategories());
		}
		// write the icon
		if (feed.getIcon() != null) {
			writeIcon(writer, feed.getIcon());
		}
		// write the logo
		if (feed.getLogo() != null) {
			writeLogo(writer, feed.getLogo());
		}
		// write the rights
		if (feed.getRights() != null) {
			writeRights(writer, feed.getRights());
		}
		// write the extensions
		if (feed.getExtensions() != null) {
			writeExtensions(writer, feed.getExtensions());
		}
		// write the entries
		if (feed.getEntries() != null) {
			writeEntries(writer, feed.getEntries());
		}

		writer.writeEndElement();

	}

	private void writeAttributes(XMLStreamWriter writer,
			List<Attribute> attributes) throws Exception {
		if (attributes != null) {
			for (Attribute attr : attributes) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
	}

	void writeSubtitle(XMLStreamWriter writer, Subtitle subtitle)
			throws Exception {
		writer.writeStartElement("subtitle");
		writeAtomTextConstruct(writer, subtitle.getAttributes(), subtitle
				.getDivWrapperStart(), subtitle
				.getDivWrapperStartAttr(), subtitle
				.getText(), subtitle.getContentType());

		writer.writeEndElement();
	}

	void writeGenerator(XMLStreamWriter writer, Generator generator)
			throws Exception {
		writer.writeStartElement("generator");
		// write the attributes.
		writeAttributes(writer, generator.getAttributes());
		writer.writeCharacters(generator.getText());
		writer.writeEndElement();
	}

	void writeID(XMLStreamWriter writer, Id id) throws Exception {
		writer.writeStartElement("id");
		// write the attributes.
		writeAttributes(writer, id.getAttributes());
		writer.writeCharacters(id.getAtomUri());
		writer.writeEndElement();
	}

	void writeUpdated(XMLStreamWriter writer, Updated updated) throws Exception {
		writer.writeStartElement("updated");
		// write the attributes.
		writeAttributes(writer, updated.getAttributes());
		writer.writeCharacters(updated.getText());
		writer.writeEndElement();
	}

	void writeTitle(XMLStreamWriter writer, Title title) throws Exception {
		writer.writeStartElement("title");
		writeAtomTextConstruct(writer, title.getAttributes(), title
				.getDivWrapperStart(), title.getDivWrapperStartAttr(), title
				.getText(), title.getContentType());

		writer.writeEndElement();
	}

	void writeXHTML(XMLStreamWriter writer, String text) throws Exception {
		System.out.println("textBefore:\n" + text);
		String txt = text.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&");
		System.out.println("textAfter:\n" + txt);
		writer.writeCharacters(txt);

		/*
		 * if (text == null || text.length() == 0) { return; }
		 * 
		 * if (text.indexOf('<') == -1) {
		 * 
		 * writer.writeCharacters(text.replace("&lt;", "<").replace("&gt;",
		 * ">").replaceAll("&amp;", "&"));
		 * 
		 * } else {
		 * 
		 * if (text.startsWith("</")) { // write the end element
		 * writer.writeEndElement(); // search for the next element text =
		 * text.substring(text.indexOf('>') + 1); writeXHTML(writer, text);
		 * 
		 * } else {
		 * 
		 * // write up until the // opening of the next element // except if
		 * this is an end tag. String localWrite = null; if
		 * (!text.startsWith("/")) { localWrite = text.substring(0,
		 * text.indexOf('<')); writer.writeCharacters(localWrite.replace("&lt;",
		 * "<") .replace("&gt;", ">").replaceAll("&amp;", "&")); text =
		 * text.substring(text.indexOf('<') + 1); }
		 * 
		 * // if we reach another < before > then this // is not a start
		 * element. /* if ((text.indexOf(">") > text.indexOf("<")) &&
		 * !text.startsWith("/")) {
		 * 
		 * writer.writeCharacters(localWrite.replace("&lt;", "<")
		 * .replace("&gt;", ">").replaceAll("&amp;", "&")); text =
		 * text.substring(localWrite.length() - 1);
		 * 
		 * writeXHTML(writer, text);
		 * 
		 * } else {
		 * 
		 * // get the start element String startElement = text.substring(0,
		 * text.indexOf('>')) .trim();
		 * 
		 * // check for end element. if (startElement.startsWith("/")) { //
		 * write the end element writer.writeEndElement(); // search for the
		 * next element text = text.substring(text.indexOf('>') + 1);
		 * writeXHTML(writer, text);
		 * 
		 * } else { System.out.println("startELement: "+startElement); // check
		 * for empty element if (startElement.indexOf('/') ==
		 * startElement.length() - 1) { // check for attributes String[]
		 * attributes = startElement.split(" "); if (attributes.length > 1) { //
		 * if the name has a prefix, just // write it as part of the local name.
		 * writer.writeEmptyElement(attributes[0]); for (int i = 1; i <
		 * attributes.length; i++) { if (attributes[i].indexOf("=") != -1) { //
		 * we nee to put everything // to the right of the first '=' // sign //
		 * in the value part because we // could // have // a query string with
		 * multiple '=' // signs. String attrName = attributes[i].substring( 0,
		 * attributes[i].indexOf('=')); String attrValue =
		 * attributes[i].substring( attributes[i].indexOf('=') + 2,
		 * attributes[i].lastIndexOf("\"")); writer.writeAttribute(attrName,
		 * attrValue); } } } else { // if the name has a prefix, just // write
		 * it as part of the local name. writer.writeEmptyElement(startElement);
		 * }
		 * 
		 * } else {// this is regular start element
		 * 
		 * // check for attributes String[] attributes =
		 * startElement.split(" ");
		 * 
		 * if (attributes.length > 1) { // if the name has a prefix, // just
		 * write it as part of the local name.
		 * 
		 * writer.writeStartElement(attributes[0]); for (int i = 1; i <
		 * attributes.length; i++) { if (attributes[i].indexOf("=") != -1) {
		 * String attrName = attributes[i].substring( 0,
		 * attributes[i].indexOf('=')); String attrValue =
		 * attributes[i].substring( attributes[i].indexOf('=') + 2,
		 * attributes[i].lastIndexOf("\"")); writer.writeAttribute(attrName,
		 * attrValue); } } startElement = attributes[0]; } else { // if the name
		 * has a prefix, // just write it as part of the local name.
		 * writer.writeStartElement(attributes[0]); } } text =
		 * text.substring(text.indexOf('>') + 1);
		 * 
		 * // search for the next element writeXHTML(writer, text); } // } } }
		 */
	}

	void writeAuthors(XMLStreamWriter writer, List<Author> authors)
			throws Exception {
		// loop through and print out each author.
		for (Author author : authors) {
			writer.writeStartElement("author");
			// write the attributes.
			writeAttributes(writer, author.getAttributes());
			writeName(writer, author.getName());
			if (author.getUri() != null) {
				writeUri(writer, author.getUri());
			}
			if (author.getEmail() != null) {
				writeEmail(writer, author.getEmail());
			}
			if (author.getExtensions() != null) {
				writeExtensions(writer, author.getExtensions());
			}
			writer.writeEndElement();
		}
	}

	void writeName(XMLStreamWriter writer, Name name) throws Exception {
		writer.writeStartElement("name");
		writer.writeCharacters(name.getText());
		writer.writeEndElement();
	}

	void writeUri(XMLStreamWriter writer, URI uri) throws Exception {
		writer.writeStartElement("uri");
		writer.writeCharacters(uri.getText());
		writer.writeEndElement();
	}

	void writeEmail(XMLStreamWriter writer, Email email) throws Exception {
		writer.writeStartElement("email");
		writer.writeCharacters(email.getText());
		writer.writeEndElement();
	}

	void writeContributors(XMLStreamWriter writer,
			List<Contributor> contributors) throws Exception {
		// loop through and print out each contributor.
		for (Contributor contributor : contributors) {
			writer.writeStartElement("contributor");
			// write the attributes.
			writeAttributes(writer, contributor.getAttributes());
			writeName(writer, contributor.getName());
			if (contributor.getUri() != null) {
				writeUri(writer, contributor.getUri());
			}
			if (contributor.getEmail() != null) {
				writeEmail(writer, contributor.getEmail());
			}
			if (contributor.getExtensions() != null) {
				writeExtensions(writer, contributor.getExtensions());
			}
			writer.writeEndElement();
		}
	}

	void writeRights(XMLStreamWriter writer, Rights rights) throws Exception {
		writer.writeStartElement("rights");
		writeAtomTextConstruct(writer, rights.getAttributes(), rights
				.getDivWrapperStart(), rights.getDivWrapperStartAttr(), rights
				.getText(), rights.getContentType());

		writer.writeEndElement();
	}

	void writeLogo(XMLStreamWriter writer, Logo logo) throws Exception {
		writer.writeStartElement("logo");
		// write the attributes.
		writeAttributes(writer, logo.getAttributes());
		writer.writeCharacters(logo.getAtomUri());
		writer.writeEndElement();
	}

	void writeIcon(XMLStreamWriter writer, Icon icon) throws Exception {
		writer.writeStartElement("icon");
		// write the attributes.
		writeAttributes(writer, icon.getAttributes());
		writer.writeCharacters(icon.getAtomUri());
		writer.writeEndElement();
	}

	void writeCategories(XMLStreamWriter writer, List<Category> categories)
			throws Exception {
		for (Category category : categories) {
			writer.writeEmptyElement("category");
			// write the attributes.
			writeAttributes(writer, category.getAttributes());
		}
	}

	void writeLinks(XMLStreamWriter writer, List<Link> links) throws Exception {
		for (Link link : links) {
			writer.writeEmptyElement("link");
			// write the attributes.
			writeAttributes(writer, link.getAttributes());
		}
	}

	void writeExtensions(XMLStreamWriter writer, List<Extension> extensions)
			throws Exception {
		for (Extension extension : extensions) {

			// if there is no content, then
			// write an empty extension element.
			if (extension.getContent() == null
					|| extension.getContent().trim().equals("")) {
				String elementName = extension.getElementName();
				if (elementName.indexOf(":") == -1) {
					writer.writeEmptyElement(elementName);
				} else {
					String prefix = elementName.substring(0, elementName
							.indexOf(":"));
					String localName = elementName.substring(elementName
							.indexOf(":") + 1);
					writer.writeEmptyElement(prefix, localName, "");
				}
				// write the attributes.
				writeAttributes(writer, extension.getAttributes());
			} else {
				String elementName = extension.getElementName();
				if (elementName.indexOf(":") == -1) {
					writer.writeStartElement(elementName);
				} else {
					String prefix = elementName.substring(0, elementName
							.indexOf(":"));
					String localName = elementName.substring(elementName
							.indexOf(":") + 1);
					writer.writeStartElement(prefix, localName, "");
				}
				// write the attributes.
				writeAttributes(writer, extension.getAttributes());
				// add the content.
				writer.writeCharacters(extension.getContent());

				// close the element.
				writer.writeEndElement();
			}
		}
	}

	void writeEntries(XMLStreamWriter writer, SortedMap<String, Entry> entries)
			throws Exception {
		// print out the entries.
		for (Entry entry : entries.values()) {
			writer.writeStartElement("entry");
			// write the attributes.
			writeAttributes(writer, entry.getAttributes());
			// write the id
			writeID(writer, entry.getId());
			// write the updated date
			writeUpdated(writer, entry.getUpdated());
			// write the title
			writeTitle(writer, entry.getTitle());
			// write the source
			if (entry.getSource() != null) {
				writeSource(writer, entry.getSource());
			}
			// write the author
			if (entry.getAuthors() != null) {
				writeAuthors(writer, entry.getAuthors());
			}
			// write the contributors
			if (entry.getContributors() != null) {
				writeContributors(writer, entry.getContributors());
			}
			// write the links
			if (entry.getLinks() != null) {
				writeLinks(writer, entry.getLinks());
			}
			// write the categories
			if (entry.getCategories() != null) {
				writeCategories(writer, entry.getCategories());
			}
			// write the published date
			if (entry.getPublished() != null) {
				writePublished(writer, entry.getPublished());
			}
			// write the rights
			if (entry.getRights() != null) {
				writeRights(writer, entry.getRights());
			}
			// write the extensions
			if (entry.getExtensions() != null) {
				writeExtensions(writer, entry.getExtensions());
			}
			// write the summary
			if (entry.getSummary() != null) {
				writeSummary(writer, entry.getSummary());
			}
			// write the content
			if (entry.getContent() != null) {
				writeContent(writer, entry.getContent());
			}

			writer.writeEndElement();
		}
	}

	void writeSummary(XMLStreamWriter writer, Summary summary) throws Exception {
		writer.writeStartElement("summary");
		writeAtomTextConstruct(writer, summary.getAttributes(), summary
				.getDivWrapperStart(), summary.getDivWrapperStartAttr(), summary
				.getText(), summary.getContentType());

		writer.writeEndElement();
	}

	private void writeAtomTextConstruct(XMLStreamWriter writer,
			List<Attribute> attributes, String divWrapperName,
			Attribute divWrapperAttribute, String text,
			AtomTextConstruct.ContentType contentType) throws Exception {
		// write the attributes if there are any
		writeAttributes(writer, attributes);
		// check to see if we need to
		// wrap the text in a an <xhtml:div> tag.
		if (contentType == AtomTextConstruct.ContentType.XHTML) {
			writer.writeStartElement(divWrapperName);
			if (divWrapperAttribute != null) {
				writer.writeAttribute(divWrapperAttribute.getName(),
						divWrapperAttribute.getValue());
			}
			writeXHTML(writer, text);
			writer.writeEndElement();
			// check to see if we need to escape the data
		} else if (contentType == AtomTextConstruct.ContentType.HTML) {

			writer.writeCharacters(text);// .replaceAll("&", "&amp;").replace(
			// "&lt;", "<").replace(">", "&gt;"));

			// just write the text.
		} else {
			writer.writeCharacters(text);
		}
	}

	void writePublished(XMLStreamWriter writer, Published published)
			throws Exception {
		writer.writeStartElement("published");
		// write the attributes.
		writeAttributes(writer, published.getAttributes());
		writer.writeCharacters(published.getText());
		writer.writeEndElement();
	}

	void writeContent(XMLStreamWriter writer, Content content) throws Exception {
		// look for the src attribute to
		// see if we need to build an empty tag.
		if (content.getAttribute("src") != null) {
			writer.writeEmptyElement("content");
			// write the attributes.
			writeAttributes(writer, content.getAttributes());
		} else {
			writer.writeStartElement("content");
			writeAtomTextConstruct(writer, content.getAttributes(), content
					.getDivWrapperStart(), content.getDivWrapperStartAttr(), content
					.getContent(), content.getContentType());

			writer.writeEndElement();
		}
	}

	void writeSource(XMLStreamWriter writer, Source source) throws Exception {
		// open the source element
		writer.writeStartElement("source");
		// write the attributes.
		writeAttributes(writer, source.getAttributes());
		// write the id
		if (source.getId() != null) {
			writeID(writer, source.getId());
		}
		// write the updated date
		if (source.getUpdated() != null) {
			writeUpdated(writer, source.getUpdated());
		}
		// write the generator
		if (source.getGenerator() != null) {
			writeGenerator(writer, source.getGenerator());
		}
		// write the title
		if (source.getTitle() != null) {
			writeTitle(writer, source.getTitle());
		}
		// write the author
		if (source.getAuthors() != null) {
			writeAuthors(writer, source.getAuthors());
		}
		// write the contributors
		if (source.getContributors() != null) {
			writeContributors(writer, source.getContributors());
		}
		// write the links
		if (source.getLinks() != null) {
			writeLinks(writer, source.getLinks());
		}
		// write the categories
		if (source.getCategories() != null) {
			writeCategories(writer, source.getCategories());
		}
		// write the icon
		if (source.getIcon() != null) {
			writeIcon(writer, source.getIcon());
		}
		// write the logo
		if (source.getLogo() != null) {
			writeLogo(writer, source.getLogo());
		}
		// write the rights
		if (source.getRights() != null) {
			writeRights(writer, source.getRights());
		}
		// write the extensions
		if (source.getExtensions() != null) {
			writeExtensions(writer, source.getExtensions());
		}

		writer.writeEndElement();
	}
}
