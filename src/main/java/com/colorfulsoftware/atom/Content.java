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
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

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
	Content(Content content) throws AtomSpecException {
		this.content = new AtomTextConstruct(content.getContent(), content
				.getAttributes(), content.getDivWrapperStart(), content
				.getDivWrapperEnd(), true);
	}

	/**
	 * 
	 * @return the content for this element.
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

	String getDivWrapperStart() {
		return content.getDivWrapperStart();
	}

	String getDivWrapperEnd() {
		return content.getDivWrapperEnd();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return content.getAttribute(attrName);
	}
}
