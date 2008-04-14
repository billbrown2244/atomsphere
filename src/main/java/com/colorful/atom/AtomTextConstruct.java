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
 * 2008-03-16 wbrown - made class immutable.
 */
package com.colorful.atom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 text construct.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre> 
 *      atomPlainTextConstruct =
 *          atomCommonAttributes,
 *          attribute type { "text" | "html" }?,
 *          text
 *
 *      atomXHTMLTextConstruct =
 *          atomCommonAttributes,
 *          attribute type { "xhtml" },
 *          xhtmlDiv
 *
 *      atomTextConstruct = atomPlainTextConstruct | atomXHTMLTextConstruct
 *  </pre>
 *  
 */
class AtomTextConstruct {
    /*
     * 
     */
    private final List<Attribute> attributes ;
    private final String text;
    
    /**
     * 
     * @param text the text content of the element
     * @param attributes the attributes of the element.
     * 		the attributes should contain a "type" attribute specifying either text,html, or xhtml.
     */
    public AtomTextConstruct(String text
    		, List<Attribute> attributes) throws AtomSpecException{
    	
    	this.text = text;
    	    	
    	if(attributes == null){
    		this.attributes = null;
    	}else{
    		this.attributes = new LinkedList<Attribute>();
    		Iterator<Attribute> attrItr = attributes.iterator();
    		while(attrItr.hasNext()){
    			Attribute attr = attrItr.next();
    			//check for unsupported attribute.
    			if(!attr.getName().equals("type")
    					&& !FeedDoc.isUndefinedAttribute(attr)){
    				throw new AtomSpecException("Unsuppported attribute "
    						+attr.getName()
    						+" that is not "
    						+"of the form "
    						+"xml:base=\"...\" "
    						+"or xml:lang=\"...\" or local:*=\"...\" for this Atom Text Construct.");
    			}
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
     * @return the text content for this element.
     */
    public String getText() {
        return text;
    }
}