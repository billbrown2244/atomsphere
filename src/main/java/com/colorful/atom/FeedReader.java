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
 *  2006-11-14 wbrown - added javadoc documentation.
 *  2008-03-11 wbrown - fix bug for atomXHTMLTextConstruct to wrap contents in xhtml:div element.
 *  2008-04-17 wbrown - add check for start document in readEntry
 */
package com.colorful.atom;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TimeZone;
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
class FeedReader {

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
		SortedMap<String, Entry> entries = null;
		String elementName = null;

		while (reader.hasNext()) {
			switch (reader.next()) {

			case XMLStreamConstants.START_DOCUMENT:
				FeedDoc.encoding = reader.getEncoding();
				FeedDoc.xml_version = reader.getVersion();
				break;

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				// call each feed elements read method depending on the name
				if (elementName.equals("feed")
						|| elementName.equals("atom:feed")) {
					attributes = getAttributes(reader);
				} else if (elementName.equals("author")
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
				throw new Exception("unknown event in the xml file = "
						+ reader.getEventType());
			}
		}

		Feed feed = FeedDoc.buildFeed(id, title, updated, rights, authors,
				categories, contributors, links, attributes, extensions,
				generator, subtitle, icon, logo, entries);

		// because the sort extension does not enforce placement of the element
		// do a check after the feed is built to determine if it needs to be
		// sorted.
		return FeedDoc.checkForAndApplyExtension(feed, FeedDoc.sort);
	}

