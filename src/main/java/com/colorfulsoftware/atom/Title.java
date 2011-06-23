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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 title element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomTitle = element atom:title { atomTextConstruct }
 * </pre>
 */
public class Title implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 823649575536535174L;
	private final AtomTextConstruct title;

	// use the factory method in the FeedDoc.
	Title(String title, List<Attribute> attributes) throws AtomSpecException {
		this.title = new AtomTextConstruct(title, attributes, false);
	}

	// copy constructor
	Title(Title title) {
		this.title = new AtomTextConstruct(title.title);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return title.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return title.getAttributes();
	}

	String getDivStartName() {
		return title.getDivStartName();
	}

	Attribute getDivStartAttribute() {
		return title.getDivStartAttribute();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return title.getAttribute(attrName);
	}

	AtomTextConstruct.ContentType getContentType() {
		return title.getContentType();
	}

	/**
	 * Shows the contents of the &lt;title> element.
	 */
	@Override
	public String toString() {
		return "<title" + title + "</title>";
	}

	List<String> getUnboundPrefixes() {
		return title.getUnboundPrefixes();
	}
}
