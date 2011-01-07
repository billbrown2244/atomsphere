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
	private ContentType contentType;
	private final String divStartName;
	private final Attribute divStartAttribute;
	private List<String> unboundPrefixes = null;

	// content elements do a different validation.
	AtomTextConstruct(String text, List<Attribute> attributes,
			boolean isContentElement) throws AtomSpecException {

		this.unboundPrefixes = new LinkedList<String>();

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
					// check for unbound attribute prefixes
					if (attr.getName().indexOf(":") != -1
							&& !attr.getName().equals("xml:lang")
							&& !attr.getName().equals("xml:base")
							&& !attr.getName().startsWith("xmlns:")
							&& getAttribute("xmlns:"
									+ attr.getName().substring(0,
											attr.getName().indexOf(":"))) == null) {
						this.unboundPrefixes.add(attr.getName().substring(0,
								attr.getName().indexOf(":")));
					}

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
					// check for unbound attribute prefixes
					if (attr.getName().indexOf(":") != -1
							&& !attr.getName().equals("xml:lang")
							&& !attr.getName().equals("xml:base")
							&& !attr.getName().startsWith("xmlns:")
							&& getAttribute("xmlns:"
									+ attr.getName().substring(0,
											attr.getName().indexOf(":"))) == null) {
						this.unboundPrefixes.add(attr.getName().substring(0,
								attr.getName().indexOf(":")));
					}
				}
			}
		}

		this.unboundPrefixes = (this.unboundPrefixes.size() == 0) ? null
				: this.unboundPrefixes;

		// get the content type
		Attribute attr = getAttribute("src");
		if (attr != null) {
			contentType = ContentType.EXTERNAL;
		}

		attr = getAttribute("type");
		if (attr != null && (attr.getValue().equals("text"))) {
			contentType = ContentType.TEXT;
		}

		if (attr != null && (attr.getValue().equals("html"))) {
			contentType = ContentType.HTML;
		}
		if (attr != null
				&& ((!attr.getValue().equals("text")
						&& !attr.getValue().equals("html") && !attr.getValue()
						.equals("xhtml")))) {
			contentType = ContentType.OTHER;
		}
		if (attr != null && (attr.getValue().equals("xhtml"))) {
			contentType = ContentType.XHTML;
		}
		if (contentType == null) {
			contentType = ContentType.TEXT;
		}

		// do we need to wrap the xhtml in a div.
		String divWrapperStart = null;
		if (contentType == ContentType.XHTML) {
			divWrapperStart = getDivStartName(text);
			getDivWrapperEnd(text);
			this.text = getXhtmlText(text);
		} else {
			this.text = text;
		}

		// prepare the div wrapper start element and optional element.
		if (divWrapperStart != null) {
			String bare = divWrapperStart.replaceAll("<", "")
					.replaceAll(">", "").trim();
			String[] split = bare.split(" ");
			if (split.length > 1) {
				this.divStartName = split[0];
				String[] attrSplit = split[1].replaceAll("\"", "").split("=");
				this.divStartAttribute = new Attribute(attrSplit[0],
						attrSplit[1]);
			} else {
				this.divStartName = bare;
				this.divStartAttribute = null;
			}
		} else {
			this.divStartName = null;
			this.divStartAttribute = null;
		}
	}

	AtomTextConstruct(AtomTextConstruct atomTextConstruct) {
		this.attributes = atomTextConstruct.getAttributes();
		this.text = atomTextConstruct.text;
		this.contentType = atomTextConstruct.getContentType();
		this.divStartName = atomTextConstruct.divStartName;
		this.divStartAttribute = atomTextConstruct.getDivStartAttribute();
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

	private String getDivStartName(String text) {
		return (text == null) ? null : text.substring(0, text.indexOf(">") + 1);
	}

	private String getDivWrapperEnd(String text) {
		if (text == null) {
			return null;
		}
		text = reverseIt(text);
		text = text.substring(0, text.indexOf("<") + 1);
		return reverseIt(text);
	}

	private String getXhtmlText(String text) {
		if (text == null) {
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

	String getDivStartName() {
		return divStartName;
	}

	Attribute getDivStartAttribute() {
		return divStartAttribute;
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

	/**
	 * Shows the contents of the &lt;content>, &lt;rights>, &lt;subtitle>,
	 * &lt;summary> or &lt;title> elements.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute);
			}
		}
		// if there is content add it.
		if (contentType != ContentType.EXTERNAL) {
			sb.append(">");
			if (contentType == ContentType.XHTML) {
				sb.append("<"
						+ divStartName
						+ ((divStartAttribute == null) ? ">"
								: divStartAttribute + " >") + text + "</"
						+ divStartName + ">");
			} else {
				sb.append(text);
			}
		}

		return sb.toString();
	}

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}
}