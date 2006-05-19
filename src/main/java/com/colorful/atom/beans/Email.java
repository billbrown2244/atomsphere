package com.colorful.atom.beans;

public class Email {

    private String text = null;
    
    public Email(){
        this.text = "";
    }
    
    public Email(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
