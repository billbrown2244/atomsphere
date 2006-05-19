package com.colorful.atom.beans;

import java.util.List;

public class Entry {
    /*
     * atomEntry =
     element atom:entry {
      atomCommonAttributes,
      (atomAuthor*
       & atomCategory*
       & atomContent?
       & atomContributor*
       & atomId
       & atomLink*
       & atomPublished?
       & atomRights?
       & atomSource?
       & atomSummary?
       & atomTitle
       & atomUpdated
       & extensionElement*)
   }
     */
    
    List attributes;
    List authors;
    List categories;
    Content content;
    List contributors;
    Id id;
    List links;
    Published published;
    Rights rights;
    Source source;
    Summary summary;
    Title title;
    Updated updated;
    List extensions;
}
