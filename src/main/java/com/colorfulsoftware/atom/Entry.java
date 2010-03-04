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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 entry element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomEntry =
 *          element atom:entry {
 *          atomCommonAttributes,
 *          (atomAuthor*
 *          &amp; atomCategory*
 *          &amp; atomContent?
 *          &amp; atomContributor*
 *          &amp; atomId
 *          &amp; atomLink*
 *          &amp; atomPublished?
 *          &amp; atomRights?
 *          &amp; atomSource?
 *          &amp; atomSummary?
 *          &amp; atomTitle
 *          &amp; atomUpdated
 *          &amp; extensionElement*)
 *          }
 * </pre>
 */
public class Entry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5291388675692200218L;
	private final AtomEntrySourceAdaptor entryAdaptor;
	private final Content content;
	private final Published published;
	private final Source source;
	private final Summary summary;

	private List<String> unboundPrefixes;

	// use the factory method in the FeedDoc.
	Entry(Id id, Title title, Updated updated, Rights rights, Content content,
			List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Published published, Summary summary, Source source)
			throws AtomSpecException {

		// check for functional requirements here because
		// they are all optional for a Source element.

		// make sure id is present
		if (id == null) {
			throw new AtomSpecException(
					"atom:entry elements MUST contain exactly one atom:id element.");
		}
		// make sure title is present
		if (title == null) {
			throw new AtomSpecException(
					"atom:entry elements MUST contain exactly one atom:title element.");
		}
		// make sure updated is present
		// it is actually checked further up in the reader because of how
		// we store entries.

		this.entryAdaptor = new AtomEntrySourceAdaptor(id, title, updated,
				rights, authors, categories, contributors, links, attributes,
				extensions);

		if (content == null) {
			this.content = null;
		} else {

			if (content.getAttributes() != null) {
				for (Attribute attr : content.getAttributes()) {
					// check for src attribute
					if (attr.getName().equals("src") && summary == null) {
						throw new AtomSpecException(
								"atom:entry elements MUST contain an atom:summary element if the atom:entry contains an atom:content that has a \"src\" attribute (and is thus empty).");
					}
				}
			}

			this.content = new Content(content);
		}

		this.published = (published == null) ? null : new Published(published);
		this.source = (source == null) ? null : new Source(source);
		this.summary = (summary == null) ? null : new Summary(summary);

		// check that the extension prefixes are bound to a namespace
		this.unboundPrefixes = new LinkedList<String>();

		if (source != null && source.getUnboundPrefixes() != null) {
			this.unboundPrefixes.addAll(source.getUnboundPrefixes());
		}

		if (entryAdaptor.getUnboundPrefixes() != null) {
			this.unboundPrefixes.addAll(entryAdaptor.getUnboundPrefixes());
		}
		this.unboundPrefixes = (this.unboundPrefixes.size() == 0) ? null
				: this.unboundPrefixes;
	}

	Entry(Entry entry) {
		this.content = entry.getContent();
		this.entryAdaptor = entry.getEntryAdaptor();
		this.published = entry.getPublished();
		this.source = entry.getSource();
		this.summary = entry.getSummary();
		this.unboundPrefixes = entry.getUnboundPrefixes();
	}

	AtomEntrySourceAdaptor getEntryAdaptor() {
		return new AtomEntrySourceAdaptor(this.entryAdaptor);
	}

	/**
	 * 
	 * @return the content for this entry.
	 */
	public Content getContent() {
		return (content == null) ? null : new Content(content);
	}

	/**
	 * 
	 * @return the published date for this entry.
	 */
	public Published getPublished() {
		return (published == null) ? null : new Published(published);
	}

	/**
	 * 
	 * @return the source for this element.
	 */
	public Source getSource() {
		return (source == null) ? null : new Source(source);
	}

	/**
	 * 
	 * @return the summary for this element.
	 */
	public Summary getSummary() {
		return (summary == null) ? null : new Summary(summary);
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 */
	public Id getId() {
		return entryAdaptor.getId();
	}

	/**
	 * 
	 * @return the title for this element.
	 */
	public Title getTitle() {
		return entryAdaptor.getTitle();
	}

	/**
	 * 
	 * @return the updated date for this element.
	 */
	public Updated getUpdated() {
		return entryAdaptor.getUpdated();
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 */
	public Rights getRights() {
		return entryAdaptor.getRights();
	}

	/**
	 * 
	 * @return the authors for this entry.
	 */
	public List<Author> getAuthors() {
		return entryAdaptor.getAuthors();
	}

	/**
	 * 
	 * @return the categories for this element.
	 */
	public List<Category> getCategories() {
		return entryAdaptor.getCategories();
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 */
	public List<Contributor> getContributors() {
		return entryAdaptor.getContributors();
	}

	/**
	 * 
	 * @return the links for this entry.
	 */
	public List<Link> getLinks() {
		return entryAdaptor.getLinks();
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {
		return entryAdaptor.getAttributes();
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 */
	public List<Extension> getExtensions() {
		return entryAdaptor.getExtensions();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return entryAdaptor.getAttribute(attrName);
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Author object if the name matches or null if not found.
	 */
	public Author getAuthor(String name) {
		return entryAdaptor.getAuthor(name);
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if the term matches or null if not found.
	 */
	public Category getCategory(String termValue) {
		return entryAdaptor.getCategory(termValue);
	}

	/**
	 * @param name
	 *            the name of the contributor
	 * @return the Contributor object if name matches or null if not found.
	 */
	public Contributor getContributor(String name) {
		return entryAdaptor.getContributor(name);
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
		return entryAdaptor.getLink(relAttributeValue);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 */
	public Extension getExtension(String extName) {
		return entryAdaptor.getExtension(extName);
	}

	/**
	 * Shows the contents of the &lt;entry> element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<entry");
		sb.append(entryAdaptor);

		if (published != null) {
			sb.append(published);
		}
		if (summary != null) {
			sb.append(summary);
		}
		if (source != null) {
			sb.append(source);
		}
		if (content != null) {
			sb.append(content);
		}

		sb.append("</entry>");
		return sb.toString();
	}

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}

}
