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
 *  2008-03-17 wbrown - added to share elements between entry and source.
 *  2008-04-09 wbrown - wrapped checked exceptions.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class serves as an adaptor for sharing the implementation of the comment
 * elements between the entry and source element.
 * 
 * @author Bill Brown
 * 
 */
class AtomEntrySourceAdaptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4534581935906399126L;

	private final List<Attribute> attributes;

	private final List<Author> authors;

	private final List<Category> categories;

	private final List<Contributor> contributors;

	private final List<Link> links;

	private final List<Extension> extensions;

	private final Id id;

	private final Title title;

	private final Updated updated;

	private final Rights rights;

	AtomEntrySourceAdaptor(Id id, Title title, Updated updated, Rights rights,
			List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {

		FeedDoc feedDoc = new FeedDoc();
		// none of these first three will be null.
		this.id = new Id(id.getAttributes(), id.getAtomUri());
		this.title = new Title(title);
		this.updated = new Updated(updated.getDateTime(), updated
				.getAttributes());

		this.rights = (rights == null) ? null : new Rights(rights);

		if (authors == null) {
			this.authors = null;
		} else {
			this.authors = new LinkedList<Author>();
			for (Author author : authors) {
				this.authors.add(new Author(author.getName(), author.getUri(),
						author.getEmail(), author.getAttributes(), author
								.getExtensions()));
			}
		}

		if (categories == null) {
			this.categories = null;
		} else {
			this.categories = new LinkedList<Category>();
			for (Category category : categories) {
				this.categories.add(new Category(category.getAttributes(),
						category.getContent()));
			}
		}

		if (contributors == null) {
			this.contributors = null;
		} else {
			this.contributors = new LinkedList<Contributor>();
			for (Contributor contributor : contributors) {
				this.contributors.add(new Contributor(contributor.getName(),
						contributor.getUri(), contributor.getEmail(),
						contributor.getAttributes(), contributor
								.getExtensions()));
			}
		}

		if (links == null) {
			this.links = null;
		} else {
			this.links = new LinkedList<Link>();
			for (Link link : links) {
				this.links
						.add(new Link(link.getAttributes(), link.getContent()));
			}
		}

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!feedDoc.isAttributeSupported(this, attr)) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " for this element.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		if (extensions == null) {
			this.extensions = null;
		} else {
			this.extensions = new LinkedList<Extension>();
			for (Extension extension : extensions) {
				this.extensions.add(new Extension(extension.getElementName(),
						extension.getAttributes(), extension.getContent()));
			}
		}
	}

	/**
	 * 
	 * @return the category attribute list.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Attribute> getAttributes() throws AtomSpecException {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attr : this.attributes) {
				attrsCopy.add(new Attribute(attr.getName(), attr.getValue()));
			}
		}
		return (this.attributes == null) ? null : attrsCopy;
	}

	/**
	 * 
	 * @return the authors for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Author> getAuthors() throws AtomSpecException {
		if (authors == null) {
			return null;
		}
		List<Author> authrosCopy = new LinkedList<Author>();
		for (Author author : authors) {
			authrosCopy.add(new Author(author.getName(), author.getUri(),
					author.getEmail(), author.getAttributes(), author
							.getExtensions()));
		}
		return authrosCopy;
	}

	/**
	 * 
	 * @return the categories for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Category> getCategories() throws AtomSpecException {
		if (categories == null) {
			return null;
		}
		List<Category> catsCopy = new LinkedList<Category>();
		for (Category category : this.categories) {
			catsCopy.add(new Category(category.getAttributes(), category
					.getContent()));
		}
		return catsCopy;
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Contributor> getContributors() throws AtomSpecException {
		if (contributors == null) {
			return null;
		}
		List<Contributor> contsCopy = new LinkedList<Contributor>();
		for (Contributor contributor : this.contributors) {
			contsCopy.add(new Contributor(contributor.getName(), contributor
					.getUri(), contributor.getEmail(), contributor
					.getAttributes(), contributor.getExtensions()));
		}
		return contsCopy;
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Extension> getExtensions() throws AtomSpecException {
		if (extensions == null) {
			return null;
		}
		List<Extension> extsCopy = new LinkedList<Extension>();
		for (Extension extension : this.extensions) {
			extsCopy.add(new Extension(extension.getElementName(), extension
					.getAttributes(), extension.getContent()));
		}
		return extsCopy;
	}

	/**
	 * 
	 * @return the links for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Link> getLinks() throws AtomSpecException {
		if (links == null) {
			return null;
		}
		List<Link> linksCopy = new LinkedList<Link>();
		for (Link link : this.links) {
			linksCopy.add(new Link(link.getAttributes(), link.getContent()));
		}
		return linksCopy;
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Id getId() throws AtomSpecException {
		return new Id(id.getAttributes(), id.getAtomUri());
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Rights getRights() throws AtomSpecException {
		return (rights == null) ? null : new Rights(rights);
	}

	/**
	 * 
	 * @return the title for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Title getTitle() throws AtomSpecException {
		return new Title(title);
	}

	/**
	 * 
	 * @return the updated date for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Updated getUpdated() throws AtomSpecException {
		return new Updated(updated.getDateTime(), updated.getAttributes());
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getAttribute(String attrName) throws AtomSpecException {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName().equals(attrName)) {
					return new Attribute(attribute.getName(), attribute
							.getValue());
				}
			}
		}
		return null;
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Author object if the name matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Author getAuthor(String name) throws AtomSpecException {
		if (this.authors != null) {
			for (Author author : this.authors) {
				if (author.getName() != null
						&& author.getName().getText() != null
						&& author.getName().getText().equals(name)) {
					return new Author(author.getName(), author.getUri(), author
							.getEmail(), author.getAttributes(), author
							.getExtensions());
				}
			}
		}
		return null;
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if the term matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Category getCategory(String termValue) throws AtomSpecException {
		if (this.categories != null) {
			for (Category category : this.categories) {
				if (category.getAttribute("term") != null
						&& category.getAttribute("term").getValue().equals(
								termValue)) {
					return new Category(category.getAttributes(), category
							.getContent());
				}
			}
		}
		return null;
	}

	/**
	 * @param name
	 *            the name of the contributor
	 * @return the Contributor object if name matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Contributor getContributor(String name) throws AtomSpecException {
		if (this.contributors != null) {
			for (Contributor contributor : this.contributors) {
				if (contributor.getName() != null
						&& contributor.getName().getText() != null
						&& contributor.getName().getText().equals(name)) {
					return new Contributor(contributor.getName(), contributor
							.getUri(), contributor.getEmail(), contributor
							.getAttributes(), contributor.getExtensions());
				}
			}
		}
		return null;
	}

	/**
	 * @param hrefVal
	 *            the href attribute value to look for.
	 * @return the Link object if href matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Link getLink(String hrefVal) throws AtomSpecException {
		if (this.links != null) {
			for (Link link : this.links) {
				if (link.getHref().getValue() != null
						&& link.getHref().getValue().equals(hrefVal)) {
					return new Link(link.getAttributes(), link.getContent());
				}
			}
		}
		return null;
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Extension getExtension(String extName) throws AtomSpecException {
		if (this.extensions != null) {
			for (Extension extension : this.extensions) {
				if (extension.getElementName().equals(extName)) {
					return new Extension(extension.getElementName(), extension
							.getAttributes(), extension.getContent());
				}
			}
		}
		return null;
	}
}
