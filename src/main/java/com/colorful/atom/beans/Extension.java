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
 * This class represents an Atom 1.0 extension element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      simpleExtensionElement =
 *          element * - atom:* {
 *          text
 *          }
 *      
 *      structuredExtensionElement =
 *          element * - atom:* {
 *          (attribute * { text }+,
 *          (text|anyElement)*)
 *          | (attribute * { text }*,
 *          (text?, anyElement+, (text|anyElement)*))
 *          }
 *  </pre>
 */
public class Extension {

    private String elementName = null;
    private List attributes = null;
    private String content = null;
    private Attribute xmlns = null;
    
    public Extension(){
        //put sutiable defaults here
        
    }

    /**
     * 
     * @param xmlns the xml namespace
     * @param elementName the name of the element
     */
    public Extension(String xmlns, String elementName){
        attributes = new LinkedList();
        attributes.add(new Attribute("xmlns",xmlns));
        this.elementName = elementName;
        
    }
    
    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Attribute getXmlns() {
        return xmlns;
    }

    public void setXmlns(Attribute xmlns) {
        this.xmlns = xmlns;
    }
}
