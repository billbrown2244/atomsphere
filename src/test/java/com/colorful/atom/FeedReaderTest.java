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

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.SortedMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorful.atom.Content;
import com.colorful.atom.Entry;
import com.colorful.atom.Feed;
import com.colorful.atom.FeedDoc;
import com.colorful.atom.FeedReader;
import com.colorful.atom.Summary;
import com.colorful.atom.Title;

public class FeedReaderTest {

	private FeedReader feedReader;
	private XMLStreamReader reader, reader2, reader3;
	private Map<String, String> configFile;
	private String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">  "
			+ "<id>http://colorfulsoftware.localhost/projects/atomsphere/atom.xml</id>  "
			+ "<updated>2007-03-08T20:52:40.70-06:00</updated>  "
			+ "<generator uri=\"http://www.colorfulsoftware.com/projects/atomsphere\" version=\"1.0.20\">Atomsphere</generator>  "
			+ "<title>Atomsphere</title>  <subtitle>a java atom feed library</subtitle>  "
			+ "<author>    <name>Bill Brown</name>    <uri>http://www.colorfulsoftware.com</uri>    <email>info@colorfulsoftware.com</email>  </author>  "
			+ "<link href=\"http://www.colorfulsoftware.com/projects/atomsphere/atom.xml\" rel=\"self\" type=\"application/atom+xml\"/>  "
			+ "<rights>Copyright 2007</rights>  "
			+ "<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#About</id>    "
			+ "<updated>2007-03-02T13:00:00.699-06:00</updated>    <title>About</title>    <published>2007-02-26T12:34:01.330-06:00</published>    "
			+ "<summary>About the project</summary>    "
			+ "<content type=\"html\">&lt;ul&gt;         &lt;li&gt;&lt;span class=\"boldText\"&gt;Atomsphere&lt;/span&gt; isa java library that allows you to create and modify atom 1.0 feeds.&lt;/li&gt;          &lt;li&gt;It is distributed under the GNU GPL license and can be used in any manner complient with the license.&lt;/li&gt;          &lt;li&gt;It is also packaged as a servlet-lib for use in web applications.&lt;/li&gt;            &lt;li&gt;It is also packaged as a customtag library to display feeds on a webapage.&lt;/li&gt;          &lt;li&gt;It also comes with an example webapp which demonstrates some example uses of the library.&lt;/li&gt;        &lt;li&gt;It is written to be tied as closely as possible to the current atom specification found &lt;a href=\"http://www.atomenabled.org/developers/syndication/atom-format-spec.php\"&gt;here&lt;/a&gt;.&lt;/li&gt;                 &lt;/ul&gt;</content>  </entry>  "
			+ "<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
			+ "<summary>Requirements for using the libraries</summary>    "
			+ "<content type=\"html\">&lt;br /&gt;the project is usable with jdk 1.4.2 and above&lt;br /&gt;                                    &amp;nbsp;&lt;br /&gt;            needed for using the library&lt;br /&gt;                &lt;ul&gt;                &lt;li&gt;&lt;a href=\"https://sjsxp.dev.java.net/\"&gt;jsr173&lt;/a&gt; (STAX api jar) - see the &lt;a href=\"http://jcp.org/aboutJava/communityprocess/final/jsr173/index.html\"&gt;API&lt;/a&gt;.&lt;/li&gt;         &lt;li&gt;&lt;a href=\"https://sjsxp.dev.java.net/\"&gt;sjsxp&lt;/a&gt; (STAX implementation) - others implementations may work but have not been tested.&lt;/li&gt;          &lt;li&gt;&lt;a href=\"https://stax-utils.dev.java.net/\"&gt;stax-utils&lt;/a&gt; (for pretty printing)&lt;/li&gt;          &lt;/ul&gt;        needed for using the atomsphere-taglib&lt;br /&gt;              &lt;ul&gt;        &lt;li&gt;the atomsphere library&lt;/li&gt;                             &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;           &lt;/ul&gt;             needed for using the atomsphere-weblib&lt;br /&gt;                &lt;ul&gt;              &lt;li&gt;the atomsphere library&lt;/li&gt;                       &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;           &lt;/ul&gt;             needed for using the example atomsphere-webapp&lt;br /&gt;                &lt;ul&gt;              &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;&lt;/ul&gt;</content>  </entry>  "
			+ "<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Documentation</id>    "
			+ "<updated>2007-03-02T12:59:45.475-06:00</updated>    <title>Documentation</title>    <published>2007-02-26T13:00:00.478-06:00</published>    "
			+ "<summary>Starting Documentation</summary>    "
			+ "<content type=\"html\">&lt;h4&gt;Installation (atomsphere library)&lt;/h4&gt;                 &lt;ul&gt;                &lt;li&gt;Add the jsr173, sjsxp, stax-utils and atomsphere jars to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;                    &lt;/ul&gt;                &lt;h4&gt;Installation (atomsphere-taglib)&lt;/h4&gt;                   &lt;ul&gt;                        &lt;li&gt;Add the atomsphere jar to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;&lt;li&gt;Add the atomsphereTags.tld tag descriptor to the top of the jsp page (See example below). &lt;/li&gt;                      &lt;li&gt;Add anyrequired attributes and optional attributes to the custom tag (See example below).&lt;/li&gt;                     &lt;li&gt;View the atomsphereTags.tld for a description of the attributes and what they do.&lt;/li&gt;&lt;/ul&gt;                &lt;h4&gt;Installation (atomsphere-weblib)&lt;/h4&gt;                   &lt;ul&gt;&lt;li&gt;Add the atomsphere and atomsphere-weblib jars to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;&lt;li&gt;Copy the web-fragrment.xml (embeded in the jar file) to your application's web.xml file.&lt;/li&gt;&lt;/ul&gt;                               &lt;h4&gt;Installation (atomsphere-webapp)&lt;/h4&gt;                    &lt;ul&gt;       &lt;li&gt;Deploy the war file to any J2EE servlet container.&lt;/li&gt;                   &lt;/ul&gt;</content>  </entry>  "
			+ "<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Examples</id>    "
			+ "<updated>2007-03-02T12:59:10.517-06:00</updated>    <title>Examples</title>    <published>2007-02-26T13:01:43.57-06:00</published>    "
			+ "<summary>Basic examples</summary>    <content type=\"html\">&lt;br /&gt;Read an atom xml file into a Feed bean&lt;br /&gt;              &lt;code&gt;                    Feed feed = FeedDoc.readFeedToBean(new File(fullPath));                   &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Read an atom URL into a Feed bean&lt;br /&gt;             &lt;code&gt;                    Feed feed = FeedDoc.readFeedToBean(new URL(\"http://www.colorfulsoftware.com/atom.xml\"));                  &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Read an atom xml file into an xml atom string&lt;br /&gt;                 &lt;code&gt;                    String atomXML = FeedDoc.readFeedToString(new File(fullPath));                    &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Write a feed bean into an xml file&lt;br /&gt;                    &lt;code&gt;                    FeedDoc.writeFeedDoc(fullPath,feed,FeedDoc.encoding,FeedDoc.xml_version);                 &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Add the custom feed tag to a jsp page.&lt;br /&gt;                                &lt;code&gt;  &amp;lt;%@taglib uri=\"/WEB-INF/atomsphereTags.tld\" prefix=\"atom\" %&amp;gt;&lt;/code&gt;&lt;br /&gt;...&lt;br /&gt;&lt;code&gt;&amp;lt;atom:atomFeed &lt;/code&gt;&lt;br /&gt;&lt;code&gt;     clazz=\"myFeed\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;    url=\"http://www.colorfulsoftware.com/atom.xml\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;feedSummary=\"true\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;entryUpdated=\"true\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;entryUpdatedFormat=\"yyyy-MM-dd\" /&amp;gt;&lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;</content> </entry></feed>";

