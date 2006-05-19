package com.colorful.atom.beans;

public class Attribute {
    
    /*
     * atomCommonAttributes =
   attribute xml:base { atomUri }?,
   attribute xml:lang { atomLanguageTag }?,
   undefinedAttribute*
     */
    
    private String name = null;
    private String value = null;

    public Attribute(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String toXML(){
        return name+"=\""+value+"\" ";
    }
    
}
