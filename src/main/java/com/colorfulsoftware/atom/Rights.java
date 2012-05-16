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
 * This class represents an Atom 1.0 rights element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomRights = element atom:rights { atomTextConstruct }
 * </pre>
 */
public class Rights implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6917308035123622641L;
	private final AtomTextConstruct rights;

	// use the factory method in the FeedDoc.
	Rights(String rights, List<Attribute> attributes) throws AtomSpecException {
		this.rights = new AtomTextConstruct(rights, attributes, false);
	}

	Rights(Rights rights) {
		this.rights = new AtomTextConstruct(rights.rights);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return rights.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return rights.getAttributes();
	}

	String getDivStartName() {
		return rights.getDivStartName();
	}

	Attribute getDivStartAttribute() {
		return rights.getDivStartAttribute();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return rights.getAttribute(attrName);
	}

	AtomTextConstruct.ContentType getContentType() {
		return rights.getContentType();
	}

	/**
	 * Shows the contents of the &lt;rights> element.
	 */
	@Override
	public String toString() {
		return "<rights" + rights + "</rights>";
	}

	List<String> getUnboundPrefixes() {
		return rights.getUnboundPrefixes();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Rights)) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
}