	List<Attribute> getAttributes(XMLStreamReader reader) throws Exception {

		List<Attribute> attributes = new LinkedList<Attribute>();

		// skip the start document section if we are passed a document fragment.
		if (reader.getEventType() == XMLStreamConstants.START_DOCUMENT) {
			reader.next();
		}

		int eventSkip = 0;
		for (int i = 0; i < reader.getNamespaceCount(); i++) {
			eventSkip++;
			String attrName = "xmlns";
			if (reader.getNamespacePrefix(i) != null) {
				attrName += ":" + reader.getNamespacePrefix(i);
			}

			attributes.add(FeedDoc.buildAttribute(attrName, reader
					.getNamespaceURI(i)));
		}
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			eventSkip++;
			String attrName = null;
			if (reader.getAttributeName(i).getPrefix() != null
					&& !reader.getAttributeName(i).getPrefix().equals("")) {
				attrName = reader.getAttributeName(i).getPrefix() + ":"
						+ reader.getAttributeName(i).getLocalPart();
			} else {
				attrName = reader.getAttributeName(i).getLocalPart();
			}

			attributes.add(FeedDoc.buildAttribute(attrName, reader
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

		StringBuffer extText = new StringBuffer();
		List<Attribute> attributes = getAttributes(reader);

		while (reader.hasNext()) {
			boolean breakOut = false;
			switch (reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				extensions = readExtension(reader, null, elementName);
				break;

			case XMLStreamConstants.END_ELEMENT:
				String elementNameEnd = getElementName(reader);
				if (elementNameEnd.equals(elementName)) {
					breakOut = true;
				} else {
					reader.next();
				}
				break;
			default:
				extText = extText.append(reader.getText());
			}
			if (breakOut) {
				break;
			}
		}
		extensions.add(FeedDoc.buildExtension(elementName, attributes, extText
				.toString()));
		return extensions;
	}

	SortedMap<String, Entry> readEntry(XMLStreamReader reader,
			SortedMap<String, Entry> entries) throws Exception {

		if (entries == null) {
			entries = new TreeMap<String, Entry>();
		}

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
		String elementName = null;

		attributes = getAttributes(reader);

		while (reader.hasNext()) {
			boolean breakOut = false;
			switch (reader.next()) {

			case XMLStreamConstants.START_DOCUMENT:
				FeedDoc.encoding = reader.getEncoding();
				FeedDoc.xml_version = reader.getVersion();
				break;

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				// call each feed elements read method depending on the name
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
		entries.put(updated.getText(), FeedDoc.buildEntry(id, title, updated,
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
		return FeedDoc.buildSummary(summary, attributes);
	}

	//used for html or xhtml.
	boolean containsXHTML(List<Attribute> attributes) {
		Attribute xhtml = FeedDoc.getAttributeFromGroup(attributes, "type");
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
		return FeedDoc.buildSource(id, title, updated, rights, authors,
				categories, contributors, links, attributes, extensions,
				generator, subtitle, icon, logo);
	}

	SimpleDateFormat getSimpleDateFormat() {
		String timeZoneOffset = null;
		TimeZone timeZone = TimeZone.getDefault();
		int hours = (((timeZone.getRawOffset() / 1000) / 60) / 60);
		if (hours >= 0) {
			timeZoneOffset = TimeZone.getTimeZone("GMT" + "+" + hours).getID()
					.substring(3);
		} else {
			timeZoneOffset = TimeZone
					.getTimeZone("GMT" + "-" + Math.abs(hours)).getID()
					.substring(3);
		}
		return new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SS\'"
				+ timeZoneOffset + "\'");
	}

	Published readPublished(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		String dateText = reader.getElementText();
		try {
			return FeedDoc.buildPublished(
					getSimpleDateFormat().parse(dateText), attributes);
		} catch (Exception e) {
			SimpleDateFormat simpleDateFmt2 = new SimpleDateFormat(
					getSimpleDateFormat().toPattern().substring(0, 19));
			return FeedDoc.buildPublished(simpleDateFmt2.parse(dateText
					.substring(0, 19)), attributes);
		}
	}

	Content readContent(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to skip the contents of the div.
		String content = null;
		if (containsXHTML(attributes)) {
			content = readXHTML(reader, "content");
		} else {
			if (reader.hasNext()) {
				int next = reader.next();
				System.out.println("next = " + next);
				if (next == XMLStreamConstants.START_ELEMENT) {
					content = reader.getElementText();
				} else if (next == XMLStreamConstants.END_ELEMENT) {
					reader.next();
				}
			}

		}
		return FeedDoc.buildContent(content, attributes);
	}

	Updated readUpdated(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		String dateText = reader.getElementText();
		try {
			return FeedDoc.buildUpdated(getSimpleDateFormat().parse(dateText),
					attributes);
		} catch (Exception e) {
			SimpleDateFormat simpleDateFmt2 = new SimpleDateFormat(
					getSimpleDateFormat().toPattern().substring(0, 19));
			return FeedDoc.buildUpdated(simpleDateFmt2.parse(dateText
					.substring(0, 19)), attributes);
		}
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
		System.out.println("title before build = " + title);
		return FeedDoc.buildTitle(title, attributes);
	}

	String readXHTML(XMLStreamReader reader, String parentElement)
			throws XMLStreamException, Exception {
		StringBuffer xhtml = new StringBuffer();
		String elementName = null;

		while (reader.hasNext()) {
			boolean breakOut = false;
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
				if (elementName.equals(parentElement)
						&& namespaceURI.equals("http://www.w3.org/2005/Atom")) {
					breakOut = true;
				} else {
					xhtml.append("</" + elementName + ">");
				}
				break;

			case XMLStreamConstants.CDATA:
				xhtml.append("<![CDATA[" + reader.getText() + "]]>");
				break;

			default:
				// escape the necessary characters.
				xhtml.append(reader.getText().replaceAll("&", "&amp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
			}
			if (breakOut) {
				// clear past the end enclosing div.
				// reader.next();
				break;
			}
		}
		return xhtml.toString().replaceAll("<br></br>", "<br />").replaceAll(
				"<hr></hr>", "<hr />");
	}

	/*
	 * // escape the tag names and & here. String readHTML(XMLStreamReader
	 * reader, String parentElement) throws XMLStreamException, Exception {
	 * StringBuffer xhtml = new StringBuffer(); String elementName = null;
	 * 
	 * while (reader.hasNext()) { boolean breakOut = false; switch
	 * (reader.next()) {
	 * 
	 * case XMLStreamConstants.START_ELEMENT:
	 * System.out.println("in start element."); elementName =
	 * getElementName(reader); xhtml.append("&lt;" + elementName);
	 * List<Attribute> attributes = getAttributes(reader); // add the attributes
	 * if (attributes != null && attributes.size() > 0) { for (Attribute attr :
	 * attributes) { xhtml.append(" " + attr.getName() + "=\"" + attr.getValue()
	 * + "\""); } } xhtml.append("&lt;"); break;
	 * 
	 * case XMLStreamConstants.END_ELEMENT:
	 * System.out.println("in end element."); elementName =
	 * getElementName(reader); if (elementName.equals(parentElement) &&
	 * namespaceURI.equals("http://www.w3.org/2005/Atom")) { breakOut = true; }
	 * else { xhtml.append("&gt;/" + elementName + "&gt;"); } break;
	 * 
	 * case XMLStreamConstants.CDATA: xhtml.append("<![CDATA[" +
	 * reader.getText() + "]]>"); break;
	 * 
	 * default: System.out.println("in regular section");
	 * xhtml.append(reader.getText()); } if (breakOut) { // clear past the end
	 * enclosing div. // reader.next(); break; } }
	 * System.out.println("before escaped in reader = " + xhtml.toString());
	 * System.out.println("escaped in reader = " +
	 * xhtml.toString().replaceAll("&", "&amp;").replaceAll("<",
	 * "&lt;").replaceAll(">", "&gt;")); return xhtml.toString().replaceAll("&",
	 * "&amp;") .replaceAll("<", "&lt;").replaceAll(">", "&gt;"); }
	 */

	Subtitle readSubtitle(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String subtitle = null;
		if (containsXHTML(attributes)) {
			subtitle = readXHTML(reader, "subtitle");
		} else {
			subtitle = reader.getElementText();
		}
		return FeedDoc.buildSubtitle(subtitle, attributes);
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
		return FeedDoc.buildRights(rights, attributes);
	}

	Logo readLogo(XMLStreamReader reader) throws Exception {
		return FeedDoc
				.buildLogo(getAttributes(reader), reader.getElementText());
	}

	List<Link> readLink(XMLStreamReader reader, List<Link> links)
			throws Exception {
		if (links == null) {
			links = new LinkedList<Link>();
		}
		links.add(FeedDoc.buildLink(getAttributes(reader), reader
				.getElementText()));
		return links;
	}

	Id readId(XMLStreamReader reader) throws Exception {
		return FeedDoc.buildId(getAttributes(reader), reader.getElementText());
	}

	Icon readIcon(XMLStreamReader reader) throws Exception {
		return FeedDoc
				.buildIcon(getAttributes(reader), reader.getElementText());
	}

	Generator readGenerator(XMLStreamReader reader) throws Exception {
		return FeedDoc.buildGenerator(getAttributes(reader), reader
				.getElementText());
	}

	List<Contributor> readContributor(XMLStreamReader reader,
			List<Contributor> contributors) throws Exception {

		if (contributors == null) {
			contributors = new LinkedList<Contributor>();
		}

		AtomPersonConstruct person = readAtomPersonConstruct(reader,
				"contributor");
		contributors.add(FeedDoc.buildContributor(person.getName(), person
				.getUri(), person.getEmail(), person.getAttributes(), person
				.getExtensions()));

		return contributors;
	}

	List<Category> readCategory(XMLStreamReader reader,
			List<Category> categories) throws Exception {
		if (categories == null) {
			categories = new LinkedList<Category>();
		}
		categories.add(FeedDoc.buildCategory(getAttributes(reader), reader
				.getElementText()));
		return categories;
	}

	List<Author> readAuthor(XMLStreamReader reader, List<Author> authors)
			throws Exception {
		if (authors == null) {
			authors = new LinkedList<Author>();
		}
		AtomPersonConstruct person = readAtomPersonConstruct(reader, "author");
		authors.add(FeedDoc.buildAuthor(person.getName(), person.getUri(),
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
					name = FeedDoc.buildName(reader.getElementText());
				} else if (elementName.equals("uri")
						|| elementName.equals("atom:uri")) {
					uri = FeedDoc.buildURI(reader.getElementText());
				} else if (elementName.equals("email")
						|| elementName.equals("atom:email")) {
					email = FeedDoc.buildEmail(reader.getElementText());
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
		return FeedDoc.buildAtomPersonConstruct(name, uri, email, attributes,
				extensions);
	}
}