	private String title1 = "<title type=\"xhtml\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">"
			+ "<xhtml:div>"
			+ "Less: <xhtml:em> &lt; </xhtml:em>"
			+ "</xhtml:div>" + "</title>";

	private String title1HTML = "<title type=\"html\">"
			+ "Less: &lt;b&gt;Bold Text&lt;/b&gt;  &lt;hr /&gt;</title>";

	private String summary1 = "<summary type=\"xhtml\">"
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "This is <b>XHTML</b> content." + "</div>" + "</summary>";

	private String summary2 = "<summary type=\"xhtml\">"
			+ "<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">"
			+ "This is <xhtml:b>XHTML</xhtml:b> content." + "</xhtml:div>"
			+ "</summary>";

	private String summary3 = "<entry xmlns:xh=\"http://some.xh.specific.uri/xh\">    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
			// prefix is bound at entry
			+ "<summary type=\"xhtml\">"
			+ "<xh:div>"
			+ "This is <xh:b>XHTML</xh:b> content."
			+ "</xh:div>"
			+ "</summary>" + "</entry>";

	private String content1 = "<content type=\"xhtml\">"
			+ "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "This is <b>XHTML</b> content." + "</div>" + "</content>";

	private String content2 = "<content type=\"xhtml\">"
			+ "<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">"
			+ "This is <xhtml:b>XHTML</xhtml:b> content." + "</xhtml:div>"
			+ "</content>";

