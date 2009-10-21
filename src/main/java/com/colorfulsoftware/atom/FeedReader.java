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
 *  2006-11-14 wbrown - added javadoc documentation.
 *  2008-03-11 wbrown - fix bug for atomXHTMLTextConstruct to wrap contents in xhtml:div element.
 *  2008-04-17 wbrown - add check for start document in readEntry
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This class is used by the FeedDoc to read an xml file into a Feed bean.
 * 
 * @author Bill Brown
 * 
 */
class FeedReader implements Serializable {

	private static final long serialVersionUID = -2510954590037544812L;

	FeedDoc feedDoc;

	/**
	 * This method transforms an xml stream into a Feed bean
	 * 
	 * @param reader
	 *            the object containing the atom data
	 * @return the atom Feed bean
	 * @throws Exception
	 *             if the stream cannot be parsed.
	 */
	Feed readFeed(XMLStreamReader reader) throws Exception {
		if (feedDoc == null) {
			feedDoc = new FeedDoc();
		}
		List<Attribute> attributes = getAttributes(reader);
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
		SortedMap<String, Entry> entries = null;
		String elementName = null;

		while (reader.hasNext()) {
			switch (reader.next()) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);

				if (elementName.equals("author")
						|| elementName.equals("atom:author")) {
					authors = readAuthor(reader, authors);
				} else if (elementName.equals("category")
						|| elementName.equals("atom:category")) {
					categories = readCategory(reader, categories);
				} else if (elementName.equals("contributor")
						|| elementName.equals("atom:contributor")) {
					contributors = readContributor(reader, contributors);
				} else if (elementName.equals("generator")
						|| elementName.equals("atom:generator")) {
					generator = readGenerator(reader);
				} else if (elementName.equals("icon")
						|| elementName.equals("atom:icon")) {
					icon = readIcon(reader);
				} else if (elementName.equals("id")
						|| elementName.equals("atom:id")) {
					id = readId(reader);
				} else if (elementName.equals("link")
						|| elementName.equals("atom:link")) {
					links = readLink(reader, links);
				} else if (elementName.equals("logo")
						|| elementName.equals("atom:logo")) {
					logo = readLogo(reader);
				} else if (elementName.equals("rights")
						|| elementName.equals("atom:rights")) {
					rights = readRights(reader);
				} else if (elementName.equals("subtitle")
						|| elementName.equals("atom:subtitle")) {
					subtitle = readSubtitle(reader);
				} else if (elementName.equals("title")
						|| elementName.equals("atom:title")) {
					title = readTitle(reader);
				} else if (elementName.equals("updated")
						|| elementName.equals("atom:updated")) {
					updated = readUpdated(reader);
				} else if (elementName.equals("entry")
						|| elementName.equals("atom:entry")) {
					entries = readEntry(reader, entries);
				} else {// extension
					extensions = readExtension(reader, extensions, elementName);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				reader.next();
				break;
			// so far: neither the stax-api or geronimo stax implementations
			// can see this :(
			// case XMLStreamConstants.ATTRIBUTE:
			// case XMLStreamConstants.CDATA:
			// case XMLStreamConstants.CHARACTERS:
			// case XMLStreamConstants.COMMENT:
			// case XMLStreamConstants.DTD:
			// case XMLStreamConstants.END_DOCUMENT:
			// case XMLStreamConstants.ENTITY_DECLARATION:
			// case XMLStreamConstants.ENTITY_REFERENCE:
			// case XMLStreamConstants.NAMESPACE:
			// case XMLStreamConstants.NOTATION_DECLARATION:
			// case XMLStreamConstants.PROCESSING_INSTRUCTION:
			// case XMLStreamConstants.SPACE:
			default:
				break;
			}
		}

		Feed feed = feedDoc.buildFeed(id, title, updated, rights, authors,
				categories, contributors, links, attributes, extensions,
				generator, subtitle, icon, logo, entries);

