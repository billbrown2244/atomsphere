package com.colorful.atom.beans;

import java.util.List;

public class Summary {
    /*
     * atomSummary = element atom:summary { atomTextConstruct }
     */
private AtomTextConstruct summary = null;
    
    public Summary(String type){
        summary = new AtomTextConstruct(type);
    }
    
    public Summary() {
        summary = new AtomTextConstruct();
    }

    public List getAttributes() {
        return summary.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.summary.setAttributes(attributes);
    }

    public String getText() {
        return summary.getText();
    }

    public void setText(String text) {
        this.summary.setText(text);
    }
}
