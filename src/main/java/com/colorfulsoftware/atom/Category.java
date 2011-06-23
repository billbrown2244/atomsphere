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
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 category.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomCategory =
 *          element atom:category {
 *          atomCommonAttributes,
 *          attribute term { text },
 *          attribute scheme { atomUri }?,
 *          attribute label { text }?,
 *          undefinedContent
 * </pre>
 */
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7475777621616906621L;
	private final List<Attribute> attributes;
	private final Attribute term; // required
	private final Attribute scheme;
	private final Attribute label;
	private final String content;
	private List<String> unboundPrefixes = null;

	// use the factory method in the FeedDoc.
	Category(List<Attribute> attributes, String content)
			throws AtomSpecException {

		this.unboundPrefixes = new LinkedList<String>();

		if (attributes == null) {
			throw new AtomSpecException(
					"Category elements MUST have a \"term\" attribute.");
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!new AttributeSupport(attr).verify(this)) {
					throw new AtomSpecException("Unsupported attribute "
							+ attr.getName() + " in the atom:category element.");
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

		if ((this.term = getAttribute("term")) == null
				|| this.term.getValue().equals("")) {
			throw new AtomSpecException(
					"Category term attribue SHOULD NOT be blank.");
		}

		this.scheme = getAttribute("scheme");

		this.label = getAttribute("label");

		this.content = (content == null || content.equals("")) ? null : content;
	}

	Category(Category category) {
		this.attributes = category.getAttributes();
		this.term = category.getTerm();
		this.scheme = category.getScheme();
		this.label = category.getLabel();
		this.content = category.content;
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		for (Attribute attr : this.attributes) {
			attrsCopy.add(new Attribute(attr));
		}
		return attrsCopy;
	}

	/**
	 * 
	 * @return the label attribute
	 */
	public Attribute getLabel() {
		return (label == null) ? null : new Attribute(label);
	}

	/**
	 * 
	 * @return the scheme attribute
	 */
	public Attribute getScheme() {
		return (scheme == null) ? null : new Attribute(scheme);
	}

	/**
	 * 
	 * @return the term attribute
	 */
	public Attribute getTerm() {
		return new Attribute(term);
	}

	/**
	 * 
	 * @return undefined text content or undefined element.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		for (Attribute attribute : this.attributes) {
			if (attribute.getName().equals(attrName)) {
				return new Attribute(attribute);
			}
		}
		return null;
	}

	/**
	 * Shows the contents of the &lt;category> element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<category");

		for (Attribute attribute : attributes) {
			sb.append(attribute);
		}

		if (content == null) {
			sb.append(" />");
		} else {
			sb.append(" >" + content + "</category>");
		}

		return sb.toString();
	}

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}
}
