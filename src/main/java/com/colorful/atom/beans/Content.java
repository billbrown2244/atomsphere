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
    public static final String atomInlineTextContent = "atomInlineTextContent";
    public static final String atomInlineXHTMLContent = "atomInlineXHTMLContent";
    public static final String atomInlineOtherContent = "atomInlineOtherContent";
    public static final String atomOutOfLineContent = "atomOutOfLineContent";

    private List attributes = null;
    private Attribute type = null;
    private Attribute source = null;
    private String content = null;
    
    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }
}
