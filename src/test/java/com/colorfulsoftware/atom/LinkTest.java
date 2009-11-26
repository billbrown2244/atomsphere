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

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorfulsoftware.atom.AtomSpecException;
import com.colorfulsoftware.atom.Attribute;
import com.colorfulsoftware.atom.FeedDoc;
import com.colorfulsoftware.atom.Link;

/**
 * This class tests links.
 * 
 * @author Bill Brown
 * 
 */
public class LinkTest implements Serializable {

	private static final long serialVersionUID = -6874756389584774058L;
	private FeedDoc feedDoc;
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

	/**
	 * @throws Exception
	 *             if there is an error creating the test data.
	 */
	@Before
	public void setUp() throws Exception {
		feedDoc = new FeedDoc();
		xmlBase = feedDoc.buildAttribute("xml:base",
				"http://www.colorfulsoftware.com/projects/atomsphere/");
		xmlLang = feedDoc.getLangEn();
		local = feedDoc.buildAttribute("xmlns:abcAttribute", "theValue");
		href = feedDoc.buildAttribute("href",
				"http://www.colorfulsoftware.com/");
		rel = feedDoc.buildAttribute("rel", "alternate");
		type = feedDoc.buildAttribute("type", "text/html");
		hreflang = feedDoc.buildAttribute("hreflang", "en-US");
		title = feedDoc.buildAttribute("title", "a cool site");
		length = feedDoc.buildAttribute("length", "100");
	}

	/**
	 * @throws Exception
	 *             if there is an error cleaning up the test data.
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * tests the link functionality.
	 */
	@Test
	public void testLink() {
		List<Attribute> attributes = new LinkedList<Attribute>();
		try {
			attributes.add(xmlBase);
			attributes.add(xmlLang);
			attributes.add(local);
			attributes.add(href);
			attributes.add(rel);
			attributes.add(type);
			attributes.add(hreflang);
			attributes.add(title);
			attributes.add(length);
			link = feedDoc.buildLink(attributes, null);
			assertTrue(link != null);

			attributes.add(feedDoc.buildAttribute("src",
					"http://www.colorfulsoftware.com"));
			link = feedDoc.buildLink(attributes, null);
			fail("should not get here");

		} catch (Exception e) {
			assertTrue(e instanceof AtomSpecException);
			assertEquals(e.getMessage(), "Unsupported attribute src"
					+ " for this link element.");
		}
	}
}
