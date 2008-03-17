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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorful.atom.beans;

/**
 * This class represents an Atom 1.0 Contributor element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      "atom:email" element's content conveys an e-mail address associated with the person. 
 *      Person constructs MAY contain an atom:email element, but MUST NOT contain more than one. 
 *      Its content MUST conform to the "addr-spec" production in [RFC2822].
 *  </pre>
 */
public class Email {

    private final String text;
    
    /**
     * 
     * @param text the email address
     */
    public Email(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
