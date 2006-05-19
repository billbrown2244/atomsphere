package com.colorful.atom.beans;

public class URI {

    private String text = null;
    
    public URI(){
        this.text = "";
    }
    
    public URI(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
