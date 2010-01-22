/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.colorfulsoftware.atom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Comparator;
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
public final class FeedDoc implements Serializable {

	private static final long serialVersionUID = -8371262728450556870L;

	/**
	 * the default document encoding of "UTF-8"
	 */
	private String encoding = System.getProperty("file.encoding");

	/**
	 * the default XML version of "1.0"
	 */
	private String xmlVersion = "1.0";
	private Generator libVersion;

	private Attribute langEn;
	private Attribute atomBase;

	private XMLInputFactory inputFactory;

	/**
	 * creates a new feed document.
	 * 
	 * @throws Exception
	 *             if the library version information cannot be loaded from the
	 *             environment.
	 */
	public FeedDoc() throws Exception {

		langEn = new Attribute("xml:lang", "en-US");
		atomBase = new Attribute("xmlns", "http://www.w3.org/2005/Atom");

		Properties props = new Properties();
		props.load(FeedDoc.class.getResourceAsStream("/atomsphere.properties"));
		List<Attribute> attributes = new LinkedList<Attribute>();
		attributes.add(new Attribute("uri", props.getProperty("uri")));
		attributes.add(new Attribute("version", props.getProperty("version")));
		libVersion = buildGenerator(attributes, "Atomsphere");

		inputFactory = XMLInputFactory.newInstance();
		// this is done to help for parsing documents that have undeclared and
		// unescaped html or xhtml entities.
		inputFactory.setProperty(
				"javax.xml.stream.isReplacingEntityReferences", Boolean.FALSE);
	}

	/**
	 * @param encoding
	 *            the document encoding. eg: UTF-8
	 * @param xmlVersion
	 *            the document xml version eg: 1.0
	 * @throws Exception
	 *             if the library version information cannot be loaded from the
	 *             environment.
	 */
	public FeedDoc(String encoding, String xmlVersion) throws Exception {
		this();
		this.encoding = encoding;
		this.xmlVersion = xmlVersion;
	}

	/**
	 * @return the Atomsphere library version in the form of a generator
	 *         element. This element is output for all feeds that are generated
	 *         by Atomsphere.
	 */
	public Generator getLibVersion() {
		return new Generator(libVersion);
	}

