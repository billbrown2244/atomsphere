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

/**
 * This is an exception for data that doesn't match the spec.
 * @see http://www.atomenabled.org/developers/syndication/atom-format-spec.php
 * @author wbrown
 *
 */
public class AtomSpecException extends Exception {

    private static final long serialVersionUID = -2552891517237912542L;

    public AtomSpecException(){
        super();
    }
    
    public AtomSpecException(String message){
        super(message);
    }
}
