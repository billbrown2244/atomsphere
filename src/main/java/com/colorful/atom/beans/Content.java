package com.colorful.atom.beans;

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

    List attributes;
    Attribute type;
    Attribute source;
    String content;
    
}
