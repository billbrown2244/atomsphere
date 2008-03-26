package com.colorful.atom.beans;

import static org.junit.Assert.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.SortedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FeedWriterTest {

	private FeedWriter feedWriter;
	private XMLStreamWriter writer;
	private String xhtml1 = "this is a test <a href=\"http://www.google.com\">link</a>.  Can we put arbitrary xhtml here?<hr />I can't say for sure. <span>hello</span>";
	private String xhtml2 = "<h4 >Installation (atomsphere library)</h4>"
		+ "<ul >        		<li>Add the jsr173, sjsxp, stax-utils and atomsphere jars to the classpath (WEB-INF/lib for webapps).</li></ul>"
		+ "<h4 >Installation (atomsphere-taglib)</h4>        		"
		+ "<ul >        		<li>Add the atomsphere jar to the classpath (WEB-INF/lib for webapps).</li>"
		+ "<li>Add the atomsphereTags.tld tag descriptor to the top of the jsp page (See example below). </li>"
		+ "<li>Add any required attributes and optional attributes to the custom tag (See example below).</li>"
		+ "<li>View the atomsphereTags.tld for a description of the attributes and what they do.</li></ul>"
		+ "<h4 >Installation (atomsphere-weblib)</h4>        		<ul >"
		+ "<li>Add the atomsphere and atomsphere-weblib jars to the classpath (WEB-INF/lib for webapps).</li>"
		+ "<li>Copy the web-fragrment.xml (embeded in the jar file) to your application's web.xml file.</li></ul>"
		+ "<h4 >Installation (atomsphere-webapp)</h4>        		"
		+ "<ul >				<li>Deploy the war file to any J2EE servlet container.</li>				</ul>";
		
	private String entry1 = 
		"<entry xmlns:xh=\"http://some.xh.specific.uri/xh\">    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
		+"<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
		//prefix is bound at entry
		+"<summary type=\"xhtml\">"
		+"<xh:div>"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</xh:div>"
		+"</summary>"
		+"</entry>";
	
	private String entry1Result = 
		"<entry xmlns:xh=\"http://some.xh.specific.uri/xh\"><id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>"
		+"<updated>2007-03-02T12:59:54.274-06:00</updated><title>Requirements</title><published>2007-02-26T12:58:53.197-06:00</published>"
		+"<summary type=\"xhtml\">"
		+"<div xmlns=\"http://www.w3.org/1999/xhtml\">"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</div>"
		+"</summary>"
		+"</entry>";
	
	private String entry2 = 
		"<entry xmlns:xh=\"http://some.xh.specific.uri/xh\">    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
		+"<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
		//prefix is bound at entry
		+"<content type=\"xhtml\">"
		+"<xh:div>"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</xh:div>"
		+"</content>"
		+"</entry>";
	
	private String entry2Result = 
		"<entry xmlns:xh=\"http://some.xh.specific.uri/xh\"><id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>"
		+"<updated>2007-03-02T12:59:54.274-06:00</updated><title>Requirements</title><published>2007-02-26T12:58:53.197-06:00</published>"
		+"<content type=\"xhtml\">"
		+"<div xmlns=\"http://www.w3.org/1999/xhtml\">"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</div>"
		+"</content>"
		+"</entry>";
	
	private String entry3 = 
		"<entry xmlns:xh=\"http://some.xh.specific.uri/xh\">    <id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>    "
		+"<updated>2007-03-02T12:59:54.274-06:00</updated>    <title>Requirements</title>    <published>2007-02-26T12:58:53.197-06:00</published>    "
		//prefix is bound at entry
		+"<summary type=\"xhtml\">"
		+"<xh:div>"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</xh:div>"
		+"</summary>"
		+"<content type=\"xhtml\">"
		+"<xh:div>"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</xh:div>"
		+"</content>"
		+"</entry>";
	
	private String entry3Result = 
		"<entry xmlns:xh=\"http://some.xh.specific.uri/xh\"><id>http://colorfulsoftware.localhost/colorfulsoftware/projects/atomsphere/atom.xml#Requirements</id>"
		+"<updated>2007-03-02T12:59:54.274-06:00</updated><title>Requirements</title><published>2007-02-26T12:58:53.197-06:00</published>"
		+"<summary type=\"xhtml\">"
		+"<div xmlns=\"http://www.w3.org/1999/xhtml\">"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</div>"
		+"</summary>"
		+"<content type=\"xhtml\">"
		+"<div xmlns=\"http://www.w3.org/1999/xhtml\">"
		+"This is <xh:b>XHTML</xh:b> content."
		+"</div>"
		+"</content>"
		+"</entry>";
	
	@Before
	public void setUp() throws Exception {
		 feedWriter = new FeedWriter();
		 writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream("out.xml"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteFeed() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteSubtitle() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteGenerator() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteID() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteUpdated() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteTitle() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteXHTML() {
		try{
			feedWriter.writeXHTML(writer,xhtml1);
			
			feedWriter.writeXHTML(writer,xhtml2);
			
		}catch(Exception e){
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}

	@Test
	public void testWriteAuthors() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteName() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteUri() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteEmail() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteContributors() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteRights() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteLogo() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteIcon() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteCategories() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteLinks() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteExtensions() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteEntries() {
		try{
			
			SortedMap<String,Entry> entries = 
				new FeedReader()
			.readEntry(XMLInputFactory
					.newInstance()
					.createXMLStreamReader(
							new StringReader(entry1))
							, null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);
			
			StringWriter theXMLString = new StringWriter();
            XMLStreamWriter writer = XMLOutputFactory
            .newInstance()
            .createXMLStreamWriter(theXMLString);
            new FeedWriter().writeEntries(writer, entries);
            writer.flush();
            writer.close();
            assertEquals(theXMLString.toString(),entry1Result);
            
			
			entries = 
				new FeedReader()
			.readEntry(XMLInputFactory
					.newInstance()
					.createXMLStreamReader(
							new StringReader(entry2))
							, null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);
			
			theXMLString = new StringWriter();
            writer = XMLOutputFactory
            .newInstance()
            .createXMLStreamWriter(theXMLString);
            new FeedWriter().writeEntries(writer, entries);
            writer.flush();
            writer.close();
            assertEquals(theXMLString.toString(),entry2Result);
			
            
			entries = 
				new FeedReader()
			.readEntry(XMLInputFactory
					.newInstance()
					.createXMLStreamReader(
							new StringReader(entry3))
							, null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);
			
			theXMLString = new StringWriter();
            writer = XMLOutputFactory
            .newInstance()
            .createXMLStreamWriter(theXMLString);
            new FeedWriter().writeEntries(writer, entries);
            writer.flush();
            writer.close();
            assertEquals(theXMLString.toString(),entry3Result);
            
            entries = 
				new FeedReader()
			.readEntry(XMLInputFactory
					.newInstance()
					.createXMLStreamReader(
							new StringReader(entry3Result))
							, null);
			assertTrue(entries != null);
			assertTrue(entries.size() == 1);
			
		}catch(Exception e){
			e.printStackTrace();
			fail("could not write entries."+e.getLocalizedMessage());
		}
	}

	@Test
	public void testWriteSummary() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWritePublished() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteContent() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteSource() {
		//fail("Not yet implemented");
	}

}
