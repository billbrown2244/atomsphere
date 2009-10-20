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
 * This class represents an Atom 1.0 Contributor element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      &quot;atom:email&quot; element's content conveys an e-mail address associated with the person. 
 *      Person constructs MAY contain an atom:email element, but MUST NOT contain more than one. 
 *      Its content MUST conform to the &quot;addr-spec&quot; production in [RFC2822].
 * </pre>
 */
public class Email implements Serializable {

	private static final long serialVersionUID = -4534267548039292775L;
	private final AtomPlainText email;

	// use the factory method in the FeedDoc.
	Email(String email) {
		this.email = new AtomPlainText(email);
	}

	/**
	 * 
	 * @return the email text.
	 */
	public String getText() {
		return email.getText();
	}

	@Override
	public String toString() {
		return "<email>" + email.getText() + "</email>";
	}
}
