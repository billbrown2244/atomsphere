package com.colorful.atom.beans;

public class Name {

    private String text = null;
    
    public Name(){
        text = "";
    }
    public Name(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
