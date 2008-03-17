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
 *  2007-02-19 wbrown - deprecated constructor and getter / setter for the xmlns field.
 *                      this should be set as an attribute in the feed element.
 *  2008-03-17 wbrown - made class immutable.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
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

    private final String elementName;
    private final List<Attribute> attributes;
    private final String content;

    
    /**
     * 
     * @param elementName the name of the extension element.
     * @param content the content of the extension element.
     * @param attributes the attributes for this extension elements (should contain attribute xmlns).
     */
    public Extension(String elementName, String content, List<Attribute> attributes){
    		
    	this.elementName = elementName;
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
    
    public String getContent() {
        return content;
    }

    public String getElementName() {
        return elementName;
    }
}
