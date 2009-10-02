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
package com.colorfulsoftware.atom;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class reads and writes atom documents to and from xml files, objects or
 * Strings. It contains all of the factory methods for building immutable copies
 * of the object elements.
 * 
 * You should not instantiate this class directly, rather, use the static
 * factory methods to obtain instances of the Feed elements.
 * 
 * @author Bill Brown
 * 
 */
public final class FeedDoc {

	/**
	 * 
	 * An enumeration of the different types of supported content.
	 * 
	 */
	public enum ContentType {
		/**
		 * text content
		 */
		TEXT, /**
		 * html content
		 */
		HTML, /**
		 * xhtml content
		 */
		XHTML, /**
		 * other non text, html or xhtml content
		 */
		OTHER, /**
		 * external content outside of the feed
		 */
		EXTERNAL
	}

	/**
	 * the default XML version of "1.0"
	 */
	private String xmlVersion = "1.0";

	private String libUri;
	private String libVersion;

	/**
	 * creates a new feed document.
	 */
	public FeedDoc() {
		try {
			Properties props = new Properties();
			props.load(FeedDoc.class
					.getResourceAsStream("/atomsphere.properties"));
			libUri = props.getProperty("uri");
			libVersion = props.getProperty("version");
		} catch (Exception e) {
			// should not happen.
			e.printStackTrace();
		}
	}

	private String encoding = "UTF-8";

	/**
	 * @param encoding
	 *            the document encoding. eg: UTF-8
	 * @param xmlVersion
	 *            the document xml version eg: 1.0
	 */
	public FeedDoc(String encoding, String xmlVersion) {
		this();
		this.encoding = encoding;
		this.xmlVersion = xmlVersion;
	}

	/**
	 * @return the Atomsphere library version in the form of a generator
	 *         element. This element is output for all feeds that are generated
	 *         by Atomsphere.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Generator getAtomsphereVersion() throws AtomSpecException {
		List<Attribute> attributes = new LinkedList<Attribute>();
		attributes.add(buildAttribute("uri", libUri));
		attributes.add(buildAttribute("version", libVersion));
		return buildGenerator(attributes, "Atomsphere");
	}

	/**
	 * Comparator for sorting feed entries in ascending order.
	 */
	public final Comparator<String> SORT_ASC = new Comparator<String>() {
		public int compare(String key1, String key2) {
			return key1.compareTo(key2);

		}
	};

	/**
	 * Comparator for sorting feed entries in descending order
	 */
	public final Comparator<String> SORT_DESC = new Comparator<String>() {
		public int compare(String key1, String key2) {
			return key2.compareTo(key1);
		}
	};

	/**
	 * 
	 * @param output
	 *            the target output for the feed document.
	 * @param feed
	 *            the atom feed object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output
	 */
	public void writeFeedDoc(OutputStream output, Feed feed, String encoding,
			String version) throws Exception {
		writeFeedDoc(XMLOutputFactory.newInstance().createXMLStreamWriter(
				output, encoding), feed, encoding, version);
	}

	/**
	 * 
	 * @param output
	 *            the target output for the entry document.
	 * @param entry
	 *            the atom entry object containing the content.
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the atom document cannot be written to the output
	 */
	public void writeEntryDoc(OutputStream output, Entry entry,
			String encoding, String version) throws Exception {
		writeEntryDoc(XMLOutputFactory.newInstance().createXMLStreamWriter(
				output, encoding), entry, encoding, version);
	}

	/**
	 * 
	 * @param output
	 *            the target output for the document.
	 * @param feed
	 *            the atom feed object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output
	 */
	public void writeFeedDoc(Writer output, Feed feed, String encoding,
			String version) throws Exception {
		writeFeedDoc(XMLOutputFactory.newInstance().createXMLStreamWriter(
				output), feed, encoding, version);
	}

	/**
	 * 
	 * @param output
	 *            the target output for the entry document.
	 * @param entry
	 *            the atom entry object containing the content.
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the atom document cannot be written to the output
	 */
	public void writeEntryDoc(Writer output, Entry entry, String encoding,
			String version) throws Exception {
		writeEntryDoc(XMLOutputFactory.newInstance().createXMLStreamWriter(
				output), entry, encoding, version);
	}

