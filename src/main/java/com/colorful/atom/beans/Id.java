package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;

public class Id {
    /*
     * atomId = element atom:id {
   atomCommonAttributes,
   (atomUri)
}
     */
    List attributes;
    URI uri;
    
    public Id(){
        //do nothing.
    }
    
    public List getAttributes() {
        return attributes;
    }
    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }
    
    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }
    
    public URI getUri() {
        return uri;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }
}
