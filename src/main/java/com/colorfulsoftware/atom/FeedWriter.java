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
import java.util.Arrays;
import java.util.List;

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

	private String namespacePrefix = null;
	private String xhtmlNamespacePrefix = null;

	private void writeAttributes(XMLStreamWriter writer,
			List<Attribute> attributes) throws Exception {
		if (attributes != null) {
			for (Attribute attr : attributes) {
				if (attr.getName().indexOf(":") != -1) {
					namespacePrefix = attr.getName().substring(
							attr.getName().indexOf(":") + 1);
				}
				if (namespacePrefix != null
						&& attr.getValue().equals(
								"http://www.w3.org/1999/xhtml")) {
					xhtmlNamespacePrefix = namespacePrefix;
				}
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
	}

	void writeSubtitle(XMLStreamWriter writer, Subtitle subtitle)
			throws Exception {
		writer.writeStartElement("subtitle");
		writeAtomTextConstruct(writer, subtitle.getAttributes(), subtitle
				.getDivStartName(), subtitle.getDivStartAttribute(), subtitle
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
				.getDivStartName(), title.getDivStartAttribute(), title
				.getText(), title.getContentType());
		writer.writeEndElement();
	}

	private String writeEmptyElement(XMLStreamWriter writer, String text)
			throws Exception {
		String rawText;
		// do we have attributes.
		if (text.indexOf("=") != -1 && (text.indexOf("=") < text.indexOf("/>"))) {

			rawText = text.substring(0, text.indexOf("/>")).trim();

			String[] allData = rawText.split(" ");

			// write the start element.
			if (allData[0].indexOf(":") == -1) {
				writer.writeEmptyElement(allData[0]);
			} else {
				String prefix = allData[0].substring(0, rawText.indexOf(":"));
				String localName = allData[0]
						.substring(rawText.indexOf(":") + 1);
				writer.writeEmptyElement(prefix, localName, "");
			}

			// write the attributes.
			String[] attrs = Arrays.copyOfRange(allData, 1, allData.length);
			for (String attr : attrs) {
				String[] attrParts = attr.replaceAll("\"", "").split("=");
				writer.writeAttribute(attrParts[0], attrParts[1]);
			}

		} else {
			// just write the start element.
			rawText = text.substring(0, text.indexOf("/>")).trim();
			if (rawText.indexOf(":") == -1) {
				writer.writeEmptyElement(rawText);
			} else {
				String prefix = rawText.substring(0, rawText.indexOf(":"));
				String localName = rawText.substring(rawText.indexOf(":") + 1);
				writer.writeEmptyElement(prefix, localName, "");
			}
		}

		// skip past this start element and re-run the routine.
		return text.substring(text.indexOf("/>") + 2);

	}

	private String writeStartElement(XMLStreamWriter writer, String text)
			throws Exception {
		String rawText;
		// do we have attributes.
		if (text.indexOf("=") != -1 && (text.indexOf("=") < text.indexOf(">"))) {

			rawText = text.substring(0, text.indexOf(">")).trim();

			String[] allData = rawText.split(" ");

			// write the start element.
			if (allData[0].indexOf(":") == -1) {
				writer.writeStartElement(allData[0]);
			} else {
				String prefix = allData[0].substring(0, rawText.indexOf(":"));
				String localName = allData[0]
						.substring(rawText.indexOf(":") + 1);
				writer.writeStartElement(prefix, localName, "");
			}

			// write the attributes.
			String[] attrs = Arrays.copyOfRange(allData, 1, allData.length);
			for (String attr : attrs) {
				String[] attrParts = attr.replaceAll("\"", "").split("=");
				writer.writeAttribute(attrParts[0], attrParts[1]);
			}

		} else {
			// just write the start element.
			rawText = text.substring(0, text.indexOf(">")).trim();
			if (rawText.indexOf(":") == -1) {
				writer.writeStartElement(rawText);
			} else {
				String prefix = rawText.substring(0, rawText.indexOf(":"));
				String localName = rawText.substring(rawText.indexOf(":") + 1);
				writer.writeStartElement(prefix, localName, "");
			}
		}

		// skip past this start element and re-run the routine.
		return text.substring(text.indexOf(">") + 1);
	}

	private String writeEndElement(XMLStreamWriter writer, String text)
			throws Exception {
		// write the end element
		writer.writeEndElement();

		return text.substring(text.indexOf(">") + 1);
	}

	private String writeLessThanChar(XMLStreamWriter writer, String text)
			throws Exception {
		String rawText;
		// make sure to include the '<' characters in the text.
		while ((text.indexOf(">") > text.indexOf("<"))
				&& text.indexOf("<") != -1) {
			writer.writeCharacters("<");
			rawText = text.substring(0, text.indexOf("<"));
			writer.writeCharacters(rawText);
			text = text.substring(text.indexOf('<') + 1);
		}
		return text;
	}

	void writeXHTML(XMLStreamWriter writer, String text) throws Exception {

		// if text is null or empty return.
		if (text == null || text.length() == 0) {
			return;
		} else {
			String rawText;

			// prepare the markup to be written.
			text = text.replaceAll("&amp;", "&").replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">");

			// we are now at a start element,
			// or an empty element
			// or a '<'
			// or an end element.
			// or text.
			// lets check.

			// always check first for an end element since this routine is
			// recursive.
			if (text.indexOf("/") == 0 && text.indexOf(">") != -1
					&& text.indexOf("/>") != 0) {
				writeXHTML(writer, writeEndElement(writer, text));
				return;
			}

			// write text up to an element or the end of the text
			if (text.indexOf("<") != -1) {
				rawText = text.substring(0, text.indexOf('<'));
				writer.writeCharacters(rawText);

				text = text.substring(text.indexOf('<') + 1);

				// do we have an end element
				if (text.indexOf("/") == 0 && text.indexOf(">") != -1
						&& text.indexOf("/>") != 0) {
					writeXHTML(writer, writeEndElement(writer, text));
					return;
				}

				// do we have a '<' character
				if ((text.indexOf("<") < text.indexOf(">"))
						&& text.indexOf("<") != -1) {
					writeXHTML(writer, writeLessThanChar(writer, text));
					return;
				}

				// do we have an empty element
				if ((text.indexOf("/>") != -1)
						&& (text.indexOf("/>") < text.indexOf(">"))) {
					writeXHTML(writer, writeEmptyElement(writer, text));
					return;
				}

				// then this is a regular start element.
				writeXHTML(writer, writeStartElement(writer, text));

			} else {
				// we have just plain text so write it.
				writer.writeCharacters(text);
				return;
			}
		}
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
				.getDivStartName(), rights.getDivStartAttribute(), rights
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
				String eleName = extension.getElementName();
				if (eleName.indexOf(":") != -1) {
					if (xhtmlNamespacePrefix != null
							&& xhtmlNamespacePrefix.equals(eleName.substring(0,
									eleName.indexOf(":")))) {
						writeXHTML(writer, extension.getContent());
					}
				} else if (extension.getAttribute("xmlns") != null
						&& extension.getAttribute("xmlns").getValue().equals(
								"http://www.w3.org/1999/xhtml")) {
					writeXHTML(writer, extension.getContent());
					
				} else {
					writer.writeCharacters(extension.getContent());
				}

				// close the element.
				writer.writeEndElement();
			}
		}
	}

	void writeEntries(XMLStreamWriter writer, List<Entry> entries)
			throws Exception {
		// print out the entries.
		for (Entry entry : entries) {
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
				.getDivStartName(), summary.getDivStartAttribute(), summary
				.getText(), summary.getContentType());

		writer.writeEndElement();
	}

	private void writeAtomTextConstruct(XMLStreamWriter writer,
			List<Attribute> attributes, String divStartName,
			Attribute divStartAttribute, String text,
			AtomTextConstruct.ContentType contentType) throws Exception {
		// write the attributes if there are any
		writeAttributes(writer, attributes);
		// check to see if we need to
		// wrap the text in a an <xhtml:div> tag.
		if (contentType == AtomTextConstruct.ContentType.XHTML) {
			writer.writeStartElement(divStartName);
			if (divStartAttribute != null) {
				writer.writeAttribute(divStartAttribute.getName(),
						divStartAttribute.getValue());
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
					.getDivStartName(), content.getDivStartAttribute(), content
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
