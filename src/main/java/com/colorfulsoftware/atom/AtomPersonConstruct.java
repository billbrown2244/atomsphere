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

	private List<String> unboundPrefixes = null;

	AtomPersonConstruct(Name name, URI uri, Email email,
			List<Attribute> attributes, List<Extension> extensions)
			throws AtomSpecException {

		// check to make sure there is a name element
		if (name == null) {
			throw new AtomSpecException(
					"Person constructs MUST contain exactly one \"atom:name\" element.");
		}

		if (name.getText() == null || name.getText().equals("")) {
			throw new AtomSpecException("The person name SHOULD NOT be blank.");
		}

		this.name = new Name(name.getText());

		this.uri = (uri == null) ? null : new URI(uri.getText());

		this.email = (email == null) ? null : new Email(email.getText());

		this.unboundPrefixes = new LinkedList<String>();

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
				// check for unbound attribute prefixes
				if (attr.getName().indexOf(":") != -1
						&& !attr.getName().equals("xml:lang")
						&& !attr.getName().equals("xml:base")
						&& !attr.getName().startsWith("xmlns:")
						&& getAttribute("xmlns:"
								+ attr.getName().substring(0,
										attr.getName().indexOf(":"))) == null) {
					this.unboundPrefixes.add(attr.getName().substring(0,
							attr.getName().indexOf(":")));
				}
			}

		}

		if (extensions == null) {
			this.extensions = null;
		} else {
			this.extensions = new LinkedList<Extension>();

			for (Extension extension : extensions) {
				// check that the extension prefix is bound to a namespace
				String namespacePrefix = extension.getNamespacePrefix();
				if (namespacePrefix != null) {
					if (getAttribute("xmlns:" + namespacePrefix) == null) {
						this.unboundPrefixes.add(namespacePrefix);
					}
				}
				this.extensions.add(new Extension(extension));
			}
		}

		this.unboundPrefixes = (this.unboundPrefixes.size() == 0) ? null
				: this.unboundPrefixes;
	}

	public AtomPersonConstruct(AtomPersonConstruct person) {
		this.attributes = person.getAttributes();
		this.name = person.getName();
		this.uri = person.getUri();
		this.email = person.getEmail();
		this.extensions = person.getExtensions();
		this.unboundPrefixes = person.getUnboundPrefixes();
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
	 * Shows the contents of the &lt;author> or &lt;contributor> elements.
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

		sb.append(name);

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

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}
}
