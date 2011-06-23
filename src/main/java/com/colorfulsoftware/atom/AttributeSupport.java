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
package com.colorfulsoftware.atom;

import java.io.Serializable;

/**
 * This class verifies if a given attribute is supported by its containing
 * element.
 * 
 * @author Bill Brown
 * 
 */
class AttributeSupport implements Serializable {

	private static final long serialVersionUID = -5670802769839692012L;
	/**
	 * the default document encoding of "UTF-8"
	 */
	private String name;
	private String value;
	private boolean atomCommonAttribute;

	/**
	 * @param attr
	 *            the attribute to verify
	 */
	public AttributeSupport(Attribute attr) {
		this.name = attr.getName();
		this.value = attr.getValue();
		this.atomCommonAttribute = name.equals("xml:base")
				|| name.equals("xml:lang")
				|| name.equals("xml:base")
				|| name.matches("xmlns:.+")
				|| name.matches(".+:.+")
				|| (name.equals("xmlns") && value
						.equals("http://www.w3.org/2005/Atom"));
	}

	// internal method to check for an attribute that is not reserved
	boolean verify(AtomDateConstruct verify) {
		return atomCommonAttribute;
	}

	boolean verify(AtomEntrySourceAdaptor verify) {
		return atomCommonAttribute;
	}

	boolean verify(AtomPersonConstruct verify) {
		return atomCommonAttribute;
	}

	boolean verify(AtomURIConstruct verify) {
		return atomCommonAttribute;
	}

	boolean verify(AtomTextConstruct verify) {
		return atomCommonAttribute
				|| (name.equals("type") && (value.equals("text")
						|| value.equals("html") || value.equals("xhtml")
						|| value.startsWith("application/")
						|| value.startsWith("audio/")
						|| value.startsWith("example/")
						|| value.startsWith("image/")
						|| value.startsWith("message/")
						|| value.startsWith("model/")
						|| value.startsWith("multipart/")
						|| value.startsWith("video/")
						|| value.startsWith("text/") || value.endsWith("/xml") || value
						.endsWith("\\+xml")));
	}

	boolean verify(Category verify) {
		return atomCommonAttribute || name.equals("term")
				|| name.equals("scheme") || name.equals("label");
	}

	boolean verify(Generator verify) {
		return atomCommonAttribute || name.equals("uri")
				|| name.equals("version");
	}

	boolean verify(Link verify) {
		return atomCommonAttribute || name.equals("href") || name.equals("rel")
				|| name.equals("type") || name.equals("hreflang")
				|| name.equals("title") || name.equals("length");
	}
}