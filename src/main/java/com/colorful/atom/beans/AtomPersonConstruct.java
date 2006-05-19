package com.colorful.atom.beans;

import java.util.List;

public class AtomPersonConstruct {

    /*
     * atomPersonConstruct =
   atomCommonAttributes,
   (element atom:name { text }
    & element atom:uri { atomUri }?
    & element atom:email { atomEmailAddress }?
    & extensionElement*)
     */
    
    private List attributes = null;
    private Name name = null;
    private URI uri = null;
    private Email email = null;
    private List extensions = null;
    
    public AtomPersonConstruct(){
        this.name = new Name();
    }
    
    public AtomPersonConstruct(String name){
        this.name = new Name(name); 
    }
    
    public AtomPersonConstruct(String name, String uri, String email){
        this.name = new Name(name);
        this.uri = new URI(uri);
        this.email = new Email(email);
    }
    
    public List getAttributes() {
        return attributes;
    }
    public void setAttributes(List attributes) {
        this.attributes = attributes;
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
}
