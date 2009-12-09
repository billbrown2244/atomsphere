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
 *  2006-11-14 wbrown - added javadoc documentation.
 *  2008-04-09 wbrown - added throws clause to constructor.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 icon element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomIcon = element atom:icon {
 *          atomCommonAttributes,
 *          (atomUri)
 *          }
 * </pre>
 */
public class Icon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5842043454182097449L;
	private final AtomURIConstruct icon;

	// use the factory method in the FeedDoc.
	Icon(List<Attribute> attributes, String atomUri) throws AtomSpecException {
		this.icon = new AtomURIConstruct(attributes, atomUri);
	}

	Icon(Icon icon) {
		this.icon = new AtomURIConstruct(icon.icon);
	}

	/**
	 * 
	 * @return the unique identifier for this document.
	 */
	public String getAtomUri() {
		return icon.getAtomUri();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return icon.getAttributes();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return icon.getAttribute(attrName);
	}

	/**
	 * Shows the contents of the &lt;icon> element.
	 */
	@Override
	public String toString() {
		return "<icon" + icon + "</icon>";
	}
}
