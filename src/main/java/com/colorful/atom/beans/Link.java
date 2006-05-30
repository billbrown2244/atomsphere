/*
Atomsphere - an atom feed library.
Copyright (C) 2006 William R. Brown.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Link {
    /*
     * atomLink =
   element atom:link {
      atomCommonAttributes,
      attribute href { atomUri },
      attribute rel { atomNCName | atomUri }?,
      attribute type { atomMediaType }?,
      attribute hreflang { atomLanguageTag }?,
      attribute title { text }?,
      attribute length { text }?,
      undefinedContent
   }
     */
    private List attributes = null;
    private Attribute href = null;
    private Attribute rel = null;
    private Attribute type = null;
    private Attribute hreflang = null;
    private Attribute title = null;
    private Attribute length = null;
    
    public Link(){
        attributes = new LinkedList();
    }
    
    public Link(String href){
        attributes = new LinkedList();
        this.href = new Attribute("href",href);
        attributes.add(this.href);
    }
    
    public Link(String href, String rel){
        attributes = new LinkedList();
        this.href = new Attribute("href",href);
        this.rel = new Attribute("rel",rel);
        attributes.add(this.href);
        attributes.add(this.rel);
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }
    
    public Attribute getHref() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("href")){
                return attr;
            }
        }
        return null;
    }

    public void setHref(Attribute href) {
        this.attributes.add(href);
    }

    public Attribute getHreflang() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("hreflang")){
                return attr;
            }
        }
        return null;
    }

    public void setHreflang(Attribute hreflang) {
        this.attributes.add(hreflang);
    }

    public Attribute getLength() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("length")){
                return attr;
            }
        }
        return null;
    }

    public void setLength(Attribute length) {
        this.attributes.add(length);
    }

    public Attribute getRel() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("rel")){
                return attr;
            }
        }
        return null;
    }

    public void setRel(Attribute rel) {
        this.attributes.add(rel);
    }

    public Attribute getTitle() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("title")){
                return attr;
            }
        }
        return null;
    }

    public void setTitle(Attribute title) {
        this.attributes.add(title);
    }

    public Attribute getType() {
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if (attr.getName().equals("type")){
                return attr;
            }
        }
        return null;
    }

    public void setType(Attribute type) {
        this.attributes.add(type);
    }
}
