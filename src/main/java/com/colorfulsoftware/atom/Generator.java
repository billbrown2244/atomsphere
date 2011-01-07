/**
 * Copyright 2011 William R. Brown
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
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 generator element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomGenerator = element atom:generator {
 *          atomCommonAttributes,
 *          attribute uri { atomUri }?,
 *          attribute version { text }?,
 *          text
 *          }
 * </pre>
 */
public class Generator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6555330269825432901L;
	private final List<Attribute> attributes;
	private final Attribute uri;
	private final Attribute version;
	private final String text;
	private List<String> unboundPrefixes = null;

	// use the factory method in the FeedDoc.
	Generator(List<Attribute> attributes, String text) throws AtomSpecException {

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
							+ " in the atom:generator element.");
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

		this.unboundPrefixes = (this.unboundPrefixes.size() == 0) ? null
				: this.unboundPrefixes;

		this.uri = getAttribute("uri");

		this.version = getAttribute("version");

		// specification customization
		if (attributes == null && (text == null || text.equals(""))) {
			throw new AtomSpecException(
					"generator elements SHOULD have either a uri or version attribute or non empty content.");
		}

		this.text = text;
	}

	Generator(Generator generator) {
		this.attributes = generator.getAttributes();
		this.uri = generator.getUri();
		this.version = generator.getVersion();
		this.text = generator.text;
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
	 * @return the label attribute
	 */
	public Attribute getUri() {
		return (uri == null) ? null : new Attribute(uri);
	}

	/**
	 * 
	 * @return the scheme attribute
	 */
	public Attribute getVersion() {
		return (version == null) ? null : new Attribute(version);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return text;
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
	 * Shows the contents of the &lt;generator> element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<generator");
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute);
			}
		}
		// if there is content add it.
		if (text == null || text.equals("")) {
			sb.append(" />");
		} else {
			sb.append(" >" + text + "</generator>");
		}

		return sb.toString();
	}

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}
}
