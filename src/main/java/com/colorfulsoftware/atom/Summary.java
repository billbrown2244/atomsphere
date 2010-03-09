/**
 * Copyright 2010 William R. Brown
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
 * This class represents an Atom 1.0 summary element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomSummary = element atom:summary { atomTextConstruct }
 * </pre>
 */
public class Summary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8464262582824268938L;
	private final AtomTextConstruct summary;

	// use the factory method in the FeedDoc.
	Summary(String summary, List<Attribute> attributes)
			throws AtomSpecException {
		this.summary = new AtomTextConstruct(summary, attributes, false);
	}

	// copy constructor
	Summary(Summary summary) {
		this.summary = new AtomTextConstruct(summary.summary);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return summary.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return summary.getAttributes();
	}

	String getDivStartName() {
		return summary.getDivStartName();
	}

	Attribute getDivStartAttribute() {
		return summary.getDivStartAttribute();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return summary.getAttribute(attrName);
	}

	AtomTextConstruct.ContentType getContentType() {
		return summary.getContentType();
	}

	/**
	 * Shows the contents of the &lt;summary> element.
	 */
	@Override
	public String toString() {
		return "<summary" + summary + "</summary>";
	}
}
