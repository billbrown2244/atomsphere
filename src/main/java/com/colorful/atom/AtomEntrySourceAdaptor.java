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
/* Change History:
 *  2008-03-17 wbrown - added to share elements between entry and source.
 *  2008-04-09 wbrown - wrapped checked exceptions.
 */
package com.colorful.atom;

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

	public AtomEntrySourceAdaptor(Id id, Title title, Updated updated,
			Rights rights, List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {
		this.id = (id == null) ? null : new Id(id.getAttributes(), id
				.getAtomUri());
		this.title = (title == null) ? null : new Title(title);
		this.updated = (updated == null) ? null : new Updated(updated
				.getDateTime(), updated.getAttributes());
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
				if (!FeedDoc.isAtomCommonAttribute(attr)
						&& !FeedDoc.isUndefinedAttribute(attr)) {
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
	 */
	public List<Attribute> getAttributes() {

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
	 */
	public List<Author> getAuthors() {
		if (authors == null) {
			return null;
		}
		List<Author> authrosCopy = new LinkedList<Author>();
		for (Author author : authors) {
			try {
				authrosCopy.add(new Author(author.getName(), author.getUri(),
						author.getEmail(), author.getAttributes(), author
								.getExtensions()));
			} catch (Exception e) {
				// this should never happen because
				// we check for errors on initial creation
				// but if it does, print the stack trace
				e.printStackTrace();
			}
		}
		return authrosCopy;
	}

	/**
	 * 
	 * @return the categories for this element.
	 */
	public List<Category> getCategories() {
		if (categories == null) {
			return null;
		}
		List<Category> catsCopy = new LinkedList<Category>();
		for (Category category : this.categories) {
			try {
				catsCopy.add(new Category(category.getAttributes(), category
						.getContent()));
			} catch (Exception e) {
				// this should never happen because
				// we check for errors on initial creation
				// but if it does, print the stack trace
				e.printStackTrace();
			}
		}
		return catsCopy;
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 */
	public List<Contributor> getContributors() {
		if (contributors == null) {
			return null;
		}
		List<Contributor> contsCopy = new LinkedList<Contributor>();
		for (Contributor contributor : this.contributors) {
			try {
				contsCopy.add(new Contributor(contributor.getName(),
						contributor.getUri(), contributor.getEmail(),
						contributor.getAttributes(), contributor
								.getExtensions()));
			} catch (Exception e) {
				// this should never happen because
				// we check for errors on initial creation
				// but if it does, print the stack trace
				e.printStackTrace();
			}
		}
		return contsCopy;
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 */
	public List<Extension> getExtensions() {
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
	 */
	public List<Link> getLinks() {
		if (links == null) {
			return null;
		}
		List<Link> linksCopy = new LinkedList<Link>();
		for (Link link : this.links) {
			try {
				linksCopy
						.add(new Link(link.getAttributes(), link.getContent()));
			} catch (Exception e) {
				// this should never happen because
				// we check for errors on initial creation
				// but if it does, print the stack trace
				e.printStackTrace();
			}
		}
		return linksCopy;
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 */
	public Id getId() {
		try {
			return (id == null) ? null : new Id(id.getAttributes(), id
					.getAtomUri());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 */
	public Rights getRights() {
		try {
			return (rights == null) ? null : new Rights(rights.getText(),
					rights.getAttributes());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the title for this element.
	 */
	public Title getTitle() {
		try {
			return (title == null) ? null : new Title(title);
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the updated date for this element.
	 */
	public Updated getUpdated() {
		try {
			return (updated == null) ? null : new Updated(
					updated.getDateTime(), updated.getAttributes());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	public Attribute getAttribute(String attrName) {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName() != null
						&& attribute.getName().equals(attrName)) {
					return new Attribute(attribute.getName(), attribute
							.getValue());
				}
			}
		}
		return null;
	}

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

	public Extension getExtension(String extName) {
		if (this.extensions != null) {
			for (Extension extension : this.extensions) {
				if (extension.getElementName() != null
						&& extension.getElementName().equals(extName)) {
					return new Extension(extension.getElementName(), extension
							.getAttributes(), extension.getContent());
				}
			}
		}
		return null;
	}
}
