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
 * This class represents an Atom 1.0 generator element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomGenerator = element atom:generator {
 *          atomCommonAttributes,
 *          attribute uri { atomUri }?,
 *          attribute version { text }?,
 *          text
 *          }
 * </pre>
 */
public class Generator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6555330269825432901L;
	private final List<Attribute> attributes;
	private final Attribute uri;
	private final Attribute version;
	private final String text;

	// use the factory method in the FeedDoc.
	Generator(List<Attribute> attributes, String text) throws AtomSpecException {
		FeedDoc feedDoc = new FeedDoc();
		this.attributes = new LinkedList<Attribute>();
		if (attributes != null) {
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!feedDoc.isAttributeSupported(this, attr)) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " in the atom:generator element");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		this.uri = feedDoc.getAttributeFromGroup(this.attributes, "uri");

		this.version = feedDoc
				.getAttributeFromGroup(this.attributes, "version");
		this.text = text;
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
	public Attribute getUri() throws AtomSpecException {
		return (uri == null) ? null : new Attribute(uri.getName(), uri
				.getValue());
	}

	/**
	 * 
	 * @return the scheme attribute
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getVersion() throws AtomSpecException {
		return (version == null) ? null : new Attribute(version.getName(),
				version.getValue());
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return text;
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
