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
 * This class represents an Atom 1.0 link element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 * <pre>
 * 	    atomLink =
 *          element atom:link {
 *          atomCommonAttributes,
 *          attribute href { atomUri },
 *          attribute rel { atomNCName | atomUri }?,
 *          attribute type { atomMediaType }?,
 *          attribute hreflang { atomLanguageTag }?,
 *          attribute title { text }?,
 *          attribute length { text }?,
 *          undefinedContent
 *         }
 * </pre>
 */
public class Link {
    
    private final List<Attribute> attributes;
    private final Attribute href;
    private final Attribute rel;
    private final Attribute type;
    private final Attribute hreflang;
    private final Attribute title;
    private final Attribute length;
    private final String content;
    
    //use the factory method in the FeedDoc.
    Link(Attribute href, Attribute rel, Attribute type
    		, Attribute hreflang, Attribute title
    		,Attribute length, List<Attribute> attributes
    		, String content) throws AtomSpecException {
    	
    	if(href == null){
    		throw new AtomSpecException("atom:link elements MUST have an href attribute, whose value MUST be a IRI reference");
    	}else{
    		this.href = new Attribute(href.getName(),href.getValue());
    	}
    	
    	this.rel = (rel == null)?null:new Attribute(rel.getName(),rel.getValue());
    	this.type = (type == null)?null:new Attribute(type.getName(),type.getValue());
    	this.hreflang = (hreflang == null)?null:new Attribute(hreflang.getName(),hreflang.getValue());
    	this.title = (rel == title)?null:new Attribute(title.getName(),title.getValue());
    	this.length = (length == null)?null:new Attribute(length.getName(),length.getValue());
        
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
        
        if(href != null){
        	attributes.add(this.href);
        }
        
        if(rel != null){
        	attributes.add(this.rel);
        }
        
        if(type != null){
        	attributes.add(this.type);
        }
        
        if(hreflang != null){
        	attributes.add(this.hreflang);
        }
        
        if(title != null){
        	attributes.add(this.title);
        }
        
        if(length != null){
        	attributes.add(this.length);
        }
        
        this.content = content;
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
     * @return the href contains the link's IRI
     */
    public Attribute getHref() {
    	return (href == null)?null:new Attribute(href.getName(),href.getValue());
    }

    /**
     * 
     * @return the hreflang describes the language of the resource pointed to by the href attribute.
     */
    public Attribute getHreflang() {
    	return (hreflang == null)?null:new Attribute(hreflang.getName(),hreflang.getValue());
     }

    /**
     * 
     * @return the length indicates an advisory length of the linked content in octets.
     */
    public Attribute getLength() {
    	return (length == null)?null:new Attribute(length.getName(),length.getValue());
    }

    /**
     * 
     * @return the rel which matches either the "isegment-nz-nc" or the "IRI" production in [RFC3987]
     */
    public Attribute getRel() {
    	return (rel == null)?null:new Attribute(rel.getName(),rel.getValue());
    }

    /**
     * 
     * @return the title conveys human-readable information about the link.
     */
    public Attribute getTitle() {
    	return (title == null)?null:new Attribute(title.getName(),title.getValue());
    }

    /**
     * 
     * @return the type which is an advisory media type
     */
    public Attribute getType() {
    	return (type == null)?null:new Attribute(type.getName(),type.getValue());
    }

    /**
     * 
     * @return undefined text content or undefined element.
     */
    public String getContent() {
    	return content;
    }
}
