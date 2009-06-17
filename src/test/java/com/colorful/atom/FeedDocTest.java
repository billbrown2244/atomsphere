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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorful.atom.Attribute;
import com.colorful.atom.Author;
import com.colorful.atom.Category;
import com.colorful.atom.Contributor;
import com.colorful.atom.Entry;
import com.colorful.atom.Extension;
import com.colorful.atom.Feed;
import com.colorful.atom.FeedDoc;
import com.colorful.atom.Generator;
import com.colorful.atom.Icon;
import com.colorful.atom.Id;
import com.colorful.atom.Link;
import com.colorful.atom.Logo;
import com.colorful.atom.Name;
import com.colorful.atom.Rights;
import com.colorful.atom.Title;
import com.colorful.atom.Updated;
import com.colorful.atom.FeedDoc.ContentType;

public class FeedDocTest {

	private Feed feed1;

	private static Calendar theDate;
	static {
		theDate = Calendar.getInstance();
		theDate.clear();
		theDate.set(2008, 0, 1);
	}

	private String mega = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
			+ "<id testAttr=\"testVal\">http://colorfulsoftware.localhost/projects/atomsphere/atom.xml</id>"
			+ "<updated fakeAttribute=\"fakeValue\">2007-03-08T20:52:40.70-06:00</updated>"
			+ "<generator uri=\"http://www.colorfulsoftware.com/projects/atomsphere\" version=\"1.0.20\">Atomsphere</generator>"
			+ "<title type=\"xhtml\">Atomsphere a <b>great atom 1.0 parser </b></title>  <subtitle>a java atom feed library</subtitle>"
			+ "<author testAttr=\"testVal\"><test:test xmlns:test=\"http://www.w3.org/1999/test\" /><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></author>"
			+ "<contributor><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></contributor>"
			+ "<contributor testAttr=\"testVal\"><test:test xmlns:test=\"http://www.w3.org/1999/test\" /><name>Bill Brown</name><uri>http://www.colorfulsoftware.com</uri><email>info@colorfulsoftware.com</email></contributor>"
			+ "<category term=\"math\" scheme=\"http://www.colorfulsoftware.com/projects/atomsphere/\" label=\"math\" />"
			+ "<category term=\"science\" scheme=\"http://www.colorfulsoftware.com/projects/atomsphere/\" label=\"science\"/>"
			+ "<link href=\"http://www.colorfulsoftware.com/projects/atomsphere/atom.xml\" rel=\"self\" type=\"application/atom+xml\" hreflang=\"UTF-8\" title=\"cool site\" />"
			+ "<icon testAttr=\"testVal\">http://www.minoritydirectory.net/images/logo.gif</icon>"
			+ "<logo testAttr=\"testVal\">http://www.minoritydirectory.net/images/logo.gif</logo>"
			+ "<rights>Copyright 2007</rights>"
			+ "<entry><id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#About</id>"
			+ "<updated>2007-03-02T13:00:00.699-06:00</updated><title>About</title><published>2007-02-26T12:34:01.330-06:00</published>"
			+ "<summary>About the project</summary>"
			+ "<content type=\"html\">&lt;ul&gt; &lt;li&gt;&lt;span class=\"boldText\"&gt;Atomsphere&lt;/span&gt; isa java library that allows you to create and modify atom 1.0 feeds.&lt;/li&gt; &lt;li&gt;It is distributed under the GNU GPL license and can be used in any manner complient with the license.&lt;/li&gt; &lt;li&gt;It is also packaged as a servlet-lib for use in web applications.&lt;/li&gt; &lt;li&gt;It is also packaged as a customtag library to display feeds on a webapage.&lt;/li&gt; &lt;li&gt;It also comes with an example webapp which demonstrates some example uses of the library.&lt;/li&gt; &lt;li&gt;It is written to be tied as closely as possible to the current atom specification found &lt;a href=\"http://www.atomenabled.org/developers/syndication/atom-format-spec.php\"&gt;here&lt;/a&gt;.&lt;/li&gt; &lt;/ul&gt;</content>"
			+ "</entry>"
			+ "<entry><id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>"
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated><title>Requirements</title><published>2007-02-26T12:58:53.197-06:00</published>"
			+ "<summary>Requirements for using the libraries</summary>"
			+ "<content type=\"html\">&lt;br /&gt;the project is usable with jdk 1.4.2 and above&lt;br /&gt; &amp;nbsp;&lt;br /&gt; needed for using the library&lt;br /&gt; &lt;ul&gt; &lt;li&gt;&lt;a href=\"https://sjsxp.dev.java.net/\"&gt;jsr173&lt;/a&gt; (STAX api jar) - see the &lt;a href=\"http://jcp.org/aboutJava/communityprocess/final/jsr173/index.html\"&gt;API&lt;/a&gt;.&lt;/li&gt; &lt;li&gt;&lt;a href=\"https://sjsxp.dev.java.net/\"&gt;sjsxp&lt;/a&gt; (STAX implementation) - others implementations may work but have not been tested.&lt;/li&gt; &lt;li&gt;&lt;a href=\"https://stax-utils.dev.java.net/\"&gt;stax-utils&lt;/a&gt; (for pretty printing)&lt;/li&gt; &lt;/ul&gt; needed for using the atomsphere-taglib&lt;br /&gt; &lt;ul&gt; &lt;li&gt;the atomsphere library&lt;/li&gt; &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt; &lt;/ul&gt; needed for using the atomsphere-weblib&lt;br /&gt; &lt;ul&gt; &lt;li&gt;the atomsphere library&lt;/li&gt; &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt; &lt;/ul&gt; needed for using the example atomsphere-webapp&lt;br /&gt; &lt;ul&gt; &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;&lt;/ul&gt;</content>"
			+ "</entry>"
			+ "<entry xmlns:test=\"http://www.w3.org/1999/test\"><id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Documentation</id>"
			+ "<updated>2007-03-02T12:59:45.475-06:00</updated><title>Documentation</title><published>2007-02-26T13:00:00.478-06:00</published>"
			+ "<summary>Starting Documentation</summary>"
			+ "<test:test xmlns:test=\"http://www.w3.org/1999/test\">this is an extension test <test:does> it work? </test:does> we'll see</test:test>"
			+ "<content type=\"html\">&lt;h4&gt;Installation (atomsphere library)&lt;/h4&gt; &lt;ul&gt; &lt;li&gt;Add the jsr173, sjsxp, stax-utils and atomsphere jars to the classpath (WEB-INF/lib for webapps).&lt;/li&gt; &lt;/ul&gt; &lt;h4&gt;Installation (atomsphere-taglib)&lt;/h4&gt; &lt;ul&gt; &lt;li&gt;Add the atomsphere jar to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;&lt;li&gt;Add the atomsphereTags.tld tag descriptor to the top of the jsp page (See example below). &lt;/li&gt; &lt;li&gt;Add anyrequired attributes and optional attributes to the custom tag (See example below).&lt;/li&gt; &lt;li&gt;View the atomsphereTags.tld for a description of the attributes and what they do.&lt;/li&gt;&lt;/ul&gt; &lt;h4&gt;Installation (atomsphere-weblib)&lt;/h4&gt; &lt;ul&gt;&lt;li&gt;Add the atomsphere and atomsphere-weblib jars to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;&lt;li&gt;Copy the web-fragrment.xml (embeded in the jar file) to your application's web.xml file.&lt;/li&gt;&lt;/ul&gt; &lt;h4&gt;Installation (atomsphere-webapp)&lt;/h4&gt; &lt;ul&gt; &lt;li&gt;Deploy the war file to any J2EE servlet container.&lt;/li&gt; &lt;/ul&gt;</content>"
			+ "</entry>" + "</feed>";

