/**
 * Copyright 2011 Bill Brown
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
package com.colorfulsoftware.atom;

import static org.junit.Assert.*;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorfulsoftware.atom.Content;
import com.colorfulsoftware.atom.Entry;
import com.colorfulsoftware.atom.Feed;
import com.colorfulsoftware.atom.FeedReader;
import com.colorfulsoftware.atom.FeedWriter;
import com.colorfulsoftware.atom.Source;
import com.colorfulsoftware.atom.Summary;
import com.colorfulsoftware.atom.Title;

/**
 * This class tests the FeedReader.
 * 
 * @author Bill Brown
 * 
 */
public class FeedReaderTest implements Serializable {

	private static final long serialVersionUID = -3640838936391729081L;
	private FeedReader feedReader;
	private XMLStreamReader reader, reader2, reader3, reader4;
	private Map<String, String> configFile;

	private String title1 = "<title type=\"xhtml\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">"
			+ "<xhtml:div>"
			+ "Less: <xhtml:em> &lt; and &amp; </xhtml:em>"
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
			+ "  <author><what:now myAttr=\"valuable\" /><name>The Minority Directory</name></author>"
			+ "  <link href=\"http://www.minoritydirectory.net/latest.xml\" rel=\"self\" />"
			+ "  <icon>http://www.minoritydirectory.net/images/favicon.ico</icon>"
			+ "  <logo>http://www.minoritydirectory.net/images/logo.gif</logo>"
			+ "  <sort:desc type=\"updated\" />"
			+ "  <sort:fake>with a fake <sort:embedded id=\"unique\" /> nested extension element and nested<sort:level1>nested <sort:level2>really embedded</sort:level2> sort extension stuff</sort:level1></sort:fake>"
			+ "  <entry>"
			+ "    <id>http://www.laptopsfast.com</id>"
			+ "    <updated>2009-04-13T09:21:26.00-06:00</updated>"
			+ "    <what:now myAttr=\"valuable\" />"
			+ "    <title type=\"xhtml\">"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"&quot;http://www.laptopsfast.com&quot;\" onclick=\"&quot;logStatistic(this.href);&quot;\">Laptops Fast</a></div>"
			+ "				</title>"
			+ "    <author><name>Laptops Fast</name><what:now myAttr=\"valuable\" /></author>"
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
			+ "    <contributor><name>Someone</name><what:now myAttr=\"valuable\" /></contributor>"
			+ "    <link href=\"http://www.brookscleaningcompany.com\" rel=\"alternate\" />"
			+ "    <summary>Brooks Cleaning Co. &#xd; Salem, MI.&#xd; For Business &amp; Industry, Est. 2000</summary>"
			+ "    <content type=\"image/jpeg\" src=\"http://www.minoritydirectory.net/loadImage?img=c8eaeee7-753c-4a80-a6b3-aeaf137590d2\" />"
			+ "  </entry></feed>";

	private String extension2 = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<feed xmlns:sort=\"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0\" xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">"
			+ "  <id>http://www.minoritydirectory.net/latest.xml</id>"
			+ "  <updated>2009-05-13T00:16:00.47-06:00</updated>"
			+ "  <generator uri=\"http://www.colorfulsoftware.com/projects/atomsphere/\" version=\"2.0.2.1\">Atomsphere</generator>"
			+ "  <title>Latest Updates...</title>"
			+ "  <author><what:now myAttr=\"valuable\" /><name>The Minority Directory</name></author>"
			+ "  <link href=\"http://www.minoritydirectory.net/latest.xml\" rel=\"self\" />"
			+ "  <icon>http://www.minoritydirectory.net/images/favicon.ico</icon>"
			+ "  <logo>http://www.minoritydirectory.net/images/logo.gif</logo>"
			+ "  <sort:desc type=\"updated\" />"
			+ "  <sort:fake>with a fake <sort:embedded id=\"unique\" /> nested extension element and nested<sort:level1>nested <sort:level2>really embedded</sort:level2> sort extension stuff</sort:level1></sort:fake>"
			+ "  <entry>"
			+ "    <id>http://www.laptopsfast.com</id>"
			+ "    <updated>2009-04-13T09:21:26.00-06:00</updated>"
			+ "    <green:now myAttr=\"valuable\" />"
			+ "    <title type=\"xhtml\">"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"&quot;http://www.laptopsfast.com&quot;\" onclick=\"&quot;logStatistic(this.href);&quot;\">Laptops Fast</a></div>"
			+ "				</title>"
			+ "    <author><name>Laptops Fast</name><green:now myAttr=\"valuable\" /></author>"
			+ "    <link href=\"http://www.laptopsfast.com\" rel=\"alternate\" />"
			+ "   <summary>Laptops and Accessories Retailer</summary>"
			+ "    <content type=\"image/jpeg\" src=\"http://www.minoritydirectory.net/loadImage?img=b8fabc9f-da35-4e5c-ba56-3e7de2a3dca1\" />  </entry>"
			+ "  <entry>"
			+ "    <id>http://www.brookscleaningcompany.com</id>"
			+ "    <updated>2009-03-24T08:04:50.00-06:00</updated>"
			+ "    <green:up>hello there</green:up>"
			+ "	<title type=\"xhtml\">"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"&quot;http://www.brookscleaningcompany.com&quot;\" onclick=\"&quot;logStatistic(this.href);&quot;\">Brooks Cleaning Company</a></div>"
			+ "</title>"
			+ "    <author><name>Brooks Cleaning Company</name></author>"
			+ "    <contributor><name>Someone</name><green:now myAttr=\"valuable\" /></contributor>"
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
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"><a href=\"http://www.laptopsfast.com\" onclick=\"logStatistic(this.href);\">Laptops Fast</a></div>"
			+ "				</title>"
			+ "    <author><name>Laptops Fast</name></author>"
			+ "    <link href=\"http://www.laptopsfast.com\" rel=\"alternate\" />"
			+ "</source>" + "  </entry></feed>";

