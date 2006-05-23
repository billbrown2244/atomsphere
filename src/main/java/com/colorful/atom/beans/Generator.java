package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;

public class Generator {
    private List attributes = null;
    private String text = null;
    
    public Generator(String uri,String version){
        attributes = new LinkedList();
        attributes.add(new Attribute("uri",uri));
        attributes.add(new Attribute("version",version));
    }

    public Generator() {
        // TODO Auto-generated constructor stub
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