	private String content3 = "<entry xmlns:xh=\"http://some.xh.specific.uri/xh\">    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
			+ "<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
			// prefix is bound at entry
			+ "<content type=\"xhtml\">"
			+ "<xh:div>"
			+ "This is <xh:b>XHTML</xh:b> content."
			+ "</xh:div>"
			+ "</content>" + "</entry>";

	// a test for extension elements.
	private String extension1 = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<feed xmlns:what=\"http://abc.def.ghi.com\" xmlns:sort=\"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0\" xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
			+ "  <id>http://www.minoritydirectory.net/latest.xml</id>"
			+ "  <updated>2009-05-13T00:16:00.47-06:00</updated>"
			+ "  <generator uri=\"http://www.colorfulsoftware.com/projects/atomsphere/\" version=\"2.0.2.1\">Atomsphere</generator>"
			+ "  <title>Latest Updates...</title>"
			+ "  <author><name>The Minority Directory</name></author>"
			+ "  <link href=\"http://www.minoritydirectory.net/latest.xml\" rel=\"self\" />"
			+ "  <icon>http://www.minoritydirectory.net/images/favicon.ico</icon>"
			+ "  <logo>http://www.minoritydirectory.net/images/logo.gif</logo>"
			+ "  <sort:desc type=\"updated\" />"
			+ "  <entry>"
			+ "    <id>http://www.laptopsfast.com</id>"
			+ "    <updated>2009-04-13T09:21:26.00-06:00</updated>"
			+ "    <what:now myAttr=\"valuable\" />"
			+ "    <title type=\"xhtml\">"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"&quot;http://www.laptopsfast.com&quot;\" onclick=\"&quot;logStatistic(this.href);&quot;\">Laptops Fast</a></div>"
			+ "				</title>"
			+ "    <author><name>Laptops Fast</name></author>"
			+ "    <link href=\"http://www.laptopsfast.com\" rel=\"alternate\" />"
			+ "   <summary>Laptops and Accessories Retailer</summary>"
			+ "    <content type=\"image/jpeg\" src=\"http://www.minoritydirectory.net/loadImage?img=b8fabc9f-da35-4e5c-ba56-3e7de2a3dca1\" />  </entry>"
			+ "  <entry>"
			+ "    <id>http://www.brookscleaningcompany.com</id>"
			+ "    <updated>2009-03-24T08:04:50.00-06:00</updated>"
			+ "    <what:up>hello there</what:up>"
			+ "	<title type=\"xhtml\">"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"&quot;http://www.brookscleaningcompany.com&quot;\" onclick=\"&quot;logStatistic(this.href);&quot;\">Brooks Cleaning Company</a></div>"
			+ "</title>"
			+ "    <author><name>Brooks Cleaning Company</name></author>"
			+ "    <link href=\"http://www.brookscleaningcompany.com\" rel=\"alternate\" />"
			+ "    <summary>Brooks Cleaning Co. &#xd; Salem, MI.&#xd; For Business &amp; Industry, Est. 2000</summary>"
			+ "    <content type=\"image/jpeg\" src=\"http://www.minoritydirectory.net/loadImage?img=c8eaeee7-753c-4a80-a6b3-aeaf137590d2\" />"
			+ "  </entry></feed>";

