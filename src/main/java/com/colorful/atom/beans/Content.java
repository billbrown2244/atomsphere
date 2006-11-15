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

    public static final String TEXT = "text"; //text content
    public static final String HTML = "html"; //html content
    public static final String XHTML = "xhtml"; //xhtml content

    private List attributes = null;
    private String content = null;
    
    public Content(){
        //nothing yet.
    }
    
    /**
     * 
     * @param type of content text, html or xhtml
     */
    public Content(String type){
        attributes = new LinkedList();
        attributes.add(new Attribute("type",type));
    }
    
    /**
     * 
     * @param type of content text, html or xhtml
     * @param src source of content (URI).
     */
    public Content(String type, String src){
        attributes = new LinkedList();
        attributes.add(new Attribute("type",type));
        attributes.add(new Attribute("src",src));
    }
    
    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
