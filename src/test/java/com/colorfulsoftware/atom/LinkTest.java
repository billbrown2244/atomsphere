/**
 * Copyright ${year} Bill Brown
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
