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

import java.util.List;

public class Rights {
    /*
     * atomRights = element atom:rights { atomTextConstruct }
     */
private AtomTextConstruct rights = null;
    
    public Rights(String type){
        rights = new AtomTextConstruct(type);
    }
    
    public Rights() {
        rights = new AtomTextConstruct();
    }

    public List getAttributes() {
        return rights.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.rights.setAttributes(attributes);
    }
    
    public void addAttribute(Attribute attribute){
        this.rights.addAttribute(attribute);
    }

    public String getText() {
        return rights.getText();
    }

    public void setText(String text) {
        this.rights.setText(text);
    }

    public AtomTextConstruct getRights() {
        return rights;
    }

    public void setRights(AtomTextConstruct rights) {
        this.rights = rights;
    }
}
