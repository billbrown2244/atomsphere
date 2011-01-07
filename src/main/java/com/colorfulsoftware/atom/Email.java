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
 * This class represents an Atom 1.0 Contributor element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      &quot;atom:email&quot; element's content conveys an e-mail address associated with the person. 
 *      Person constructs MAY contain an atom:email element, but MUST NOT contain more than one. 
 *      Its content MUST conform to the &quot;addr-spec&quot; production in [RFC2822].
 * </pre>
 */
public class Email implements Serializable {

	private static final long serialVersionUID = -4534267548039292775L;
	private final AtomPlainText email;

	// use the factory method in the FeedDoc.
	Email(String email) {
		this.email = new AtomPlainText(email);
	}

	/**
	 * 
	 * @return the email text.
	 */
	public String getText() {
		return email.getText();
	}

	/**
	 * Shows the contents of the &lt;email> element.
	 */
	@Override
	public String toString() {
		return "<email>" + email.getText() + "</email>";
	}
}
