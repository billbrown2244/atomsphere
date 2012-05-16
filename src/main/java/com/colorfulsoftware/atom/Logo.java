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
 *  2008-04-09 wbrown - added throws clause to constructor.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 logo element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomLogo = element atom:logo {
 *          atomCommonAttributes,
 * 		    (atomUri)
 *         }
 * </pre>
 */
public class Logo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -726698144318106924L;
	private final AtomURIConstruct logo;

	// use the factory method in the FeedDoc.
	Logo(List<Attribute> attributes, String atomUri) throws AtomSpecException {
		this.logo = new AtomURIConstruct(attributes, atomUri);
	}

	Logo(Logo logo) {
		this.logo = new AtomURIConstruct(logo.logo);
	}

	/**
	 * 
	 * @return the unique identifier for this document.
	 */
	public String getAtomUri() {
		return logo.getAtomUri();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return logo.getAttributes();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return logo.getAttribute(attrName);
	}

	/**
	 * Shows the contents of the &lt;logo> element.
	 */
	@Override
	public String toString() {
		return "<logo" + logo + "</logo>";
	}

	List<String> getUnboundPrefixes() {
		return logo.getUnboundPrefixes();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Logo)) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
}
