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
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorful.atom;

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

		if (attributes == null) {
			this.attributes = new LinkedList<Attribute>();
		} else {
			this.attributes = new LinkedList<Attribute>();
			for(Attribute attr: attributes){
				// check for unsupported attribute.
				if (!FeedDoc.isAtomCommonAttribute(attr)
						&& !FeedDoc.isUndefinedAttribute(attr)
						&& !attr.getName().equals("term")
						&& !attr.getName().equals("scheme")
						&& !attr.getName().equals("label")) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " in the atom:category element");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		if ((this.term = FeedDoc.getAttributeFromGroup(this.attributes, "term")) == null) {
			throw new AtomSpecException(
					"Category elements MUST have a \"term\" attribute.");
		}

		this.scheme = FeedDoc.getAttributeFromGroup(this.attributes, "scheme");

		this.label = FeedDoc.getAttributeFromGroup(this.attributes, "label");

		this.content = content;
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {
		if (attributes == null) {
			return null;
		}
		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		for(Attribute attr: this.attributes){
			attrsCopy.add(new Attribute(attr.getName(), attr.getValue()));
		}
		return attrsCopy;
	}

	/**
	 * 
	 * @return the label attribute
	 */
	public Attribute getLabel() {
		return (label == null) ? null : new Attribute(label.getName(), label
				.getValue());
	}

	/**
	 * 
	 * @return the scheme attribute
	 */
	public Attribute getScheme() {
		return (scheme == null) ? null : new Attribute(scheme.getName(), scheme
				.getValue());
	}

	/**
	 * 
	 * @return the term attribute
	 */
	public Attribute getTerm() {
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

}
