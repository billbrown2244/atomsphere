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

		// none of these first three will be null.
		this.id = new Id(id);
		this.title = new Title(title);
		this.updated = new Updated(updated);

		this.rights = (rights == null) ? null : new Rights(rights);

		if (authors == null) {
			this.authors = null;
		} else {
			this.authors = new LinkedList<Author>();
			for (Author author : authors) {
				this.authors.add(new Author(author));
			}
		}

		if (categories == null) {
			this.categories = null;
		} else {
			this.categories = new LinkedList<Category>();
			for (Category category : categories) {
				this.categories.add(new Category(category));
			}
		}

		if (contributors == null) {
			this.contributors = null;
		} else {
			this.contributors = new LinkedList<Contributor>();
			for (Contributor contributor : contributors) {
				this.contributors.add(new Contributor(contributor));
			}
		}

		if (links == null) {
			this.links = null;
		} else {
			this.links = new LinkedList<Link>();
			for (Link link : links) {
				this.links.add(new Link(link));
			}
		}

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!new AttributeSupport(attr).verify(this)) {
					throw new AtomSpecException("Unsupported attribute "
							+ attr.getName() + " for this element.");
				}
				this.attributes.add(new Attribute(attr));
			}
		}

		if (extensions == null) {
			this.extensions = null;
		} else {
			this.extensions = new LinkedList<Extension>();
			for (Extension extension : extensions) {
				this.extensions.add(new Extension(extension));
			}
		}
	}

	AtomEntrySourceAdaptor(AtomEntrySourceAdaptor atomEntrySourceAdaptor) {
		this.attributes = atomEntrySourceAdaptor.getAttributes();
		this.authors = atomEntrySourceAdaptor.getAuthors();
		this.categories = atomEntrySourceAdaptor.getCategories();
		this.contributors = atomEntrySourceAdaptor.getContributors();
		this.links = atomEntrySourceAdaptor.getLinks();
		this.extensions = atomEntrySourceAdaptor.getExtensions();
		this.id = atomEntrySourceAdaptor.getId();
		this.title = atomEntrySourceAdaptor.getTitle();
		this.updated = atomEntrySourceAdaptor.getUpdated();
		this.rights = atomEntrySourceAdaptor.getRights();
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attr : this.attributes) {
				attrsCopy.add(new Attribute(attr));
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
			authrosCopy.add(new Author(author));
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
			catsCopy.add(new Category(category));
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
			contsCopy.add(new Contributor(contributor));
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
			extsCopy.add(new Extension(extension));
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
			linksCopy.add(new Link(link));
		}
		return linksCopy;
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 */
	public Id getId() {
		return new Id(id);
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 */
	public Rights getRights() {
		return (rights == null) ? null : new Rights(rights);
	}

	/**
	 * 
	 * @return the title for this element.
	 */
	public Title getTitle() {
		return new Title(title);
	}

	/**
	 * 
	 * @return the updated date for this element.
	 */
	public Updated getUpdated() {
		return new Updated(updated);
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName().equals(attrName)) {
					return new Attribute(attribute);
				}
			}
		}
		return null;
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Author object if the name matches or null if not found.
	 */
	public Author getAuthor(String name) {
		if (this.authors != null) {
			for (Author author : this.authors) {
				if (author.getName() != null
						&& author.getName().getText() != null
						&& author.getName().getText().equals(name)) {
					return new Author(author);
				}
			}
		}
		return null;
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if the term matches or null if not found.
	 */
	public Category getCategory(String termValue) {
		if (this.categories != null) {
			for (Category category : this.categories) {
				if (category.getAttribute("term") != null
						&& category.getAttribute("term").getValue().equals(
								termValue)) {
					return new Category(category);
				}
			}
		}
		return null;
	}

	/**
	 * @param name
	 *            the name of the contributor
	 * @return the Contributor object if name matches or null if not found.
	 */
	public Contributor getContributor(String name) {
		if (this.contributors != null) {
			for (Contributor contributor : this.contributors) {
				if (contributor.getName() != null
						&& contributor.getName().getText() != null
						&& contributor.getName().getText().equals(name)) {
					return new Contributor(contributor);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param relAttributeValue the value of the rel attribute.
	 * @return the Link object based on the semantics of the rel attribute of
	 *         the link element. See <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php#element.link"
	 *         >atom:link</a>.
	 */
	public Link getLink(String relAttributeValue) {
		if (relAttributeValue == null) {
			return null;
		}
		if (this.links != null) {
			for (Link link : this.links) {
				if (relAttributeValue.equals("self")
						&& link.getRel().getValue() != null
						&& link.getRel().getValue().equals("self")) {
					return new Link(link);
				}
				if (relAttributeValue.equals("alternate")
						&& link.getRel().getValue() != null
						&& link.getRel().getValue().equals("alternate")) {
					return new Link(link);
				}
			}
		}
		return null;
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 */
	public Extension getExtension(String extName) {
		if (this.extensions != null) {
			for (Extension extension : this.extensions) {
				if (extension.getElementName().equals(extName)) {
					return new Extension(extension);
				}
			}
		}
		return null;
	}

	/**
	 * Shows the contents of the &lt;entry> or &lt;source> elements.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute);
			}
		}
		// close the parent element
		sb.append(">");

		if (id != null) {
			sb.append(id);
		}

		if (title != null) {
			sb.append(title);
		}

		if (updated != null) {
			sb.append(updated);
		}

		if (rights != null) {
			sb.append(rights);
		}

		if (categories != null) {
			for (Category category : categories) {
				sb.append(category);
			}
		}

		if (authors != null) {
			for (Author author : authors) {
				sb.append(author);
			}
		}

		if (contributors != null) {
			for (Contributor contributor : contributors) {
				sb.append(contributor);
			}
		}

		if (links != null) {
			for (Link link : links) {
				sb.append(link);
			}
		}

		if (extensions != null) {
			for (Extension extension : extensions) {
				sb.append(extension);
			}
		}

		return sb.toString();
	}
}
