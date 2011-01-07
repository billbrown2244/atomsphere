/**
 * Copyright 2011 William R. Brown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import javax.xml.stream.XMLStreamConstants;
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

	public FeedReader(FeedDoc feedDoc) {
		this.feedDoc = feedDoc;
	}

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
		List<Entry> entries = null;
		String elementName = null;

		while (reader.hasNext()) {
			switch (reader.next()) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);

				if (elementName.equals("author")) {
					authors = readAuthor(reader, authors);
				} else if (elementName.equals("category")) {
					categories = readCategory(reader, categories);
				} else if (elementName.equals("contributor")) {
					contributors = readContributor(reader, contributors);
				} else if (elementName.equals("generator")) {
					generator = readGenerator(reader);
				} else if (elementName.equals("icon")) {
					icon = readIcon(reader);
				} else if (elementName.equals("id")) {
					id = readId(reader);
				} else if (elementName.equals("link")) {
					links = readLink(reader, links);
				} else if (elementName.equals("logo")) {
					logo = readLogo(reader);
				} else if (elementName.equals("rights")) {
					rights = readRights(reader);
				} else if (elementName.equals("subtitle")) {
					subtitle = readSubtitle(reader);
				} else if (elementName.equals("title")) {
					title = readTitle(reader);
				} else if (elementName.equals("updated")) {
					updated = readUpdated(reader);
				} else if (elementName.equals("entry")) {
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

		// because the sort extension does not enforce placement of the element
		// do a check after the feed is built to determine if it needs to be
		// sorted.
		return feedDoc
				.buildFeed(feedDoc.checkForAndApplyExtension(
						feedDoc.buildFeed(id, title, updated, rights, authors,
								categories, contributors, links, attributes,
								extensions, generator, subtitle, icon, logo,
								entries),
						feedDoc.buildAttribute("xmlns:sort",
								"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0")));
	}

	private List<Attribute> getAttributes(XMLStreamReader reader)
			throws Exception {
		List<Attribute> attributes = new LinkedList<Attribute>();

		// this is here to accommodate initially calling sub elements from the
		// FeedReader
		if (reader.getEventType() == XMLStreamConstants.START_DOCUMENT) {
			feedDoc.setEncoding(reader.getEncoding());
			feedDoc.setXmlVersion(reader.getVersion());
			reader.next();
		}

		// make sure all the attribute values are properly xml encoded/escaped
		// with value.replaceAll("&amp;","&").replaceAll("&", "&amp;")

		// add the processing instructions for now.
		List<FeedDoc.ProcessingInstruction> processingInstructions = null;
		while (reader.getEventType() != XMLStreamConstants.START_ELEMENT
				&& reader.getEventType() != XMLStreamConstants.END_ELEMENT
				&& reader.getEventType() != XMLStreamConstants.NAMESPACE) {
			if (reader.getEventType() == XMLStreamConstants.PROCESSING_INSTRUCTION) {
				if (processingInstructions == null) {
					processingInstructions = new LinkedList<FeedDoc.ProcessingInstruction>();
				}
				processingInstructions
						.add(new FeedDoc().new ProcessingInstruction(reader
								.getPITarget(), reader.getPIData()));
			}
			reader.next();
		}

		if (processingInstructions != null) {
			feedDoc.setProcessingInstructions(processingInstructions);
		}

		// add the namespace attributes.
		for (int i = 0; i < reader.getNamespaceCount(); i++) {
			String attrName = "xmlns";
			if (reader.getNamespacePrefix(i) != null) {
				attrName += ":" + reader.getNamespacePrefix(i);
			}

			if (reader.getNamespaceURI(i) != null) {
				attributes.add(feedDoc.buildAttribute(attrName, reader
						.getNamespaceURI(i).replaceAll("&amp;", "&")
						.replaceAll("&", "&amp;")));
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

			attributes.add(feedDoc.buildAttribute(attrName,
					reader.getAttributeValue(i).replaceAll("&amp;", "&")
							.replaceAll("&", "&amp;")));
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

		// if this is a top level extension and it is has type of xhtml then
		// treat it as such.
		if (containsXHTML(reader, elementName)) {
			extText.append(readXHTML(reader, elementName, true));

		} else {

			boolean breakOut = false;
			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamConstants.START_ELEMENT:
					String elementNameStart = getElementName(reader);
					if (!elementNameStart.equals(elementName)) {
						extText.append(readSubExtension(reader,
								elementNameStart, attributes));
					}
					break;

				case XMLStreamConstants.END_ELEMENT:
					String elementNameEnd = getElementName(reader);
					if (elementNameEnd.equals(elementName)) {
						breakOut = true;
					}
					break;

				default:
					extText.append(reader.getText());
					break;
				}
				if (breakOut) {
					break;
				}
			}
		}

		extensions.add(feedDoc.buildExtension(elementName, attributes,
				extText.toString()));
		return extensions;
	}

	private String readSubExtension(XMLStreamReader reader, String elementName,
			List<Attribute> parentAttributes) throws Exception {

		StringBuffer xhtml = new StringBuffer("<" + elementName);

		List<Attribute> attributes = getAttributes(reader);
		// add the attributes
		if (attributes != null && attributes.size() > 0) {
			for (Attribute attr : attributes) {
				xhtml.append(" " + attr.getName() + "=\"" + attr.getValue()
						+ "\"");
			}
		}
		boolean openElementClosed = false;
		String elementNameStart = elementName;

		while (reader.hasNext()) {
			boolean breakOut = false;
			int next = reader.next();

			switch (next) {

			case XMLStreamConstants.START_ELEMENT:
				elementNameStart = getElementName(reader);
				if (!elementNameStart.equals(elementName)) {
					xhtml.append(readSubExtension(reader, elementNameStart,
							attributes));
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				String elementNameEnd = getElementName(reader);
				if (elementNameEnd.equals(elementName)) {
					breakOut = true;
				}

				if (openElementClosed) {
					xhtml.append("</" + elementName + ">");
				} else {
					xhtml.append(" />");
				}

				break;

			// so far no parsers seem to be able to detect CDATA :(. Maybe a
			// not necessary?
			// case XMLStreamConstants.CDATA:
			// xhtml.append("<![CDATA[" + reader.getText() + "]]>");
			// break;

			default:
				// close the open element if we get here
				if (elementNameStart.equals(elementName)) {
					xhtml.append(" >");
					openElementClosed = true;
				}
				xhtml.append(reader.getText());
			}
			if (breakOut) {
				break;
			}
		}
		return xhtml.toString();
	}

	private String readXHTML(XMLStreamReader reader, String parentElement,
			boolean escapeHTML) throws Exception {
		String parentNamespaceURI = namespaceURI;
		StringBuffer xhtml = new StringBuffer();
		String elementName = null;
		boolean justReadStart = false;

		while (reader.hasNext()) {
			boolean breakOut = false;

			switch (reader.next()) {

			case XMLStreamConstants.START_ELEMENT:
				elementName = getElementName(reader);
				// if we read 2 start elements in a row, we need to close the
				// first start element.
				if (justReadStart) {
					xhtml.append(">");
				}

				xhtml.append("<" + elementName);

				List<Attribute> attributes = getAttributes(reader);
				// add the attributes
				if (attributes != null && attributes.size() > 0) {
					for (Attribute attr : attributes) {
						String attrVal = attr.getValue();
						xhtml.append(" " + attr.getName() + "=\"" + attrVal
								+ "\"");
					}
				}
				justReadStart = true;

				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if ((elementName.equals(parentElement) && !namespaceURI
						.equals("http://www.w3.org/1999/xhtml"))
						|| (elementName.equals(parentElement) && parentNamespaceURI
								.equals("http://www.w3.org/1999/xhtml"))) {
					breakOut = true;
				} else {
					if (justReadStart) {
						xhtml.append(" />");
					} else {
						xhtml.append("</" + elementName + ">");
					}
					justReadStart = false;
				}
				break;

			// so far no parsers seem to be able to detect CDATA :(. Maybe a
			// not necessary?
			// case XMLStreamConstants.CDATA:
			// xhtml.append("<![CDATA[" + reader.getText() + "]]>");
			// break;

			default:
				if (justReadStart) {
					xhtml.append(">");
					justReadStart = false;
				}
				// if this is html, escape the markup.
				if (escapeHTML) {
					String text = reader.getText();
					// if the feed we are reading has invalid escaping the text
					// will be null which results in a skipping of the malformed
					// character.
					if (text != null) {
						xhtml.append(text.replaceAll("&", "&amp;")
								.replaceAll("<", "&lt;")
								.replaceAll(">", "&gt;"));
					}
				} else {
					String text = reader.getText();
					// escape the sole '&lt;' and '&amp;' sole characters.
					xhtml.append(text.replaceAll("&", "&amp;").replaceAll("<",
							"&lt;"));
				}
			}
			if (breakOut) {
				break;
			}
		}
		return xhtml.toString();
	}

	List<Entry> readEntry(XMLStreamReader reader, List<Entry> entries)
			throws Exception {

		if (entries == null) {
			entries = new LinkedList<Entry>();
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
				if (elementName.equals("id")) {
					id = readId(reader);
				} else if (elementName.equals("author")) {
					authors = readAuthor(reader, authors);
				} else if (elementName.equals("category")) {
					categories = readCategory(reader, categories);
				} else if (elementName.equals("contributor")) {
					contributors = readContributor(reader, contributors);
				} else if (elementName.equals("content")) {
					content = readContent(reader);
				} else if (elementName.equals("link")) {
					links = readLink(reader, links);
				} else if (elementName.equals("published")) {
					published = readPublished(reader);
				} else if (elementName.equals("rights")) {
					rights = readRights(reader);
				} else if (elementName.equals("source")) {
					source = readSource(reader);
				} else if (elementName.equals("summary")) {
					summary = readSummary(reader);
				} else if (elementName.equals("title")) {
					title = readTitle(reader);
				} else if (elementName.equals("updated")) {
					updated = readUpdated(reader);
				} else {// extension
					extensions = readExtension(reader, extensions, elementName);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals("entry")) {
					breakOut = true;
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
		entries.add(feedDoc.buildEntry(id, title, updated, rights, content,
				authors, categories, contributors, links, attributes,
				extensions, published, summary, source));

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
			summary = readXHTML(reader, "summary", false);
		} else if (containsHTML(attributes)) {
			summary = readXHTML(reader, "summary", true);
		} else {
			summary = reader.getElementText();
		}
		return feedDoc.buildSummary(summary, attributes);
	}

	// used to check if the extension prefix matches the xhtml namespace
	private boolean containsXHTML(XMLStreamReader reader, String elementName) {
		if (reader.getNamespaceURI().equals("http://www.w3.org/1999/xhtml")) {
			return true;
		}
		if (elementName.indexOf(":") != -1) {
			String ns = reader.getNamespaceURI(elementName.substring(0,
					elementName.indexOf(":")));
			return ns != null && ns.equals("http://www.w3.org/1999/xhtml");
		}
		return false;
	}

	// used for xhtml.
	private boolean containsXHTML(List<Attribute> attributes) {
		Attribute xhtml = null;
		if (attributes != null) {
			for (Attribute attr : attributes) {
				if (attr.getName().equalsIgnoreCase("type")) {
					xhtml = attr;
				}
			}
		}
		return ((xhtml != null) && (xhtml.getValue().equals("xhtml")));
	}

	// used for html.
	private boolean containsHTML(List<Attribute> attributes) {
		Attribute html = null;
		if (attributes != null) {
			for (Attribute attr : attributes) {
				if (attr.getName().equalsIgnoreCase("type")) {
					html = attr;
				}
			}
		}
		return ((html != null) && (html.getValue().equals("html")));
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
				if (elementName.equals("author")) {
					authors = readAuthor(reader, authors);
				} else if (elementName.equals("category")) {
					categories = readCategory(reader, categories);
				} else if (elementName.equals("contributor")) {
					contributors = readContributor(reader, contributors);
				} else if (elementName.equals("generator")) {
					generator = readGenerator(reader);
				} else if (elementName.equals("icon")) {
					icon = readIcon(reader);
				} else if (elementName.equals("id")) {
					id = readId(reader);
				} else if (elementName.equals("link")) {
					links = readLink(reader, links);
				} else if (elementName.equals("logo")) {
					logo = readLogo(reader);
				} else if (elementName.equals("rights")) {
					rights = readRights(reader);
				} else if (elementName.equals("subtitle")) {
					subtitle = readSubtitle(reader);
				} else if (elementName.equals("title")) {
					title = readTitle(reader);
				} else if (elementName.equals("updated")) {
					updated = readUpdated(reader);
				} else {// extension
					extensions = readExtension(reader, extensions, elementName);
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				elementName = getElementName(reader);
				if (elementName.equals("source")) {
					breakOut = true;
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
		return feedDoc.buildPublished(getAttributes(reader),
				reader.getElementText());
	}

	Content readContent(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to skip the contents of the div.
		String content = null;
		if (containsXHTML(attributes)) {
			content = readXHTML(reader, "content", false);
		} else if (containsHTML(attributes)) {
			content = readXHTML(reader, "content", true);
		} else {
			content = reader.getElementText();
		}
		return feedDoc.buildContent(content, attributes);
	}

	Updated readUpdated(XMLStreamReader reader) throws Exception {
		return feedDoc.buildUpdated(getAttributes(reader),
				reader.getElementText());
	}

	Title readTitle(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String title = null;
		if (containsXHTML(attributes)) {
			title = readXHTML(reader, "title", false);
		} else if (containsHTML(attributes)) {
			title = readXHTML(reader, "title", true);
		} else {
			title = reader.getElementText();
		}
		return feedDoc.buildTitle(title, attributes);
	}

	Subtitle readSubtitle(XMLStreamReader reader) throws Exception {
		List<Attribute> attributes = getAttributes(reader);
		// if the content is XHTML, we need to read in the contents of the div.
		String subtitle = null;
		if (containsXHTML(attributes)) {
			subtitle = readXHTML(reader, "subtitle", false);
		} else if (containsHTML(attributes)) {
			subtitle = readXHTML(reader, "subtitle", true);
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
			rights = readXHTML(reader, "rights", false);
		} else if (containsHTML(attributes)) {
			rights = readXHTML(reader, "rights", true);
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
		links.add(feedDoc.buildLink(getAttributes(reader),
				reader.getElementText()));
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
		return feedDoc.buildGenerator(getAttributes(reader),
				reader.getElementText());
	}

	List<Contributor> readContributor(XMLStreamReader reader,
			List<Contributor> contributors) throws Exception {

		if (contributors == null) {
			contributors = new LinkedList<Contributor>();
		}

		AtomPersonConstruct person = readAtomPersonConstruct(reader,
				"contributor");
		contributors.add(feedDoc.buildContributor(person.getName(),
				person.getUri(), person.getEmail(), person.getAttributes(),
				person.getExtensions()));

		return contributors;
	}

	List<Category> readCategory(XMLStreamReader reader,
			List<Category> categories) throws Exception {
		if (categories == null) {
			categories = new LinkedList<Category>();
		}
		categories.add(feedDoc.buildCategory(getAttributes(reader),
				reader.getElementText()));
		return categories;
	}

	List<Author> readAuthor(XMLStreamReader reader, List<Author> authors)
			throws Exception {
		if (authors == null) {
			authors = new LinkedList<Author>();
		}
		AtomPersonConstruct person = readAtomPersonConstruct(reader, "author");
		authors.add(feedDoc.buildAuthor(person.getName(), person.getUri(),
				person.getEmail(), person.getAttributes(),
				person.getExtensions()));
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
				if (elementName.equals("name")) {
					name = feedDoc.buildName(reader.getElementText());
				} else if (elementName.equals("uri")) {
					uri = feedDoc.buildURI(reader.getElementText());
				} else if (elementName.equals("email")) {
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
				if (elementName.equals(personType)) {
					breakOut = true;
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