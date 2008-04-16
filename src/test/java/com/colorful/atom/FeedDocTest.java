/*
Atomsphere - an atom feed library.
Copyright (C) 2006 William R. Brown.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.colorful.atom;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.stream.XMLOutputFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.colorful.atom.Attribute;
import com.colorful.atom.Author;
import com.colorful.atom.Category;
import com.colorful.atom.Contributor;
import com.colorful.atom.Entry;
import com.colorful.atom.Extension;
import com.colorful.atom.Feed;
import com.colorful.atom.FeedDoc;
import com.colorful.atom.Generator;
import com.colorful.atom.Icon;
import com.colorful.atom.Id;
import com.colorful.atom.Link;
import com.colorful.atom.Logo;
import com.colorful.atom.Name;
import com.colorful.atom.Rights;
import com.colorful.atom.Title;
import com.colorful.atom.Updated;

public class FeedDocTest {

	private Feed feed1;

	@Before
	public void setUp() throws Exception {
		try{
			Id id = FeedDoc.buildId(null,"http://www.colorfulsoftware.com/atom.xml");

			Updated updated = FeedDoc.buildUpdated(Calendar.getInstance().getTime(),null);

			Title title = FeedDoc.buildTitle("test feed",null);
			
			Generator generator = FeedDoc.atomsphereVersion;
			
			List<Author> authors = new LinkedList<Author>();
			authors.add(FeedDoc.buildAuthor(FeedDoc.buildName("Bill Brown"),null,null,null,null));
			
			feed1 = FeedDoc.buildFeed(id
					, title
					, updated
					, null
					, authors
					, null
					, null
					, null
					, null
					, null
					, generator
					, null
					, null
					, null
					, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		new File("out.xml").deleteOnExit();
		new File("out2.xml").deleteOnExit();
	}

	@Test
	public void testWriteFeedDocOutputStreamFeedStringString() {
		try {
			FeedDoc.writeFeedDoc(new FileOutputStream("out2.xml"), feed1,
					FeedDoc.encoding, FeedDoc.xml_version);
			Feed feed = FeedDoc.readFeedToBean(new File("out2.xml"));
			assertNotNull(feed);
		} catch (Exception e) {
			e.printStackTrace();
			fail("could not write output file with file output stream.");
		}
	}

	@Test
	public void testWriteFeedDocWriterFeedStringString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWriteFeedDocXMLStreamWriterFeedStringString() {
		try {
			// pretty print version.
			FeedDoc.writeFeedDoc(
					new javanet.staxutils.IndentingXMLStreamWriter(
							XMLOutputFactory.newInstance()
									.createXMLStreamWriter(
											new FileOutputStream("out.xml"),
											FeedDoc.encoding)), feed1,
					FeedDoc.encoding, FeedDoc.xml_version);
			Feed feed = FeedDoc.readFeedToBean(new File("out.xml"));
			assertNotNull(feed);
		} catch (Exception e) {
			fail("could not write output file with custom xml stream writer.");
		}
	}

	@Test
	public void testReadFeedToStringInputStream() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToStringFile() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToStringURL() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToStringFeedString() {
		try {

			String feedStr = FeedDoc.readFeedToString(feed1,
					"com.sun.xml.txw2.output.IndentingXMLStreamWriter");
			assertNotNull(feedStr);
		} catch (Exception e) {
			assertTrue(e instanceof ClassNotFoundException);
		}

		try {
			String feedStr = FeedDoc.readFeedToString(feed1,
					"javanet.staxutils.IndentingXMLStreamWriter");
			assertNotNull(feedStr);
		} catch (Exception e) {
			fail("could not read feed to string.");
		}

		try {
			String feedStr = FeedDoc.readFeedToString(feed1, "bunk");
			assertNotNull(feedStr);
		} catch (Exception e) {
			assertTrue(e instanceof ClassNotFoundException);
		}
	}

	@Test
	public void testReadFeedToStringFeed() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadEntryToString() {
		try{
		Calendar theDate = Calendar.getInstance();
		theDate.clear();
		theDate.set(2008,0,1);
		String expected = "<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">";
		expected += "<id>http://www.colorfulsoftware.com/projects/atomsphere/</id>";
		expected += "<updated>2008-01-01T00:00:00.00-06:00</updated>";
		expected += "<title>test entry</title>";
		expected += "</entry>";
		Entry entry = FeedDoc.buildEntry(
				FeedDoc.buildId(null,"http://www.colorfulsoftware.com/projects/atomsphere/")
				, FeedDoc.buildTitle("test entry",null)
				, FeedDoc.buildUpdated(theDate.getTime(),null)
				, null
				, null
				, null
				, null
				, null
				, null
				, null
				, null
				, null
				, null
				, null);
		String entryStr = FeedDoc.readEntryToString(entry);
		assertTrue(entryStr != null);
		assertEquals(entryStr,expected);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e instanceof AtomSpecException);
		}
	}

	@Test
	public void testReadFeedToBeanString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToBeanFile() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToBeanURL() {
		// fail("Not yet implemented");
	}

	@Test
	public void testReadFeedToBeanInputStream() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildFeed() {
		try {
			List<Attribute> genAttrs = new LinkedList<Attribute>();
			genAttrs.add(FeedDoc.buildAttribute("uri",
					"http://www.colorfulsoftware.com/projects/atomsphere"));
			genAttrs.add(FeedDoc.buildAttribute("version", "2.0.0.0"));
			Generator generator = FeedDoc.atomsphereVersion;

			Id id = FeedDoc.buildId(null,
					"http://www.colorfulsoftware.com/atom.xml");

			Updated updated = FeedDoc.buildUpdated(Calendar.getInstance()
					.getTime(),null);

			Title title = FeedDoc.buildTitle("test feed", null);

			List<Contributor> contributors = new LinkedList<Contributor>();
			Contributor contributor = FeedDoc.buildContributor(new Name(
					"Mad Dog"), null, FeedDoc.buildEmail("info@maddog.net"),
					null, null);
			contributors.add(contributor);

			Rights rights = FeedDoc.buildRights("GPL 1.0", null);

			Icon icon = FeedDoc.buildIcon(null, "http://host/images/icon.png");

			Logo logo = FeedDoc.buildLogo(null, "http://host/images/logo.png");

			List<Attribute> catAttrs = new LinkedList<Attribute>();
			catAttrs.add(FeedDoc.buildAttribute("term", "music"));
			catAttrs.add(FeedDoc.buildAttribute("scheme",
					"http://mtv.com/genere"));
			catAttrs.add(FeedDoc.buildAttribute("label", "music"));
			List<Category> categories = new LinkedList<Category>();
			Category category = FeedDoc.buildCategory(catAttrs, null);
			categories.add(category);

			List<Attribute> linkAttrs = new LinkedList<Attribute>();
			linkAttrs.add(FeedDoc
					.buildAttribute("href", "http://www.yahoo.com"));
			linkAttrs.add(FeedDoc.buildAttribute("rel", "self"));
			linkAttrs.add(FeedDoc.buildAttribute("hreflang", "en-US"));
			List<Link> links = new LinkedList<Link>();
			Link link = FeedDoc.buildLink(linkAttrs, null);
			links.add(link);

			Attribute extAttr = FeedDoc.buildAttribute("xmlns:xhtml",
					"http://www.w3.org/1999/xhtml");
			List<Attribute> extAttrs = new LinkedList<Attribute>();
			extAttrs.add(extAttr);

			// the base feed attributes.
			List<Attribute> feedAttrs = new LinkedList<Attribute>();
			feedAttrs.add(FeedDoc.atomBase);
			feedAttrs.add(FeedDoc.lang_en);
			feedAttrs.addAll(extAttrs);

			List<Extension> extensions = new LinkedList<Extension>();
			Extension extension = FeedDoc.buildExtension("xhtml:div", null,
					"<span style='color:red;'>hello there</span>");
			extensions.add(extension);

			List<Author> authors = new LinkedList<Author>();
			authors.add(FeedDoc.buildAuthor(FeedDoc.buildName("Bill Brown"),
					null, null, null, null));
			Entry entry = FeedDoc
					.buildEntry(
							FeedDoc
									.buildId(null,
											"http://www.colorfulsoftware.com/atom.xml#entry1"),
							FeedDoc.buildTitle("an example atom entry", null),
							FeedDoc.buildUpdated(Calendar.getInstance()
									.getTime(),null),
							null,
							FeedDoc
									.buildContent(
											"Hello World.  Welcome to the atomsphere feed builder for atom 1.0 builds.  I hope it is useful for you.",
											null), authors, null, null, null,
							null, null, null, null, null);

			SortedMap<String, Entry> entries = new TreeMap<String, Entry>();
			entries.put(entry.getUpdated().getText(), entry);

			Feed feed = FeedDoc.buildFeed(id, title, updated, rights, null,
					categories, contributors, links, feedAttrs, extensions,
					generator, null, icon, logo, entries);

			assertNotNull(feed);
		} catch (Exception e) {
			fail("Issue building feed doc.");
		}
	}

	@Test
	public void testBuildAttribute() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildAuthor() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildCategory() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildContent() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildContributor() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildEmail() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildEntry() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildExtension() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildGenerator() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildIcon() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildId() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildLink() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildLogo() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildName() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildPublished() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildRights() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildSource() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildSubtitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildSummary() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildTitle() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildUpdated() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildURI() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildAtomPersonConstruct() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetAttributeFromGroup() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetContentType() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSortEntries() {
		// fail("Not yet implemented");
	}

	@Test
	public void testCheckForAndApplyExtension() {
		// fail("Not yet implemented");
	}

	@Test
	public void testMain() {
		// fail("Not yet implemented");
	}

}