	String rights1 = "<rights type=\"xhtml\"><div xmlns=\"http://www.w3.org/1999/xhtml\">A marked up <br /> rights.This is <span style=\"color:blue;\">blue text :). <hr id=\"unique\" class=\"phat\" /> <a href=\"http://maps.google.com?q=something&amp;b=somethingElse\">a fake map link</a></span>. </div></rights>";

	/**
	 * @throws Exception
	 *             if there is an error creating the test data.
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		feedReader = new FeedReader(new FeedDoc());
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
		reader4 = XMLInputFactory.newInstance().createXMLStreamReader(
				new URL("http://www.atomenabled.org/atom.xml").openStream());
	}

	/**
	 * @throws Exception
	 *             if there is an error cleaning up the test data.
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * test the feed reading functionality.
	 */
	@Test
	public void testReadFeed() {

		try {
			Feed feed = feedReader.readFeed(reader);
			assertTrue(feed.getEntries().size() == 4);

			feed = feedReader.readFeed(reader2);
			assertTrue(feed.getEntries().size() == 4);

			feed = feedReader.readFeed(reader3);
			assertTrue(feed.getEntries().size() == 4);

			feed = feedReader.readFeed(reader4);
			assertNotNull(feed);
			
			String atomStr = "file:///C:/Users/billbrown/workspace/feedbenchmarks/atomenabled.atom";
			feed = new FeedDoc().readFeedToBean(new URL(atomStr));
			assertNotNull(feed);
			System.out.println("feed is not null"+feed);
		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read fead.");
		}
	}

	/**
	 * test the extension reading functionality.
	 */
	@Test
	public void testReadExtension() {

		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(extension1));
			Feed feed = feedReader.readFeed(reader);
			assertNotNull(feed.getExtensions());
			assertNotNull(feed.getEntries());
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"target/dump2.xml"));
			out.write(feed.toString());
			out.flush();
			out.close();
			for (Entry entry : feed.getEntries()) {
				assertNotNull(entry.getExtensions());
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read fead.");
		}

		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(extension2));
			feedReader.readFeed(reader);
			fail("should not get here.");
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof XMLStreamException);
			assertTrue(e.getMessage().indexOf("ElementPrefixUnbound") != -1);
		}

	}

	/**
	 * test the summary reading functionality.
	 */
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
			List<Entry> entries = feedReader.readEntry(reader, null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			Entry entry = entries.iterator().next();
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

	/**
	 * test the source reading functionality.
	 */
	@Test
	public void testReadSource() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(source1));
			Feed feed = feedReader.readFeed(reader);

			assertNotNull(feed.getEntries());

			for (Entry entry : feed.getEntries()) {
				assertNotNull(entry.getSource());
				Source source = entry.getSource();
				assertNotNull(source.getId());
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(
					"target/source1.xml"));
			out.write(feed.toString());
			out.flush();
			out.close();

			// make sure it can also be written afterwards.
			FeedWriter feedWriter = new FeedWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(
							new FileOutputStream("target/dump1.xml"));
			feedWriter.writeFeed(writer, feed);

		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read fead.");
		}
	}

	/**
	 * 
	 */
	@Test
	public void testReadRights() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(rights1));
			Rights rights = feedReader.readRights(reader);
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"target/rights.xml"));
			out.write(rights.toString());
			out.flush();
			out.close();

			FeedWriter feedWriter = new FeedWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(
							new FileOutputStream("target/rights2.xml"));
			feedWriter.writeRights(writer, rights);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("could not read and write feed.");
		}
	}

	/**
	 * test the content reading functionality.
	 */
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
			List<Entry> entries = feedReader.readEntry(reader, null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);

			Entry entry = entries.iterator().next();
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

	/**
	 * test the title reading functionality.
	 */
	@Test
	public void testReadTitle() {
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new StringReader(title1));
			Title title = feedReader.readTitle(reader);
			assertTrue(title.getText().equals(
					"Less: <xhtml:em> &lt; and &amp; </xhtml:em>"));
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
}
