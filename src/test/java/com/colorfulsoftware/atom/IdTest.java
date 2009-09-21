/**
 * Copyright (C) 2009 William R. Brown <info@colorfulsoftware.com>
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

public class IdTest {

	private Id id;
	private Attribute xmlBase;
	private Attribute xmlLang;
	private Attribute local;
	
	@Before
	public void setUp() throws Exception {
		xmlBase = FeedDoc.buildAttribute("xml:base", "http://www.colorfulsoftware.com/projects/atomsphere/");
		xmlLang = FeedDoc.lang_en;
		local = FeedDoc.buildAttribute("xmlns:abcAttribute","theValue");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testId() {
		List<Attribute> attributes = new LinkedList<Attribute>();
		try{
			attributes.add(xmlBase);
			attributes.add(xmlLang);
			attributes.add(local);
			id = FeedDoc.buildId(attributes, "http://www.colorfulsoftware.com/");
			assertTrue(id != null);
			
			attributes.add(FeedDoc.buildAttribute("href","http://www.colorfulsoftware.com"));
			id = FeedDoc.buildId(attributes, "http://www.colorfulsoftware.com/");
			fail("should not get here");
			
		}catch(Exception e){
			assertTrue(e instanceof AtomSpecException);
			assertEquals(e.getMessage(),"Unsuppported attribute "
					+"href for this element.");
		}
	}

	@Test
	public void testGetAtomUri() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetAttributes() {
		//fail("Not yet implemented");
	}

}
