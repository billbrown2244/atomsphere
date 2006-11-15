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
 */
package com.colorful.atom.beans;

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
public class AtomPersonConstruct {
    
    private List attributes = null;
    private Name name = null;
    private URI uri = null;
    private Email email = null;
    private List extensions = null;
    
    public AtomPersonConstruct(){
        this.name = new Name();
    }
    
    /**
     * 
     * @param name the name of the person
     */
    public AtomPersonConstruct(String name){
        this.name = new Name(name); 
    }
    
    /**
     * 
     * @param name the name of the person
     * @param uri the uri of the person
     * @param email the email address of the person
     */
    public AtomPersonConstruct(String name, String uri, String email){
        this.name = new Name(name);
        this.uri = new URI(uri);
        this.email = new Email(email);
    }
    
    /**
     * 
     * @return the attributes of this element
     */
    public List getAttributes() {
        return attributes;
    }
    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }
    
    /**
     * 
     * @param attribute the attribute to add to this element.
     */
    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }
    
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }
    public URI getUri() {
        return uri;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }
    
    public List getExtensions() {
        return extensions;
    }
    public void setExtensions(List extensions) {
        this.extensions = extensions;
    }
    
    public void addExtension(Extension extension) {
        if(this.extensions == null){
            this.extensions = new LinkedList();
        }
        this.extensions.add(extension);
        
    }
}