	/**
	 * For example: to pass the TXW
	 * com.sun.xml.txw2.output.IndentingXMLStreamWriter or the stax-utils
	 * javanet.staxutils.IndentingXMLStreamWriter for indented printing do this:
	 * 
	 * <pre>
	 * XmlStreamWriter writer = new IndentingXMLStreamWriter(XMLOutputFactory
	 * 		.newInstance().createXMLStreamWriter(
	 * 				new FileOutputStream(outputFilePath), encoding));
	 * FeedDoc.writeFeedDoc(writer, myFeed, null, null);
	 * </pre>
	 * 
	 * @param output
	 *            the target output for the feed.
	 * @param feed
	 *            the atom feed object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output
	 */
	public void writeFeedDoc(XMLStreamWriter output, Feed feed,
			String encoding, String version) throws Exception {

		writeFeedOutput(feed, output, encoding, version);

	}

	/**
	 * Writes and entry element to a document.
	 * 
	 * @param output
	 *            the target output for the entry document.
	 * @param entry
	 *            the atom entry object containing the content of the entry
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output see
	 * 
	 *             <code>writeFeedDoc(XMLStreamWriter output,Feed feed,String encoding,String version)</code>
	 */
	public void writeEntryDoc(XMLStreamWriter output, Entry entry,
			String encoding, String version) throws Exception {
		writeEntryOutput(entry, output, encoding, version);
	}

	/**
	 * This method reads in a Feed element and returns the contents as an atom
	 * feed string with formatting specified by the fully qualified
	 * XMLStreamWriter class name (uses reflection internally). For example you
	 * can pass the TXW com.sun.xml.txw2.output.IndentingXMLStreamWriter or the
	 * stax-utils javanet.staxutils.IndentingXMLStreamWriter for indented
	 * printing. It will fall back to
	 * 
	 * <pre>
	 * readFeedToString(Feed)
	 * </pre>
	 * 
	 * if the XMLStreamWriter class cannot be found in the classpath.
	 * 
	 * @param feed
	 *            the feed to be converted to an atom document string.
	 * @param xmlStreamWriter
	 *            the fully qualified XMLStreamWriter class name.
	 * @return an atom feed document string.
	 * @throws Exception
	 *             thrown if the feed cannot be returned as a String
	 */
	public String readFeedToString(Feed feed, String xmlStreamWriter)
			throws Exception {

		StringWriter theString = new StringWriter();
		try {
			Class<?> cls = Class.forName(xmlStreamWriter);
			Constructor<?> ct = cls
					.getConstructor(new Class[] { XMLStreamWriter.class });
			Object arglist[] = new Object[] { XMLOutputFactory.newInstance()
					.createXMLStreamWriter(theString) };
			XMLStreamWriter writer = (XMLStreamWriter) ct.newInstance(arglist);

			writeFeedOutput(feed, writer, encoding, xmlVersion);

		} catch (Exception e) {
			return readFeedToString(feed);
		}
		return theString.toString();
	}

	/**
	 * This method reads in an Entry element and returns the contents as an atom
	 * feed document string
	 * 
	 * @param entry
	 *            the entry to be converted to an atom document string.
	 * @param xmlStreamWriter
	 *            the XMLStreamWriter to use
	 * @return the atom entry document string.
	 * @throws Exception
	 *             if the entry cannot be returned as a String see
	 *             <code>readFeedToString(Feed feed, String xmlStreamWriter)</code>
	 */
	public String readEntryToString(Entry entry, String xmlStreamWriter)
			throws Exception {

		StringWriter theString = new StringWriter();
		try {
			Class<?> cls = Class.forName(xmlStreamWriter);
			Constructor<?> ct = cls
					.getConstructor(new Class[] { XMLStreamWriter.class });
			Object arglist[] = new Object[] { XMLOutputFactory.newInstance()
					.createXMLStreamWriter(theString) };
			XMLStreamWriter writer = (XMLStreamWriter) ct.newInstance(arglist);

			writeEntryOutput(entry, writer, encoding, xmlVersion);

		} catch (Exception e) {
			return readEntryToString(entry);
		}
		return theString.toString();
	}

