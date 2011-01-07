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
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;

/**
 * This class represents an Atom 1.0 name element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atom:name { text }
 * </pre>
 */
public class Name implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4036528312427650254L;
	private final AtomPlainText name;

	// use the factory method in the FeedDoc.
	Name(String name) {
		this.name = new AtomPlainText(name);
	}

	/**
	 * 
	 * @return the name text.
	 */
	public String getText() {
		return name.getText();
	}

	/**
	 * Shows the contents of the &lt;name> element.
	 */
	@Override
	public String toString() {
		return "<name>" + name.getText() + "</name>";
	}

}
