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
 * This class represents an Atom 1.0 author.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomAuthor = element atom:author { atomPersonConstruct }
 * </pre>
 */
public class Author implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4952942936430794533L;
	private final AtomPersonConstruct person;

	// use the factory method in the FeedDoc.
	Author(Name name, URI uri, Email email, List<Attribute> attributes,
			List<Extension> extensions) throws AtomSpecException {
		this.person = new AtomPersonConstruct(name, uri, email, attributes,
				extensions);
	}

	/**
	 * @return the author's name eg. Bill Brown
	 */
	public Name getName() {
		return person.getName();
	}

	/**
	 * @return the author's web address eg. http://www.colorfulsoftware.com
	 */
	public URI getUri() {
		return person.getUri();
	}

	/**
	 * @return the author's email address eg. wbrown@colorfulsoftware.com
	 */
	public Email getEmail() {
		return person.getEmail();
	}

	/**
	 * @return a list of attributes
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Attribute> getAttributes() throws AtomSpecException {
		return person.getAttributes();
	}

	/**
	 * @return a list of extension elements
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public List<Extension> getExtensions() throws AtomSpecException {
		return person.getExtensions();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Attribute getAttribute(String attrName) throws AtomSpecException {
		return person.getAttribute(attrName);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the data is not valid.
	 */
	public Extension getExtension(String extName) throws AtomSpecException {
		return person.getExtension(extName);
	}
}
