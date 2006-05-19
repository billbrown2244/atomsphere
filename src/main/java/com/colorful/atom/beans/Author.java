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
}
