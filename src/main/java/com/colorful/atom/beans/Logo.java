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
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorful.atom.beans;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 logo element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *	<pre>
 *      atomLogo = element atom:logo {
 *          atomCommonAttributes,
 * 		    (atomUri)
 *	        }
 *	</pre>
 */
public class Logo {
    
    private List attributes = null;
    private URI uri = null;

    public Logo(){
        this.uri = new URI();
    }
    
    /**
     * 
     * @param uri identification for the logo (possibly a URL for the location)
     */
    public Logo(URI uri){
        this.uri = uri;
    }
    
    public URI getUri() {
        return uri;
    }


    public void setUri(URI uri) {
        this.uri = uri;
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

    public String getText() {
        return this.uri.getText();
    }

    public void setText(String text) {
        this.uri.setText(text);
    }
}
