/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
public class AttributeSupport implements Serializable {
	
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