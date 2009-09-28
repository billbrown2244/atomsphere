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
 *  2008-04-08 wbrown - added exception for unsupported attribute.
 *  2008-04-09 wbrown - added throws clause to constructor and check for non supported attribute.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 link element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	    atomLink =
 *          element atom:link {
 *          atomCommonAttributes,
 *          attribute href { atomUri },
 *          attribute rel { atomNCName | atomUri }?,
 *          attribute type { atomMediaType }?,
 *          attribute hreflang { atomLanguageTag }?,
 *          attribute title { text }?,
 *          attribute length { text }?,
 *          undefinedContent
 *         }
 * </pre>
 */
public class Link implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7730515433344653457L;
	private final List<Attribute> attributes;
	private final Attribute href;
	private final Attribute rel;
	private final Attribute type;
	private final Attribute hreflang;
	private final Attribute title;
	private final Attribute length;
	private final String content;

	// use the factory method in the FeedDoc.
	Link(List<Attribute> attributes, String content) throws AtomSpecException {
		FeedDoc feedDoc = new FeedDoc();
		System.out.println("attributes here: "+attributes);
		if (attributes == null) {
			this.attributes = new LinkedList<Attribute>();
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				System.out.println("attr here: "+attr);
				
				if (!feedDoc.isAtomCommonAttribute(attr)
						&& !feedDoc.isUndefinedAttribute(attr)
						&& !attr.getName().equals("href")
						&& !attr.getName().equals("rel")
						&& !attr.getName().equals("type")
						&& !attr.getName().equals("hreflang")
						&& !attr.getName().equals("title")
						&& !attr.getName().equals("length")) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " for this link element.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		if ((this.href = feedDoc.getAttributeFromGroup(this.attributes, "href")) == null) {
			throw new AtomSpecException(
					"atom:link elements MUST have an href attribute, whose value MUST be a IRI reference");
		}

		this.rel = feedDoc.getAttributeFromGroup(this.attributes, "rel");

		this.type = feedDoc.getAttributeFromGroup(this.attributes, "type");

		this.hreflang = feedDoc.getAttributeFromGroup(this.attributes,
				"hreflang");

		this.title = feedDoc.getAttributeFromGroup(this.attributes, "title");

		this.length = feedDoc.getAttributeFromGroup(this.attributes, "length");

		this.content = content;
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attr : this.attributes) {
				try {
					attrsCopy
							.add(new Attribute(attr.getName(), attr.getValue()));
				} catch (AtomSpecException e) {
					// this should not happen.
					return null;
				}
			}
		}
		return (this.attributes == null) ? null : attrsCopy;
	}

	/**
	 * 
	 * @return the href contains the link's IRI
	 */
	public Attribute getHref() {
		try {
			return (href == null) ? null : new Attribute(href.getName(), href
					.getValue());
		} catch (AtomSpecException e) {
			// this should not happen.
			return null;
		}
	}

	/**
	 * 
	 * @return the hreflang describes the language of the resource pointed to by
	 *         the href attribute.
	 */
	public Attribute getHreflang() {
		try {
			return (hreflang == null) ? null : new Attribute(
					hreflang.getName(), hreflang.getValue());
		} catch (AtomSpecException e) {
			// this should not happen.
			return null;
		}
	}

	/**
	 * 
	 * @return the length indicates an advisory length of the linked content in
	 *         octets.
	 */
	public Attribute getLength() {
		try {
			return (length == null) ? null : new Attribute(length.getName(),
					length.getValue());
		} catch (AtomSpecException e) {
			// this should not happen.
			return null;
		}
	}

	/**
	 * 
	 * @return the rel which matches either the "isegment-nz-nc" or the "IRI"
	 *         production in [RFC3987]
	 */
	public Attribute getRel() {
		try {
			return (rel == null) ? null : new Attribute(rel.getName(), rel
					.getValue());
		} catch (AtomSpecException e) {
			// this should not happen.
			return null;
		}
	}

	/**
	 * 
	 * @return the title conveys human-readable information about the link.
	 */
	public Attribute getTitle() {
		try {
			return (title == null) ? null : new Attribute(title.getName(),
					title.getValue());
		} catch (AtomSpecException e) {
			// this should not happen.
			return null;
		}
	}

	/**
	 * 
	 * @return the type which is an advisory media type
	 */
	public Attribute getType() {
		try {
			return (type == null) ? null : new Attribute(type.getName(), type
					.getValue());
		} catch (AtomSpecException e) {
			// this should not happen.
			return null;
		}
	}

	/**
	 * 
	 * @return undefined text content or undefined element.
	 */
	public String getContent() {
		return content;
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
					try {
						return new Attribute(attribute.getName(), attribute
								.getValue());
					} catch (AtomSpecException e) {
						// this should not happen.
					}
				}
			}
		}
		return null;
	}
}
