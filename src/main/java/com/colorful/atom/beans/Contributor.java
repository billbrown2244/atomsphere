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

import java.util.List;

/**
 * This class represents an Atom 1.0 Contributor element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomContributor = element atom:contributor { atomPersonConstruct }
 *  </pre>
 */
public class Contributor {
    AtomPersonConstruct contributor;

    public Contributor(){
        contributor = new AtomPersonConstruct();
    }
    
    /**
     * 
     * @param name the name of the contributor
     */
    public Contributor(String name){
        contributor = new AtomPersonConstruct(name);
    }
    
    /**
     * 
     * @param name the name of the contributor
     * @param uri the uri of the contributor (eg. homepage, blog)
     * @param email the email of the contributor
     */
    public Contributor(String name, String uri, String email){
        contributor = new AtomPersonConstruct(name,uri,email);
    }
    
    public AtomPersonConstruct getContributor() {
        return contributor;
    }

    public void setContributor(AtomPersonConstruct contributor) {
        this.contributor = contributor;
    }
    
    public List getAttributes() {
        return this.contributor.getAttributes();
    }
    
    public void setAttributes(List attributes) {
        this.contributor.setAttributes(attributes);
    }
    
    public void addAttribute(Attribute attribute){
        this.contributor.addAttribute(attribute);
    }
    
    public Email getEmail() {
        return this.contributor.getEmail();
    }
    public void setEmail(Email email) {
        this.contributor.setEmail(email);
    }
    public Name getName() {
        return this.contributor.getName();
    }
    public void setName(Name name) {
        this.contributor.setName(name);
    }
    public URI getUri() {
        return this.contributor.getUri();
    }
    public void setUri(URI uri) {
        this.contributor.setUri(uri);
    }
    
    public List getExtensions() {
        return this.contributor.getExtensions();
    }
    public void setExtensions(List extensions) {
        this.contributor.setExtensions(extensions);
    }
    
    public void addExtension(Extension extension) {
        this.contributor.addExtension(extension);
        
    }
}