	/**
	 * This method reads in a Feed bean and returns the contents as an atom feed
	 * string.
	 * 
	 * @param feed
	 *            the feed to be converted to an atom string.
	 * @return an atom feed document string.
	 * @throws Exception
	 *             thrown if the feed cannot be returned as a String
	 */
	public String readFeedToString(Feed feed) throws Exception {

		StringWriter theString = new StringWriter();

		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = outputFactory.createXMLStreamWriter(theString);

		writeFeedOutput(feed, writer, encoding, xmlVersion);

		return theString.toString();
	}

	/**
	 * This method reads in an atom Entry element and returns the contents as an
	 * atom Entry document String containing the entry.
	 * 
	 * @param entry
	 *            the entry to be converted to an atom entry document string.
	 * @return an atom entry document string containing the entry argument
	 *         passed in.
	 * @throws Exception
	 *             thrown if the feed cannot be returned as a String
	 */
	public String readEntryToString(Entry entry) throws Exception {

		StringWriter theString = new StringWriter();

		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = outputFactory.createXMLStreamWriter(theString);

		writeEntryOutput(entry, writer, encoding, xmlVersion);

		return theString.toString();
	}

	// used for writing entry documents to their output.
	private void writeEntryOutput(Entry entry, XMLStreamWriter writer,
			String encoding, String version) throws AtomSpecException,
			Exception, XMLStreamException {
		SortedMap<String, Entry> entries = new TreeMap<String, Entry>();

		// add atom base and language to the entry if they are not there.
		List<Attribute> attributes = entry.getAttributes();
		if (attributes == null) {
			attributes = new LinkedList<Attribute>();
		}
		if (getAttributeFromGroup(attributes, getAtomBase().getName()) == null) {
			attributes.add(getAtomBase());
		}
		if (getAttributeFromGroup(attributes, getLangEn().getName()) == null) {
			attributes.add(getLangEn());
		}

		// rebuild the entry with the added attributes.
		entries
				.put(entry.getUpdated().getText(), buildEntry(entry.getId(),
						entry.getTitle(), entry.getUpdated(),
						entry.getRights(), entry.getContent(), entry
								.getAuthors(), entry.getCategories(), entry
								.getContributors(), entry.getLinks(),
						attributes, entry.getExtensions(),
						entry.getPublished(), entry.getSummary(), entry
								.getSource()));

		// write the xml header.
		writer.writeStartDocument(encoding, version);
		new FeedWriter().writeEntries(writer, entries);
		writer.flush();
		writer.close();
	}