		// because the sort extension does not enforce placement of the element
		// do a check after the feed is built to determine if it needs to be
		// sorted.
		return feedDoc
				.checkForAndApplyExtension(
						feed,
						feedDoc
								.buildAttribute("xmlns:sort",
										"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0"));
	}

	private List<Attribute> getAttributes(XMLStreamReader reader)
			throws Exception {
		List<Attribute> attributes = new LinkedList<Attribute>();

		// this is here to accommodate initially calling sub elements from the
		// FeedReader
		if (reader.getEventType() == XMLStreamConstants.START_DOCUMENT) {
			feedDoc = new FeedDoc(reader.getEncoding(), reader.getVersion());
			reader.next();
		}

		// add the namespace attributes.
		for (int i = 0; i < reader.getNamespaceCount(); i++) {
			String attrName = "xmlns";
			if (reader.getNamespacePrefix(i) != null) {
				attrName += ":" + reader.getNamespacePrefix(i);
			}

			if (reader.getNamespaceURI(i) != null) {
				attributes.add(feedDoc.buildAttribute(attrName, reader
						.getNamespaceURI(i)));
			}

		}
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			String attrName = null;
			if (reader.getAttributeName(i).getPrefix() != null
					&& !reader.getAttributeName(i).getPrefix().equals("")) {
				attrName = reader.getAttributeName(i).getPrefix() + ":"
						+ reader.getAttributeName(i).getLocalPart();
			} else {
				attrName = reader.getAttributeName(i).getLocalPart();
			}

			attributes.add(feedDoc.buildAttribute(attrName, reader
					.getAttributeValue(i)));
		}

		// return null if no attributes were created.
		return (attributes.size() == 0) ? null : attributes;
	}

	List<Extension> readExtension(XMLStreamReader reader,
			List<Extension> extensions, String elementName) throws Exception {
		if (extensions == null) {
			extensions = new LinkedList<Extension>();
		}

		StringBuilder extText = new StringBuilder();
		List<Attribute> attributes = getAttributes(reader);

		String elementNameOrig = elementName;
		boolean breakOut = false;
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				if (!elementName.equals(elementNameOrig)) {
					extText.append(readEncodedHTML(reader, elementName));
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				String elementNameEnd = getElementName(reader);
				if (elementNameEnd.equals(elementNameOrig)) {
					breakOut = true;
				}
				break;

			default:
				extText = extText.append(reader.getText());
			}
			if (breakOut) {
				break;
			}
		}
		extensions.add(feedDoc.buildExtension(elementName, attributes, extText
				.toString()));
		return extensions;
	}

	SortedMap<String, Entry> readEntry(XMLStreamReader reader,
			SortedMap<String, Entry> entries) throws Exception {
		if (feedDoc == null) {
			feedDoc = new FeedDoc();
		}

		if (entries == null) {
			entries = new TreeMap<String, Entry>();
		}

		Id id = null;
		Title title = null;
		Updated updated = null;
		Rights rights = null;
		Content content = null;
		List<Attribute> attributes = getAttributes(reader);
		List<Author> authors = null;
		List<Category> categories = null;
		List<Contributor> contributors = null;
		List<Link> links = null;
		List<Extension> extensions = null;
		Published published = null;
		Summary summary = null;
		Source source = null;
		String elementName = null;

		while (reader.hasNext()) {
			boolean breakOut = false;
			switch (reader.next()) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals("id") || elementName.equals("atom:id")) {
					id = readId(reader);
				} else if (elementName.equals("author")
						|| elementName.equals("atom:author")) {
					authors = readAuthor(reader, authors);
				} else if (elementName.equals("category")
						|| elementName.equals("atom:category")) {
					categories = readCategory(reader, categories);
				} else if (elementName.equals("contributor")
						|| elementName.equals("atom:contributor")) {
					contributors = readContributor(reader, contributors);
				} else if (elementName.equals("content")
						|| elementName.equals("atom:content")) {
					content = readContent(reader);
				} else if (elementName.equals("link")
						|| elementName.equals("atom:link")) {
					links = readLink(reader, links);
				} else if (elementName.equals("published")
						|| elementName.equals("atom:published")) {
					published = readPublished(reader);
				} else if (elementName.equals("rights")
						|| elementName.equals("atom:rights")) {
					rights = readRights(reader);
				} else if (elementName.equals("source")
						|| elementName.equals("atom:source")) {
					source = readSource(reader);
				} else if (elementName.equals("summary")
						|| elementName.equals("atom:summary")) {
					summary = readSummary(reader);
				} else if (elementName.equals("title")
						|| elementName.equals("atom:title")) {
					title = readTitle(reader);
				} else if (elementName.equals("updated")
						|| elementName.equals("atom:updated")) {
					updated = readUpdated(reader);
					// this may be called with a <feed> wrapping entries so we
					// need to skip all top level feed elements until we get to
					// the entry.
				} else if (elementName.equals("feed")
						|| elementName.equals("atom:feed")
						|| elementName.equals("generator")
						|| elementName.equals("atom:generator")
						|| elementName.equals("subtitle")
						|| elementName.equals("atom:subtitle")
						|| elementName.equals("icon")
						|| elementName.equals("atom:icon")
						|| elementName.equals("logo")
						|| elementName.equals("atom:logo")) {
					break;
				} else {// extension
					extensions = readExtension(reader, extensions, elementName);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals("entry")
						|| elementName.equals("atom:entry")) {
					breakOut = true;
				} else {
					reader.next();
				}
				break;
			}
			if (breakOut) {
				break;
			}
		}

		// throw an error if the updated entry is not available here.
		if (updated == null) {
			throw new AtomSpecException(
					"atom:entry elements MUST contain exactly one atom:updated element.");
		}
		entries.put(updated.getText(), feedDoc.buildEntry(id, title, updated,
				rights, content, authors, categories, contributors, links,
				attributes, extensions, published, summary, source));

		return entries;
	}

	// set the current namespace.
	private String namespaceURI = "http://www.w3.org/2005/Atom";

	private String getElementName(XMLStreamReader reader) {
		String elementName = null;
		String prefix = reader.getPrefix();
		if (prefix != null && !prefix.equals("")) {
			elementName = prefix + ":" + reader.getLocalName();
			if (elementName.equals(prefix + ":")) {
				return "";
			}
		} else {
			elementName = reader.getLocalName();
		}
		// set the current namespace prefix:
		namespaceURI = (reader.getNamespaceURI() == null) ? "http://www.w3.org/2005/Atom"
				: reader.getNamespaceURI();
		return elementName;
	}

	Summary readSummary(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String summary = null;
		if (containsXHTML(attributes)) {
			summary = readXHTML(reader, "summary");
		} else {
			summary = reader.getElementText();
		}
		return feedDoc.buildSummary(summary, attributes);
	}

	// used for xhtml.
	private boolean containsXHTML(List<Attribute> attributes) {
		Attribute xhtml = feedDoc.getAttributeFromGroup(attributes, "type");
		return ((xhtml != null) && (xhtml.getValue().equals("xhtml") || xhtml
				.getValue().equals("html")));
	}

	Source readSource(XMLStreamReader reader) throws Exception {
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
		String elementName = null;

		attributes = getAttributes(reader);

		while (reader.hasNext()) {
			switch (reader.next()) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				// call each feed elements read method depending on the name
				if (elementName.equals("author")
						|| elementName.equals("atom:author")) {
					authors = readAuthor(reader, authors);
				} else if (elementName.equals("category")
						|| elementName.equals("atom:category")) {
					categories = readCategory(reader, categories);
				} else if (elementName.equals("contributor")
						|| elementName.equals("atom:contributor")) {
					contributors = readContributor(reader, contributors);
				} else if (elementName.equals("generator")
						|| elementName.equals("atom:generator")) {
					generator = readGenerator(reader);
				} else if (elementName.equals("icon")
						|| elementName.equals("atom:icon")) {
					icon = readIcon(reader);
				} else if (elementName.equals("id")
						|| elementName.equals("atom:id")) {
					id = readId(reader);
				} else if (elementName.equals("link")
						|| elementName.equals("atom:link")) {
					links = readLink(reader, links);
				} else if (elementName.equals("logo")
						|| elementName.equals("atom:logo")) {
					logo = readLogo(reader);
				} else if (elementName.equals("rights")
						|| elementName.equals("atom:rights")) {
					rights = readRights(reader);
				} else if (elementName.equals("subtitle")
						|| elementName.equals("atom:subtitle")) {
					subtitle = readSubtitle(reader);
				} else if (elementName.equals("title")
						|| elementName.equals("atom:title")) {
					title = readTitle(reader);
				} else if (elementName.equals("updated")
						|| elementName.equals("atom:updated")) {
					updated = readUpdated(reader);
				} else {// extension
					extensions = readExtension(reader, extensions, elementName);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals("source")
						|| elementName.equals("atom:source")) {
					breakOut = true;
				} else {
					reader.next();
				}
				break;
			}
			if (breakOut) {
				break;
			}
		}
		return feedDoc.buildSource(id, title, updated, rights, authors,
				categories, contributors, links, attributes, extensions,
				generator, subtitle, icon, logo);
	}

	Published readPublished(XMLStreamReader reader) throws Exception {
		return feedDoc.buildPublished(getAttributes(reader), reader
				.getElementText());
	}

	Content readContent(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to skip the contents of the div.
		String content = null;
		if (containsXHTML(attributes)) {
			content = readXHTML(reader, "content");
		} else {
			content = reader.getElementText();
		}
		return feedDoc.buildContent(content, attributes);
	}

	Updated readUpdated(XMLStreamReader reader) throws Exception {
		return feedDoc.buildUpdated(getAttributes(reader), reader
				.getElementText());
	}

	Title readTitle(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String title = null;
		if (containsXHTML(attributes)) {
			title = readXHTML(reader, "title");
		} else {
			title = reader.getElementText();
		}
		return feedDoc.buildTitle(title, attributes);
	}

	private String readEncodedHTML(XMLStreamReader reader, String parentElement)
			throws XMLStreamException, Exception {
		StringBuilder xhtml = new StringBuilder();
		String elementName = null;
		boolean breakOut = false;
		while (reader.hasNext()) {
			switch (reader.next()) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				xhtml.append("<" + elementName);
				List<Attribute> attributes = getAttributes(reader);
				// add the attributes
				if (attributes != null && attributes.size() > 0) {
					for (Attribute attr : attributes) {
						xhtml.append(" " + attr.getName() + "=\""
								+ attr.getValue() + "\"");
					}
				}
				xhtml.append(">");
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals(parentElement)) {
					breakOut = true;
				} else {
					xhtml.append("</" + elementName + ">");
				}
				break;

			// so far: neither the stax-api or geronimo stax implementations
			// can see this :(
			// case XMLStreamConstants.CDATA:
			// xhtml.append("<![CDATA[" + reader.getText() + "]]>");
			// break;

			default:
				// escape the necessary characters.
				String escapedTxt = reader.getText().replaceAll("&", "&amp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				xhtml.append(escapedTxt);
			}
			if (breakOut) {
				break;
			}
		}
		return xhtml.toString();
	}

	private String readXHTML(XMLStreamReader reader, String parentElement)
			throws Exception {
		StringBuffer xhtml = new StringBuffer();
		String elementName = null;

		while (reader.hasNext()) {
			boolean breakOut = false;
			int next = reader.next();

			switch (next) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				xhtml.append("<" + elementName);
				List<Attribute> attributes = getAttributes(reader);
				// add the attributes
				if (attributes != null && attributes.size() > 0) {
					for (Attribute attr : attributes) {
						xhtml.append(" " + attr.getName() + "=\""
								+ attr.getValue() + "\"");
					}
				}
				xhtml.append(">");
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals(parentElement)
						&& namespaceURI.equals("http://www.w3.org/2005/Atom")) {
					breakOut = true;
				} else {
					xhtml.append("</" + elementName + ">");
				}
				break;

			// so far no parsers seem to be able to detect CDATA :(. Maybe it's
			// not necessary?
			// case XMLStreamConstants.CDATA:
			// xhtml.append("<![CDATA[" + reader.getText() + "]]>");
			// break;

			default:
				// escape the necessary characters.
				String escapedTxt = reader.getText().replaceAll("&", "&amp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				xhtml.append(escapedTxt);
			}
			if (breakOut) {
				break;
			}
		}
		return xhtml.toString();
	}

	Subtitle readSubtitle(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String subtitle = null;
		if (containsXHTML(attributes)) {
			subtitle = readXHTML(reader, "subtitle");
		} else {
			subtitle = reader.getElementText();
		}
		return feedDoc.buildSubtitle(subtitle, attributes);
	}

	Rights readRights(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String rights = null;
		if (containsXHTML(attributes)) {
			rights = readXHTML(reader, "rights");
		} else {
			rights = reader.getElementText();
		}
		return feedDoc.buildRights(rights, attributes);
	}

	Logo readLogo(XMLStreamReader reader) throws Exception {
		return feedDoc
				.buildLogo(getAttributes(reader), reader.getElementText());
	}

	List<Link> readLink(XMLStreamReader reader, List<Link> links)
			throws Exception {
		if (links == null) {
			links = new LinkedList<Link>();
		}
		links.add(feedDoc.buildLink(getAttributes(reader), reader
				.getElementText()));
		return links;
	}

	Id readId(XMLStreamReader reader) throws Exception {
		return feedDoc.buildId(getAttributes(reader), reader.getElementText());
	}

	Icon readIcon(XMLStreamReader reader) throws Exception {
		return feedDoc
				.buildIcon(getAttributes(reader), reader.getElementText());
	}

	Generator readGenerator(XMLStreamReader reader) throws Exception {
		return feedDoc.buildGenerator(getAttributes(reader), reader
				.getElementText());
	}

	List<Contributor> readContributor(XMLStreamReader reader,
			List<Contributor> contributors) throws Exception {

		if (contributors == null) {
			contributors = new LinkedList<Contributor>();
		}

		AtomPersonConstruct person = readAtomPersonConstruct(reader,
				"contributor");
		contributors.add(feedDoc.buildContributor(person.getName(), person
				.getUri(), person.getEmail(), person.getAttributes(), person
				.getExtensions()));

		return contributors;
	}

	List<Category> readCategory(XMLStreamReader reader,
			List<Category> categories) throws Exception {
		if (categories == null) {
			categories = new LinkedList<Category>();
		}
		categories.add(feedDoc.buildCategory(getAttributes(reader), reader
				.getElementText()));
		return categories;
	}

	List<Author> readAuthor(XMLStreamReader reader, List<Author> authors)
			throws Exception {
		if (authors == null) {
			authors = new LinkedList<Author>();
		}
		AtomPersonConstruct person = readAtomPersonConstruct(reader, "author");
		authors.add(feedDoc.buildAuthor(person.getName(), person.getUri(),
				person.getEmail(), person.getAttributes(), person
						.getExtensions()));
		return authors;
	}

	AtomPersonConstruct readAtomPersonConstruct(XMLStreamReader reader,
			String personType) throws Exception {
		boolean breakOut = false;
		final List<Attribute> attributes = getAttributes(reader);
		Name name = null;
		URI uri = null;
		Email email = null;
		List<Extension> extensions = null;
		String elementName = null;

		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals("name")
						|| elementName.equals("atom:name")) {
					name = feedDoc.buildName(reader.getElementText());
				} else if (elementName.equals("uri")
						|| elementName.equals("atom:uri")) {
					uri = feedDoc.buildURI(reader.getElementText());
				} else if (elementName.equals("email")
						|| elementName.equals("atom:email")) {
					email = feedDoc.buildEmail(reader.getElementText());
				} else {
					if (extensions == null) {
						extensions = new LinkedList<Extension>();
					}
					extensions = readExtension(reader, extensions, elementName);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals(personType)
						|| elementName.equals("atom:" + personType)) {
					breakOut = true;
				} else {
					reader.next();
				}
				break;
			}
			if (breakOut) {
				break;
			}

		}
		return feedDoc.buildAtomPersonConstruct(name, uri, email, attributes,
				extensions);
	}
}