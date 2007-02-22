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
 *  2007-02-20 wbrown - added override of equals method.
 */
package com.colorful.atom.beans;

/**
 * This class represents an Atom 1.0 attribute.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomCommonAttributes =
 *          attribute xml:base { atomUri }?,
 *          attribute xml:lang { atomLanguageTag }?,
 *          undefinedAttribute*
 *  </pre>
 */
public class Attribute {
    
    /*
     * atomCommonAttributes =
   attribute xml:base { atomUri }?,
   attribute xml:lang { atomLanguageTag }?,
   undefinedAttribute*
     */
    
    private String name = null;
    private String value = null;

    public Attribute(){
        //nothing?
    }
    
    public Attribute(String name){
        this.name = name;
        this.value = "";
    }
    
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
    
    
    /**
     * @Override return true if the name and value of the attributes are equal.
     */
    public boolean equals(Object obj) {
    	if(obj instanceof Attribute){   		
    		Attribute local = (Attribute)obj;
    		return local.name.equals(this.name)
    			&& local.value.equals(this.value);
    	}
    	return false;
    }
}
