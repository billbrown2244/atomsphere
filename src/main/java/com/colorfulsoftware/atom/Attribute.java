/**
 * Copyright 2011 Bill Brown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
			throw new AtomSpecException("Attribute names SHOULD NOT be blank.");
		}
		this.name = name;
		this.value = (value == null) ? "" : value;
	}

	Attribute(Attribute attribute) {
		this.name = attribute.name;
		this.value = attribute.value;
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

	/**
	 * Shows the contents of the element's attribute in the form of '
	 * attrName="attrValue"'.
	 */
	@Override
	public String toString() {
		return " " + name + "=\"" + value + "\"";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Attribute)) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
}
