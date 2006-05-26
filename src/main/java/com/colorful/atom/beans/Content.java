package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;

public class Content {
    /*
     * atomInlineTextContent =
   element atom:content {
      atomCommonAttributes,
      attribute type { "text" | "html" }?,
      (text)*
   }

atomInlineXHTMLContent =
   element atom:content {
      atomCommonAttributes,
      attribute type { "xhtml" },
      xhtmlDiv
   }

atomInlineOtherContent =
   element atom:content {
      atomCommonAttributes,
      attribute type { atomMediaType }?,
      (text|anyElement)*
   }

atomOutOfLineContent =
   element atom:content {
      atomCommonAttributes,
      attribute type { atomMediaType }?,
      attribute src { atomUri },
      empty
   }
     * atomContent = atomInlineTextContent
 | atomInlineXHTMLContent
 | atomInlineOtherContent
 | atomOutOfLineContent
     */
    public static final String TEXT = "text";
    public static final String HTML = "html";
    public static final String XHTML = "xhtml";

    private List attributes = null;
    private String content = null;
    
    public Content(){
        //nothing yet.
    }
    
    public Content(String type){
        attributes = new LinkedList();
        attributes.add(new Attribute("type",type));
    }
    
    public Content(String type, String src){
        attributes = new LinkedList();
        attributes.add(new Attribute("type",type));
        attributes.add(new Attribute("src",src));
    }
    
    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
