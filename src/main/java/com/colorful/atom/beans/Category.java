package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;

public class Category {
    List attributes = null;
    Attribute term = null; //required
    Attribute scheme = null;
    Attribute label = null;
    public List getAttributes() {
        return attributes;
    }
    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }
    public Attribute getLabel() {
        return label;
    }
    public void setLabel(Attribute label) {
        this.label = label;
    }
    public Attribute getScheme() {
        return scheme;
    }
    public void setScheme(Attribute scheme) {
        this.scheme = scheme;
    }
    public Attribute getTerm() {
        return term;
    }
    public void setTerm(Attribute term) {
        this.term = term;
    }

    
    public String toXML(){
        StringBuffer buff = new StringBuffer();
        buff.append("<category ");
        if(attributes != null){
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            buff.append(((Attribute)attrs.next()).toXML());
        }
        }
        buff.append(term.toXML());
        if(scheme != null){
            buff.append(scheme.toXML());
        }
        if(label != null){
            buff.append(label.toXML());
        }
        buff.append(" />\n");       
        return buff.toString();
    }
}
