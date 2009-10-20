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

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!new AttributeSupport(attr).verify(this)) {
					throw new AtomSpecException("Unsupported attribute "
							+ attr.getName()
							+ " in the atom:generator element.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		this.uri = getAttribute("uri");

		this.version = getAttribute("version");
		this.text = text;
	}

	Generator(Generator generator) {
		this.attributes = generator.getAttributes();
		this.uri = generator.getUri();
		this.version = generator.getVersion();
		this.text = generator.getText();
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
	 * @return the label attribute
	 */
	public Attribute getUri() {
		return (uri == null) ? null : new Attribute(uri);
	}

	/**
	 * 
	 * @return the scheme attribute
	 */
	public Attribute getVersion() {
		return (version == null) ? null : new Attribute(version);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<generator");
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute.toString());
			}
		}
		// if there is content add it.
		if (text == null || text.equals("")) {
			sb.append(" />");
		} else {
			sb.append(" >" + text + "</generator>");
		}

		return sb.toString();
	}
}
