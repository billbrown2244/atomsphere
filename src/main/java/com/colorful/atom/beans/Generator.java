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
 * This class represents an Atom 1.0 generator element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomGenerator = element atom:generator {
 *          atomCommonAttributes,
 *          attribute uri { atomUri }?,
 *          attribute version { text }?,
 *          text
 *          }
 *  </pre>
 */
public class Generator {
    private final List<Attribute> attributes;
    private final Attribute uri;
    private final Attribute version;
    private final String text;
    
    /**
     * 
     * @param uri the uri of the generator (eg homepage).
     * @param version the version of the generator.
     * @param attributes the attributes for this element.
     * @param text the text content for this element.
     */
    public Generator(Attribute uri,Attribute version, List<Attribute> attributes, String text){
    	
    	this.uri = (uri == null)?null:new Attribute(uri.getName(),uri.getValue());
    	this.version = (version == null)?null:new Attribute(version.getName(),version.getValue());
    	
    	if(uri == null && version == null && attributes == null){
    		this.text = text;
    		this.attributes = null;
    		return;
    	}
        
        if(attributes == null){
        	this.attributes = new LinkedList<Attribute>();
    	}else{
    		this.attributes = new LinkedList<Attribute>();
    		Iterator<Attribute> attrItr = attributes.iterator();
    		while(attrItr.hasNext()){
    			Attribute attr = attrItr.next();
    			this.attributes.add(new Attribute(attr.getName(),attr.getValue()));
    		}
    	}
        
        if(uri != null){
        	attributes.add(this.uri);
        }
        
        if(version != null){
        	attributes.add(this.version);
        }
        
        this.text = text;
    }

    /**
     * 
     * @return the category attribute list.
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
     * @return the label attribute
     */
    public Attribute getUri() {
    	return (uri == null)?null:new Attribute(uri.getName(),uri.getValue());
    }

    /**
     * 
     * @return the scheme attribute
     */
    public Attribute getVersion() {
    	return (version == null)?null:new Attribute(version.getName(),version.getValue());
    }

    /**
     * 
     * @return the text content for this element.
     */
    public String getText() {
        return text;
    }

}
