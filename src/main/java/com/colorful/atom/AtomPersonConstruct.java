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
 * 2006-11-12 wbrown - added javadoc documentation.
 * 2008-03-16 wbrown - made class immutable.
 */
package com.colorful.atom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * This class represents an Atom 1.0 person construct.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre> 
 *      atomPersonConstruct =
 *          atomCommonAttributes,
 *          (element atom:name { text }
 *          & element atom:uri { atomUri }?
 *          & element atom:email { atomEmailAddress }?
 *          & extensionElement*)
 *  </pre>
 */
class AtomPersonConstruct {
    
    private final List<Attribute> attributes;
    private final Name name;
    private final URI uri;
    private final Email email;
    private final List<Extension> extensions;
    
    /**
     * 
     * @param name the name of the person
     * @param uri the uri of the person
     * @param email the email address of the person
     * @param attributes
     * @param extensions
     */
    public AtomPersonConstruct(Name name, URI uri
    		, Email email, List<Attribute> attributes
    		, List<Extension> extensions) throws AtomSpecException{
    	
    	//check to make sure there is a name element
    	if(name == null){
    		throw new AtomSpecException("Person constructs MUST contain exactly one \"atom:name\" element.");
    	}else{
    		this.name = new Name(name.getText());
    	}
    	
    	this.uri = (uri == null)?null:new URI(uri.getText());
    	
    	this.email = (email == null)?null:new Email(email.getText());
    	
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
    	
    	if(extensions == null){
    		this.extensions = null;
    	}else{
    		this.extensions = new LinkedList<Extension>();
    		Iterator<Extension> extItr = extensions.iterator();
    		while(extItr.hasNext()){
    			Extension extension = extItr.next();
    			this.extensions.add(new Extension(extension.getElementName(),extension.getAttributes(),extension.getContent()));
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
     * @return the email address for this element.
     */
    public Email getEmail() {
        return (email == null)?null: new Email(email.getText());
    }

    /**
     * 
     * @return the name for this element.
     */
    public Name getName() {
        return (name == null)?null: new Name(name.getText());
    }

    /**
     * 
     * @return the URI for this element.
     */
    public URI getUri() {
        return (uri == null)?null: new URI(uri.getText());
    }

    /**
     * 
     * @return the extensions for this element.
     */
    public List<Extension> getExtensions() {
    	if(extensions == null){
    		return null;
    	}
    	List<Extension> extnsCopy = new LinkedList<Extension>();
		Iterator<Extension> extItr = extensions.iterator();
		while(extItr.hasNext()){
			Extension extension = extItr.next();
			extnsCopy.add(new Extension(extension.getElementName(),extension.getAttributes(),extension.getContent()));
		}
        return extnsCopy;
    }

}
