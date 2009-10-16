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
/* Change History:
 * 2008-03-16 wbrown - made class immutable.
 */
package com.colorfulsoftware.atom;

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

	private static final long serialVersionUID = 5347393958118559606L;

	private final List<Attribute> attributes;
	private final String text;
	private final String divWrapperStart;
	private final String divWrapperEnd;
	private ContentType contentType;

	// content elements do a different validation.
	AtomTextConstruct(String text, List<Attribute> attributes,
			boolean isContentElement) throws AtomSpecException {

		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			// content elements have a slightly different validation.
			if (isContentElement) {
				for (Attribute attr : attributes) {
					// check for unsupported attribute.
					if (!new AttributeSupport(attr).verify(this)
							&& !attr.getName().equals("src")) {
						throw new AtomSpecException("Unsupported attribute "
								+ attr.getName()
								+ " for this Atom Text Construct.");
					}
					this.attributes.add(new Attribute(attr));
				}
			} else {
				for (Attribute attr : attributes) {
					// check for unsupported attribute.
					if (!new AttributeSupport(attr).verify(this)) {
						throw new AtomSpecException("Unsupported attribute "
								+ attr.getName()
								+ " for this Atom Text Construct.");
					}
					this.attributes.add(new Attribute(attr));
				}
			}
		}

		// get the content type
		Attribute attr = getAttribute("src");
		if (attr != null) {
			contentType = ContentType.EXTERNAL;
		}

		attr = getAttribute("type");
		if (attr != null
				&& (attr.getValue().equals(
						"text"))) {
			contentType = ContentType.TEXT;
		}

		if (attr != null
				&& (attr.getValue().equals(
						"html"))) {
			contentType = ContentType.HTML;
		}
		if (attr != null
				&& ((!attr.getValue().equals(
						"text")
						&& !attr.getValue().equals("html") && !attr.getValue()
						.equals("xhtml")))) {
			contentType = ContentType.OTHER;
		}
		if (attr != null
				&& (attr.getValue().equals(
						"xhtml"))) {
			contentType = ContentType.XHTML;
		}
		if(contentType == null){
			contentType = ContentType.TEXT;
		}

		// do we need to wrap the xhtml in a div.
		System.out.println("contnetType: "+contentType);
		System.out.println("text"+text);
		if (contentType == ContentType.XHTML) {
			this.divWrapperStart = getDivWrapperStart(text);
			this.divWrapperEnd = getDivWrapperEnd(text);
			this.text = getXhtmlText(text);
		} else {
			this.divWrapperStart = null;
			this.divWrapperEnd = null;
			this.text = text;
		}
	}

	AtomTextConstruct(AtomTextConstruct atomTextConstruct) {
		this.attributes = atomTextConstruct.getAttributes();
		this.text = atomTextConstruct.getText();
		this.divWrapperStart = atomTextConstruct.getDivWrapperStart();
		this.divWrapperEnd = atomTextConstruct.getDivWrapperEnd();
		this.contentType = atomTextConstruct.getContentType();
	}

	ContentType getContentType() {
		return contentType;
	}

	/**
	 * 
	 * An enumeration of the different types of supported content.
	 * 
	 */
	public enum ContentType {
		/**
		 * text content
		 */
		TEXT, /**
		 * html content
		 */
		HTML, /**
		 * xhtml content
		 */
		XHTML, /**
		 * other non text, html or xhtml content
		 */
		OTHER, /**
		 * external content outside of the feed
		 */
		EXTERNAL
	}

	private String getDivWrapperStart(String text) {
		return (text == null)?null:text.substring(0, text.indexOf(">") + 1);
	}

	private String getDivWrapperEnd(String text) {
		if(text == null){
			return null;
		}
		text = reverseIt(text);
		text = text.substring(0, text.indexOf("<") + 1);
		return reverseIt(text);
	}

	private String getXhtmlText(String text) {
		if(text == null){
			return null;
		}
		// strip the wrapper start and end tags.
		text = text.substring(text.indexOf(">") + 1);
		text = reverseIt(text);
		text = text.substring(text.indexOf("<") + 1);
		return reverseIt(text);
	}

	// Convenience for reversing the string.
	private String reverseIt(String source) {
		int i, len = source.length();
		StringBuilder dest = new StringBuilder(len);
		for (i = (len - 1); i >= 0; i--)
			dest.append(source.charAt(i));
		return dest.toString();
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {

		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attr : this.attributes) {
				attrsCopy.add(new Attribute(attr));
			}
		}
		return (this.attributes == null) ? null : attrsCopy;
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getText() {
		return text;
	}

	String getDivWrapperStart() {
		return divWrapperStart;
	}

	String getDivWrapperEnd() {
		return divWrapperEnd;
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName().equals(attrName)) {
					return new Attribute(attribute);
				}
			}
		}
		return null;
	}
}