/**
 * Copyright (C) 2009 William R. Brown <info@colorfulsoftware.com>
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
package com.colorful.atom;

import java.util.List;
import java.util.SortedMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Used by the FeedDoc to write a Feed bean to a xml file or java String.
 * 
 * @author Bill Brown
 * 
 */
class FeedWriter {

	// used internally by FeedDoc to write feed to output streams.
	void writeFeed(XMLStreamWriter writer, Feed feed) throws Exception {

		// open the feed element
		writer.writeStartElement("feed");
		if (feed.getAttributes() != null) {
			for (Attribute attr : feed.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
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

	void writeSubtitle(XMLStreamWriter writer, Subtitle subtitle)
			throws Exception {

		writer.writeStartElement("subtitle");

		if (writeAttributesAndCheckForXhtml(writer, subtitle.getAttributes())) {
			writer.writeCharacters(subtitle.getDivWrapperStart());
			writeXHTML(writer, subtitle.getText());
			writer.writeCharacters(subtitle.getDivWrapperEnd());
		} else {
			writer.writeCharacters(subtitle.getText());
		}

		writer.writeEndElement();
	}

	void writeGenerator(XMLStreamWriter writer, Generator generator)
			throws Exception {
		writer.writeStartElement("generator");
		if (generator.getAttributes() != null) {
			for (Attribute attr : generator.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
			writer.writeCharacters(generator.getText());
		}
		writer.writeEndElement();
	}

	void writeID(XMLStreamWriter writer, Id id) throws Exception {
		writer.writeStartElement("id");
		if (id.getAttributes() != null) {
			for (Attribute attr : id.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
		writer.writeCharacters(id.getAtomUri());
		writer.writeEndElement();
	}

	void writeUpdated(XMLStreamWriter writer, Updated updated) throws Exception {
		writer.writeStartElement("updated");
		writer.writeCharacters(updated.getText());
		writer.writeEndElement();
	}

	void writeTitle(XMLStreamWriter writer, Title title) throws Exception {

		writer.writeStartElement("title");

		if (writeAttributesAndCheckForXhtml(writer, title.getAttributes())) {
			System.out
					.println("title.startWrap: " + title.getDivWrapperStart());
			// writer.writeCharacters(title.getDivWrapperStart());
			writeXHTML(writer, title.getDivWrapperStart() + title.getText()
					+ title.getDivWrapperEnd());
			// writer.writeCharacters(title.getDivWrapperEnd());
		} else {
			writer.writeCharacters(title.getText());
		}

		writer.writeEndElement();
	}

	void writeXHTML(XMLStreamWriter writer, String text) throws Exception {
		try {
			
			System.out.println("Initial Text = " + text);
			
			if (text.indexOf('<') == -1) {
				System.out.println("writing all characters: "+text);
				writer.writeCharacters(text);
			} else {

				// write up until the
				// opening of the next element
				String localWrite = text.substring(0, text.indexOf('<'));
				System.out.println("writing some characters: "+localWrite);
				writer.writeCharacters(localWrite);
				text = text.substring(text.indexOf('<') + 1);
				System.out.println("remaining after some characters: "+text);

				// if we reach another < before > then this
				// is not a start element.
				if (text.indexOf(">") > text.indexOf("<")) {
					
					System.out.println("found < that isn't start element.");
					localWrite = text.substring(0, text.indexOf('<') + 1);
					System.out.println("writing some more characters: "+localWrite);
					writer.writeCharacters(localWrite);
					text = text.substring(text.indexOf('<') + 1);
					System.out.println("remaining after some characters: "+text);
					writeXHTML(writer,text);

				} else {

					//get the start element
					String startElement = text.substring(0, text.indexOf('>'))
							.trim();
					System.out.println("startElement: " + startElement);

					// check for end element.
					if (startElement.startsWith("/")) {
						System.out.println("writing end element.");
						// write the end element
						writer.writeEndElement();
						// search for the next element
						text = text.substring(text.indexOf('>') + 1);
						System.out.println("remaining after end element: "+text);
						writeXHTML(writer,text);
						
					} else {

						// check for empty element
						if (startElement.indexOf('/') == startElement.length() - 1) {
							System.out.println("writing empty element.");
							// check for attributes
							String[] attributes = startElement.split(" ");
							if (attributes.length > 1) {
								// if the name has a prefix, just
								// write it as part of the local name.
								writer.writeEmptyElement(attributes[0]);
								for (int i = 1; i < attributes.length; i++) {
									if (attributes[i].indexOf("=") != -1) {
										// we nee to put everything
										// to the right of the first '=' sign
										// in the value part because we could
										// have
										// a query string with multiple '='
										// signs.
										String attrName = attributes[i]
												.substring(0, attributes[i]
														.indexOf('='));
										String attrValue = attributes[i]
												.substring(attributes[i]
														.indexOf('=') + 1);
										writer.writeAttribute(attrName,
												attrValue);
									}
								}
							} else {
								// if the name has a prefix, just
								// write it as part of the local name.
								writer.writeEmptyElement(startElement);
							}
							

						} else {// this is regular start element
							System.out.println("writing normal element.");
							
							// check for attributes
							String[] attributes = startElement.split(" ");

							if (attributes.length > 1) {
								// if the name has a prefix,
								// just write it as part of the local name.

								writer.writeStartElement(attributes[0]);
								for (int i = 1; i < attributes.length; i++) {
									if (attributes[i].indexOf("=") != -1) {
										String attrName = attributes[i]
												.substring(0, attributes[i]
														.indexOf('='));
										String attrValue = attributes[i]
												.substring(attributes[i]
														.indexOf('=') + 1);
										writer.writeAttribute(attrName,
												attrValue);
									}
								}
								startElement = attributes[0];
							} else {
								// if the name has a prefix,
								// just write it as part of the local name.
								writer.writeStartElement(startElement);
							}
						}
						text = text.substring(text.indexOf('>') + 1);
						System.out.println("End Text = " + text);
						
						// search for the next element
						writeXHTML(writer, text);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Content is not valid XHTML", e);
		}
	}

	void writeAuthors(XMLStreamWriter writer, List<Author> authors)
			throws Exception {
		// loop through and print out each author.
		for (Author author : authors) {
			writer.writeStartElement("author");
			if (author.getAttributes() != null) {
				for (Attribute attr : author.getAttributes()) {
					writer.writeAttribute(attr.getName(), attr.getValue());
				}
			}
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
			if (contributor.getAttributes() != null) {
				for (Attribute attr : contributor.getAttributes()) {
					writer.writeAttribute(attr.getName(), attr.getValue());
				}
			}
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

		if (writeAttributesAndCheckForXhtml(writer, rights.getAttributes())) {
			writer.writeCharacters(rights.getDivWrapperStart());
			writeXHTML(writer, rights.getText());
			writer.writeCharacters(rights.getDivWrapperEnd());
		} else {
			writer.writeCharacters(rights.getText());
		}

		writer.writeEndElement();

	}

	void writeLogo(XMLStreamWriter writer, Logo logo) throws Exception {
		writer.writeStartElement("logo");
		if (logo.getAttributes() != null) {
			for (Attribute attr : logo.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
		writer.writeCharacters(logo.getAtomUri());
		writer.writeEndElement();
	}

	void writeIcon(XMLStreamWriter writer, Icon icon) throws Exception {
		writer.writeStartElement("icon");
		if (icon.getAttributes() != null) {
			for (Attribute attr : icon.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
		writer.writeCharacters(icon.getAtomUri());
		writer.writeEndElement();
	}

	void writeCategories(XMLStreamWriter writer, List<Category> categories)
			throws Exception {

		for (Category category : categories) {
			writer.writeEmptyElement("category");
			if (category.getAttributes() != null) {
				for (Attribute attr : category.getAttributes()) {
					writer.writeAttribute(attr.getName(), attr.getValue());
				}
			}
		}
	}

	void writeLinks(XMLStreamWriter writer, List<Link> links) throws Exception {

		for (Link link : links) {
			writer.writeEmptyElement("link");
			if (link.getAttributes() != null) {
				for (Attribute attr : link.getAttributes()) {
					writer.writeAttribute(attr.getName(), attr.getValue());
				}
			}
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
				if (extension.getAttributes() != null) {
					for (Attribute attr : extension.getAttributes()) {
						writer.writeAttribute(attr.getName(), attr.getValue());
					}
				}
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
				if (extension.getAttributes() != null) {
					for (Attribute attr : extension.getAttributes()) {
						writer.writeAttribute(attr.getName(), attr.getValue());
					}
				}
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
			if (entry.getAttributes() != null) {
				for (Attribute attr : entry.getAttributes()) {
					writer.writeAttribute(attr.getName(), attr.getValue());
				}
			}
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

		if (writeAttributesAndCheckForXhtml(writer, summary.getAttributes())) {
			writer.writeCharacters(summary.getDivWrapperStart());
			writeXHTML(writer, summary.getText());
			writer.writeCharacters(summary.getDivWrapperEnd());
		} else {
			writer.writeCharacters(summary.getText());
		}

		writer.writeEndElement();
	}

	private boolean writeAttributesAndCheckForXhtml(XMLStreamWriter writer,
			List<Attribute> attributes) throws XMLStreamException {
		if (attributes != null) {
			for (Attribute attr : attributes) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
		// check to see if we need to
		// wrap the text in a an <xhtml:div> tag.
		return (FeedDoc.getContentType(attributes) == FeedDoc.ContentType.XHTML);
	}

	void writePublished(XMLStreamWriter writer, Published published)
			throws Exception {
		writer.writeStartElement("published");
		writer.writeCharacters(published.getText());
		writer.writeEndElement();
	}

	void writeContent(XMLStreamWriter writer, Content content) throws Exception {

		// look for the src attribute to
		// see if we need to build an empty tag.
		if (FeedDoc.getAttributeFromGroup(content.getAttributes(), "src") != null) {
			System.out.println("writing empty content.");
			writer.writeEmptyElement("content");
		} else {
			System.out.println("writing filled content.");
			writer.writeStartElement("content");

			if (writeAttributesAndCheckForXhtml(writer, content.getAttributes())) {
				writer.writeCharacters(content.getDivWrapperStart());
				writeXHTML(writer, content.getContent());
				writer.writeCharacters(content.getDivWrapperEnd());
			} else {
				writer.writeCharacters(content.getContent());
			}

			writer.writeEndElement();
		}

	}

	void writeSource(XMLStreamWriter writer, Source source) throws Exception {

		// open the source element
		writer.writeStartElement("source");
		if (source.getAttributes() != null) {
			for (Attribute attr : source.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
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