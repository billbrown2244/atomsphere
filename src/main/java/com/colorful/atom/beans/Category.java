package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Category {
    /*
     * atomCategory =
   element atom:category {
      atomCommonAttributes,
      attribute term { text },
      attribute scheme { atomUri }?,
      attribute label { text }?,
      undefinedContent
   }
     */
    
    List attributes = null;
    Attribute term = null; //required
    Attribute scheme = null;
    Attribute label = null;
    
    public Category(){
        attributes = new LinkedList();
    }
    
    public Category(String term){
        attributes = new LinkedList();
        this.term = new Attribute("term",term);
        attributes.add(this.term);
    }
    
    public Category(String term, String scheme, String label){
        attributes = new LinkedList();
        this.term = new Attribute("term",term);
        this.scheme = new Attribute("scheme",scheme);
        this.label = new Attribute("label",label);
        attributes.add(this.term);
        attributes.add(this.scheme);
        attributes.add(this.label);
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
    
    public Attribute getLabel() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("label")){
                return attr;
            }
        }
        return null;
    }
    public void setLabel(Attribute label) {
        this.attributes.add(label);

    }
    public Attribute getScheme() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("scheme")){
                return attr;
            }
        }
        return null;
    }
    public void setScheme(Attribute scheme) {
        this.attributes.add(scheme);
    }
    public Attribute getTerm() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("term")){
                return attr;
            }
        }
        return null;
    }
    public void setTerm(Attribute term) {
        this.attributes.add(term);
    }
}
