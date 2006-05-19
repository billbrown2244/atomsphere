package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;


public class AtomTextConstruct {
    /*
     * atomPlainTextConstruct =
   atomCommonAttributes,
   attribute type { "text" | "html" }?,
   text

atomXHTMLTextConstruct =
   atomCommonAttributes,
   attribute type { "xhtml" },
   xhtmlDiv

atomTextConstruct = atomPlainTextConstruct | atomXHTMLTextConstruct
     */
    private List attributes = null;
    private String text = null;
    
    public AtomTextConstruct(){
        text = "";
    }
    public AtomTextConstruct(String type){
        this();
        attributes = new LinkedList();
        attributes.add(new Attribute("type",type));        
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}