	private String expectedEntry1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
			+ "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>"
			+ "<updated>2008-01-01T00:00:00.00-06:00</updated>"
			+ "<title>test entry 1</title>" + "</entry>";

	private String expectedFeed1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ " <title>Example Feed</title>"
			+ " <subtitle>A subtitle.</subtitle>"
			+ " <link href=\"http://example.org/feed/\" rel=\"self\"/>"
			+ " <link href=\"http://example.org/\"/>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ " <entry>" + "   <title>Atom-Powered Robots Run Amok</title>"
			+ "   <link href=\"http://example.org/2003/12/13/atom03\"/>"
			+ "   <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>"
			+ "   <updated>2003-12-13T18:30:02Z</updated>"
			+ "   <summary>Some text.</summary>" + " </entry>" + "</feed>";

	private Entry entry1, entry2, entry3;

	// found these examples here:
	// http://www.xml.com/pub/a/2005/12/07/handling-atom-text-and-content-constructs.html
	// that should apply to text constructs:
	// title, subtitle, summary, rights.
	private String title1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<title>One bold foot forward</title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";

	private String title2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<title type=\"text\">One bold foot forward</title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";

	// bad example here. the code actually unencodes the text.
	private String title3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<title>One &lt;strong&gt;bold&lt;/strong&gt; foot forward</title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";

