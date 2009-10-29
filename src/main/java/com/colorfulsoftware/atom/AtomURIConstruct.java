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
 *  2008-03-16 wbrown - Introduced to share between Id, Icon and Logo.
 *  2008-04-09 wbrown - added checked exception for non atomCommonAttribute.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

class AtomURIConstruct implements Serializable {

	private static final long serialVersionUID = -6063992140422293183L;
	private final List<Attribute> attributes;
	private final String atomUri;

	AtomURIConstruct(List<Attribute> attributes, String atomUri)
			throws AtomSpecException {

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

		this.atomUri = atomUri;
	}

	AtomURIConstruct(AtomURIConstruct atomURIConstruct) {
		this.attributes = atomURIConstruct.getAttributes();
		this.atomUri = atomURIConstruct.getAtomUri();
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
	 * @return the unique identifier for this document.
	 */
	public String getAtomUri() {
		return atomUri;
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
	 * Shows the contents of the <id>, <icon> or <logo> elements.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute.toString());
			}
		}
		// close the parent element
		sb.append(">" + atomUri);

		return sb.toString();
	}
}
