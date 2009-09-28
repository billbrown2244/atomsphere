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

	/**
	 * 
	 */
	private static final long serialVersionUID = -6063992140422293183L;
	private final List<Attribute> attributes;
	private final String atomUri;

	/**
	 * 
	 * @param attributes
	 *            for this uri element.
	 * @param atomUri
	 *            the unique identifier for the document.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public AtomURIConstruct(List<Attribute> attributes, String atomUri)
			throws AtomSpecException {

		FeedDoc feedDoc = new FeedDoc();
		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!feedDoc.isAtomCommonAttribute(attr)
						&& !feedDoc.isUndefinedAttribute(attr)) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " for this element.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		this.atomUri = atomUri;
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
				}
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
