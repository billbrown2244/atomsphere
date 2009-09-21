/**
 * Copyright (C) 2009 William R. Brown <info@colorfulsoftware.com>
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
 *  2007-02-19 wbrown - deprecated constructor and getter / setter for the xmlns field.
 *                      this should be set as an attribute in the feed element.
 *  2008-03-17 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 extension element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      simpleExtensionElement =
 *          element * - atom:* {
 *          text
 *          }
 *      
 *      structuredExtensionElement =
 *          element * - atom:* {
 *          (attribute * { text }+,
 *          (text|anyElement)*)
 *          | (attribute * { text }*,
 *          (text?, anyElement+, (text|anyElement)*))
 *          }
 * </pre>
 */
public class Extension implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7382104018423864548L;
	private final String elementName;
	private final List<Attribute> attributes;
	private final String content;

	// use the factory method in the FeedDoc.
	Extension(String elementName, List<Attribute> attributes, String content) {

		this.elementName = elementName;
		this.content = content;

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for(Attribute attr: attributes){
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attr : this.attributes) {
				attrsCopy.add(new Attribute(attr.getName(), attr.getValue()));
			}
		}
		return (this.attributes == null) ? null : attrsCopy;
	}

	public String getContent() {
		return content;
	}

	public String getElementName() {
		return elementName;
	}
	
	public Attribute getAttribute(String attrName) {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName() != null
						&& attribute.getName().equals(attrName)) {
					return new Attribute(attribute.getName(), attribute
							.getValue());
				}
			}
		}
		return null;
	}
}