	/**
	 * This method reads an xml string into a Feed element.
	 * 
	 * @param xmlString
	 *            the xml string to be transformed into a Feed element.
	 * @return the atom Feed element
	 * @throws Exception
	 *             if the string cannot be parsed into a Feed element.
	 */
	public Feed readFeedToBean(String xmlString) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(new java.io.StringReader(xmlString));
		return new FeedReader().readFeed(reader);
	}

	/**
	 * This method reads an xml string into a Entry element.
	 * 
	 * @param xmlString
	 *            the xml string to be transformed into a Entry element.
	 * @return the atom Entry element
	 * @throws Exception
	 *             if the string cannot be parsed into a Entry element.
	 */
	public Entry readEntryToBean(String xmlString) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(new java.io.StringReader(xmlString));
		SortedMap<String, Entry> entries = new FeedReader().readEntry(reader,
				null);
		// readEntry() only reads at most one entry.
		// so if the string contains more than one entry, only the first gets
		// returned.
		return entries.get(entries.firstKey());
	}

	/**
	 * This method reads an xml File object into a Feed element.
	 * 
	 * @param file
	 *            the file object representing an atom file.
	 * @return the atom Feed element.
	 * @throws Exception
	 *             if the file cannot be parsed into a Feed element.
	 */
	public Feed readFeedToBean(File file) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(new FileInputStream(file));
		return new FeedReader().readFeed(reader);
	}

	/**
	 * This method reads an xml File object into an Entry element.
	 * 
	 * @param file
	 *            the file object representing an atom file.
	 * @return the atom Entry element.
	 * @throws Exception
	 *             if the file cannot be parsed into an Entry element.
	 */
	public Entry readEntryToBean(File file) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(new FileInputStream(file));
		SortedMap<String, Entry> entries = new FeedReader().readEntry(reader,
				null);
		// readEntry() only reads at most one entry.
		// so if the string contains more than one entry, only the first gets
		// returned.
		return entries.get(entries.firstKey());
	}

	/**
	 * This method reads an atom file from a URL into a Feed element.
	 * 
	 * @param url
	 *            the Internet network location of an atom file.
	 * @return the atom Feed element.
	 * @throws Exception
	 *             if the URL cannot be parsed into a Feed element.
	 */
	public Feed readFeedToBean(URL url) throws Exception {
		return readFeedToBean(url.openStream());
	}

	/**
	 * This method reads an atom file from a URL into a Entry element.
	 * 
	 * @param url
	 *            the Internet network location of an atom file.
	 * @return the atom Entry element.
	 * @throws Exception
	 *             if the URL cannot be parsed into a Entry element.
	 */
	public Entry readEntryToBean(URL url) throws Exception {
		return readEntryToBean(url.openStream());
	}

	/**
	 * This method reads an atom file from an input stream into a Feed element.
	 * 
	 * @param inputStream
	 *            the input stream containing an atom file.
	 * @return the atom Feed element.
	 * @throws Exception
	 *             if the URL cannot be parsed into a Feed element.
	 */
	public Feed readFeedToBean(InputStream inputStream) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(inputStream);
		return new FeedReader().readFeed(reader);
	}

	/**
	 * This method reads an atom file from an input stream into a Entry element.
	 * 
	 * @param inputStream
	 *            the input stream containing an atom file.
	 * @return the atom Entry element.
	 * @throws Exception
	 *             if the URL cannot be parsed into a Feed element.
	 */
	public Entry readEntryToBean(InputStream inputStream) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(inputStream);
		SortedMap<String, Entry> entries = new FeedReader().readEntry(reader,
				null);
		// readEntry() only reads at most one entry.
		// so if the string contains more than one entry, only the first gets
		// returned.
		return entries.get(entries.firstKey());
	}

	/**
	 * 
	 * @param id
	 *            the unique id element (optional)
	 * @param title
	 *            the title element (optional)
	 * @param updated
	 *            the updated element (optional)
	 * @param rights
	 *            the rights element (optional)
	 * @param authors
	 *            a list of author elements (optional)
	 * @param categories
	 *            a list of category elements (optional)
	 * @param contributors
	 *            a list of contributor elements (optional)
	 * @param links
	 *            a list of link elements (optional)
	 * @param attributes
	 *            additional attributes (optional)
	 * @param extensions
	 *            a list of extension elements (optional)
	 * @param generator
	 *            the generator element (optional)
	 * @param subtitle
	 *            the subtitle element (optional)
	 * @param icon
	 *            the icon element (optional)
	 * @param logo
	 *            the logo element (optional)
	 * @param entries
	 *            a list of entry elements (optional)
	 * @return an immutable Feed object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Feed buildFeed(Id id, Title title, Updated updated, Rights rights,
			List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Generator generator, Subtitle subtitle, Icon icon, Logo logo,
			SortedMap<String, Entry> entries) throws AtomSpecException {
		return new Feed(id, title, updated, rights, authors, categories,
				contributors, links, attributes, extensions, generator,
				subtitle, icon, logo, entries);
	}

	/**
	 * 
	 * @param name
	 *            the attribute name.
	 * @param value
	 *            the attribute value.
	 * @return an immutable Attribute object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Attribute buildAttribute(String name, String value)
			throws AtomSpecException {
		return new Attribute(name, value);
	}

	/**
	 * 
	 * @param name
	 *            the name element. (required)
	 * @param uri
	 *            the uri element.
	 * @param email
	 *            the email element.
	 * @param attributes
	 *            additional attributes.
	 * @param extensions
	 *            a list of extension elements.
	 * @return an immutable Author object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Author buildAuthor(Name name, URI uri, Email email,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {
		return new Author(name, uri, email, attributes, extensions);
	}

	/**
	 * 
	 * @param attributes
	 *            the attributes list which must contain "term" and may contain
	 *            "scheme", "label" or others
	 * @param content
	 *            the undefined element content.
	 * @return an immutable Category object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Category buildCategory(List<Attribute> attributes, String content)
			throws AtomSpecException {
		return new Category(attributes, content);
	}

	/**
	 * 
	 * @param content
	 *            the content of this element
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Content object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Content buildContent(String content, List<Attribute> attributes)
			throws AtomSpecException {
		return new Content(content, attributes);
	}

	/**
	 * 
	 * @param name
	 *            the name element. (required)
	 * @param uri
	 *            the uri element.
	 * @param email
	 *            the email element.
	 * @param attributes
	 *            additional attributes.
	 * @param extensions
	 *            a list of extension elements.
	 * @return an immutable Contributor object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Contributor buildContributor(Name name, URI uri, Email email,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {
		return new Contributor(name, uri, email, attributes, extensions);
	}

	/**
	 * 
	 * @param email
	 *            a human-readable email for the person
	 * @return an immutable Email object.
	 */
	public Email buildEmail(String email) {
		return new Email(email);
	}

	/**
	 * 
	 * @param id
	 *            the id element (required)
	 * @param title
	 *            the title element (required)
	 * @param updated
	 *            the updated element (required)
	 * @param rights
	 *            the rights element (optional)
	 * @param content
	 *            the content element (optional)
	 * @param authors
	 *            a list of author elements (optional)
	 * @param categories
	 *            a list of category elements (optional)
	 * @param contributors
	 *            a list of contributor elements (optional)
	 * @param links
	 *            a list of link elements (optional)
	 * @param attributes
	 *            additional attributes.(optional)
	 * @param extensions
	 *            a list of extension elements (optional)
	 * @param published
	 *            the published element (optional)
	 * @param summary
	 *            the summary element (optional)
	 * @param source
	 *            the source element (optional)
	 * @return an immutable Entry object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Entry buildEntry(Id id, Title title, Updated updated, Rights rights,
			Content content, List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Published published, Summary summary, Source source)
			throws AtomSpecException {
		return new Entry(id, title, updated, rights, content, authors,
				categories, contributors, links, attributes, extensions,
				published, summary, source);
	}

	/**
	 * 
	 * @param elementName
	 *            the name of the extension element.
	 * @param attributes
	 *            additional attributes.
	 * @param content
	 *            the content of the extension element.
	 * @return an immutable Extension object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Extension buildExtension(String elementName,
			List<Attribute> attributes, String content)
			throws AtomSpecException {
		return new Extension(elementName, attributes, content);
	}

	/**
	 * 
	 * @param attributes
	 *            the attributes list which can contain "uri" and or "version"
	 *            or others
	 * @param text
	 *            the text content.
	 * @return an immutable Generator object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Generator buildGenerator(List<Attribute> attributes, String text)
			throws AtomSpecException {
		return new Generator(attributes, text);
	}

	/**
	 * 
	 * @param atomUri
	 *            the URI reference.
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Icon object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Icon buildIcon(List<Attribute> attributes, String atomUri)
			throws AtomSpecException {
		return new Icon(attributes, atomUri);
	}

	/**
	 * 
	 * @param atomUri
	 *            the URI reference.
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Id object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Id buildId(List<Attribute> attributes, String atomUri)
			throws AtomSpecException {
		return new Id(attributes, atomUri);
	}

	/**
	 * 
	 * @param attributes
	 *            the attributes list which must contain "href" and may contain
	 *            "rel", "type", "hreflang", "title", "length" or others
	 * @param content
	 *            the undefined link content.
	 * @return an immutable Link object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Link buildLink(List<Attribute> attributes, String content)
			throws AtomSpecException {
		return new Link(attributes, content);
	}

	/**
	 * 
	 * @param atomUri
	 *            the logo uri reference.
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Logo object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Logo buildLogo(List<Attribute> attributes, String atomUri)
			throws AtomSpecException {
		return new Logo(attributes, atomUri);
	}

	/**
	 * 
	 * @param name
	 *            a human-readable name for the person
	 * @return an immutable Name object.
	 */
	public Name buildName(String name) {
		return new Name(name);
	}

	/**
	 * 
	 * @param published
	 *            the date formatted to [RFC3339]
	 * @param attributes
	 *            the attributes for the published object.
	 * @return an immutable Published object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Published buildPublished(Date published, List<Attribute> attributes)
			throws AtomSpecException {
		return new Published(published, attributes);
	}

	/**
	 * 
	 * @param rights
	 *            the rights text.
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Rights object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Rights buildRights(String rights, List<Attribute> attributes)
			throws AtomSpecException {
		return new Rights(rights, attributes);
	}

	/**
	 * 
	 * @param id
	 *            the unique id element (optional)
	 * @param title
	 *            the title element (optional)
	 * @param updated
	 *            the updated element (optional)
	 * @param rights
	 *            the rights element (optional)
	 * @param authors
	 *            a list of author elements (optional)
	 * @param categories
	 *            a list of category elements (optional)
	 * @param contributors
	 *            a list of contributor elements (optional)
	 * @param links
	 *            a list of link elements (optional)
	 * @param attributes
	 *            additional attributes (optional)
	 * @param extensions
	 *            a list of extension elements (optional)
	 * @param generator
	 *            the generator element (optional)
	 * @param subtitle
	 *            the subtitle element (optional)
	 * @param icon
	 *            the icon element (optional)
	 * @param logo
	 *            the logo element (optional)
	 * @return an immutable Source object.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Source buildSource(Id id, Title title, Updated updated,
			Rights rights, List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Generator generator, Subtitle subtitle, Icon icon, Logo logo)
			throws AtomSpecException {
		return new Source(id, title, updated, rights, authors, categories,
				contributors, links, attributes, extensions, generator,
				subtitle, icon, logo);
	}

	/**
	 * 
	 * @param subtitle
	 *            the subtitle text.
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Subtitle object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Subtitle buildSubtitle(String subtitle, List<Attribute> attributes)
			throws AtomSpecException {
		return new Subtitle(subtitle, attributes);
	}

	/**
	 * 
	 * @param summary
	 *            the summary text.
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Summary object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Summary buildSummary(String summary, List<Attribute> attributes)
			throws AtomSpecException {
		return new Summary(summary, attributes);
	}

	/**
	 * 
	 * @param title
	 *            the title text
	 * @param attributes
	 *            additional attributes.
	 * @return an immutable Title object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Title buildTitle(String title, List<Attribute> attributes)
			throws AtomSpecException {
		return new Title(title, attributes);
	}

	/**
	 * 
	 * @param updated
	 *            the date formatted to [RFC3339]
	 * @param attributes
	 *            the attributes for the updated object.
	 * @return a immutable Updated object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Updated buildUpdated(Date updated, List<Attribute> attributes)
			throws AtomSpecException {
		return new Updated(updated, attributes);
	}

	/**
	 * 
	 * @param uri
	 *            the content of the uri according to Section 7 of [RFC3986]
	 * @return and immutable URI object.
	 */
	public URI buildURI(String uri) {
		return new URI(uri);
	}

	// used to write feed output for several feed writing methods.
	private void writeFeedOutput(Feed feed, XMLStreamWriter writer,
			String encoding, String version) throws XMLStreamException,
			Exception {

		// make sure the feed is sorted before it is written out to the file.
		// this prevents the client code from having to
		// maintain the sorting during usage
		feed = checkForAndApplyExtension(
				feed,
				buildAttribute("xmlns:sort",
						"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0"));

		// add atom base and xml_language to the entry if they are not there.
		List<Attribute> attributes = feed.getAttributes();
		if (attributes == null) {
			attributes = new LinkedList<Attribute>();
		}
		if (getAttributeFromGroup(attributes, getAtomBase().getName()) == null) {
			attributes.add(getAtomBase());
		}
		if (getAttributeFromGroup(attributes, getLangEn().getName()) == null) {
			attributes.add(getLangEn());
		}

		// rebuild the feed with the updated attributes
		// and atomsphere generator element
		feed = buildFeed(feed.getId(), feed.getTitle(), feed.getUpdated(), feed
				.getRights(), feed.getAuthors(), feed.getCategories(), feed
				.getContributors(), feed.getLinks(), attributes, feed
				.getExtensions(), getAtomsphereVersion(), feed.getSubtitle(),
				feed.getIcon(), feed.getLogo(), feed.getEntries());

		// write the xml header.
		writer.writeStartDocument(encoding, version);
		new FeedWriter().writeFeed(writer, feed);
		writer.flush();
		writer.close();
	}

	// used internally by feed reader
	AtomPersonConstruct buildAtomPersonConstruct(Name name, URI uri,
			Email email, List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {
		return new AtomPersonConstruct(name, uri, email, attributes, extensions);
	}

	// checks for and returns the Attribute from the String attribute (argument)
	// in the list of attributes (argument)
	// used by Category, Generator and Link.
	Attribute getAttributeFromGroup(List<Attribute> attributes,
			String attributeName) {
		if (attributes != null) {
			for (Attribute attr : attributes) {
				if (attr.getName().equalsIgnoreCase(attributeName)) {
					try {
						return buildAttribute(attr.getName(), attr.getValue());
					} catch (AtomSpecException e) {
						// this shouldn't happen.
					}
				}
			}
		}
		return null;
	}

	/**
	 * Convenience method for getting the content type for this element.
	 * Examines the "type" and "src" attributes if they exist in the list.
	 * 
	 * @param attributes
	 *            the attributes to examine.
	 * 
	 * @return the content type for this element. One of TEXT,HTML,XHTML,OTHER
	 *         or EXTERNAL
	 */
	public ContentType getContentType(List<Attribute> attributes) {
		ContentType contentType = ContentType.TEXT; // default
		if (attributes != null) {
			for (Attribute attr : attributes) {
				if (attr.getName().equals("src")) {
					return ContentType.EXTERNAL;
				}

				if (attr.getName().equals("type")
						&& attr.getValue().equals("text")) {
					contentType = ContentType.TEXT;
					break;
				} else if (attr.getName().equals("type")
						&& attr.getValue().equals("html")) {
					contentType = ContentType.HTML;
					break;
				} else if (attr.getName().equals("type")
						&& attr.getValue().equals("xhtml")) {
					contentType = ContentType.XHTML;
				} else if (attr.getName().equals("type")
						&& (!attr.getValue().equals("text")
								&& !attr.getValue().equals("html") && !attr
								.getValue().equals("xhtml"))) {
					contentType = ContentType.OTHER;
					break;
				}
			}
		}
		return contentType;
	}

	/**
	 * This method sorts the entries of the feed. The Updated, Title and Summary
	 * are currently the only elementInstance types supported.
	 * 
	 * @param feed
	 *            the feed whose entries are to be sorted
	 * @param comparator
	 *            used to determine sort order
	 * @param elementClass
	 *            serves as the key element for the entries collection
	 * @return the sorted feed.
	 * @throws AtomSpecException
	 *             if the data violates the <a href=
	 *             "http://atomenabled.org/developers/syndication/atom-format-spec.php"
	 *             >specification</a>.
	 */
	public Feed sortEntries(Feed feed, Comparator<String> comparator,
			Class<?> elementClass) throws AtomSpecException {

		if (feed.getEntries() != null) {
			// sort the entries with the passed in instance as the key
			SortedMap<String, Entry> resortedEntries = new TreeMap<String, Entry>(
					comparator);
			SortedMap<String, Entry> currentEntries = feed.getEntries();
			for (Entry entry : currentEntries.values()) {
				if (elementClass.getSimpleName().equals("Updated")) {
					resortedEntries.put(entry.getUpdated().getText(), entry);
				}
				if (elementClass.getSimpleName().equals("Title")) {
					resortedEntries.put(entry.getTitle().getText(), entry);
				}
				if (elementClass.getSimpleName().equals("Summary")) {
					resortedEntries.put(entry.getSummary().getText(), entry);
				}
			}

			// rebuild the top level feed attributes to include the sort
			// if it isn't already there.
			List<Attribute> localFeedAttrs = new LinkedList<Attribute>();
			Attribute attrLocal = buildAttribute("xmlns:sort",
					"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0");
			if (feed.getAttributes() == null) {
				localFeedAttrs.add(attrLocal);
			} else {
				for (Attribute attr : feed.getAttributes()) {
					if (!attr.equals(attrLocal)) {
						localFeedAttrs.add(attr);
					}
				}

				// finally add the sort extension attribute declaration
				localFeedAttrs.add(attrLocal);
			}

			// add or replace this extension element.

			String elementName = null;
			if (comparator == SORT_ASC) {
				elementName = "sort:asc";
			} else {
				elementName = "sort:desc";
			}
			Attribute sortElement = null;
			if (elementClass.getSimpleName().equals("Updated")) {
				sortElement = buildAttribute("type", "updated");
			} else if (elementClass.getSimpleName().equals("Title")) {
				sortElement = buildAttribute("type", "title");
			} else if (elementClass.getSimpleName().equals("Summary")) {
				sortElement = buildAttribute("type", "summary");
			}
			List<Attribute> extAttrs = new LinkedList<Attribute>();
			extAttrs.add(sortElement);
			Extension localFeedExtension = buildExtension(elementName,
					extAttrs, null);

			// rebuild the extensions
			// we have to look for the sort extension and
			// replace any occurrences of it with the one we just created.
			List<Extension> localFeedExtensions = new LinkedList<Extension>();
			if (feed.getExtensions() == null) {
				localFeedExtensions.add(localFeedExtension);
			} else {
				for (Extension extn : feed.getExtensions()) {
					// if we find an existing sort extension, ignore it.
					// add all others to the return list.
					if (!extn.getElementName().equalsIgnoreCase("sort:asc")
							&& !extn.getElementName().equalsIgnoreCase(
									"sort:desc")) {
						localFeedExtensions.add(extn);
					}
				}
				// finally add the new one.
				localFeedExtensions.add(localFeedExtension);
			}

			// this is an immutable sorted copy of the feed.
			return buildFeed(feed.getId(), feed.getTitle(), feed.getUpdated(),
					feed.getRights(), feed.getAuthors(), feed.getCategories(),
					feed.getContributors(), feed.getLinks(), localFeedAttrs,
					localFeedExtensions, feed.getGenerator(), feed
							.getSubtitle(), feed.getIcon(), feed.getLogo(),
					resortedEntries);
		}
		// return the feed in the original order.
		return feed;
	}

	// Checks the xmlns (namespace) argument and applies the extension
	// to the feed argument if it is recognized by the atomsphere library.
	// used by FeedReader and FeedWriter
	Feed checkForAndApplyExtension(Feed feed, Attribute xmlns) throws Exception {

		// if there aren't any attributes for the feed and thus no xmlns:sort
		// attribute
		// return the defaults.
		if (feed.getAttributes() == null) {
			return feed;
		}

		// check for the first supported extension
		// currently only sort is implemented.
		for (Attribute attr : feed.getAttributes()) {
			if (attr.equals(xmlns)) {
				return applySort(feed);
			}
		}
		return feed;
	}

	// check for and apply the first sort extension.
	private Feed applySort(Feed feed) throws AtomSpecException {
		// only do the work if there are extensions.
		if (feed.getExtensions() != null) {
			// look for the first extension element if the namespace exists.
			for (Extension ext : feed.getExtensions()) {
				if (ext.getElementName().equals("sort:asc")) {
					for (Attribute attr : ext.getAttributes()) {
						if (attr.getName().equalsIgnoreCase("type")) {
							String value = attr.getValue();
							if (value.equals("updated")) {
								return sortEntries(feed, SORT_ASC,
										Updated.class);
							}
							if (value.equals("title")) {
								return sortEntries(feed, SORT_ASC, Title.class);
							}
							if (value.equals("summary")) {
								return sortEntries(feed, SORT_ASC,
										Summary.class);
							}
						}
					}
				} else if (ext.getElementName().equals("sort:desc")) {
					for (Attribute attr : ext.getAttributes()) {
						if (attr.getName().equalsIgnoreCase("type")) {
							String value = attr.getValue();
							if (value.equals("updated")) {
								return sortEntries(feed, SORT_DESC,
										Updated.class);
							}
							if (value.equals("title")) {
								return sortEntries(feed, SORT_DESC, Title.class);
							}
							if (value.equals("summary")) {
								return sortEntries(feed, SORT_DESC,
										Summary.class);
							}
						}
					}
				}
			}
		}
		return feed;
	}

	/**
	 * @return the base namespace for the atom 1.0 spec
	 *         http://www.w3.org/2005/Atom
	 */
	public Attribute getAtomBase() {
		try {
			return buildAttribute("xmlns", "http://www.w3.org/2005/Atom");
		} catch (AtomSpecException e) {
			// this shouldn't happen.
			return null;
		}
	}

	/**
	 * @return the base language for the library en-US.
	 */
	public Attribute getLangEn() {
		try {
			return buildAttribute("xml:lang", "en-US");
		} catch (AtomSpecException e) {
			// this shouldn't happen.
			return null;
		}
	}

	/**
	 * @return the default encoding for the library UTF-8.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return the default xml version 1.0
	 */
	public String getXmlVersion() {
		return xmlVersion;
	}

	/**
	 * @return the atomsphere library version.
	 */
	public String getLibVersion() {
		return libVersion;
	}
}
