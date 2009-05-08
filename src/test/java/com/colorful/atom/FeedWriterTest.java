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
package com.colorful.atom;

import static org.junit.Assert.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.SortedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorful.atom.Entry;
import com.colorful.atom.FeedReader;
import com.colorful.atom.FeedWriter;

public class FeedWriterTest {

	private FeedWriter feedWriter;
	private XMLStreamWriter writer;
	private String queryStringXHTML = "<content type=\"xhtml\">"
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\"> "
			+ "this is a querystring test <a href=\"http://www.google.com?"
			+ "q=test%20text&a=hello\">link</a>.</div></content>";
	private String xhtml1 = "this is a test "
			+ "<a href=\"http://www.google.com?"
			+ "q=test%20text&a=hello\">link</a>.  "
			+ "Can we put arbitrary xhtml here?"
			+ "<hr />I can't say for sure. <span>hello</span>";
	private String xhtml2 = "<h4 >Installation (atomsphere library)</h4>"
			+ "<ul >        		<li>Add the jsr173, sjsxp, stax-utils and "
			+ "atomsphere jars to the classpath (WEB-INF/lib for webapps)"
			+ ".</li></ul>"
			+ "<h4 >Installation (atomsphere-taglib)</h4>        		"
			+ "<ul >        		<li>Add the atomsphere jar to the "
			+ "classpath (WEB-INF/lib for webapps).</li>"
			+ "<li>Add the atomsphereTags.tld tag descriptor to the top "
			+ "of the jsp page (See example below). </li>"
			+ "<li>Add any required attributes and optional attributes "
			+ "to the custom tag (See example below).</li>"
			+ "<li>View the atomsphereTags.tld for a description of the "
			+ "attributes and what they do.</li></ul>"
			+ "<h4 >Installation (atomsphere-weblib)</h4>        		<ul >"
			+ "<li>Add the atomsphere and atomsphere-weblib jars to the "
			+ "classpath (WEB-INF/lib for webapps).</li>"
			+ "<li>Copy the web-fragrment.xml (embeded in the jar file) to "
			+ "your application's web.xml file.</li></ul>"
			+ "<h4 >Installation (atomsphere-webapp)</h4>        		"
			+ "<ul >				<li>Deploy the war file to any J2EE "
			+ "servlet container.</li>				</ul>";

	private String entry1 = "<entry xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/"
			+ "projects/atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    "
			+ "<title type=\"xhtml\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:div>"
			+ "Less: <xhtml:em> &lt; </xhtml:em></xhtml:div></title>"
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<summary>About the project</summary>    "
			+ "<published>"
			+ "2007-02-26T12:58:53.197-06:00</published>    "
			+ "<content type=\"text/html\" src=\"http://some.xh.specific.uri/xh\" />"
			+ "</entry>";

	private String entry1Result = "<entry xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/projects/"
			+ "atomsphere/atom.xml#Requirements</id>"
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>"
			+ "<title xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" type=\"xhtml\"><xhtml:div>"
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "Less: <xhtml:em> &lt; </xhtml:em></xhtml:div></title>"
			+ "<summary>About the project</summary>    "
			+ "<published>2007-02-26T12:58:53.197-06:00</published>"
			+ "<content type=\"text/html\" src=\"http://some.xh.specific.uri/xh\" />"
			+ "</entry>";

	private String entry2 = "<entry xmlns:xh=\"http://www.w3.org/1999/xhtml\">    "
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/projects/"
			+ "atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    "
			+ "<title>Requirements</title>    "
			+ "<published>2007-02-26T12:58:53.197-06:00</published>    "
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			// xhtml prefix xh is bound at entry
			+ "<content type=\"xhtml\">"
			+ "<xh:div>"
			+ "This is <xh:b>XHTML</xh:b> content."
			+ "</xh:div>"
			+ "</content>" + "</entry>";

	private String entry2Result = "<entry xmlns:xh=\"http://www.w3.org/1999/xhtml\">"
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/"
			+ "projects/atomsphere/atom.xml#Requirements</id>"
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>"
			+ "<title>Requirements</title>"
			+ "<published>2007-02-26T12:58:53.197-06:00</published>"
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<content type=\"xhtml\">"
			// xhtml prefix xh is bound at entry
			// no need to wrap in supplied div.
			+ "<xh:div>This is &lt;xh:b&gt;XHTML&lt;/xh:b&gt; content.</xh:div>"
			+ "</content>" + "</entry>";

	private String entry3 = "<entry xmlns:xh=\"http://www.w3.org/1999/xhtml\">    "
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/"
			+ "projects/atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    "
			+ "<title>Requirements</title>    "
			+ "<published>2007-02-26T12:58:53.197-06:00</published>    "
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<summary type=\"xhtml\">"
			// xhtml namespace is bound at div
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "This is <b>XHTML</b> content."
			+ "</div>"
			+ "</summary>"
			+ "<content type=\"xhtml\">"
			// xhtml prefix xh is bound at entry
			+ "<xh:div>"
			+ "This is <xh:b>XHTML</xh:b> content."
			+ "</xh:div>"
			+ "</content>" + "</entry>";

	private String entry3Result = "<entry xmlns:xh=\"http://www.w3.org/1999/xhtml\">"
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/"
			+ "projects/atomsphere/atom.xml#Requirements</id>"
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>"
			+ "<title>Requirements</title>"
			+ "<published>2007-02-26T12:58:53.197-06:00</published>"
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<summary type=\"xhtml\">"
			// xhtml namespace is bound at div
			// no need to wrap in supplied div.
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "<xh:div>This is &lt;xh:b&gt;XHTML&lt;/xh:b&gt; content.</xh:div>"
			+ "</div>"
			+ "</summary>"
			+ "<content type=\"xhtml\">"
			// xhtml prefix xh is bound at entry
			// no need to wrap in supplied div.
			+ "<xh:div>"
			+ "This is &lt;xh:b&gt;XHTML&lt;/xh:b&gt; content."
			+ "</xh:div>" + "</content>" + "</entry>";

