package com.colorful.atom.beans;

import static org.junit.Assert.*;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FeedReaderTest {

	private FeedReader feedReader;
	private XMLStreamReader reader;
	private XMLStreamReader reader2;
	private Map<String,String> configFile;
	
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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFeed() {
		System.out.println("in testRead Feed.");
		try{
			Feed feed = feedReader.readFeed(reader);
			System.out.println("number of entries = "+feed.getEntries().size());
			
			feed = feedReader.readFeed(reader2);
			System.out.println("number of entries2 = "+feed.getEntries().size());
			
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
