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
package com.colorful.atom.beans;

import java.util.List;

public class Author {
    AtomPersonConstruct author;

    public Author(){
        author = new AtomPersonConstruct();
    }
    
    public Author(String name){
        author = new AtomPersonConstruct(name);
    }
    
    public Author(String name, String uri, String email){
        author = new AtomPersonConstruct(name,uri,email);
    }
    
    public AtomPersonConstruct getAuthor() {
        return author;
    }

    public void setAuthor(AtomPersonConstruct author) {
        this.author = author;
    }
    
    public List getAttributes() {
        return this.author.getAttributes();
    }
    
    public void setAttributes(List attributes) {
        this.author.setAttributes(attributes);
    }
    
    public void addAttribute(Attribute attribute){
        this.author.addAttribute(attribute);
    }
    
    public Email getEmail() {
        return this.author.getEmail();
    }
    public void setEmail(Email email) {
        this.author.setEmail(email);
    }
    public Name getName() {
        return this.author.getName();
    }
    public void setName(Name name) {
        this.author.setName(name);
    }
    public URI getUri() {
        return this.author.getUri();
    }
    public void setUri(URI uri) {
        this.author.setUri(uri);
    }
    
    public List getExtensions() {
        return this.author.getExtensions();
    }
    public void setExtensions(List extensions) {
        this.author.setExtensions(extensions);
    }
    
    public void addExtension(Extension extension) {
        this.author.addExtension(extension);        
    }
}
