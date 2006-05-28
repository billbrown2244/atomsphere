package com.colorful.atom.beans;

import java.util.List;

public class Subtitle {
	/*
	 * atomSubsubtitle = element atom:subsubtitle { atomTextConstruct }
	 */
	
private AtomTextConstruct subtitle = null;
    
    public Subtitle(String type){
        subtitle = new AtomTextConstruct(type);
    }
    
    public Subtitle() {
        subtitle = new AtomTextConstruct();
    }

    public List getAttributes() {
        return subtitle.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.subtitle.setAttributes(attributes);
    }

    public String getText() {
        return subtitle.getText();
    }

    public void setText(String text) {
        this.subtitle.setText(text);
    }
}
