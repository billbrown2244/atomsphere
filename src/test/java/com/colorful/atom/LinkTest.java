package com.colorful.atom;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LinkTest {

	private Link link;
	private Attribute xmlBase;
	private Attribute xmlLang;
	private Attribute local;
	private Attribute href;
	private Attribute rel;
	private Attribute type;
	private Attribute hreflang;
	private Attribute title;
	private Attribute length;
	
	@Before
	public void setUp() throws Exception {
		xmlBase = FeedDoc.buildAttribute("xml:base", "http://www.colorfulsoftware.com/projects/atomsphere/");
		xmlLang = FeedDoc.lang_en;
		local = FeedDoc.buildAttribute("local:abcAttribute","theValue");
		href = FeedDoc.buildAttribute("href","http://www.colorfulsoftware.com/");
		rel = FeedDoc.buildAttribute("rel","alternate");
		type = FeedDoc.buildAttribute("type","text/html");
		hreflang = FeedDoc.buildAttribute("hreflang","en-US");
		title = FeedDoc.buildAttribute("title","a cool site");
		length = FeedDoc.buildAttribute("length","100");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLink() {
		List<Attribute> attributes = new LinkedList<Attribute>();
		try{
			attributes.add(xmlBase);
			attributes.add(xmlLang);
			attributes.add(local);
			attributes.add(href);
			attributes.add(rel);
			attributes.add(type);
			attributes.add(hreflang);
			attributes.add(title);
			attributes.add(length);
			link = FeedDoc.buildLink(attributes,null);
			assertTrue(link != null);
			
			attributes.add(FeedDoc.buildAttribute("junkAttr","junk"));
			link = FeedDoc.buildLink(attributes,null);
			fail("should not get here");
			
		}catch(Exception e){
			assertTrue(e instanceof AtomSpecException);
			assertEquals(e.getMessage(),"Unsuppported attribute junkAttr in the atom:link element.");
		}
	}

	@Test
	public void testGetAttributes() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetHref() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetHreflang() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetLength() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRel() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetTitle() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetType() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetContent() {
		//fail("Not yet implemented");
	}

}
