package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Logo {
    /*
     * atomLogo = element atom:logo {
   atomCommonAttributes,
   (atomUri)
}
     */
    
    private List attributes = null;
    private String uri = null;

    public Logo(){
        this.uri = "";
    }
    
    public Logo(String uri){
        this.uri = uri;
    }
    
    public String getUri() {
        return uri;
    }


    public void setUri(String uri) {
        this.uri = uri;
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
}
