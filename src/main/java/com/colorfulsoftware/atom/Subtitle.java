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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 subtitle element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomSubsubtitle = element atom:subsubtitle { atomTextConstruct }
 * </pre>
 */
public class Subtitle implements Serializable {

	private static final long serialVersionUID = -1899291850703846655L;
	private final AtomTextConstruct subtitle;

	// use the factory method in the FeedDoc.
	Subtitle(String subtitle, List<Attribute> attributes)
			throws AtomSpecException {
		this.subtitle = new AtomTextConstruct(subtitle, attributes, false);
	}

	// copy constructor
	Subtitle(Subtitle subtitle) {
		this.subtitle = new AtomTextConstruct(subtitle.subtitle);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return subtitle.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return subtitle.getAttributes();
	}

	String getDivStartName() {
		return subtitle.getDivStartName();
	}

	Attribute getDivStartAttribute() {
		return subtitle.getDivStartAttribute();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return subtitle.getAttribute(attrName);
	}

	AtomTextConstruct.ContentType getContentType() {
		return subtitle.getContentType();
	}

	/**
	 * Shows the contents of the &lt;subtitle> element.
	 */
	@Override
	public String toString() {
		return "<subtitle" + subtitle + "</subtitle>";
	}

	List<String> getUnboundPrefixes() {
		return subtitle.getUnboundPrefixes();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Subtitle)) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
}
