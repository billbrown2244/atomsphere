package com.colorful.atom.beans;

import java.util.List;

public class Icon {

    private List attributes = null;
    private String uri = null;

    public Icon(){
        this.uri = "";
    }
    
    public Icon(String uri){
        this.uri = uri;
    }
    
    public String getUri() {
        return uri;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public List getAttributes() {
        return attributes;
    }


    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

}
