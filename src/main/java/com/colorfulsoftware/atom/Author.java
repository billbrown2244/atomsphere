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
import java.util.List;

/**
 * This class represents an Atom 1.0 author.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomAuthor = element atom:author { atomPersonConstruct }
 * </pre>
 */
public class Author implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4952942936430794533L;
	private final AtomPersonConstruct person;
	private List<String> unboundPrefixes = null;

	// use the factory method in the FeedDoc.
	Author(Name name, URI uri, Email email, List<Attribute> attributes,
			List<Extension> extensions) throws AtomSpecException {
		this.person = new AtomPersonConstruct(name, uri, email, attributes,
				extensions);
		this.unboundPrefixes = person.getUnboundPrefixes();
	}

	Author(Author author) {
		this.person = new AtomPersonConstruct(author.person);
	}

	/**
	 * @return the author's name eg. Bill Brown
	 */
	public Name getName() {
		return person.getName();
	}

	/**
	 * @return the author's web address eg. http://www.colorfulsoftware.com
	 */
	public URI getUri() {
		return person.getUri();
	}

	/**
	 * @return the author's email address eg. wbrown@colorfulsoftware.com
	 */
	public Email getEmail() {
		return person.getEmail();
	}

	/**
	 * @return a list of attributes
	 */
	public List<Attribute> getAttributes() {
		return person.getAttributes();
	}

	/**
	 * @return a list of extension elements
	 */
	public List<Extension> getExtensions() {
		return person.getExtensions();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return person.getAttribute(attrName);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 */
	public Extension getExtension(String extName) {
		return person.getExtension(extName);
	}

	/**
	 * Shows the contents of the &lt;author> element.
	 */
	@Override
	public String toString() {
		return "<author" + person + "</author>";
	}

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}
}
