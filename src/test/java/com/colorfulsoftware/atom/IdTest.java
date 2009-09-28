/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
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
package com.colorfulsoftware.atom;

import static org.junit.Assert.*;

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
public class IdTest {
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
			assertEquals(e.getMessage(), "Unsuppported attribute "
					+ "href for this element.");
		}
	}
}
