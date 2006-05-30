package com.colorful.atom.beans;

import java.util.List;

public class Contributor {
    AtomPersonConstruct contributor;

    public Contributor(){
        contributor = new AtomPersonConstruct();
    }
    
    public Contributor(String name){
        contributor = new AtomPersonConstruct(name);
    }
    
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
