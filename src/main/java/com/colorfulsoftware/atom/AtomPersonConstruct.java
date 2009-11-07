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
 * 2006-11-12 wbrown - added javadoc documentation.
 * 2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

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

	AtomPersonConstruct(Name name, URI uri, Email email,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {

		// check to make sure there is a name element
		if (name == null) {
			throw new AtomSpecException(
					"Person constructs MUST contain exactly one \"atom:name\" element.");
		}
		this.name = new Name(name.getText());

		this.uri = (uri == null) ? null : new URI(uri.getText());

		this.email = (email == null) ? null : new Email(email.getText());

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!new AttributeSupport(attr).verify(this)) {
					throw new AtomSpecException("Unsupported attribute "
							+ attr.getName()
							+ " for this Atom Person Construct.");
				}
				this.attributes.add(new Attribute(attr));
			}
		}

		if (extensions == null) {
			this.extensions = null;
		} else {
			this.extensions = new LinkedList<Extension>();
			for (Extension extension : extensions) {
				this.extensions.add(new Extension(extension));
			}
		}

	}

	public AtomPersonConstruct(AtomPersonConstruct person) {
		this.attributes = person.getAttributes();
		this.name = person.getName();
		this.uri = person.getUri();
		this.email = person.getEmail();
		this.extensions = person.getExtensions();
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attr : this.attributes) {
				attrsCopy.add(new Attribute(attr));
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
		return new Name(name.getText());
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
		List<Extension> extsCopy = new LinkedList<Extension>();
		for (Extension extension : this.extensions) {
			extsCopy.add(new Extension(extension));
		}
		return extsCopy;
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName().equals(attrName)) {
					return new Attribute(attribute);
				}
			}
		}
		return null;
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 */
	public Extension getExtension(String extName) {
		if (this.extensions != null) {
			for (Extension extension : this.extensions) {
				if (extension.getElementName().equals(extName)) {
					return new Extension(extension);
				}
			}
		}
		return null;
	}

	/**
	 * Shows the contents of the <author> or <contributor> elements.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute);
			}
		}
		// close the parent element
		sb.append(">");

		if (name != null) {
			sb.append(name);
		}
		if (email != null) {
			sb.append(email);
		}
		if (uri != null) {
			sb.append(uri);
		}
		if (extensions != null) {
			for (Extension extension : extensions) {
				sb.append(extension);
			}
		}
		return sb.toString();
	}
}
