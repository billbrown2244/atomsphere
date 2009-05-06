/**
 * Copyright (C) 2009 William R. Brown <info@colorfulsoftware.com>
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
/* Change History:
 * 2008-03-16 wbrown - made class immutable.
 */
package com.colorful.atom;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 text construct.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomPlainTextConstruct =
 *          atomCommonAttributes,
 *          attribute type { &quot;text&quot; | &quot;html&quot; }?,
 *          text
 * 
 *      atomXHTMLTextConstruct =
 *          atomCommonAttributes,
 *          attribute type { &quot;xhtml&quot; },
 *          xhtmlDiv
 * 
 *      atomTextConstruct = atomPlainTextConstruct | atomXHTMLTextConstruct
 * </pre>
 * 
 */
class AtomTextConstruct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5347393958118559606L;

	private final List<Attribute> attributes;
	private final String text;
	private final String xhtmlPrefix;

	/**
	 * 
	 * @param text
	 *            the text content of the element
	 * @param attributes
	 *            the attributes of the element. the attributes should contain a
	 *            "type" attribute specifying either text,html, or xhtml.
	 */
	public AtomTextConstruct(String text, List<Attribute> attributes)
			throws AtomSpecException {

		if (attributes == null) {
			this.attributes = null;
		} else {

			// add the attributes
			this.attributes = new LinkedList<Attribute>();

			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!FeedDoc.isAtomCommonAttribute(attr)
						&& !FeedDoc.isUndefinedAttribute(attr)
						&& !attr.getName().equals("type")) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " for this Atom Text Construct.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

		// if this is xhtml, strip the outer div tag from the content
		String textLocal = text;
		String prefix = null;
		System.out.println("textLocal = " + textLocal);
		System.out.println("contains xhtml: "+(FeedDoc.getContentType(this.attributes) == FeedDoc.ContentType.XHTML));
		if (FeedDoc.getContentType(this.attributes) == FeedDoc.ContentType.XHTML) {
			// check for a prefix.
			int endBracket = textLocal.indexOf(">");
			int firstSpace = textLocal.indexOf(" ");
			if (firstSpace != -1 && firstSpace < endBracket) {
				prefix = textLocal.substring(0, firstSpace + 1);
			} else {
				prefix = textLocal.substring(0, endBracket);
			}
			prefix = prefix.substring(1).trim();
			System.out.println("xhtmlPrefix before = " + prefix);
			prefix = (prefix.equals("div")) ? null : prefix.substring(0, prefix
					.indexOf(":"));
			System.out.println("xhtmlPrefix = " + prefix);

			// strip the wrapper start and end tags.
			System.out.println("textLocal orig = " + textLocal);
			textLocal = textLocal.substring(textLocal.indexOf(">") + 1);
			System.out.println("textLocal stripped front = " + textLocal);
			textLocal = reverseIt(textLocal);
			System.out.println("textLocal reversed = " + textLocal);
			textLocal = textLocal.substring(textLocal.indexOf("<") + 1);
			System.out
					.println("textLocal stripped back reverse = " + textLocal);
			textLocal = reverseIt(textLocal);
			System.out.println("textLocal final = " + textLocal);
		}
		this.text = textLocal;
		this.xhtmlPrefix = prefix;
	}

	//copy constructor
	AtomTextConstruct(String text, List<Attribute> attributes,
			String xhtmlPrefix) throws AtomSpecException {
		System.out.println("in copy constructor");
		System.out.println("xhtmlPrefix = " + xhtmlPrefix);
		this.text = text;
		this.xhtmlPrefix = xhtmlPrefix;
		if (attributes == null) {
			this.attributes = null;
		} else {

			// add the attributes
			this.attributes = new LinkedList<Attribute>();

			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!FeedDoc.isAtomCommonAttribute(attr)
						&& !FeedDoc.isUndefinedAttribute(attr)
						&& !attr.getName().equals("type")) {
					throw new AtomSpecException("Unsuppported attribute "
							+ attr.getName() + " for this Atom Text Construct.");
				}
				this.attributes.add(new Attribute(attr.getName(), attr
						.getValue()));
			}
		}

	}

	// Convenience for reversing the string.
	private String reverseIt(String source) {
		int i, len = source.length();
		StringBuffer dest = new StringBuffer(len);
		for (i = (len - 1); i >= 0; i--)
			dest.append(source.charAt(i));
		return dest.toString();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		if (attributes == null) {
			return null;
		}
		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		for (Attribute attr : this.attributes) {
			attrsCopy.add(new Attribute(attr.getName(), attr.getValue()));
		}
		return attrsCopy;
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return text;
	}

	// used in the feed writer.
	String getXhtmlPrefix() {
		return xhtmlPrefix;
	}

}