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
import java.util.Date;
import java.util.List;

/**
 * This class represents an Atom 1.0 updated element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomUpdated = element atom:updated { atomDateConstruct}
 * </pre>
 */
public class Updated implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7849178087953320683L;
	private final AtomDateConstruct updated;

	// use the factory method in the FeedDoc.
	Updated(List<Attribute> attributes, String updated)
			throws AtomSpecException {
		this.updated = new AtomDateConstruct(attributes, updated);
	}

	Updated(Updated updated) {
		this.updated = new AtomDateConstruct(updated.updated);
	}

	/**
	 * 
	 * @return the date timestamp for this element.
	 */
	public Date getDateTime() {
		return updated.getDateTime();
	}

	/**
	 * 
	 * @return the string formated version of the time for example
	 *         2006-04-28T12:50:43.337-05:00
	 */
	public String getText() {
		return updated.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return updated.getAttributes();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return updated.getAttribute(attrName);
	}

	/**
	 * Shows the contents of the &lt;updated> element.
	 */
	@Override
	public String toString() {
		return "<updated" + updated + "</updated>";
	}
}
