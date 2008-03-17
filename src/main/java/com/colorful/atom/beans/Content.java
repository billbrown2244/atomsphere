/*
Atomsphere - an atom feed library.
Copyright (C) 2006 William R. Brown.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 content element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomInlineTextContent =
 *          element atom:content {
 *          atomCommonAttributes,
 *          attribute type { "text" | "html" }?,
 *          (text)*
 *          }
 *
 *      atomInlineXHTMLContent =
 *          element atom:content {
 *          atomCommonAttributes,
 *          attribute type { "xhtml" },
 *          xhtmlDiv
 *          }

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
 *  </pre>
 */
public class Content {
	
	public enum ContentType {TEXT,HTML,XHTML,OTHER,EXTERNAL}; 
	
    private final List<Attribute> attributes;
    private final String content;
    
    /**
     * 
     * @param content the text content of the element
     * @param attributes the attributes 
     */
    public Content(String content, List<Attribute> attributes){
    	this.content = content;
    	
    	if(attributes == null){
    		this.attributes = null;
    	}else{
    		this.attributes = new LinkedList<Attribute>();
    		Iterator<Attribute> attrItr = attributes.iterator();
    		while(attrItr.hasNext()){
    			Attribute attr = attrItr.next();
    			this.attributes.add(new Attribute(attr.getName(),attr.getValue()));
    		}
    	}
    }
    
    /**
     * 
     * @return the attributes for this element.
     */
    public List<Attribute> getAttributes() {
    	if(attributes == null){
    		return null;
    	}
    	List<Attribute> attrsCopy = new LinkedList<Attribute>();
		Iterator<Attribute> attrItr = this.attributes.iterator();
		while(attrItr.hasNext()){
			Attribute attr = attrItr.next();
			attrsCopy.add(new Attribute(attr.getName(),attr.getValue()));
		}
        return attrsCopy;
    }

    /**
     * 
     * @return the content for this element.
     */
    public String getContent() {
        return content;
    }

    /**
     * Convenience method for getting the content type for this element
     * @return the content type for this element. One of TEXT,HTML,XHTML,OTHER or EXTERNAL
     */
    public ContentType getContentType(){
    	ContentType contentType = null;
		Iterator<Attribute> attrItr = attributes.iterator();
		while(attrItr.hasNext()){
			Attribute attr = attrItr.next();
			if(attr.getName().equals("type") && attr.getValue().equals("text")){
				contentType = ContentType.TEXT;
				break;
			}else if(attr.getName().equals("type") && attr.getValue().equals("html")){
				contentType = ContentType.HTML;
				break;
			}else if(attr.getName().equals("type") && attr.getValue().equals("xhtml")){
				contentType = ContentType.XHTML;
			}else if(attr.getName().equals("type") && (!attr.getValue().equals("text")
					&& !attr.getValue().equals("html")
					&& !attr.getValue().equals("xhtml"))){
				contentType = ContentType.OTHER;
				break;
			}else if(attr.getName().equals("src")){
				contentType = ContentType.EXTERNAL;
				break;
			}
		}
		//default.
		if(contentType == null){
			contentType = ContentType.TEXT;
		}
		return contentType;
    }
}
