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
 * This class represents an Atom 1.0 subtitle element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomSubsubtitle = element atom:subsubtitle { atomTextConstruct }
 * </pre>
 */
public class Subtitle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1899291850703846655L;
	private final AtomTextConstruct subtitle;

	// use the factory method in the FeedDoc.
	Subtitle(String subtitle, List<Attribute> attributes)
			throws AtomSpecException {
		this.subtitle = new AtomTextConstruct(subtitle, attributes, false);
	}

	// copy constructor
	Subtitle(Subtitle subtitle) throws AtomSpecException {
		this.subtitle = new AtomTextConstruct(subtitle.getText(), subtitle
				.getAttributes(), subtitle.getDivWrapperStart(), subtitle
				.getDivWrapperEnd(), false);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return subtitle.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Attribute> getAttributes() throws AtomSpecException {
		return subtitle.getAttributes();
	}

	String getDivWrapperStart() {
		return subtitle.getDivWrapperStart();
	}

	String getDivWrapperEnd() {
		return subtitle.getDivWrapperEnd();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getAttribute(String attrName) throws AtomSpecException {
		return subtitle.getAttribute(attrName);
	}
}
