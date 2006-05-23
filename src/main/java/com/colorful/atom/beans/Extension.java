package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;

public class Extension {
    /*
     * simpleExtensionElement =
   element * - atom:* {
      text
   }
       
       structuredExtensionElement =
   element * - atom:* {
      (attribute * { text }+,
         (text|anyElement)*)
    | (attribute * { text }*,
       (text?, anyElement+, (text|anyElement)*))
   }
     */
    private String elementName = null;
    private List attributes = null;
    private String content = null;
    private Attribute xmlns = null;
    
    public Extension(){
        //put sutiable defaulst here
        
    }

    public Extension(String xmlns, String elementName){
        attributes = new LinkedList();
        attributes.add(new Attribute("xmlns",xmlns));
        this.elementName = elementName;
        
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
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Attribute getXmlns() {
        return xmlns;
    }

    public void setXmlns(Attribute xmlns) {
        this.xmlns = xmlns;
    }
}
