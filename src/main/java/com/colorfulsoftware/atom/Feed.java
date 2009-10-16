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
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 *  2007-02-05 wbrown - added sort method for sorting entries.
 *  2007-02-19 wbrown - add support for sorting entries by title and updated.
 *  2007-06-20 wbrown - adding support for sorting by summary. 
 *  2007-07-10 wbrown - commented out debug code.
 *  2008-03-17 wbrown - made class immutable.
 *  2008-03-21 wbrown - move comparators to feed doc.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class represents an Atom 1.0 feed element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomFeed =
 *          element atom:feed {
 *          atomCommonAttributes,
 *          (atomAuthor*
 *          &amp; atomCategory*
 *          &amp; atomContributor*
 *          &amp; atomGenerator?
 *          &amp; atomIcon?
 *          &amp; atomId
 *          &amp; atomLink*
 *          &amp; atomLogo?
 *          &amp; atomRights?
 *          &amp; atomSubtitle?
 *          &amp; atomTitle
 *          &amp; atomUpdated
 *          &amp; extensionElement*),
 *          atomEntry*
 *          }
 * </pre>
 * 
 */
public class Feed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8576480736811385690L;
	private final Source source;
	private final SortedMap<String, Entry> entries;

	// use the factory method in the FeedDoc.
	Feed(Id id, Title title, Updated updated, Rights rights,
			List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Generator generator, Subtitle subtitle, Icon icon, Logo logo,
			SortedMap<String, Entry> entries) throws AtomSpecException {

		// make sure id is present
		if (id == null) {
			throw new AtomSpecException(
					"atom:feed elements MUST contain exactly one atom:id element.");
		}
		// make sure title is present
		if (title == null) {
			throw new AtomSpecException(
					"atom:feed elements MUST contain exactly one atom:title element.");
		}
		// make sure updated is present
		if (updated == null) {
			throw new AtomSpecException(
					"atom:feed elements MUST contain exactly one atom:updated element.");
		}

		source = new Source(id, title, updated, rights, authors, categories,
				contributors, links, attributes, extensions, generator,
				subtitle, icon, logo);

		if (entries == null) {
			this.entries = null;
			if (source.getAuthors() == null) {
				throw new AtomSpecException(
						"atom:feed elements MUST contain one or more atom:author elements, unless the atom:entry contains an atom:source element that contains an atom:author element or, in an Atom Feed Document, the atom:feed element contains an atom:author element itself.");
			}
		} else {

			this.entries = new TreeMap<String, Entry>(entries.comparator());
			for (String entryKey : entries.keySet()) {
				Entry entry = entries.get(entryKey);

				// if there is no author element at the feed level
				// check to make sure the entry has an author element
				if (source.getAuthors() == null) {
					if (entry.getAuthors() == null) {
						throw new AtomSpecException(
								"atom:feed elements MUST contain one or more atom:author elements, unless all of the atom:feed element's child atom:entry elements contain at least one atom:author element.");
					}
				}

				this.entries.put(entryKey, new Entry(entry));
			}
		}
	}

	/**
	 * @return the map of entry elements. Sorted by updated descending by
	 *         default. see FeedDoc.sortEntries(Feed feed, Comparator<String>
	 *         comparator, Class<?> elementClass)
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public SortedMap<String, Entry> getEntries() throws AtomSpecException {
		if (entries == null) {
			return null;
		}
		SortedMap<String, Entry> entriesCopy = new TreeMap<String, Entry>(
				entries.comparator());
		for (String entryKey : this.entries.keySet()) {
			Entry entry = entries.get(entryKey);
			entriesCopy.put(entryKey, new Entry(entry.getId(),
					entry.getTitle(), entry.getUpdated(), entry.getRights(),
					entry.getContent(), entry.getAuthors(), entry
							.getCategories(), entry.getContributors(), entry
							.getLinks(), entry.getAttributes(), entry
							.getExtensions(), entry.getPublished(), entry
							.getSummary(), entry.getSource()));
		}
		return entriesCopy;
	}

	/**
	 * 
	 * @return the generator for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Generator getGenerator() throws AtomSpecException {
		return source.getGenerator();
	}

	/**
	 * 
	 * @return the icon for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Icon getIcon() throws AtomSpecException {
		return source.getIcon();
	}

	/**
	 * 
	 * @return the logo for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Logo getLogo() throws AtomSpecException {
		return source.getLogo();
	}

	/**
	 * 
	 * @return the subtitle for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Subtitle getSubtitle() throws AtomSpecException {
		return source.getSubtitle();
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Id getId() throws AtomSpecException {
		return source.getId();
	}

	/**
	 * 
	 * @return the title for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Title getTitle() throws AtomSpecException {
		return source.getTitle();
	}

	/**
	 * 
	 * @return the updated date for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Updated getUpdated() throws AtomSpecException {
		return source.getUpdated();
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Rights getRights() throws AtomSpecException {
		return source.getRights();
	}

	/**
	 * 
	 * @return the authors for this entry.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Author> getAuthors() throws AtomSpecException {
		return source.getAuthors();
	}

	/**
	 * 
	 * @return the categories for this element.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Category> getCategories() throws AtomSpecException {
		return source.getCategories();
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Contributor> getContributors() throws AtomSpecException {
		return source.getContributors();
	}

	/**
	 * 
	 * @return the links for this entry.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Link> getLinks() throws AtomSpecException {
		return source.getLinks();
	}

	/**
	 * 
	 * @return the category attribute list.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Attribute> getAttributes() throws AtomSpecException {
		return source.getAttributes();
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Extension> getExtensions() throws AtomSpecException {
		return source.getExtensions();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Attribute getAttribute(String attrName) throws AtomSpecException {
		return source.getAttribute(attrName);
	}

	/**
	 * @param name
	 *            the name of the author.
	 * @return the Author object object if name matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Author getAuthor(String name) throws AtomSpecException {
		return source.getAuthor(name);
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if term matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Category getCategory(String termValue) throws AtomSpecException {
		return source.getCategory(termValue);
	}

	/**
	 * @param name
	 *            the name of the contributor.
	 * @return the Contributor object if the name matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Contributor getContributor(String name) throws AtomSpecException {
		return source.getContributor(name);
	}

	/**
	 * @param hrefVal
	 *            the href attribute value to look for.
	 * @return the Link object if href matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Link getLink(String hrefVal) throws AtomSpecException {
		return source.getLink(hrefVal);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Extension getExtension(String extName) throws AtomSpecException {
		return source.getExtension(extName);
	}
}
