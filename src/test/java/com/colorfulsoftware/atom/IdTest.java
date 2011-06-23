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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorfulsoftware.atom.AtomSpecException;
import com.colorfulsoftware.atom.Attribute;
import com.colorfulsoftware.atom.FeedDoc;
import com.colorfulsoftware.atom.Id;

/**
 * @author Bill Brown
 * 
 */
public class IdTest implements Serializable {

	private static final long serialVersionUID = 315700003232965189L;
	private FeedDoc feedDoc;
	private Id id;
	private Attribute xmlBase;
	private Attribute xmlLang;
	private Attribute local;

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
	}

	/**
	 * @throws Exception
	 *             if there is an error cleaning up the test data.
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * test the id building functionality.
	 */
	@Test
	public void testId() {
		List<Attribute> attributes = new LinkedList<Attribute>();
		try {
			attributes.add(xmlBase);
			attributes.add(xmlLang);
			attributes.add(local);
			id = feedDoc
					.buildId(attributes, "http://www.colorfulsoftware.com/");
			assertTrue(id != null);

			attributes.add(feedDoc.buildAttribute("href",
					"http://www.colorfulsoftware.com"));
			id = feedDoc
					.buildId(attributes, "http://www.colorfulsoftware.com/");
			fail("should not get here");

		} catch (Exception e) {
			assertTrue(e instanceof AtomSpecException);
			assertEquals(e.getMessage(), "Unsupported attribute "
					+ "href for this element.");
		}
	}
}