	private String source1 = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<feed xmlns:what=\"http://abc.def.ghi.com\" xmlns:sort=\"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0\" xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
			+ "  <id>http://www.minoritydirectory.net/latest.xml</id>"
			+ "  <updated>2009-05-13T00:16:00.47-06:00</updated>"
			+ "  <generator uri=\"http://www.colorfulsoftware.com/projects/atomsphere/\" version=\"2.0.2.1\">Atomsphere</generator>"
			+ "  <title>Latest Updates...</title>"
			+ "  <author><name>The Minority Directory</name></author>"
			+ "  <link href=\"http://www.minoritydirectory.net/latest.xml\" rel=\"self\" />"
			+ "  <icon>http://www.minoritydirectory.net/images/favicon.ico</icon>"
			+ "  <logo>http://www.minoritydirectory.net/images/logo.gif</logo>"
			+ "  <sort:desc type=\"updated\" />"
			+ "  <entry>"
			+ "    <title type=\"xhtml\"><div>test title</div></title>"
			+ "<id>http://www.laptopsfast.com</id>"
			+ "    <updated>2009-04-13T09:21:26.00-06:00</updated>"
			+ "<source>"
			+ "    <id>http://www.laptopsfaster.com</id>"
			+ "    <updated>2009-04-13T09:21:26.00-06:00</updated>"
			+ "    <what:now myAttr=\"valuable\" />"
			+ "    <title type=\"xhtml\">"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"&quot;http://www.laptopsfast.com&quot;\" onclick=\"&quot;logStatistic(this.href);&quot;\">Laptops Fast</a></div>"
			+ "				</title>"
			+ "    <author><name>Laptops Fast</name></author>"
			+ "    <link href=\"http://www.laptopsfast.com\" rel=\"alternate\" />"
			+ "   <summary>Laptops and Accessories Retailer</summary>"
			+ "</source>" + "  </entry></feed>";

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		feedReader = new FeedReader();
		XMLDecoder decode = new XMLDecoder(new BufferedInputStream(
				new FileInputStream("src/test/resources/atomConfig.xml")));
		configFile = (Map<String, String>) decode.readObject();
		decode.close();
		for (String feedName : configFile.values()) {
			// there should only be 1 element.
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(feedName));
		}
		reader2 = XMLInputFactory.newInstance().createXMLStreamReader(
				new FileInputStream("src/test/resources/flat.xml"));
		reader3 = XMLInputFactory.newInstance().createXMLStreamReader(
				new FileInputStream("src/test/resources/dump.xml"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFeed() {

		try {
			Feed feed = feedReader.readFeed(reader);
			assertTrue(feed.getEntries().size() == 4);

			feed = feedReader.readFeed(reader2);
			assertTrue(feed.getEntries().size() == 4);

			feed = feedReader.readFeed(reader3);
			assertTrue(feed.getEntries().size() == 4);

			feed = FeedDoc.readFeedToBean(xmlString);
			assertTrue(feed.getEntries().size() == 4);

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read fead.");
		}
	}

	@Test
	public void testGetAttributes() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadExtension() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(extension1));
			Feed feed = feedReader.readFeed(reader);
			assertNotNull(feed.getExtensions());
			for (Entry entry : feed.getEntries().values()) {
				assertNotNull(entry.getExtensions());
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read fead.");
		}
	}

	@Test
	public void testReadEntry() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadSummary() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(summary1));
			Summary summary = feedReader.readSummary(reader);
			assertTrue(summary.getText()
					.equals("This is <b>XHTML</b> content."));
			assertTrue(summary.getAttributes() != null);
			assertTrue(summary.getAttributes().size() == 1);
			assertTrue(summary.getAttributes().get(0).getName().equals("type"));
			assertTrue(summary.getAttributes().get(0).getValue()
					.equals("xhtml"));

			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(summary2));
			summary = feedReader.readSummary(reader);
			assertTrue(summary.getText().equals(
					"This is <xhtml:b>XHTML</xhtml:b> content."));
			assertTrue(summary.getAttributes() != null);
			assertTrue(summary.getAttributes().size() == 1);
			assertTrue(summary.getAttributes().get(0).getName().equals("type"));
			assertTrue(summary.getAttributes().get(0).getValue()
					.equals("xhtml"));

			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(summary3));
			SortedMap<String, Entry> entries = feedReader.readEntry(reader,
					null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			Entry entry = entries.values().iterator().next();
			assertTrue(entry.getAttributes() != null);
			assertTrue(entry.getAttributes().size() == 1);
			assertTrue(entry.getAttributes().get(0).getName()
					.equals("xmlns:xh"));
			assertTrue(entry.getAttributes().get(0).getValue().equals(
					"http://some.xh.specific.uri/xh"));

			summary = entry.getSummary();
			assertTrue(summary != null);
			assertTrue(summary.getText().equals(
					"This is <xh:b>XHTML</xh:b> content."));
			assertTrue(summary.getAttributes() != null);
			assertTrue(summary.getAttributes().size() == 1);
			assertTrue(summary.getAttributes().get(0).getName().equals("type"));
			assertTrue(summary.getAttributes().get(0).getValue()
					.equals("xhtml"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read title fragment.");
		}
	}

	@Test
	public void testContainsXHTML() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadSource() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(source1));
			Feed feed = feedReader.readFeed(reader);
			assertNotNull(feed.getEntries());
			for (Entry entry : feed.getEntries().values()) {
				assertNotNull(entry.getSource());
				Source source = entry.getSource();
				assertNotNull(source.getId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read fead.");
		}
	}

	@Test
	public void testGetSimpleDateFormat() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadPublished() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadContent() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(content1));
			Content content = feedReader.readContent(reader);
			assertTrue(content.getContent().equals(
					"This is <b>XHTML</b> content."));
			assertTrue(content.getAttributes() != null);
			assertTrue(content.getAttributes().size() == 1);
			assertTrue(content.getAttributes().get(0).getName().equals("type"));
			assertTrue(content.getAttributes().get(0).getValue()
					.equals("xhtml"));

			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(content2));
			content = feedReader.readContent(reader);
			assertTrue(content.getContent().equals(
					"This is <xhtml:b>XHTML</xhtml:b> content."));
			assertTrue(content.getAttributes() != null);
			assertTrue(content.getAttributes().size() == 1);
			assertTrue(content.getAttributes().get(0).getName().equals("type"));
			assertTrue(content.getAttributes().get(0).getValue()
					.equals("xhtml"));

			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(content3));
			SortedMap<String, Entry> entries = feedReader.readEntry(reader,
					null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			Entry entry = entries.values().iterator().next();
			assertTrue(entry.getAttributes() != null);
			assertTrue(entry.getAttributes().size() == 1);
			assertTrue(entry.getAttributes().get(0).getName()
					.equals("xmlns:xh"));
			assertTrue(entry.getAttributes().get(0).getValue().equals(
					"http://some.xh.specific.uri/xh"));

			content = entry.getContent();
			assertTrue(content != null);
			assertTrue(content.getContent().equals(
					"This is <xh:b>XHTML</xh:b> content."));
			assertTrue(content.getAttributes() != null);
			assertTrue(content.getAttributes().size() == 1);
			assertTrue(content.getAttributes().get(0).getName().equals("type"));
			assertTrue(content.getAttributes().get(0).getValue()
					.equals("xhtml"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read title fragment.");
		}
	}

	@Test
	public void testReadUpdated() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadTitle() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(title1));
			Title title = feedReader.readTitle(reader);
			assertTrue(title.getText().equals("Less: <xhtml:em> &lt; </xhtml:em>"));
			assertTrue(title.getAttributes() != null);
			assertTrue(title.getAttributes().size() == 2);
			assertTrue(title.getAttributes().get(1).getName().equals("type"));
			assertTrue(title.getAttributes().get(1).getValue().equals("xhtml"));
			assertTrue(title.getAttributes().get(0).getName().equals(
					"xmlns:xhtml"));
			assertTrue(title.getAttributes().get(0).getValue().equals(
					"http://www.w3.org/1999/xhtml"));

			// test reading html
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(title1HTML));
			title = feedReader.readTitle(reader);
			System.out.println("title text: " + title.getText());
			System.out.println("test text: "+"Less: &lt;b&gt;Bold Text&lt;/b&gt;  &lt;hr /&gt;");
			assertEquals(title.getText(),
					"Less: &lt;b&gt;Bold Text&lt;/b&gt;  &lt;hr /&gt;");
			assertTrue(title.getAttributes() != null);
			assertTrue(title.getAttributes().size() == 1);
			assertTrue(title.getAttributes().get(0).getName().equals("type"));
			assertTrue(title.getAttributes().get(0).getValue().equals("html"));

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read title fragment.");
		}
	}

	@Test
	public void testReadXHTML() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadSubtitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadRights() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadLogo() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadLink() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadId() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadIcon() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadGenerator() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadContributor() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadCategory() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadAuthor() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadAtomPersonConstruct() {
		// fail("Not yet implemented");
	}

}
