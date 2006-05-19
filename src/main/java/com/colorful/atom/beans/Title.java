package com.colorful.atom.beans;

import java.util.List;

public class Title {
    /*
     * atomTitle = element atom:title { atomTextConstruct }
     */
    private AtomTextConstruct title = null;
    
    public Title(String type){
        title = new AtomTextConstruct(type);
    }
    
    public Title() {
        title = new AtomTextConstruct();
    }

    public List getAttributes() {
        return title.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.title.setAttributes(attributes);
    }

    public String getText() {
        return title.getText();
    }

    public void setText(String text) {
        this.title.setText(text);
    }
}
