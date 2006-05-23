package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;

public class Rights {
    /*
     * atomRights = element atom:rights { atomTextConstruct }
     */
private AtomTextConstruct rights = null;
    
    public Rights(String type){
        rights = new AtomTextConstruct(type);
    }
    
    public Rights() {
        rights = new AtomTextConstruct();
    }

    public List getAttributes() {
        return rights.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.rights.setAttributes(attributes);
    }
    
    public void addAttribute(Attribute attribute){
        this.rights.addAttribute(attribute);
    }

    public String getText() {
        return rights.getText();
    }

    public void setText(String text) {
        this.rights.setText(text);
    }
}
