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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 rights element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomRights = element atom:rights { atomTextConstruct }
 * </pre>
 */
public class Rights implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6917308035123622641L;
	private final AtomTextConstruct rights;

	// use the factory method in the FeedDoc.
	Rights(String rights, List<Attribute> attributes) throws AtomSpecException {
		this.rights = new AtomTextConstruct(rights, attributes, false);
	}

	Rights(Rights rights) {
		this.rights = new AtomTextConstruct(rights.rights);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return rights.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return rights.getAttributes();
	}

	String getDivStartName() {
		return rights.getDivStartName();
	}

	Attribute getDivStartAttribute() {
		return rights.getDivStartAttribute();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return rights.getAttribute(attrName);
	}

	AtomTextConstruct.ContentType getContentType() {
		return rights.getContentType();
	}

	/**
	 * Shows the contents of the <rights> element.
	 */
	@Override
	public String toString() {
		return "<rights" + rights.toString() + "</rights>";
	}
}
