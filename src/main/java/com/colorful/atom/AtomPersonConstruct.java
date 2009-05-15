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
 * 2006-11-12 wbrown - added javadoc documentation.
 * 2008-03-16 wbrown - made class immutable.
 */
package com.colorful.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 person construct.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomPersonConstruct =
 *          atomCommonAttributes,
 *          (element atom:name { text }
 *          &amp; element atom:uri { atomUri }?
 *          &amp; element atom:email { atomEmailAddress }?
 *          &amp; extensionElement*)
 * </pre>
 */
class AtomPersonConstruct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470418749893434673L;

	private final List<Attribute> attributes;

	private final Name name;

	private final URI uri;

	private final Email email;

	private final List<Extension> extensions;

	/**
	 * 
	 * @param name
	 *            the name of the person
	 * @param uri
	 *            the uri of the person
	 * @param email
	 *            the email address of the person
	 * @param attributes
	 * @param extensions
	 */
	public AtomPersonConstruct(Name name, URI uri, Email email,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {

		// check to make sure there is a name element
		if (name == null) {
			throw new AtomSpecException(
					"Person constructs MUST contain exactly one \"atom:name\" element.");
		} else {
			this.name = new Name(name.getText());
		}

		this.uri = (uri == null) ? null : new URI(uri.getText());

		this.email = (email == null) ? null : new Email(email.getText());

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!FeedDoc.isAtomCommonAttribute(attr)
						&& !FeedDoc.isUndefinedAttribute(attr)) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName()
							+ " for this Atom Person Construct.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		if (extensions == null) {
			this.extensions = null;
		} else {
			this.extensions = new LinkedList<Extension>();
			for (Extension extension : extensions) {
				this.extensions.add(new Extension(extension.getElementName(),
						extension.getAttributes(), extension.getContent()));
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

	/**
	 * 
	 * @return the email address for this element.
	 */
	public Email getEmail() {
		return (email == null) ? null : new Email(email.getText());
	}

	/**
	 * 
	 * @return the name for this element.
	 */
	public Name getName() {
		return (name == null) ? null : new Name(name.getText());
	}

	/**
	 * 
	 * @return the URI for this element.
	 */
	public URI getUri() {
		return (uri == null) ? null : new URI(uri.getText());
	}

	/**
	 * 
	 * @return the extensions for this element.
	 */
	public List<Extension> getExtensions() {
		if (extensions == null) {
			return null;
		}
		List<Extension> extnsCopy = new LinkedList<Extension>();
		for (Extension extension : this.extensions) {
			extnsCopy.add(new Extension(extension.getElementName(), extension
					.getAttributes(), extension.getContent()));
		}
		return extnsCopy;
	}

}
