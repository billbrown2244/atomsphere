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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This class represents an Atom 1.0 published element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomPublished = element atom:published { atomDateConstruct}
 * </pre>
 */
public class Published implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787924946511618248L;
	private final AtomDateConstruct published;

	// use the factory method in the FeedDoc.
	Published(List<Attribute> attributes, String published)
			throws AtomSpecException {
		this.published = new AtomDateConstruct(attributes, published);
	}

	Published(Published published) {
		this.published = new AtomDateConstruct(published.published);
	}

	/**
	 * 
	 * @return the date timestamp for this element.
	 */
	public Date getDateTime() {
		return published.getDateTime();
	}

	/**
	 * 
	 * @return the string formated version of the time for example
	 *         2006-04-28T12:50:43.337-05:00
	 */
	public String getText() {
		return published.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return published.getAttributes();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return published.getAttribute(attrName);
	}

	/**
	 * Shows the contents of the &lt;published> element.
	 */
	@Override
	public String toString() {
		return "<published" + published + "</published>";
	}
}