	private String title4 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<title type=\"html\">One &lt;strong&gt;bold&lt;/strong&gt; foot forward</title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";

	/*
	 * currently of the 3 implementations tested: sjsxp stax woodstox None of
	 * them are able to detect CDATA sections. so the markup display ends up
	 * being escaped in the output without the cdata section.
	 */
	private String title5 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<title type=\"html\"><![CDATA[One <strong>bold</strong> foot forward]]></title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";

	private String title6 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
			+ "<title type=\"xhtml\"><div xmlns=\"http://www.w3.org/1999/xhtml\">One <strong>bold</strong> foot forward<title>can you see me</title></div></title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";

	private String title7 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\"  xmlns:xh=\"http://www.w3.org/1999/xhtml\">"
			+ "<title type=\"xhtml\"><xh:div>One <xh:strong>bold</xh:strong> foot forward </xh:div></title>"
			+ " <updated>2003-12-13T18:30:02Z</updated>" + " <author>"
			+ "   <name>John Doe</name>"
			+ "   <email>johndoe@example.com</email>" + " </author>"
			+ " <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>"
			+ "</feed>";
	
	//mising id
	private String brokenEntry1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
		//+ "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>"
		+ "<updated>2008-01-01T00:00:00.00-06:00</updated>"
		+ "<title>test entry 1</title>" + "</entry>";
	
	//missing title
	private String brokenEntry2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
		+ "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>"
		+ "<updated>2008-01-01T00:00:00.00-06:00</updated>";
		//+ "<title>test entry 1</title>" + "</entry>";
	
	//missing updated
	private String brokenEntry3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
		+ "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>"
		//+ "<updated>2008-01-01T00:00:00.00-06:00</updated>"
		+ "<title>test entry 1</title>" + "</entry>";
	
	//missing summary attribute for empty content element
	private String brokenEntry4 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
		+ "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>"
		+ "<updated>2008-01-01T00:00:00.00-06:00</updated>"
		+ "<title>test entry 1</title>" 
		+ "<content src=\"missingSummaryAttr\" />"
		+ "</entry>";
	
	//bad content content type
	private String brokenEntry5 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
		+ "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>"
		+ "<updated>2008-01-01T00:00:00.00-06:00</updated>"
		+ "<title>test entry 1</title>" 
		+ "<content type=\"noGood\">this is no good</content>"
		+ "</entry>";

