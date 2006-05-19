package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;

public class Icon {

    private List attributes = null;
    private String uri = null;

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

    public String toXML(){
        StringBuffer buff = new StringBuffer();
        buff.append("<icon ");
        if(attributes != null){
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            buff.append(((Attribute)attrs.next()).toXML());
        }
        }
        buff.append(">");
        buff.append(uri);
        buff.append("</icon>\n");       
        return buff.toString();
    }
}