	private String entry4 = "<entry xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">    "
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/"
			+ "projects/atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    "
			+ "<title>Requirements</title>    "
			+ "<published>2007-02-26T12:58:53.197-06:00</published>    "
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<summary type=\"xhtml\">"
			// xhtml namespace is bound at div
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "This is <b>XHTML</b> content."
			+ "</div>"
			+ "</summary>"
			+ "<content type=\"xhtml\">"
			// xhtml prefix xhtml is bound at entry
			+ "<xh:div>"
			+ "This is <xh:b>XHTML</xh:b> content."
			+ "</xh:div>"
			+ "</content>" + "</entry>";

	private String entry4Result = "<entry xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">"
			+ "<id>http://colorfulsoftware.localhost/colorfulsoftware/"
			+ "projects/atomsphere/atom.xml#Requirements</id>"
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>"
			+ "<title>Requirements</title>"
			+ "<published>2007-02-26T12:58:53.197-06:00</published>"
			+ "<author><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<summary type=\"xhtml\">"
			// xhtml namespace is bound at div
			// no need to wrap in supplied div.
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "<xh:div>This is &lt;xh:b&gt;XHTML&lt;/xh:b&gt; content.</xh:div>"
			+ "</div>"
			+ "</summary>"
			+ "<content type=\"xhtml\">"
			// xhtml prefix xhtml is bound at entry
			// no need to wrap in supplied div.
			+ "<xhtml:div>"
			+ "This is &lt;xhtml:b&gt;XHTML&lt;/xhtml:b&gt; content."
			+ "</xhtml:div>" + "</content>" + "</entry>";

	@Before
	public void setUp() throws Exception {
		feedWriter = new FeedWriter();
		writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
				new FileOutputStream("out.xml"));
	}

	@After
	public void tearDown() throws Exception {
		if (writer != null) {
			writer.flush();
			writer.close();
		}

		new File("out.xml").deleteOnExit();
		new File("out2.xml").deleteOnExit();
		new File("xhtml.xml").deleteOnExit();
	}

	@Test
	public void testWriteFeed() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteSubtitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteGenerator() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteID() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteUpdated() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteTitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteXHTML() {
		try {
			feedWriter.writeXHTML(writer, xhtml1);

			feedWriter.writeXHTML(writer, xhtml2);

			// test query string attribute.
			FeedWriter feedWriter2 = new FeedWriter();
			XMLStreamWriter writer2 = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(new FileOutputStream("xhtml.xml"));
			feedWriter2.writeXHTML(writer2, queryStringXHTML);
			writer2.flush();
			writer2.close();
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(new FileInputStream("xhtml.xml"));
			Content content = (new FeedReader()).readContent(reader);

			assertTrue(content.getContent()
					.indexOf(
							"href=\"http://www.google.com?"
									+ "q=test%20text&a=hello\"") > 0);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}

	@Test
	public void testWriteAuthors() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteName() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteUri() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteEmail() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteContributors() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteRights() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteLogo() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteIcon() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteCategories() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteLinks() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteExtensions() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteEntries() {
		try {

			SortedMap<String, Entry> entries = new FeedReader().readEntry(
					XMLInputFactory.newInstance().createXMLStreamReader(
							new StringReader(entry1)), null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			StringWriter theXMLString = new StringWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(theXMLString);
			new FeedWriter().writeEntries(writer, entries);
			writer.flush();
			writer.close();
			System.out.println("\nxml1 = " + theXMLString.toString()
					+ "\nxml2 = " + entry1Result);
			assertEquals(theXMLString.toString(), entry1Result);

			entries = new FeedReader().readEntry(XMLInputFactory.newInstance()
					.createXMLStreamReader(new StringReader(entry2)), null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			theXMLString = new StringWriter();
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
					theXMLString);
			new FeedWriter().writeEntries(writer, entries);
			writer.flush();
			writer.close();
			System.out.println("\nxml3 = " + theXMLString.toString()
					+ "\nxml4 = " + entry2Result);
			assertEquals(theXMLString.toString(), entry2Result);

			entries = new FeedReader().readEntry(XMLInputFactory.newInstance()
					.createXMLStreamReader(new StringReader(entry3)), null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			theXMLString = new StringWriter();
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
					theXMLString);
			new FeedWriter().writeEntries(writer, entries);
			writer.flush();
			writer.close();
			System.out.println("\nxml5 = " + theXMLString.toString()
					+ "\nxml6 = " + entry3Result);
			assertEquals(theXMLString.toString(), entry3Result);

			entries = new FeedReader().readEntry(XMLInputFactory.newInstance()
					.createXMLStreamReader(new StringReader(entry3Result)),
					null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			entries = new FeedReader().readEntry(XMLInputFactory.newInstance()
					.createXMLStreamReader(new StringReader(entry4)), null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			theXMLString = new StringWriter();
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
					theXMLString);
			new FeedWriter().writeEntries(writer, entries);
			writer.flush();
			writer.close();
			System.out.println("\nxml7 = " + theXMLString.toString()
					+ "\nxml8 = " + entry4Result);
			assertEquals(theXMLString.toString(), entry4Result);

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not write entries." + e.getLocalizedMessage());
		}
	}

	@Test
	public void testWriteSummary() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWritePublished() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteContent() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteSource() {
		// fail("Not yet implemented");
	}

}
