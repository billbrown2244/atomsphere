package com.colorful.atom.beans;

import static org.junit.Assert.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import java.io.FileOutputStream;

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
		//fail("Not yet implemented");
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
