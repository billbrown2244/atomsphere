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
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 category.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomCategory =
 *          element atom:category {
 *          atomCommonAttributes,
 *          attribute term { text },
 *          attribute scheme { atomUri }?,
 *          attribute label { text }?,
 *          undefinedContent
 * </pre>
 */
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7475777621616906621L;
	private final List<Attribute> attributes;
	private final Attribute term; // required
	private final Attribute scheme;
	private final Attribute label;
	private final String content;

	// use the factory method in the FeedDoc.
	Category(List<Attribute> attributes, String content)
			throws AtomSpecException {

		FeedDoc feedDoc = new FeedDoc();
		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!new AttributeSupport(attr).verify(this)) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " in the atom:category element");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		if ((this.term = feedDoc.getAttributeFromGroup(this.attributes, "term")) == null) {
			throw new AtomSpecException(
					"Category elements MUST have a \"term\" attribute.");
		}

		this.scheme = feedDoc.getAttributeFromGroup(this.attributes, "scheme");

		this.label = feedDoc.getAttributeFromGroup(this.attributes, "label");

		this.content = content;
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
	 * @return the label attribute
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getLabel() throws AtomSpecException {
		return (label == null) ? null : new Attribute(label.getName(), label
				.getValue());
	}

	/**
	 * 
	 * @return the scheme attribute
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getScheme() throws AtomSpecException {
		return (scheme == null) ? null : new Attribute(scheme.getName(), scheme
				.getValue());
	}

	/**
	 * 
	 * @return the term attribute
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getTerm() throws AtomSpecException {
		return (term == null) ? null : new Attribute(term.getName(), term
				.getValue());
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
}