	@Before
	public void setUp() throws Exception {
		try {
			Id id = FeedDoc.buildId(null,
					"http://www.colorfulsoftware.com/atom.xml");

			Updated updated = FeedDoc.buildUpdated(Calendar.getInstance()
					.getTime(), null);

			Title title = FeedDoc.buildTitle("test feed", null);

			Generator generator = FeedDoc.getAtomsphereVersion();

			List<Author> authors = new LinkedList<Author>();
			authors.add(FeedDoc.buildAuthor(FeedDoc.buildName("Bill Brown"),
					null, null, null, null));

			feed1 = FeedDoc.buildFeed(id, title, updated, null, authors, null,
					null, null, null, null, generator, null, null, null, null);

			entry1 = FeedDoc.buildEntry(FeedDoc.buildId(null,
					"http://www.colorfulsoftware.com/projects/atomsphere/"),
					FeedDoc.buildTitle("test entry 1", null), FeedDoc
							.buildUpdated(theDate.getTime(), null), null, null,
					null, null, null, null, null, null, null, null, null);

			entry2 = FeedDoc.buildEntry(FeedDoc.buildId(null,
					"http://www.colorfulsoftware.com/projects/atomsphere/"),
					FeedDoc.buildTitle("test entry 2", null), FeedDoc
							.buildUpdated(theDate.getTime(), null), null, null,
					null, null, null, null, null, null, null, null, null);

			entry3 = FeedDoc.buildEntry(FeedDoc.buildId(null,
					"http://www.colorfulsoftware.com/projects/atomsphere/"),
					FeedDoc.buildTitle("test entry 3", null), FeedDoc
							.buildUpdated(theDate.getTime(), null), null, null,
					null, null, null, null, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		// new File("out.xml").deleteOnExit();
		// new File("out2.xml").deleteOnExit();
	}

	@Test
	public void testWriteFeedDocOutputStreamFeedStringString() {
		try {
			feed1 = FeedDoc.readFeedToBean(new java.net.URL(
					"http://www.rand.org/news/press/index.xml"));
			FeedDoc.writeFeedDoc(new FileOutputStream("out1.xml"), feed1,
					FeedDoc.encoding, FeedDoc.xml_version);
			Feed feed2 = FeedDoc.readFeedToBean(new File("out1.xml"));
			assertNotNull(feed2);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof XMLStreamException);
		}
	}

	@Test
	public void testWriteFeedDocWriterFeedStringString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteFeedDocXMLStreamWriterFeedStringString() {
		try {
			// pretty print version.
			feed1 = FeedDoc.readFeedToBean(new java.net.URL(
					"http://www.rand.org/news/press/index.xml"));
			FeedDoc.writeFeedDoc(
					new javanet.staxutils.IndentingXMLStreamWriter(
							XMLOutputFactory.newInstance()
									.createXMLStreamWriter(
											new FileOutputStream("out2.xml"),
											FeedDoc.encoding)), feed1,
					FeedDoc.encoding, FeedDoc.xml_version);
			Feed feed2 = FeedDoc.readFeedToBean(new File("out2.xml"));
			assertNotNull(feed2);
		} catch (Exception e) {
			assertTrue(e instanceof XMLStreamException);
		}
	}

	@Test
	public void testReadFeedToStringInputStream() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToStringFile() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToStringURL() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToStringFeedString() {
		try {
			feed1 = FeedDoc.readFeedToBean(new java.net.URL(
					"http://www.rand.org/news/press/index.xml"));
			String feedStr = FeedDoc.readFeedToString(feed1,
					"com.sun.xml.txw2.output.IndentingXMLStreamWriter");
			assertNotNull(feedStr);
		} catch (Exception e) {
			assertTrue(e instanceof XMLStreamException);
		}

		try {
			feed1 = FeedDoc.readFeedToBean(new java.net.URL(
					"http://www.rand.org/news/press/index.xml"));
			String feedStr = FeedDoc.readFeedToString(feed1,
					"javanet.staxutils.IndentingXMLStreamWriter");
			assertNotNull(feedStr);
		} catch (Exception e) {
			assertTrue(e instanceof XMLStreamException);
		}

		try {
			feed1 = FeedDoc.readFeedToBean(new java.net.URL(
					"http://www.rand.org/news/press/index.xml"));
			String feedStr = FeedDoc.readFeedToString(feed1, "bunk");
			assertNotNull(feedStr);
		} catch (Exception e) {
			assertTrue(e instanceof XMLStreamException);
		}
	}

	@Test
	public void testReadFeedToStringFeed() {
		try {
			feed1 = FeedDoc.readFeedToBean(expectedFeed1);
			String feed1Str = FeedDoc.readFeedToString(feed1);
			assertNotNull(feed1Str);
			feed1 = FeedDoc.readFeedToBean(feed1Str);
			assertNotNull(feed1);
			assertNotNull(feed1.getId());
			assertNotNull(feed1.getTitle());
			assertNotNull(feed1.getUpdated());

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof XMLStreamException);
		}

		// test the seven title variants.
		try {

			feed1 = FeedDoc.readFeedToBean(title1);
			assertNotNull(feed1.getTitle());
			assertEquals(feed1.getTitle().getText(), "One bold foot forward");
			assertNotNull(FeedDoc.readFeedToString(feed1));

			feed1 = FeedDoc.readFeedToBean(title2);
			assertNotNull(feed1.getTitle());
			assertEquals(feed1.getTitle().getText(), "One bold foot forward");
			assertNotNull(FeedDoc.readFeedToString(feed1));

			feed1 = FeedDoc.readFeedToBean(title3);
			assertNotNull(feed1.getTitle());
			assertEquals(feed1.getTitle().getText(),
					"One <strong>bold</strong> foot forward");
			assertNotNull(FeedDoc.readFeedToString(feed1));

			feed1 = FeedDoc.readFeedToBean(title4);
			assertNotNull(feed1.getTitle());
			assertEquals(feed1.getTitle().getText(),
					"One &lt;strong&gt;bold&lt;/strong&gt; foot forward");
			assertNotNull(FeedDoc.readFeedToString(feed1));

			/*
			 * currently of the 3 implementations tested: sjsxp stax woodstox
			 * None of them are able to detect CDATA sections.
			 */
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory
					.createXMLStreamReader(new java.io.StringReader(title5));
			FeedReader.checkForCDATA(reader);

			feed1 = FeedDoc.readFeedToBean(title5);
			assertNotNull(feed1.getTitle());
			assertEquals(feed1.getTitle().getText(),
					"One &lt;strong&gt;bold&lt;/strong&gt; foot forward");
			assertNotNull(FeedDoc.readFeedToString(feed1));

			feed1 = FeedDoc.readFeedToBean(title6);
			assertNotNull(feed1.getTitle());
			System.out
					.println("feed title from raw\n"
							+ "One <strong>bold</strong> foot forward<title>can you see me</title>");
			assertEquals(feed1.getTitle().getText(),
					"One <strong>bold</strong> foot forward<title>can you see me</title>");
			assertNotNull(FeedDoc.readFeedToString(feed1));

			feed1 = FeedDoc.readFeedToBean(title7);
			assertNotNull(feed1.getTitle());
			assertEquals(feed1.getTitle().getText(),
					"One <xh:strong>bold</xh:strong> foot forward ");
			assertNotNull(FeedDoc.readFeedToString(feed1));

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof XMLStreamException);
		}
	}

	@Test
	public void testReadEntryToString() {
		try {
			String entryStr = FeedDoc.readEntryToString(entry1);
			assertTrue(entryStr != null);
			assertEquals(entryStr, expectedEntry1);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
		
		try {
			String entryStr = FeedDoc.readEntryToString(entry1);
			assertTrue(entryStr != null);
			assertEquals(entryStr, expectedEntry1);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
		
		try {
			String entryStr = FeedDoc.readEntryToString(entry1);
			assertTrue(entryStr != null);
			assertEquals(entryStr, expectedEntry1);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
		
		try {
			String entryStr = FeedDoc.readEntryToString(entry1);
			assertTrue(entryStr != null);
			assertEquals(entryStr, expectedEntry1);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
		
		try {
			String entryStr = FeedDoc.readEntryToString(entry1);
			assertTrue(entryStr != null);
			assertEquals(entryStr, expectedEntry1);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
		
		try {
			String entryStr = FeedDoc.readEntryToString(entry1);
			assertTrue(entryStr != null);
			assertEquals(entryStr, expectedEntry1);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
	}

	@Test
	public void testReadFeedToBeanString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToBeanFile() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToBeanURL() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToBeanInputStream() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildFeed() {
		try {

			Generator generator = FeedDoc.getAtomsphereVersion();

			Id id = FeedDoc.buildId(null,
					"http://www.colorfulsoftware.com/atom.xml");

			Updated updated = FeedDoc.buildUpdated(Calendar.getInstance()
					.getTime(), null);

			Title title = FeedDoc.buildTitle("test feed", null);

			List<Contributor> contributors = new LinkedList<Contributor>();
			Contributor contributor = FeedDoc.buildContributor(new Name(
					"Mad Dog"), null, FeedDoc.buildEmail("info@maddog.net"),
					null, null);
			contributors.add(contributor);

			Rights rights = FeedDoc.buildRights("GPL 1.0", null);

			Icon icon = FeedDoc.buildIcon(null, "http://host/images/icon.png");

			Logo logo = FeedDoc.buildLogo(null, "http://host/images/logo.png");

			List<Attribute> catAttrs = new LinkedList<Attribute>();
			catAttrs.add(FeedDoc.buildAttribute("term", "music"));
			catAttrs.add(FeedDoc.buildAttribute("scheme",
					"http://mtv.com/genere"));
			catAttrs.add(FeedDoc.buildAttribute("label", "music"));
			List<Category> categories = new LinkedList<Category>();
			Category category = FeedDoc.buildCategory(catAttrs, null);
			categories.add(category);

			List<Attribute> linkAttrs = new LinkedList<Attribute>();
			linkAttrs.add(FeedDoc
					.buildAttribute("href", "http://www.yahoo.com"));
			linkAttrs.add(FeedDoc.buildAttribute("rel", "self"));
			linkAttrs.add(FeedDoc.buildAttribute("hreflang", "en-US"));
			List<Link> links = new LinkedList<Link>();
			Link link = FeedDoc.buildLink(linkAttrs, null);
			links.add(link);

			Attribute extAttr = FeedDoc.buildAttribute("xmlns:xhtml",
					"http://www.w3.org/1999/xhtml");
			List<Attribute> extAttrs = new LinkedList<Attribute>();
			extAttrs.add(extAttr);

			// the base feed attributes.
			List<Attribute> feedAttrs = new LinkedList<Attribute>();
			feedAttrs.add(FeedDoc.atomBase);
			feedAttrs.add(FeedDoc.lang_en);
			feedAttrs.addAll(extAttrs);

			List<Extension> extensions = new LinkedList<Extension>();
			Extension extension = FeedDoc.buildExtension("xhtml:div", null,
					"<span style='color:red;'>hello there</span>");
			extensions.add(extension);

			List<Author> authors = new LinkedList<Author>();
			authors.add(FeedDoc.buildAuthor(FeedDoc.buildName("Bill Brown"),
					null, null, null, null));
			Entry entry = FeedDoc
					.buildEntry(
							FeedDoc
									.buildId(null,
											"http://www.colorfulsoftware.com/atom.xml#entry1"),
							FeedDoc.buildTitle("an example atom entry", null),
							FeedDoc.buildUpdated(Calendar.getInstance()
									.getTime(), null),
							null,
							FeedDoc
									.buildContent(
											"Hello World.  Welcome to the atomsphere feed builder for atom 1.0 builds.  I hope it is useful for you.",
											null), authors, null, null, null,
							null, null, null, null, null);

			SortedMap<String, Entry> entries = new TreeMap<String, Entry>();
			entries.put(entry.getUpdated().getText(), entry);

			Feed feed = FeedDoc.buildFeed(id, title, updated, rights, authors,
					categories, contributors, links, feedAttrs, extensions,
					generator, null, icon, logo, entries);

			assertNotNull(feed);

			// read and write a full feed.
			feed = FeedDoc.readFeedToBean(mega);
			FeedWriter feedWriter = new FeedWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(new FileOutputStream("dump1.xml"));
			feedWriter.writeFeed(writer, feed);
			writer.flush();
			writer.close();

			// re read the written feed and check the data.
			feed = FeedDoc.readFeedToBean(new File("dump1.xml"));

			assertNotNull(feed);
			assertNotNull(feed.getCategories());
			List<Category> cats = feed.getCategories();
			for (Category cat : cats) {
				assertNotNull(cat.getTerm());
				assertNotNull(cat.getLabel());
				assertNotNull(cat.getScheme());
			}

			List<Link> lnks = feed.getLinks();
			for (Link lnk : lnks) {
				assertNotNull(lnk.getHref());
				assertNotNull(lnk.getHreflang());
				assertNotNull(lnk.getTitle());
				assertNotNull(lnk.getType());
				assertNotNull(lnk.getRel());
			}

			new File("dump1.xml").deleteOnExit();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue building feed doc.");
		}
	}

	@Test
	public void testBuildAttribute() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildAuthor() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildCategory() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildContent() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildContributor() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildEmail() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildEntry() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildExtension() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildGenerator() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildIcon() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildId() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildLink() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildLogo() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildName() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildPublished() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildRights() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildSource() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildSubtitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildSummary() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildTitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildUpdated() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildURI() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildAtomPersonConstruct() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetAttributeFromGroup() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetContentType() {
		List<Attribute> attrs = new LinkedList<Attribute>();
		attrs.add(FeedDoc.buildAttribute("src",
				"http://www.colorfulsoftware.com/images/logo.gif"));
		attrs.add(FeedDoc.buildAttribute("type", "image/gif"));
		assertEquals(FeedDoc.getContentType(attrs), ContentType.EXTERNAL);
		attrs = new LinkedList<Attribute>();
		attrs.add(FeedDoc.buildAttribute("type", "image/gif"));
		assertEquals(FeedDoc.getContentType(attrs), ContentType.OTHER);
		attrs = new LinkedList<Attribute>();
		attrs.add(FeedDoc.buildAttribute("type", "text"));
		assertEquals(FeedDoc.getContentType(attrs), ContentType.TEXT);
		attrs = new LinkedList<Attribute>();
		attrs.add(FeedDoc.buildAttribute("type", "html"));
		assertEquals(FeedDoc.getContentType(attrs), ContentType.HTML);
		attrs = new LinkedList<Attribute>();
		attrs.add(FeedDoc.buildAttribute("type", "xhtml"));
		assertEquals(FeedDoc.getContentType(attrs), ContentType.XHTML);
	}

	@Test
	public void testSortEntries() {
		try {
			SortedMap<String, Entry> entries = new TreeMap<String, Entry>();
			String entryStr1 = entry1.getTitle().getText();
			String entryStr2 = entry2.getTitle().getText();
			String entryStr3 = entry3.getTitle().getText();
			entries.put(entryStr1, entry1);
			entries.put(entryStr2, entry2);
			entries.put(entryStr3, entry3);
			feed1 = FeedDoc.buildFeed(feed1.getId(), feed1.getTitle(), feed1
					.getUpdated(), feed1.getRights(), feed1.getAuthors(), feed1
					.getCategories(), feed1.getContributors(),
					feed1.getLinks(), feed1.getAttributes(), feed1
							.getExtensions(), feed1.getGenerator(), feed1
							.getSubtitle(), feed1.getIcon(), feed1.getLogo(),
					entries);
			assertEquals(Title.class.getSimpleName(), "Title");
			feed1 = FeedDoc.sortEntries(feed1, FeedDoc.SORT_ASC, Title.class);
			for (Entry entry : feed1.getEntries().values()) {
				assertNotNull(entry);
			}
			SortedMap<String, Entry> entries2 = feed1.getEntries();
			assertEquals(entries2.firstKey(), entryStr1);
			entries2.remove(entries2.firstKey());
			assertEquals(entries2.firstKey(), entryStr2);
			entries2.remove(entries2.firstKey());
			assertEquals(entries2.firstKey(), entryStr3);

			feed1 = FeedDoc.sortEntries(feed1, FeedDoc.SORT_DESC, Title.class);
			for (Entry entry : feed1.getEntries().values()) {
				assertNotNull(entry);
			}
			entries2 = feed1.getEntries();
			assertEquals(entries2.firstKey(), entryStr3);
			entries2.remove(entries2.firstKey());
			assertEquals(entries2.firstKey(), entryStr2);
			entries2.remove(entries2.firstKey());
			assertEquals(entries2.firstKey(), entryStr1);

			// test the null entries case.
			feed1 = FeedDoc.buildFeed(feed1.getId(), feed1.getTitle(), feed1
					.getUpdated(), feed1.getRights(), feed1.getAuthors(), feed1
					.getCategories(), feed1.getContributors(),
					feed1.getLinks(), feed1.getAttributes(), feed1
							.getExtensions(), feed1.getGenerator(), feed1
							.getSubtitle(), feed1.getIcon(), feed1.getLogo(),
					null);
			feed1 = FeedDoc.sortEntries(feed1, FeedDoc.SORT_DESC, Title.class);
			assertNull(feed1.getEntries());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckForAndApplyExtension() {
		// fail("Not yet implemented");
	}

	@Test
	public void testMain() {
		// fail("Not yet implemented");
	}

}
