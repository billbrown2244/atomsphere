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

/**
 * This class represents an Atom 1.0 uri element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      the content of the uri according to Section 7 of [RFC3986]
 * </pre>
 */
public class URI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3212774124149440834L;
	private final AtomPlainText uri;

	// use the factory method in the FeedDoc.
	URI(String uri) {
		this.uri = new AtomPlainText(uri);
	}

	/**
	 * 
	 * @return the uri text.
	 */
	public String getText() {
		return uri.getText();
	}
	
	@Override
	public String toString() {
		return "<uri>" + uri.getText() + "</uri>";
	}
}
