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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 title element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomTitle = element atom:title { atomTextConstruct }
 * </pre>
 */
public class Title implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 823649575536535174L;
	private final AtomTextConstruct title;

	// use the factory method in the FeedDoc.
	Title(String title, List<Attribute> attributes) throws AtomSpecException {
		this.title = new AtomTextConstruct(title, attributes, false);
	}

	// copy constructor
	Title(Title title) throws AtomSpecException {
		this.title = new AtomTextConstruct(title.getText(), title
				.getAttributes(), title.getDivWrapperStart(), title
				.getDivWrapperEnd(), false);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return title.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return title.getAttributes();
	}

	String getDivWrapperStart() {
		return title.getDivWrapperStart();
	}

	String getDivWrapperEnd() {
		return title.getDivWrapperEnd();
	}
	
	public Attribute getAttribute(String attrName) {
		return title.getAttribute(attrName);
	}
}