	/**
	 * 
	 * @param output
	 *            the target output for the feed document.
	 * @param feed
	 *            the atom feed object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is
	 *            System.getProperty("file.encoding"))
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
	 *            the file encoding (default is
	 *            System.getProperty("file.encoding"))
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
	 *            the file encoding (default is
	 *            System.getProperty("file.encoding"))
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the atom document cannot be written to the output
	 *             or if the writer output parameter's underlying encoding does
	 *             not match the passed encoding parameter.
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
	 *            the file encoding (default is
	 *            System.getProperty("file.encoding"))
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the atom document cannot be written to the output
	 *             or if the writer output parameter's underlying encoding does
	 *             not match the passed encoding parameter.
	 * 
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
	 *            the file encoding (default is
	 *            System.getProperty("file.encoding"))
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
	 *            the file encoding (default is
	 *            System.getProperty("file.encoding"))
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

		if (feed == null) {
			throw new AtomSpecException("The atom feed object cannot be null.");
		}

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
			return feed.toString();
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

		if (entry == null) {
			throw new AtomSpecException("The atom entry object cannot be null.");
		}

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
			return entry.toString();
		}
		return theString.toString();
	}

	// used for writing entry documents to their output.
	private void writeEntryOutput(Entry entry, XMLStreamWriter writer,
			String encoding, String version) throws AtomSpecException,
			Exception, XMLStreamException {
		List<Entry> entries = new LinkedList<Entry>();

		if (entry == null) {
			throw new AtomSpecException("The atom entry object cannot be null.");
		}

		// add atom base and language to the entry if they are not there.
		List<Attribute> attributes = entry.getAttributes();
		if (attributes == null) {
			attributes = new LinkedList<Attribute>();
		}
		if (entry.getAttribute("xmlns") == null) {
			attributes.add(getAtomBase());
		}
		if (entry.getAttribute("xml:lang") == null) {
			attributes.add(getLangEn());
		}

		// rebuild the entry with the added attributes.
		entries
				.add(buildEntry(entry.getId(), entry.getTitle(), entry
						.getUpdated(), entry.getRights(), entry.getContent(),
						entry.getAuthors(), entry.getCategories(), entry
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
		// try to grab the encoding first:
		if (xmlString.contains("encoding=\"")) {
			String localEncoding = xmlString.substring(xmlString
					.indexOf("encoding=\"") + 10);
			localEncoding = localEncoding.substring(0, localEncoding
					.indexOf('"'));
			encoding = localEncoding;

		}
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(new ByteArrayInputStream(xmlString
						.getBytes(encoding)));
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
		// try to grab the encoding first:
		if (xmlString.contains("encoding=\"")) {
			String localEncoding = xmlString.substring(xmlString
					.indexOf("encoding=\"") + 10);
			localEncoding = localEncoding.substring(0, localEncoding
					.indexOf('"'));
			encoding = localEncoding;

		}
		XMLStreamReader reader = inputFactory
				.createXMLStreamReader(new ByteArrayInputStream(xmlString
						.getBytes(encoding)));
		List<Entry> entries = new FeedReader().readEntry(reader, null);
		// readEntry() only reads at most one entry.
		return entries.get(0);
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
		XMLStreamReader reader = inputFactory.createXMLStreamReader(
				new FileInputStream(file), encoding);
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
		XMLStreamReader reader = inputFactory.createXMLStreamReader(
				new FileInputStream(file), encoding);
		List<Entry> entries = new FeedReader().readEntry(reader, null);
		// readEntry() only reads at most one entry.
		return entries.get(0);
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
		XMLStreamReader reader = inputFactory.createXMLStreamReader(
				inputStream, encoding);
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
		XMLStreamReader reader = inputFactory.createXMLStreamReader(
				inputStream, encoding);
		Feed feed = new FeedReader().readFeed(reader);
		// readEntry() reads the first entry from a feed or the sole entry if it
		// is the root element.
		// so if the string contains more than one entry, only the first gets
		// returned.
		List<Entry> entries = feed.getEntries();
		return entries == null ? null : entries.get(0);
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
			List<Entry> entries) throws AtomSpecException {
		Feed feed = new Feed(id, title, updated, rights, authors, categories,
				contributors, links, attributes, extensions, generator,
				subtitle, icon, logo, entries);
		
		/*
		throw new AtomSpecException(
				"extension prefix '' is not bound to a namespace declaration 'xmlns:"
						+ namePrefix + "' in a parent element.");
		List<Attribute> feedAttributes = feed.getAttributes();
		validateExtensions(feedAttributes,feed.getExtensions());
		for(Author author: authors){
			List<Attribute> authorAttributes = author.getAttributes();
			authorAttributes.addAll(feedAttributes);
			validateExtensions(authorAttributes,author.getExtensions());
		}
		for(Author author: authors){
			List<Attribute> authorAttributes = author.getAttributes();
			authorAttributes.addAll(feedAttributes);
			validateExtensions(authorAttributes,author.getExtensions());
		}
		*/
		return feed;
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
	 * @param attributes
	 *            the attributes for the published object
	 * @param published
	 *            the date formatted to [RFC3339]
	 * @return an immutable Published object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Published buildPublished(List<Attribute> attributes, String published)
			throws AtomSpecException {
		return new Published(attributes, published);
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
	 * @param attributes
	 *            the attributes for the updated object.
	 * @param updated
	 *            the date formatted to [RFC3339]
	 * @return a immutable Updated object.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Updated buildUpdated(List<Attribute> attributes, String updated)
			throws AtomSpecException {
		return new Updated(attributes, updated);
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
			String encoding, String version) throws Exception {

		if (feed == null) {
			throw new AtomSpecException("The atom feed object cannot be null.");
		}

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
		if (feed.getAttribute("xmlns") == null) {
			attributes.add(getAtomBase());
		}
		if (feed.getAttribute("xml:lang") == null) {
			attributes.add(getLangEn());
		}
		// rebuild the feed with the updated attributes
		// and atomsphere generator element
		feed = buildFeed(feed.getId(), feed.getTitle(), feed.getUpdated(), feed
				.getRights(), feed.getAuthors(), feed.getCategories(), feed
				.getContributors(), feed.getLinks(), attributes, feed
				.getExtensions(), getLibVersion(), feed.getSubtitle(), feed
				.getIcon(), feed.getLogo(), feed.getEntries());
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

	/**
	 * This method sorts the entries of the feed. The Updated, Title and Summary
	 * are currently the only elementInstance types supported.
	 * 
	 * @param feed
	 *            the feed whose entries are to be sorted.
	 * @param elementClass
	 *            the element to be used for the sort.
	 * @param ascDesc
	 *            the sort direction of ascending or descending.
	 * @return the sorted feed.
	 * @throws AtomSpecException
	 *             if the feed cannot be sorted.
	 */
	public Feed sortEntries(Feed feed, Class<?> elementClass, String ascDesc)
			throws AtomSpecException {

		// short circuit if we are asked to sort an invalid element type
		if (!elementClass.getSimpleName().equals("Updated")
				&& !elementClass.getSimpleName().equals("Title")
				&& !elementClass.getSimpleName().equals("Summary")) {
			throw new AtomSpecException(
					"The feed entries cannot be sorted by an invalid type '"
							+ elementClass.getSimpleName() + "'.");
		}

		// only attempt to sort if there are enough entries.
		if (feed.getEntries() != null && feed.getEntries().size() > 1) {

			final Comparator<String> SORT_ASC = new Comparator<String>() {
				public int compare(String key1, String key2) {
					return key1.compareTo(key2);

				}
			};

			final Comparator<String> SORT_DESC = new Comparator<String>() {
				public int compare(String key1, String key2) {
					return key2.compareTo(key1);
				}
			};

			// set the sort order.
			SortedMap<String, Entry> resortedEntries = new TreeMap<String, Entry>(
					(ascDesc.equals("asc")) ? SORT_ASC : SORT_DESC);

			// order the entries.
			List<Entry> currentEntries = feed.getEntries();
			Attribute sortElement = null; // used to add to the sorted feed.
			for (Entry entry : currentEntries) {
				if (elementClass.getSimpleName().equals("Updated")) {
					resortedEntries.put(entry.getUpdated().getText(), entry);
					sortElement = buildAttribute("type", "updated");
				}
				if (elementClass.getSimpleName().equals("Title")) {
					resortedEntries.put(entry.getTitle().getText(), entry);
					sortElement = buildAttribute("type", "title");
				}
				if (elementClass.getSimpleName().equals("Summary")) {
					if (entry.getSummary() == null) {
						throw new AtomSpecException(
								"The feed entries cannot be sorted by <summary> because not all of them have one.");
					}
					sortElement = buildAttribute("type", "summary");
					resortedEntries.put(entry.getSummary().getText(), entry);
				}
			}

			// rebuild the top level feed attributes to include the sort
			// if it isn't already there.
			List<Attribute> feedAttrs = new LinkedList<Attribute>();
			Attribute sortNamespaceAttribute = buildAttribute("xmlns:sort",
					"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0");
			if (feed.getAttributes() == null) {
				feedAttrs.add(sortNamespaceAttribute);
			} else {
				// add everything but the sort namespace attribute.
				for (Attribute attr : feed.getAttributes()) {
					if (!attr.equals(sortNamespaceAttribute)) {
						feedAttrs.add(attr);
					}
				}
				// finally add the sort extension namespace declaration
				feedAttrs.add(sortNamespaceAttribute);
			}

			// add or replace this extension element.
			List<Attribute> extAttrs = new LinkedList<Attribute>();
			extAttrs.add(sortElement);
			Extension sortExtension = buildExtension("sort:" + ascDesc,
					extAttrs, null);

			// rebuild the extensions
			// we have to look for the sort extension and
			// replace any occurrences of it with the one we just created.
			List<Extension> localFeedExtensions = new LinkedList<Extension>();
			if (feed.getExtensions() == null) {
				localFeedExtensions.add(sortExtension);
			} else {
				for (Extension extn : feed.getExtensions()) {
					// add all others but the sort extension to the return list.
					if (!extn.getElementName().equals("sort:asc")
							&& !extn.getElementName().equals("sort:desc")) {
						localFeedExtensions.add(extn);
					}
				}
				// finally add the new one.
				localFeedExtensions.add(sortExtension);
			}

			// this is an immutable sorted copy of the feed.
			return buildFeed(feed.getId(), feed.getTitle(), feed.getUpdated(),
					feed.getRights(), feed.getAuthors(), feed.getCategories(),
					feed.getContributors(), feed.getLinks(), feedAttrs,
					localFeedExtensions, feed.getGenerator(), feed
							.getSubtitle(), feed.getIcon(), feed.getLogo(),
					new LinkedList<Entry>(resortedEntries.values()));
		}
		// return the feed in the original order.
		return feed;
	}

	// Checks the xmlns (namespace) argument and applies the extension
	// to the feed argument if it is recognized by the atomsphere library.
	// used by FeedReader and FeedWriter
	Feed checkForAndApplyExtension(Feed feed, Attribute xmlns) throws Exception {

		// check for the first supported extension
		// currently only sort is implemented.
		if (feed.getAttributes() != null
				&& feed.getAttribute(xmlns.getName()) != null) {
			return applySort(feed);
		}

		return feed;
	}

	// check for and apply the first sort extension.
	private Feed applySort(Feed feed) throws AtomSpecException {
		// only do the work if there are extensions.
		// look for the first extension element if the namespace exists.
		Extension sortAsc = feed.getExtension("sort:asc");
		Extension sortDesc = feed.getExtension("sort:desc");
		if (sortAsc != null) {
			Attribute type = sortAsc.getAttribute("type");
			if (type != null && type.getValue().equals("updated")) {
				return sortEntries(feed, Updated.class, "asc");
			} else if (type != null && type.getValue().equals("title")) {
				return sortEntries(feed, Title.class, "asc");
			} else if (type != null && type.getValue().equals("summary")) {
				return sortEntries(feed, Summary.class, "asc");
			}

		}

		if (sortDesc != null) {
			Attribute type = sortDesc.getAttribute("type");
			if (type != null && type.getValue().equals("updated")) {
				return sortEntries(feed, Updated.class, "desc");
			} else if (type != null && type.getValue().equals("title")) {
				return sortEntries(feed, Title.class, "desc");
			} else if (type != null && type.getValue().equals("summary")) {
				return sortEntries(feed, Summary.class, "desc");
			}
		}

		// if we made it here, there was not sort found.
		return feed;
	}

	/**
	 * @return the base namespace for the atom 1.0 spec
	 *         http://www.w3.org/2005/Atom
	 */
	public Attribute getAtomBase() {
		return new Attribute(atomBase);
	}

	/**
	 * @return the base language for the library en-US.
	 */
	public Attribute getLangEn() {
		return new Attribute(langEn);
	}

	/**
	 * @return the default encoding for the library is
	 *         System.getProperty("file.encoding").
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

}
