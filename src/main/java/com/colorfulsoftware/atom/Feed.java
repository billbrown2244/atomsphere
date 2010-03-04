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
import java.util.LinkedList;
import java.util.List;

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

	private final AtomEntrySourceAdaptor sourceAdaptor;
	private final Generator generator;
	private final Icon icon;
	private final Logo logo;
	private final Subtitle subtitle;
	private final List<Entry> entries;

	// extra processing fields
	private List<String> unboundPrefixes;

	// use the factory method in the FeedDoc.
	Feed(Id id, Title title, Updated updated, Rights rights,
			List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Generator generator, Subtitle subtitle, Icon icon, Logo logo,
			List<Entry> entries) throws AtomSpecException {

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

		this.sourceAdaptor = new AtomEntrySourceAdaptor(id, title, updated,
				rights, authors, categories, contributors, links, attributes,
				extensions);

		this.generator = (generator == null) ? null : new Generator(generator);
		this.subtitle = (subtitle == null) ? null : new Subtitle(subtitle);
		this.icon = (icon == null) ? null : new Icon(icon);
		this.logo = (logo == null) ? null : new Logo(logo);

		// check that the extension prefixes are bound to a namespace
		this.unboundPrefixes = new LinkedList<String>();
		if (sourceAdaptor.getUnboundPrefixes() != null) {
			this.unboundPrefixes.addAll(sourceAdaptor.getUnboundPrefixes());
		}

		if (entries == null) {
			this.entries = null;
			if (sourceAdaptor.getAuthors() == null) {
				throw new AtomSpecException(
						"atom:feed elements MUST contain one or more atom:author elements, unless the atom:entry contains an atom:source element that contains an atom:author element or, in an Atom Feed Document, the atom:feed element contains an atom:author element itself.");
			}
		} else {

			this.entries = new LinkedList<Entry>();

			for (Entry entry : entries) {
				// check that the entry unbound prefixes are ok.
				if (entry.getUnboundPrefixes() != null) {
					for (String unboundPrefix : entry.getUnboundPrefixes()) {
						if (sourceAdaptor
								.getAttribute("xmlns:" + unboundPrefix) == null) {
							this.unboundPrefixes.addAll(entry
									.getUnboundPrefixes());
						}
					}
				}
				// if there is no author element at the feed level
				// check to make sure the entry has an author element
				if (sourceAdaptor.getAuthors() == null
						&& entry.getAuthors() == null) {
					throw new AtomSpecException(
							"atom:feed elements MUST contain one or more atom:author elements, unless all of the atom:feed element's child atom:entry elements contain at least one atom:author element.");

				}

				this.entries.add(new Entry(entry));
			}
		}

		// if there are any unbound prefixes, throw an exception
		if (this.unboundPrefixes.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String namePrefix : this.unboundPrefixes) {
				sb.append(namePrefix + " ");
			}
			throw new AtomSpecException(
					"the following extension prefix(es) ( "
							+ sb
							+ ") are not bound to a namespace declaration. See http://www.w3.org/TR/1999/REC-xml-names-19990114/#ns-decl.");
		}

	}

	/**
	 * @return the list of entry elements. Sorted by updated descending by
	 *         default.
	 */
	public List<Entry> getEntries() {
		if (entries == null) {
			return null;
		}
		List<Entry> entriesCopy = new LinkedList<Entry>();
		for (Entry entry : this.entries) {
			entriesCopy.add(new Entry(entry));
		}
		return entriesCopy;
	}

	/**
	 * 
	 * @return the generator for this element.
	 */
	public Generator getGenerator() {
		return (generator == null) ? null : new Generator(generator);
	}

	/**
	 * 
	 * @return the icon for this element.
	 */
	public Icon getIcon() {
		return (icon == null) ? null : new Icon(icon);
	}

	/**
	 * 
	 * @return the logo for this element.
	 */
	public Logo getLogo() {
		return (logo == null) ? null : new Logo(logo);
	}

	/**
	 * 
	 * @return the subtitle for this element.
	 */
	public Subtitle getSubtitle() {
		return (subtitle == null) ? null : new Subtitle(subtitle);
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 */
	public Id getId() {
		return sourceAdaptor.getId();
	}

	/**
	 * 
	 * @return the title element.
	 */
	public Title getTitle() {
		return sourceAdaptor.getTitle();
	}

	/**
	 * 
	 * @return the updated date for this element.
	 */
	public Updated getUpdated() {
		return sourceAdaptor.getUpdated();
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 */
	public Rights getRights() {
		return sourceAdaptor.getRights();
	}

	/**
	 * 
	 * @return the authors for this entry.
	 */
	public List<Author> getAuthors() {
		return sourceAdaptor.getAuthors();
	}

	/**
	 * 
	 * @return the categories for this element.
	 */
	public List<Category> getCategories() {
		return sourceAdaptor.getCategories();
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 */
	public List<Contributor> getContributors() {
		return sourceAdaptor.getContributors();
	}

	/**
	 * 
	 * @return the links for this entry.
	 */
	public List<Link> getLinks() {
		return sourceAdaptor.getLinks();
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {
		return sourceAdaptor.getAttributes();
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 */
	public List<Extension> getExtensions() {
		return sourceAdaptor.getExtensions();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return sourceAdaptor.getAttribute(attrName);
	}

	/**
	 * @param name
	 *            the name of the author.
	 * @return the Author object object if name matches or null if not found.
	 */
	public Author getAuthor(String name) {
		return sourceAdaptor.getAuthor(name);
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if term matches or null if not found.
	 */
	public Category getCategory(String termValue) {
		return sourceAdaptor.getCategory(termValue);
	}

	/**
	 * @param name
	 *            the name of the contributor.
	 * @return the Contributor object if the name matches or null if not found.
	 */
	public Contributor getContributor(String name) {
		return sourceAdaptor.getContributor(name);
	}

	/**
	 * @param entryTitle
	 *            the title of this entry
	 * @return the entry with the given title or null if not found.
	 */
	public Entry getEntry(String entryTitle) {
		if (this.entries != null) {
			for (Entry entry : this.entries) {
				if (entry.getTitle().getText().equals(entryTitle)) {
					return new Entry(entry);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param relAttributeValue
	 *            the value of the rel attribute.
	 * @return the Link object based on the semantics of the rel attribute of
	 *         the link element. See <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php#element.link"
	 *         >atom:link</a>.
	 */
	public Link getLink(String relAttributeValue) {
		return sourceAdaptor.getLink(relAttributeValue);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 */
	public Extension getExtension(String extName) {
		return sourceAdaptor.getExtension(extName);
	}

	/**
	 * Shows the contents of the &lt;feed> element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<feed");
		sb.append(sourceAdaptor);

		if (generator != null) {
			sb.append(generator);
		}

		if (subtitle != null) {
			sb.append(subtitle);
		}

		if (icon != null) {
			sb.append(icon);
		}

		if (logo != null) {
			sb.append(logo);
		}

		if (entries != null) {
			for (Entry entry : entries) {
				sb.append(entry);
			}
		}

		sb.append("</feed>");
		return sb.toString();
	}
}
