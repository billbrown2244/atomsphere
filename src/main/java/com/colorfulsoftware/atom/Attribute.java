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
 *  2007-02-20 wbrown - added override of equals method.
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;

/**
 * This class represents an Atom 1.0 attribute.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomCommonAttributes =
 *          attribute xml:base { atomUri }?,
 *          attribute xml:lang { atomLanguageTag }?,
 *          undefinedAttribute*
 * </pre>
 */
public class Attribute implements Serializable {

	private static final long serialVersionUID = 5289914532308394264L;
	private final String name;
	private final String value;

	// use the factory method in the FeedDoc.
	Attribute(String name, String value) throws AtomSpecException {
		// specification customization
		if (name == null || name.equals("")) {
			throw new AtomSpecException(
					"Attributes SHOULD NOT be null and SHOULD NOT be blank.");
		}
		this.name = name;
		this.value = value;
	}

	Attribute(Attribute attribute) {
		this.name = attribute.getName();
		this.value = attribute.getValue();
	}

	/**
	 * 
	 * @return the name of this attribute
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the value of this attribute
	 */
	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Attribute) {
			Attribute local = (Attribute) obj;
			return local.name.equals(this.name)
					&& local.value.equals(this.value);
		}
		return false;
	}

	/**
	 * Shows the contents of the element's attribute in the form of '
	 * attrName="attrValue"'.
	 */
	@Override
	public String toString() {
		return " " + name + "=\"" + value + "\"";
	}
}
