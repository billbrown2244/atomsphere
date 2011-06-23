/**
 * Copyright ${year} Bill Brown
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
 *  2007-02-19 wbrown - deprecated constructor and getter / setter for the xmlns field.
 *                      this should be set as an attribute in the feed element.
 *  2008-03-17 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 extension element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      simpleExtensionElement =
 *          element * - atom:* {
 *          text
 *          }
 *      
 *      structuredExtensionElement =
 *          element * - atom:* {
 *          (attribute * { text }+,
 *          (text|anyElement)*)
 *          | (attribute * { text }*,
 *          (text?, anyElement+, (text|anyElement)*))
 *          }
 * </pre>
 */
public class Extension implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7382104018423864548L;
	private final String elementName;
	private final List<Attribute> attributes;
	private final String content;
	private final String namespacePrefix;

	// use the factory method in the FeedDoc.
	Extension(String elementName, List<Attribute> attributes, String content)
			throws AtomSpecException {

		this.elementName = elementName;
		this.content = content;

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				this.attributes.add(new Attribute(attr));
			}
		}

		if (elementName == null
				|| elementName.equals("")
				|| (elementName.indexOf(":") == -1 && getAttribute("xmlns") == null)
				|| elementName.indexOf(":") == 0) {

			throw new AtomSpecException(
					"Extension element '"
							+ elementName
							+ "' is missing a namespace prefix or namespace declaration.");
		}

		// the namespace prefix is used here for validation only.
		if (elementName.indexOf(":") != -1) {
			String potentialPrefix = elementName.substring(0,
					elementName.indexOf(":"));
			if (getAttribute("xmlns:" + potentialPrefix) == null) {
				this.namespacePrefix = potentialPrefix;
			} else {
				this.namespacePrefix = null;
			}
		} else {
			this.namespacePrefix = null;
		}
	}

	// copy constructor
	Extension(Extension extension) {
		this.elementName = extension.elementName;
		this.attributes = extension.getAttributes();
		this.content = extension.content;
		this.namespacePrefix = extension.namespacePrefix;
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
	 * @return the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the extension element name. eg "atom:link" or "someExtension"
	 */
	public String getElementName() {
		return elementName;
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
	 * Shows the contents of the extension element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<" + elementName);
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute);
			}
		}

		if (content == null || content.equals("")) {
			sb.append(" />");
		} else {
			sb.append(" >" + content + "</" + elementName + ">");
		}

		return sb.toString();
	}

	String getNamespacePrefix() {
		return namespacePrefix;
	}
}
