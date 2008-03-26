package com.colorful.atom.beans;

import static org.junit.Assert.*;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FeedReaderTest {

	private FeedReader feedReader;
	private XMLStreamReader reader,reader2,reader3;
	private Map<String,String> configFile;
	private String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
		+"<feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">  "
		+"<id>http://colorfulsoftware.localhost/projects/atomsphere/atom.xml</id>  "
		+"<updated>2007-03-08T20:52:40.70-06:00</updated>  "
		+"<generator uri=\"http://www.colorfulsoftware.com/projects/atomsphere\" version=\"1.0.20\">Atomsphere</generator>  "
		+"<title>Atomsphere</title>  <subtitle>a java atom feed library</subtitle>  "
		+"<author>    <name>Bill Brown</name>    <uri>http://www.colorfulsoftware.com</uri>    <email>info@colorfulsoftware.com</email>  </author>  "
		+"<link href=\"http://www.colorfulsoftware.com/projects/atomsphere/atom.xml\" rel=\"self\" type=\"application/atom+xml\"/>  "
		+"<rights>Copyright 2007</rights>  "
		+"<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#About</id>    "
		+"<updated>2007-03-02T13:00:00.699-06:00</updated>    <title>About</title>    <published>2007-02-26T12:34:01.330-06:00</published>    "
		+"<summary>About the project</summary>    "
		+"<content type=\"html\">&lt;ul&gt;         &lt;li&gt;&lt;span class=\"boldText\"&gt;Atomsphere&lt;/span&gt; isa java library that allows you to create and modify atom 1.0 feeds.&lt;/li&gt;          &lt;li&gt;It is distributed under the GNU GPL license and can be used in any manner complient with the license.&lt;/li&gt;          &lt;li&gt;It is also packaged as a servlet-lib for use in web applications.&lt;/li&gt;            &lt;li&gt;It is also packaged as a customtag library to display feeds on a webapage.&lt;/li&gt;          &lt;li&gt;It also comes with an example webapp which demonstrates some example uses of the library.&lt;/li&gt;        &lt;li&gt;It is written to be tied as closely as possible to the current atom specification found &lt;a href=\"http://www.atomenabled.org/developers/syndication/atom-format-spec.php\"&gt;here&lt;/a&gt;.&lt;/li&gt;                 &lt;/ul&gt;</content>  </entry>  "
		+"<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
		+"<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
		+"<summary>Requirements for using the libraries</summary>    "
		+"<content type=\"html\">&lt;br /&gt;the project is usable with jdk 1.4.2 and above&lt;br /&gt;                                    &amp;nbsp;&lt;br /&gt;            needed for using the library&lt;br /&gt;                &lt;ul&gt;                &lt;li&gt;&lt;a href=\"https://sjsxp.dev.java.net/\"&gt;jsr173&lt;/a&gt; (STAX api jar) - see the &lt;a href=\"http://jcp.org/aboutJava/communityprocess/final/jsr173/index.html\"&gt;API&lt;/a&gt;.&lt;/li&gt;         &lt;li&gt;&lt;a href=\"https://sjsxp.dev.java.net/\"&gt;sjsxp&lt;/a&gt; (STAX implementation) - others implementations may work but have not been tested.&lt;/li&gt;          &lt;li&gt;&lt;a href=\"https://stax-utils.dev.java.net/\"&gt;stax-utils&lt;/a&gt; (for pretty printing)&lt;/li&gt;          &lt;/ul&gt;        needed for using the atomsphere-taglib&lt;br /&gt;              &lt;ul&gt;        &lt;li&gt;the atomsphere library&lt;/li&gt;                             &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;           &lt;/ul&gt;             needed for using the atomsphere-weblib&lt;br /&gt;                &lt;ul&gt;              &lt;li&gt;the atomsphere library&lt;/li&gt;                       &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;           &lt;/ul&gt;             needed for using the example atomsphere-webapp&lt;br /&gt;                &lt;ul&gt;              &lt;li&gt;Any J2EE Servlet Container&lt;/li&gt;&lt;/ul&gt;</content>  </entry>  "
		+"<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Documentation</id>    "
		+"<updated>2007-03-02T12:59:45.475-06:00</updated>    <title>Documentation</title>    <published>2007-02-26T13:00:00.478-06:00</published>    "
		+"<summary>Starting Documentation</summary>    "
		+"<content type=\"html\">&lt;h4&gt;Installation (atomsphere library)&lt;/h4&gt;                 &lt;ul&gt;                &lt;li&gt;Add the jsr173, sjsxp, stax-utils and atomsphere jars to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;                    &lt;/ul&gt;                &lt;h4&gt;Installation (atomsphere-taglib)&lt;/h4&gt;                   &lt;ul&gt;                        &lt;li&gt;Add the atomsphere jar to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;&lt;li&gt;Add the atomsphereTags.tld tag descriptor to the top of the jsp page (See example below). &lt;/li&gt;                      &lt;li&gt;Add anyrequired attributes and optional attributes to the custom tag (See example below).&lt;/li&gt;                     &lt;li&gt;View the atomsphereTags.tld for a description of the attributes and what they do.&lt;/li&gt;&lt;/ul&gt;                &lt;h4&gt;Installation (atomsphere-weblib)&lt;/h4&gt;                   &lt;ul&gt;&lt;li&gt;Add the atomsphere and atomsphere-weblib jars to the classpath (WEB-INF/lib for webapps).&lt;/li&gt;&lt;li&gt;Copy the web-fragrment.xml (embeded in the jar file) to your application's web.xml file.&lt;/li&gt;&lt;/ul&gt;                               &lt;h4&gt;Installation (atomsphere-webapp)&lt;/h4&gt;                    &lt;ul&gt;       &lt;li&gt;Deploy the war file to any J2EE servlet container.&lt;/li&gt;                   &lt;/ul&gt;</content>  </entry>  "
		+"<entry>    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Examples</id>    "
		+"<updated>2007-03-02T12:59:10.517-06:00</updated>    <title>Examples</title>    <published>2007-02-26T13:01:43.57-06:00</published>    "
		+"<summary>Basic examples</summary>    <content type=\"html\">&lt;br /&gt;Read an atom xml file into a Feed bean&lt;br /&gt;              &lt;code&gt;                    Feed feed = FeedDoc.readFeedToBean(new File(fullPath));                   &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Read an atom URL into a Feed bean&lt;br /&gt;             &lt;code&gt;                    Feed feed = FeedDoc.readFeedToBean(new URL(\"http://www.colorfulsoftware.com/atom.xml\"));                  &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Read an atom xml file into an xml atom string&lt;br /&gt;                 &lt;code&gt;                    String atomXML = FeedDoc.readFeedToString(new File(fullPath));                    &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Write a feed bean into an xml file&lt;br /&gt;                    &lt;code&gt;                    FeedDoc.writeFeedDoc(fullPath,feed,FeedDoc.encoding,FeedDoc.xml_version);                 &lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;                                   Add the custom feed tag to a jsp page.&lt;br /&gt;                                &lt;code&gt;  &amp;lt;%@taglib uri=\"/WEB-INF/atomsphereTags.tld\" prefix=\"atom\" %&amp;gt;&lt;/code&gt;&lt;br /&gt;...&lt;br /&gt;&lt;code&gt;&amp;lt;atom:atomFeed &lt;/code&gt;&lt;br /&gt;&lt;code&gt;     clazz=\"myFeed\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;    url=\"http://www.colorfulsoftware.com/atom.xml\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;feedSummary=\"true\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;entryUpdated=\"true\"&lt;/code&gt;&lt;br /&gt;&lt;code&gt;entryUpdatedFormat=\"yyyy-MM-dd\" /&amp;gt;&lt;/code&gt;&lt;br /&gt;&amp;nbsp;&lt;br /&gt;</content> </entry></feed>";
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		feedReader = new FeedReader();
		XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream("src/test/resources/atomConfig.xml")));
		configFile = (Map<String, String>)decode.readObject();
		decode.close();
		Iterator<String> feeds = configFile.values().iterator();
		while(feeds.hasNext()){
			//there should only be 1 element.
			reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(feeds.next()));
		}
		reader2 = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream("src/test/resources/flat.xml"));
		reader3 = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream("src/test/resources/dump.xml"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFeed() {
		System.out.println("in testRead Feed.");
		Enumeration<?> props = System.getProperties().propertyNames();
		String val = null;
		for (;props.hasMoreElements();) {
	        System.out.println((val = (String)props.nextElement())+ " = " + System.getProperty(val));		
		}
		try{
			Feed feed = feedReader.readFeed(reader);
			System.out.println("number of entries = "+feed.getEntries().size());
			
			feed = feedReader.readFeed(reader2);
			System.out.println("number of entries2 = "+feed.getEntries().size());
			
			feed = feedReader.readFeed(reader3);
			System.out.println("number of entries3 = "+feed.getEntries().size());
			
			feed = FeedDoc.readFeedToBean(xmlString);
			System.out.println("number of entries4 = "+feed.getEntries().size());
			
		}catch(Exception e){
			e.printStackTrace();
			fail("could not read fead.");
		}
	}

	@Test
	public void testGetAttributes() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadExtension() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadEntry() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadSummary() {
		//fail("Not yet implemented");
	}

	@Test
	public void testContainsXHTML() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadSource() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetSimpleDateFormat() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadPublished() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadContent() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadUpdated() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadTitle() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadXHTML() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadSubtitle() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadRights() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadLogo() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadLink() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadId() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadIcon() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadGenerator() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadContributor() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadCategory() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadAuthor() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadAtomPersonConstruct() {
		//fail("Not yet implemented");
	}

}
