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
 *  2008-03-16 wbrown - made class immutable.
 *  2008-04-09 wbrown - added throws clause to constructor.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 id element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomId = element atom:id {
 * 		   atomCommonAttributes,
 * 		   (atomUri)
 *      }
 * </pre>
 */
public class Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1551131397328572231L;
	private final AtomURIConstruct id;

	// use the factory method in the FeedDoc.
	Id(List<Attribute> attributes, String atomUri) throws AtomSpecException {
		this.id = new AtomURIConstruct(attributes, atomUri);
	}

	Id(Id id) {
		this.id = new AtomURIConstruct(id.id);
	}

	/**
	 * 
	 * @return the unique identifier for this document.
	 */
	public String getAtomUri() {
		return id.getAtomUri();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return id.getAttributes();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return id.getAttribute(attrName);
	}

	/**
	 * Shows the contents of the &lt;id> element.
	 */
	@Override
	public String toString() {
		return "<id" + id + "</id>";
	}
}
