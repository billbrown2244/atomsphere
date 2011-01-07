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
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

import com.colorfulsoftware.atom.AtomTextConstruct.ContentType;

/**
 * This class represents an Atom 1.0 content element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 *      atomInlineTextContent =
 *          element atom:content {
 *          atomCommonAttributes,
 *          attribute type { &quot;text&quot; | &quot;html&quot; }?,
 *          (text)*
 *          }
 * 
 *      atomInlineXHTMLContent =
 *          element atom:content {
 *          atomCommonAttributes,
 *          attribute type { &quot;xhtml&quot; },
 *          xhtmlDiv
 *          }
 * 
 *      atomInlineOtherContent =
 *          element atom:content {
 *          atomCommonAttributes,
 *          attribute type { atomMediaType }?,
 *          (text|anyElement)*
 *          }
 * 
 *      atomOutOfLineContent =
 *          element atom:content {
 *          atomCommonAttributes,
 *          attribute type { atomMediaType }?,
 *          attribute src { atomUri },
 *          empty
 *          }
 *          
 *      atomContent = atomInlineTextContent
 *          | atomInlineXHTMLContent
 *          | atomInlineOtherContent
 *          | atomOutOfLineContent
 * </pre>
 */
public class Content implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6990178735588333050L;
	private final AtomTextConstruct content;

	// use the factory method in the FeedDoc.
	Content(String content, List<Attribute> attributes)
			throws AtomSpecException {
		this.content = new AtomTextConstruct(content, attributes, true);
	}

	// copy constructor
	Content(Content content) {
		this.content = new AtomTextConstruct(content.content);
	}

	/**
	 * 
	 * @return the text content for this element.
	 */
	public String getContent() {
		return content.getText();
	}

	/**
	 * 
	 * @return the attributes for this element.
	 */
	public List<Attribute> getAttributes() {
		return content.getAttributes();
	}

	String getDivStartName() {
		return content.getDivStartName();
	}

	Attribute getDivStartAttribute() {
		return content.getDivStartAttribute();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return content.getAttribute(attrName);
	}

	AtomTextConstruct.ContentType getContentType() {
		return content.getContentType();
	}

	/**
	 * Shows the contents of the &lt;content> element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<content");
		sb.append(content);
		if (content.getContentType() == ContentType.EXTERNAL) {
			sb.append(" />");
		} else {
			sb.append("</content>");
		}

		return sb.toString();
	}
}
