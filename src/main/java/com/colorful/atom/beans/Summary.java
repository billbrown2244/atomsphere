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

public class Summary {
    /*
     * atomSummary = element atom:summary { atomTextConstruct }
     */
private AtomTextConstruct summary = null;
    
    public Summary(String type){
        summary = new AtomTextConstruct(type);
    }
    
    public Summary() {
        summary = new AtomTextConstruct();
    }

    public List getAttributes() {
        return summary.getAttributes();
    }

    public void setAttributes(List attributes) {
        this.summary.setAttributes(attributes);
    }

    public String getText() {
        return summary.getText();
    }

    public void setText(String text) {
        this.summary.setText(text);
    }

    public AtomTextConstruct getSummary() {
        return summary;
    }

    public void setSummary(AtomTextConstruct summary) {
        this.summary = summary;
    }
}
