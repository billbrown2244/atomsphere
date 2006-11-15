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

import java.util.List;

/**
 * This class represents an Atom 1.0 subtitle element.
 * @see http://www.atomenabled.org/developers/syndication/atom-format-spec.php
 * @author bill
 *	<code>
 *		atomSubsubtitle = element atom:subsubtitle { atomTextConstruct }
 *	</code>
 */
public class Subtitle {
	
private AtomTextConstruct subtitle = null;
    
	/**
	 * 
	 * @param type the type of text construct text, html or xhtml.
	 */
    public Subtitle(String type){
        subtitle = new AtomTextConstruct(type);
    }
    
    public Subtitle() {
        subtitle = new AtomTextConstruct();
    }

    public List getAttributes() {
        return subtitle.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.subtitle.setAttributes(attributes);
    }

    public String getText() {
        return subtitle.getText();
    }

    public void setText(String text) {
        this.subtitle.setText(text);
    }

    public AtomTextConstruct getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(AtomTextConstruct subtitle) {
        this.subtitle = subtitle;
    }
}
